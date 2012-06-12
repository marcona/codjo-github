package net.codjo.pyp.services;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.model.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
/**
 *
 */
public class BrinServiceTest {
    private BrinService service = new BrinService("");
    private List<Brin> list;


    @Before
    public void setUp() throws Exception {
        list = new ArrayList<Brin>();
        int index = 0;
        String uuid = "uuid";
        addGenericBrin(list, uuid + index);
        index++;
        addGenericBrin(list, uuid + index);
        index++;
        addGenericBrin(list, uuid + index);
        index++;
        addGenericBrin(list, uuid + index);
        index++;
        addGenericBrin(list, uuid + index);
        index++;
        addGenericBrin(list, uuid + index);
    }


    @Test
    public void test_updateBrinInListInTheMiddle() throws Exception {
        Brin brinToUpdate = new Brin("NewTitle");
        brinToUpdate.setUuid("uuid2");
        List<Brin> resultList = service.updateBrinInList(brinToUpdate, list);

        assertThat(6, is(resultList.size()));
        assertThat("NewTitle", is(resultList.get(2).getTitle()));
    }


    @Test
    public void test_updateBrinBug() throws Exception {
        Brin brinToUpdate = new Brin("NewTitle");
        brinToUpdate.setUuid("6eeb55dc-1962-479f-851d-b063ab74b0b4");

        list.clear();
        addGenericBrin(list, "6eeb55dc-1962-479f-851d-b063ab74b0b4");
        addGenericBrin(list, "3dfd8048-07c0-41cc-b474-8185c5866a19");
        addGenericBrin(list, "f9bc04cb-b90f-4812-9500-718cbf895f5d");
        addGenericBrin(list, "7");
        addGenericBrin(list, "1");
        addGenericBrin(list, "2");
        addGenericBrin(list, "9");

        List<Brin> resultList = service.updateBrinInList(brinToUpdate, list);

        assertThat(7, is(resultList.size()));
        assertThat("NewTitle", is(resultList.get(0).getTitle()));
    }


    @Test
    public void test_updateBrinInListAtTheBeginning() throws Exception {
        Brin brinToUpdate = new Brin("NewTitleDebutListe");
        brinToUpdate.setUuid("uuid0");

        List<Brin> resultList = service.updateBrinInList(brinToUpdate, list);
        assertThat(6, is(resultList.size()));
        assertThat("NewTitleDebutListe", is(resultList.get(0).getTitle()));
    }


    @Test
    public void test_updateBrinInListAtTheEnd() throws Exception {
        Brin brinToUpdate = new Brin("NewTitleFinListe");
        brinToUpdate.setUuid("uuid5");
        List<Brin> resultList = service.updateBrinInList(brinToUpdate, list);

        assertThat(6, is(resultList.size()));
        assertThat("NewTitleFinListe", is(resultList.get(5).getTitle()));
    }


    @Test
    public void test_updateBrinInListNotFound() throws Exception {
        Brin brinToUpdate = new Brin("TitreXXX");
        brinToUpdate.setTitle("NewTitle");
        brinToUpdate.setUuid("uuidNotFound");
        List<Brin> resultList = service.updateBrinInList(brinToUpdate, list);

        assertThat(6, is(resultList.size()));
    }


    @Test
    public void test_calculateBrinNumber() throws Exception {

        list.get(3).setStatus(Status.eradicated);

        Map<Status, Integer> map = service.calculateBrinNumber(list);

        assertThat(4, is(map.size()));
        assertThat(5, is(map.get(Status.current)));
        assertThat(0, is(map.get(Status.unblocked)));
        assertThat(0, is(map.get(Status.toEradicate)));
        assertThat(1, is(map.get(Status.eradicated)));
    }


    @Test
    public void test_loadConfig() throws Exception {
        String configPath = getClass().getResource("/PypRepository.xml").getPath();
        service = new BrinService(configPath);

        List<Brin> brins = service.loadConfig();
        assertThat(5, is(brins.size()));

        assertThat("uuidCinq", is(brins.get(0).getUuid()));
        assertThat(true, is(brins.get(0).getDescription().contains("\n")));
        assertThat("uuidDeux", is(brins.get(1).getUuid()));
        assertThat("uuidUn", is(brins.get(2).getUuid()));
        assertThat("uuidQuatre", is(brins.get(3).getUuid()));
        assertThat("uuidtrois", is(brins.get(4).getUuid()));
    }


    @Test
    public void test_getBrin() throws Exception {
        String configPath = getClass().getResource("/PypRepository.xml").getPath();
        service = new BrinService(configPath);

        Brin brin = service.getBrin("uuidCinq");

        assertThat("uuidCinq", is(brin.getUuid()));
        assertThat("current Date plus recente", is(brin.getTitle()));
    }


    private void addGenericBrin(List<Brin> brinList, String index) {
        Brin tmpBrin = new Brin("Titre" + index);
        tmpBrin.setUuid(index);
        brinList.add(tmpBrin);
    }
}
