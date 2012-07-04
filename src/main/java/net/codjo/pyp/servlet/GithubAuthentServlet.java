package net.codjo.pyp.servlet;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        File file = null;

        if (file.exists() && file.isFile()) {
            if (hasBeenUpdated(request, file)) {
                response.setContentLength((int)file.length());
                response.setContentType("text/xml");
                response.addDateHeader("Last-Modified", new Date(System.currentTimeMillis()));

                OutputStream out = response.getOutputStream();
                try {
                    InputStream in = new BufferedInputStream(new FileInputStream(file));
                    try {
                        int nread;
                        byte[] buffer = new byte[(int)file.length()];
                        while ((nread = in.read(buffer)) > 0) {
                            out.write(buffer, 0, nread);
                        }
                    }
                    finally {
                        in.close();
                    }
                }
                finally {
                    out.close();
                }
            }
            else {
                response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }
        else {
            handleFileDoesNotExist(response, file);
        }
    }


    @Override
    protected long getLastModified(HttpServletRequest req) {
        File localeFile = null;
        if (localeFile != null && localeFile.exists()) {
            return getLastModified(localeFile);
        }
        return super.getLastModified(req);
    }


    private long getLastModified(File file) {
        long lastModified = file.lastModified();
        if (lastModified == 0) {
            return -1;
        }
        return lastModified;
    }


    @SuppressWarnings({"NestedAssignment"})
    protected void download(HttpServletResponse response, String mimeType, File file) throws IOException {

        response.setContentLength((int)file.length());
        response.setContentType(mimeType);
        response.addDateHeader("Last-Modified", getLastModified(file));

        OutputStream out = response.getOutputStream();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            try {
                int nread;
                byte[] buffer = new byte[(int)file.length()];
                while ((nread = in.read(buffer)) > 0) {
                    out.write(buffer, 0, nread);
                }
            }
            finally {
                in.close();
            }
        }
        finally {
            out.close();
        }
    }


    protected void handleFileDoesNotExist(HttpServletResponse response, File file) throws IOException {
        logger.info("Unknown file : " + file.getName());
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }


    private boolean hasBeenUpdated(HttpServletRequest request, File file) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        return ifModifiedSince / 1000 * 1000 < getLastModified(file) / 1000 * 1000;
    }
}
