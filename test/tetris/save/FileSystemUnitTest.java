package tetris.save;

import org.junit.Assert;
import org.junit.Test;

public class FileSystemUnitTest {
    @Test
    public void testEcriture() {
        Assert.assertEquals(0, FileSystem.save(".test.save", "test"));
    }

    @Test
    public void testErreurEcritureDansUnDossier() {Assert.assertEquals(-1, FileSystem.save("./", "test"));}

    @Test
    public void testFichierVideRetourneJSON() {
        FileSystem.save(".test.save", "");
        Assert.assertEquals(Save.JSON_EMPTY, FileSystem.load(".test.save"));
    }

    @Test
    public void testLecture() {
        FileSystem.save(".test.save", "test");
        Assert.assertEquals("test", FileSystem.load(".test.save"));
    }

    @Test
    public void testLectureDansUnDossier() {
        String data = FileSystem.load("./");
        Assert.assertEquals(Save.JSON_EMPTY, data);
    }
}
