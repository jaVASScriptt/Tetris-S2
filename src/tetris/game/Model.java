package tetris.game;

import tetris.save.FileSystem;
import tetris.save.Parser;
import tetris.save.Save;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Model {
    private static final String DEFAULT_PLAYER = "?";
    private final List<Save> saves;
    private String player;
    private Save save;

    public Model() {
        this.saves = Parser.parse(FileSystem.load());
        this.player = DEFAULT_PLAYER;
    }

    public List<Save> getSaves() {
        return saves;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<Save> getPlayerSaves() {
        return Save.filterByUserName(saves, player);
    }

    public Save createSave() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Save save = new Save(player, 0, dateFormat.format(date), timeFormat.format(date));
        this.save = save;
        return save;
    }

    public Save getSave() {
        return this.save;
    }
}
