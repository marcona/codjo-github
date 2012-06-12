package net.codjo.pyp.services;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.model.Status;
import net.codjo.pyp.model.Team;
import net.codjo.pyp.model.UnblockingType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 */
public class CsvServiceTest extends TestCase {

    @Test
    public void test_exportempty() throws Exception {
        StringBuilder export = CsvService.export(new ArrayList<Brin>());
        Assert.assertEquals(115, export.length());
        Assert.assertEquals(
              "title;creationDate;status;description;affectedTeams;unblockingType;unblockingDate;unblockingDescription;rootCause;\n",
              export.toString());
    }


    @Test
    public void test_exportList() throws Exception {
        List<Brin> brinList = new ArrayList<Brin>();
        Brin brin = new Brin("a title");
        brin.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2011-11-28"));
        brin.setStatus(Status.unblocked);
        brin.setDescription("a description très compliquée");
        brin.setAffectedTeams(Arrays.asList(Team.frm, Team.rdm_codaf));
        brin.setunblockingType(UnblockingType.medium);
        brin.setUnBlockingDate(new SimpleDateFormat("yyyy-MM-dd").parse("2011-11-30"));
        brin.setUnBlockingDescription("toto\ntata a la ligne");
        brin.setRootCause("root cause \ndu problème");
        brinList.add(brin);

        Brin emptyBrin = new Brin("empty");
        brinList.add(emptyBrin);

        StringBuilder export = CsvService.export(brinList);

        String[] split = export.toString().split(";\n");
        Assert.assertEquals(
              "title;creationDate;status;description;affectedTeams;unblockingType;unblockingDate;unblockingDescription;rootCause",
              split[0]);
        Assert.assertEquals(
              "a title;2011-11-28;unblocked;\"a description très compliquée\";[frm, rdm_codaf];medium;2011-11-30;\"toto\ntata a la ligne\";\"root cause \ndu problème\"",
              split[1]);
        Assert.assertEquals(
              "empty;" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ";current;\"\";;;;\"\";\"\"",
              split[2]);
    }
}
