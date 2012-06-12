package net.codjo.pyp;
import java.util.Collection;
import javax.servlet.http.Cookie;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.wicket.util.tester.WicketTester;
/**
 *
 */
public class WicketFixture {
    private WicketTester wicketTester;


    public void doInit(String propertyFilePath) {
        wicketTester = new WicketTester(new PypApplication(propertyFilePath));
    }


    public void doTearDown() {
    }


    public WicketTester getWicketTester() {
        return wicketTester;
    }


    public void assertTextIsNotPresent(String textToFind) {
        try {
            wicketTester.assertContains(textToFind);
        }
        catch (AssertionFailedError e) {
            return;
        }
        Assert.fail("Le texte : " + textToFind + " devrait etre absent de la page");
    }


    protected Collection<Cookie> getCookieList() {
        return getWicketTester().getServletResponse().getCookies();
    }


    protected void setContextAttribute(String applicationConfigFile, String attributeName) {
        if (applicationConfigFile != null && attributeName != null) {
            getWicketTester().getApplication()
                  .getServletContext()
                  .setAttribute(attributeName,
                                applicationConfigFile);
        }
    }
}
