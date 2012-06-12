package net.codjo.pyp.pages;
import net.codjo.pyp.WicketFixture;
import net.codjo.test.common.Directory.NotDeletedException;
import net.codjo.test.common.fixture.DirectoryFixture;
import org.apache.wicket.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 *
 */
public class HomePageTest extends WicketFixture {
    DirectoryFixture fixture = new DirectoryFixture("target/pyp");


    @Before
    public void setUp() throws NotDeletedException {
        fixture.doSetUp();
        doInit("/pyp.properties");
    }


    @After
    public void tearDown() throws NotDeletedException {
        fixture.doTearDown();
    }


    @Test
    public void test_homePage() throws Exception {
        getWicketTester().startPage(HomePage.class);
        getWicketTester().assertContains("Title");
        getWicketTester().assertContains("Creation");
        getWicketTester().assertContains("Status");
        getWicketTester().assertContains("Add new BRIN");

        assertTextIsNotPresent("thisIsA new Brin");

        getWicketTester().assertLabel("leftPanel:leftContainer:infoList:0:statusLabel", "current");
        getWicketTester().assertLabel("leftPanel:leftContainer:infoList:1:statusLabel", "unblocked");
        getWicketTester().assertLabel("leftPanel:leftContainer:infoList:2:statusLabel", "toEradicate");
        getWicketTester().assertLabel("leftPanel:leftContainer:infoList:3:statusLabel", "eradicated");

        getWicketTester().assertLabel("leftPanel:leftContainer:infoList:0:nbBrin", "0");

        addNewBrin("thisIsA new Brin", 2);

        getWicketTester().assertRenderedPage(HomePage.class);
        getWicketTester().assertLabel("leftPanel:leftContainer:infoList:2:nbBrin", "1");
        getWicketTester().assertContains("thisIsA new Brin");
        getWicketTester().assertContains("toEradicate");

        updateBrin("titre Modifié", 2);
        getWicketTester().assertRenderedPage(HomePage.class);
        getWicketTester().assertContains("titre Modifié");
        getWicketTester().assertContains("toEradicate");
    }


    private void addNewBrin(String title, int statusIndex) {
        getWicketTester().assertRenderedPage(HomePage.class);
        getWicketTester().clickLink("rightPanel:myContainer:menuList:0:imageLink");
        getWicketTester().assertRenderedPage(BrinEditPage.class);

        getWicketTester().setParameterForNextRequest("brinForm:title", title);
        getWicketTester().setParameterForNextRequest("brinForm:status", statusIndex);
        getWicketTester().submitForm("brinForm");
        getWicketTester().assertErrorMessages(new String[]{"Le champ 'creationDate' est obligatoire."});

        Session.get().cleanupFeedbackMessages();
        getWicketTester().setParameterForNextRequest("brinForm:title", title);
        getWicketTester().setParameterForNextRequest("brinForm:status", statusIndex);
        getWicketTester().setParameterForNextRequest("brinForm:creationDate", "2001-01-12");
        getWicketTester().submitForm("brinForm");

        getWicketTester().assertNoErrorMessage();
    }


    private void updateBrin(String title, int statusIndex) {
        getWicketTester().clickLink("brinList:1:editBrin");
        getWicketTester().assertRenderedPage(BrinEditPage.class);

        getWicketTester().setParameterForNextRequest("brinForm:title", title);
        getWicketTester().setParameterForNextRequest("brinForm:status", statusIndex);
        getWicketTester().setParameterForNextRequest("brinForm:creationDate", "2001-01-12");

        getWicketTester().submitForm("brinForm");

        getWicketTester().assertNoErrorMessage();
    }
}
