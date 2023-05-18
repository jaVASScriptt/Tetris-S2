package tetris.save;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveUnitTest {
    @Test
    public void testChargementDesSauvegardesDUnUtilisateur() {
        Save s1 = new Save("test", 0, "", "");
        Save s2 = new Save("", 0, "", "");

        List<Save> listSave = Arrays.asList(s1, s1, s2, s1, s2, s1);
        List<Save> listSaveFiltered = Save.filterByUserName(listSave, "test");
        Assert.assertEquals(4, listSaveFiltered.size());
    }

    @Test
    public void testAjoutDUneSauvegardeAvecListeSizePlusPetitQueLeMax() {
        Save s4 = new Save("", 1, "", "");
        Save s3 = new Save("", 10, "", "");
        Save s2 = new Save("", 100, "", "");
        Save s1 = new Save("", 1000, "", "");

        Save s = new Save("test", 1000, "", "");
        Save newSave = new Save("", 50, "", "");

        ArrayList<Save> saves = new ArrayList<>(Arrays.asList(s1, s2, s3, s, s4, s));
        ArrayList<Save> oracle = new ArrayList<>(Arrays.asList(s, s, s1, s2, newSave, s3, s4));

        Save.persist(saves, newSave);
        Assert.assertEquals(oracle, saves);
    }

    @Test
    public void testAjoutDUneSauvegardeAvecListeSizeEgaleOuSuperieurAuMax() {
        Save s5 = new Save("", 1, "", "");
        Save s4 = new Save("", 10, "", "");
        Save s3 = new Save("", 100, "", "");
        Save s2 = new Save("", 1000, "", "");
        Save s1 = new Save("", 10000, "", "");

        Save s = new Save("test", 1000, "", "");
        Save newSave = new Save("", 10001, "", "");

        ArrayList<Save> saves = new ArrayList<>(Arrays.asList(s1, s2, s, s3, s, s4, s5));
        ArrayList<Save> oracle = new ArrayList<>(Arrays.asList(s, s, newSave, s1, s2, s3, s4));

        Save.persist(saves, newSave);
        Assert.assertEquals(oracle, saves);
    }
}