package net.codjo.pyp.services;
import java.io.IOException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 *
 */
public class ConfluenceServiceTest {
    private ConfluenceService confluenceService;


    @Before
    public void setup() throws IOException {
        PropertyLoader properties = new PropertyLoader("/pyp.properties");
        confluenceService = new ConfluenceService(properties.getConfluenceSpaceKey(), properties.getConfluencePage());
    }


    @Test
    public void test_extractUserList() throws Exception {
        String confluenceContent = "ignored text\n"
                                   + "* VILLARD  (*g) \n"
                                   + "\n"
                                   + "+Equipe 5+\n"
                                   + "\n"
                                   + "* *GALABER* (Responsable de liste) (*g)\n"
                                   + "* externe.toto (*g)\n"
                                   + "* mailNonPris\n"
                                   + "*   CASSAGC (*g)";

        Set userList = confluenceService.extractDeveloperList(confluenceContent);

        Assert.assertEquals("[CASSAGC, GALABER, VILLARD, externe.toto]", userList.toString());
    }
}
