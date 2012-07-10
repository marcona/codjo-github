package net.codjo.pyp.servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
/**
 *
 */
//<?php
// Step 6
//$data = 'client_id=' . '58a3dcf21a0bae21db44' . '&' .
//		'client_secret=' . 'd102461f3339bad28ac26998be39a1e26b5205b9' . '&' .
//		'code=' . urlencode($_GET['code']);
//
//$ch = curl_init('https://github.com/login/oauth/access_token');
//curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
//curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
//$response = curl_exec($ch);
//
//preg_match('/access_token=([0-9a-f]+)/', $response, $out);
//echo $out[1];
//curl_close($ch);
//?>
//

//See the [blog post](http://blog.vjeux.com/2012/javascript/github-oauth-login-browser-side.html) for explanation.

public class GithubAuthentServlet extends HttpServlet {
    private final static String GITHUB_AUTHENTICATION_URL = "https://github.com/login/oauth/access_token";

    protected final Logger logger = Logger.getLogger(getClass().getName());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

        response.setContentType("text/plain");
        response.addDateHeader("Last-Modified", System.currentTimeMillis());

        PrintWriter outWriter = null;
        try {

            HttpClient httpClient = initHttpClient(new ProxyInformation("ehttp1", 80, "GROUPE\\MARCONA", "XXXXX"));
//            GetMethod getCode= new GetMethod("https://github.com/login/oauth/authorize");
//            httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("marcona","jmdp1FS2"));
//            getCode.getParams().setParameter("client_id","cfd3e56a5d608efc0801");
//            executeAndTraceResponse(httpClient, getCode);

            PostMethod getTokenMethod = new PostMethod(GITHUB_AUTHENTICATION_URL);
            getTokenMethod.addParameter("client_id", "cfd3e56a5d608efc0801");
            getTokenMethod.addParameter("client_secret", "34f47910ac8d08da7a08a30952cec817b7898165");
            getTokenMethod.addParameter("code", request.getParameter("code"));
//            getTokenMethod.setRequestEntity(new StringRequestEntity("", "application/xml", "utf-8"));
            String responseResult = executeAndTraceResponse(httpClient, getTokenMethod);
            System.out.println("responseResult = " + responseResult);

            Pattern p = Pattern.compile(".*access_token=(.*)&.*");
            Matcher m = p.matcher(responseResult);
            String accessToken = "";
            if (m.find()) {
                accessToken = m.group(1);
            }
//
//            GitHubClient client = new GitHubClient();
//            client.setCredentials("marcona", "XXXXXXXXXXXXXXX");
//            UserService service = new UserService(client);
//            List<Key> keys = service.getKeys();

            outWriter = response.getWriter();
            StringBuilder builder = new StringBuilder("{\"code\":\"").append(accessToken).append("\"}");
            String result = builder.toString();
            System.out.println("result = " + result);
            outWriter.println(result);
        }
        finally {
            outWriter.close();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
        doGet(request, response);
    }


    private HttpClient initHttpClient(ProxyInformation proxy) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        if (proxy != null) {
            client.getHostConfiguration().setProxy(proxy.getProxyHost(), proxy.getProxyPort());
            client.getState()
                  .setProxyCredentials(null, proxy.getProxyHost(), new UsernamePasswordCredentials(proxy.getProxyUser(),
                                                                                                   proxy.getProxyPassword()));
            client.getState().setAuthenticationPreemptive(true);
        }
        return client;
    }


    private String executeAndTraceResponse(HttpClient client, HttpMethod getMethod) throws IOException {
        try {
            int returnCode = client.executeMethod(getMethod);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return getMethod.getResponseBodyAsString();
            }
            else {
                return readResponseStream(getMethod.getResponseBodyAsStream());
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }


    private String readResponseStream(InputStream responseBodyAsStream) throws IOException {
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(responseBodyAsStream));
            String readLine;
            while (((readLine = br.readLine()) != null)) {
                builder.append(readLine).append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                br.close();
            }
        }
        return builder.toString();
    }
}
