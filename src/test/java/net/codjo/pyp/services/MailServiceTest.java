package net.codjo.pyp.services;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.model.Status;
import net.codjo.test.common.fixture.MailFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 *
 */
public class MailServiceTest {
    private MailFixture mailFixture = new MailFixture(89);
    private MailService mailService = new MailService("http://localhost:8080/pyp/");


    @Before
    public void setup() throws Exception {
        mailFixture.doSetUp();
    }


    @After
    public void dotearDown() throws Exception {
        mailFixture.doTearDown();
    }


    @Test
    public void test_executeNewBrin() throws Exception {
        Brin brin = new Brin("La plateforme est out");
        brin.setUuid("myId");
        brin.setDescription("Impossible de travailler\nici");
        mailService.sendMail(brin);

        mailFixture.getReceivedMessage(0).assertThat()
              .from(System.getProperty("user.name") + "@allianz.fr")
              .to("USER1@allianz.fr", "USER2@allianz.fr")
              .subject("[BRIN][CURRENT] - La plateforme est out")
              .bodyContains("Bonjour,<br><br>Le BRIN suivant a été créé.<br>")
              .bodyContains("Impossible de travailler<br>ici")
              .bodyContains("Pour plus de détails, ")
              .bodyContains("merci de consulter le BRIN dans l'application ")
              .bodyContains("<a href=\"http://localhost:8080/pyp/edit.html/id/myId\">PostYourProblem</a>")
              .bodyContains("Cordialement.")
        ;

        mailFixture.assertReceivedMessagesCount(1);
    }


    @Test
    public void test_executeBrinUnBlocked() throws Exception {
        Brin brin = new Brin("La plateforme est out");
        brin.setStatus(Status.unblocked);
        brin.setUuid("unblockedBrin");
        brin.setDescription("Impossible de travailler");
        brin.setUnBlockingDescription("On prend des vacances\n et c'est super et à Bientôt");
        mailService.sendMail(brin);

        mailFixture.getReceivedMessage(0).assertThat()
              .from(System.getProperty("user.name") + "@allianz.fr")
              .to("USER1@allianz.fr", "USER2@allianz.fr")
              .subject("[BRIN][UNBLOCKED] - La plateforme est out")
              .bodyContains("Impossible de travailler")
              .bodyContains("On prend des vacances<br> et c'est super et à Bientôt")
              .bodyContains("Pour plus de détails, ")
              .bodyContains("merci de consulter le BRIN dans l'application ")
              .bodyContains("<a href=\"http://localhost:8080/pyp/edit.html/id/unblockedBrin\">PostYourProblem</a>")
              .bodyContains("Cordialement.")
        ;

        mailFixture.assertReceivedMessagesCount(1);
    }


    @Test
    public void test_executeBrinUnBlockedWithNoDescription() throws Exception {
        Brin brin = new Brin("La plateforme est out");
        brin.setStatus(Status.unblocked);
        mailService.sendMail(brin);

        mailFixture.getReceivedMessage(0).assertThat()
              .from(System.getProperty("user.name") + "@allianz.fr")
              .to("USER1@allianz.fr", "USER2@allianz.fr")
              .subject("[BRIN][UNBLOCKED] - La plateforme est out")
              .bodyContains("Description:</b><br><br><br>")
              .bodyContains("Unblocking:</b><br><br><br><br>")
              .bodyContains("Cordialement.")
        ;

        mailFixture.assertReceivedMessagesCount(1);
    }
}
