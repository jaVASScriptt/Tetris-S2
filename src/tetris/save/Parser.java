package tetris.save;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static List<Save> parse(String data) {
        List<Save> list = new ArrayList<>();
        JSONObject json = new JSONObject(data);
        JSONArray array = json.getJSONArray("saves");
        for (int i = 0; i < array.length(); i++) {
            JSONObject save = array.getJSONObject(i);
            String name = save.getString("name");
            int score = save.getInt("score");
            String date = save.getString("date");
            String heure = save.getString("heure");
            Save s = new Save(name, score, date, heure);
            list.add(s);
        }
        return list;
    }

    public static String stringify(List<Save> list) {
        StringBuilder sb = new StringBuilder("{saves:[");
        List<String> saves = new ArrayList<>();
        list.forEach(s -> saves.add(s.toJSON()));
        sb.append(String.join(",", saves));
        sb.append("]}");
        return sb.toString();
    }
}
