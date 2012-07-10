package net.codjo.pyp.xml;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.model.Team;
import net.codjo.test.common.XmlUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 */
public class XmlCodecTest {
    private XmlCodec xmlCodec = new XmlCodec();


    @Test
    public void test_toXml() throws Exception {
        List<Brin> brins = new ArrayList<Brin>();

        Brin brin = new Brin("titre1");
        brin.setUuid("MonUUID");
        brin.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2011-06-01"));
        brin.setDescription("description1");
        brins.add(brin);

        Brin titre2 = new Brin("titre2");
        titre2.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2011-06-01"));
        titre2.setDescription("description2");
        List<Team> affectedTeams = new ArrayList<Team>();
        affectedTeams.add(Team.gacpa);
        affectedTeams.add(Team.rdm_codaf);
        titre2.setAffectedTeams(affectedTeams);
        brins.add(titre2);

        String actual = xmlCodec.toXml(brins);

        XmlUtil.assertEquivalent("<brinList>"
                                 + "  <repository>\n"
                                 + "    <brin>\n"
                                 + "      <uuid>MonUUID</uuid>\n"
                                 + "      <title>titre1</title>\n"
                                 + "      <description>description1</description>\n"
                                 + "      <creationDate>2011-06-01 00:00:00.0 CEST</creationDate>\n"
                                 + "      <affectedTeams/>\n"
                                 + "      <status>current</status>\n"
                                 + "    </brin>\n"
                                 + "    <brin>\n"
                                 + "      <title>titre2</title>\n"
                                 + "      <description>description2</description>\n"
                                 + "      <creationDate>2011-06-01 00:00:00.0 CEST</creationDate>\n"
                                 + "      <affectedTeams>\n"
                                 + "           <team>gacpa</team>\n"
                                 + "           <team>rdm_codaf</team>\n"
                                 + "      </affectedTeams>\n"
                                 + "      <status>current</status>\n"
                                 + "    </brin>"
                                 + "  </repository>\n"
                                 + "</brinList>", actual);
    }


    @Test
    public void test_fromXml() throws Exception {
        String expected = "<brinList>"
                          + "  <repository>\n"
                          + "    <brin>\n"
                          + "      <uuid>uniqueUuid</uuid>\n"
                          + "      <title>titre1</title>\n"
                          + "      <description>description1</description>\n"
                          + "      <creationDate>2011-06-01 00:00:00.0 CEST</creationDate>\n"
                          + "      <unblockingDate>2011-06-01 00:00:00.0 CEST</unblockingDate>\n"
                          + "      <unblockingType>medium</unblockingType>\n"
                          + "      <unblockingDescription>Bon ben c'est bon</unblockingDescription>\n"
                          + "      <affectedTeams/>\n"
                          + "      <status>current</status>\n"
                          + "    </brin>\n"
                          + "    <brin>\n"
                          + "      <title>titre2</title>\n"
                          + "      <description>description2</description>\n"
                          + "      <creationDate>2011-06-01 00:00:00.0 CEST</creationDate>\n"
                          + "      <affectedTeams>\n"
                          + "      <team>rdm_codaf</team>\n"
                          + "      <team>transverse</team>\n"
                          + "      </affectedTeams>\n"
                          + "      <rootCause>Pas d'électricité</rootCause>\n"
                          + "      <status>current</status>\n"
                          + "    </brin>"
                          + "  </repository>\n"
                          + "</brinList>";

        String actual = xmlCodec.toXml(xmlCodec.fromXml(expected));

        XmlUtil.assertEquivalent(expected, actual);
    }


    @Test
    public void test_pattern() throws Exception {
        Pattern pattern = Pattern.compile("\\$([A-Za-z0-9]+)\\$");

        matchons(pattern, "dollar($)", 0);
        matchons(pattern, "$variable$", 1);
        matchons(pattern, "dollar($) ha mais j'ai fait expres de mettre $ dans la phrase", 0);
        matchons(pattern, "dollar($vraiVariable$) dans la phrase", 1);
        matchons(pattern, "dollar($vraiVa2riable$) dans la phrase", 1);
        matchons(pattern, "dollar($vraiVa 2riable$) dans la phrase", 0);
        matchons(pattern, "dollar($vraiVariable$$deuxiemeVariable$) dans la phrase", 2);
        matchons(pattern, "dollar($vraiVariable$$deuxiemeVariable) dans la phrase", 1);
        matchons(pattern, "dollar($vraiVariable$a$deuxiemeVariable$) dans la phrase", 2);
        matchons(pattern, "dollar($$) dans la phrase", 0);
    }

    private void matchons(Pattern pattern, String textToAnalyse, int expected) {
        System.out.println("-----------  textToAnalyse = " + textToAnalyse);
        Matcher matcher = pattern.matcher(textToAnalyse);

        int actual = 0;

        while (matcher.find()) {
            actual++;
            System.out.println("found : " + matcher.group());
        }
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void test_rez() throws Exception {
        Pattern compile = Pattern.compile("\\$([A-Za-z]+)*\\$");

        String input="dhsf$ddd$$dadd$sqddqqdq";
         input="dhsf$sdsdsd";
        Matcher matcher = compile.matcher(input);
        
        while (matcher.find()){
            System.out.println("matcher.group() = " + matcher.group());
        }
        
        Assert.assertTrue(matcher.matches());
        System.out.println("compile.matcher(input).matches() = " + matcher.groupCount());
    }
}
