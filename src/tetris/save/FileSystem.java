package tetris.save;

import java.io.*;

public class FileSystem {
    private static final String DEFAULT_PATH = ".tetris.save";

    public static int save(String data) {
        return save(DEFAULT_PATH, data);
    }

    public static String load() {
        return load(DEFAULT_PATH);
    }

    public static int save(String path, String data) {
        try {
            Writer fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String load(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            if (sb.toString().equals("")) {
                return Save.JSON_EMPTY;
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return Save.JSON_EMPTY;
        }
    }
}