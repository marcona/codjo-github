package net.codjo.pyp.services;
import net.codjo.confluence.ConfluenceException;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.model.Status;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
/**
 * TODO pas mal de copier coller from codjo-plugin
 */
public class MailService implements Serializable {
    //    private static final String MAIL_ENCODING = "windows-1252";
    private static final String MAIL_ENCODING = "ISO-8859-1";
    private static final Logger LOG = Logger.getLogger(MailService.class);
    public static final String CR = "<br>";
    private PropertyLoader properties;
    private String pypUrl;


    public MailService(String contextUrl) {
        properties = new PropertyLoader("/pyp.properties");
        pypUrl = contextUrl;
    }


    public void sendMail(Brin brin) throws Exception {
        String from = System.getProperty("user.name");

        ConfluenceService confluenceService = new ConfluenceService(properties.getConfluenceSpaceKey(),
                                                                    properties.getConfluencePage());
        Set<String> to = null;
        try {
            to = confluenceService.extractUserListFromConfluence(properties.getConfluenceUrl(),
                                                                 properties.getConfluenceUser(),
                                                                 properties.getConfluencePassword());
        }
        catch (ConfluenceException e) {
            e.printStackTrace();
        }

        String subject = "[BRIN][" + brin.getStatus().getStatus() + "] - " + brin.getTitle();
        String mailBody = createMailBody(brin);

        LOG.info("----------------------------------------------------------------------");
        LOG.info("");
        LOG.info("   From    : " + from);
        LOG.info("   To      : " + to);
        LOG.info("   Subject : " + subject);
        LOG.info("   Body    : \n"
                 + mailBody);

        sendMail(from, to, subject, mailBody);
    }


    private String createMailBody(Brin brin) {
        StringBuilder builder = new StringBuilder();
        builder.append("Bonjour,<br>" + "<br>")
              .append(getPhraseIntroduction(brin))
              .append(getParagraph("Description:", brin.getDescription()));

        if (Status.unblocked.equals(brin.getStatus())) {
            builder.append(getParagraph("Unblocking:", brin.getUnBlockingDescription()));
        }
        if (Status.eradicated.equals(brin.getStatus())) {
            builder.append(getParagraph("Root cause :", brin.getRootCause()));
        }
        addBrinUrl(brin, builder);
        builder.append("<br>Cordialement.");
        return builder.toString().replaceAll("\n", "<br>");
    }


    private String getPhraseIntroduction(Brin brin) {
        if (Status.current.equals(brin.getStatus())) {
            return "Le BRIN suivant a été créé.<br><br>";
        }
        else if (Status.unblocked.equals(brin.getStatus())) {
            return "Le BRIN suivant a été débloqué.<br><br>";
        }
        else if (Status.toEradicate.equals(brin.getStatus())) {
            return "Le BRIN suivant doit être éradiqué.<br><br>";
        }
        else if (Status.eradicated.equals(brin.getStatus())) {
            return "Le BRIN suivant a été éradiqué.<br><br>";
        }
        return "";
    }


    private void addBrinUrl(Brin brin, StringBuilder builder) {
        if (pypUrl != null) {
            builder.append("<br>")
                  .append("Pour plus de détails, merci de consulter le BRIN dans l'application <a href=\"")
                  .append(pypUrl).append("edit.html/id/").append(brin.getUuid())
                  .append("\">")
                  .append("PostYourProblem")
                  .append("</a>.<br>");
        }
    }


    private String getParagraph(String paragraphTitle, String content) {
        StringBuilder builder = new StringBuilder();
        builder.append("<b>").append(paragraphTitle).append("</b>").append(CR);
        if (content != null) {
            builder.append(content);
        }
        builder.append(CR).append(CR);
        return builder.toString();
    }


    private void sendMail(String from, Set<String> toSet, String subject, String body) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", properties.getSmtpServer());
        props.setProperty("mail.smtp.port", properties.getSmtpPort());

        Session session = Session.getInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from + properties.getMailDomain()));

        for (String aToSet : toSet) {
            String to = aToSet + properties.getMailDomain();
            msg.addRecipients(RecipientType.TO, InternetAddress.parse(to, false));
        }

        msg.setSubject(subject);
        msg.setContent(body, "text/html; charset=" + MAIL_ENCODING);

        msg.setHeader("X-Mailer", "PostYourProblem");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}
