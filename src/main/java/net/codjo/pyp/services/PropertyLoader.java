package net.codjo.pyp.services;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
/**
 *
 */
public class PropertyLoader implements Serializable {

    private String confluenceSpaceKey;
    private String confluencePage;
    private String confluenceUrl;
    private String confluenceUser;
    private String confluencePassword;
    private String smtpServer;
    private String smtpPort;
    private String mailDomain;
    private String repositoryFilePath;
    private String applicationVersion;
    private String environmentMode = "development";


    public PropertyLoader(String propertyFilePath) {
        Properties props = null;
        try {
            props = loadProperties(propertyFilePath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (props != null) {
            confluenceSpaceKey = props.getProperty("confluenceSpaceKey");
            confluencePage = props.getProperty("confluencePage");
            confluencePage = props.getProperty("confluencePage");
            confluenceUrl = props.getProperty("confluenceUrl");
            confluenceUser = props.getProperty("confluenceUser");
            confluencePassword = props.getProperty("confluencePassword");
            smtpServer = props.getProperty("smtpServer");
            smtpPort = props.getProperty("smtpPort");
            mailDomain = props.getProperty("mailDomain");
            repositoryFilePath = props.getProperty("repositoryFilePath");
            applicationVersion = props.getProperty("application.version");
            environmentMode = props.getProperty("application.environmentMode");
        }
    }


    private Properties loadProperties(String propertyFilePath) throws IOException {
        Properties properties = new Properties();
        InputStream stream = getClass().getResourceAsStream(propertyFilePath);
        try {
            properties.load(stream);
        }
        finally {
            stream.close();
        }
        return properties;
    }


    public String getConfluenceSpaceKey() {
        return confluenceSpaceKey;
    }


    public String getConfluencePage() {
        return confluencePage;
    }


    public String getConfluenceUrl() {
        return confluenceUrl;
    }


    public String getConfluenceUser() {
        return confluenceUser;
    }


    public String getConfluencePassword() {
        return confluencePassword;
    }


    public String getSmtpServer() {
        return smtpServer;
    }


    public String getSmtpPort() {
        return smtpPort;
    }


    public String getMailDomain() {
        return mailDomain;
    }


    public String getRepositoryFilePath() {
        return repositoryFilePath;
    }


    public String getApplicationVersion() {
        return applicationVersion;
    }


    public String getEnvironmentMode() {
        return environmentMode;
    }
}
