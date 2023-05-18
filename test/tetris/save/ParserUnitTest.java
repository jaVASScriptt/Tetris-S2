package tetris.save;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class ParserUnitTest {
    @Test
    public void testParsing() {
        Save s = new Save("leo", 0, "11/10/2002", "00:00");
        String data = "{saves:[{name: \"leo\", score: 0, date: \"11/10/2002\", heure: \"00:00\"}]}";
        List<Save> list = Parser.parse(data);
        Save test = list.get(0);

        Assert.assertEquals(s, test);
    }

    @Test
    public void testStringify() {
        Save s = new Save("", 0, "", "");
        List<Save> saves = Arrays.asList(s, s);
        String data = Parser.stringify(saves);

        Assert.assertEquals("{saves:[{name: \"\", score: 0.0, date: \"\", heure: \"\"},{name: \"\", score: 0.0, date: \"\", heure: \"\"}]}", data);
    }
}
