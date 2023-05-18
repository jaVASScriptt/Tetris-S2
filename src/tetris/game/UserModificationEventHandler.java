package tetris.game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tetris.save.Save;
import tetris.scenes.HomeScene;

import java.util.Collections;
import java.util.List;

public class UserModificationEventHandler implements EventHandler<ActionEvent> {
    private final Stage stage;
    private final Model model;
    private final HomeScene scene;

    public UserModificationEventHandler(Model model, Stage stage, HomeScene scene) {
        this.model = model;
        this.stage = stage;
        this.scene = scene;
    }

    @Override
    public void handle(ActionEvent event) {
        TextField userInput = scene.getUserNameInput();
        String user = userInput.getText();
        model.setPlayer(user);
        loadSaves(model, stage, scene);
    }

    public static void loadSaves(Model model, Stage stage, HomeScene scene) {
        Text userDisplayText = scene.getUserDisplay();
        HBox modificationBox = scene.getModificationPanel();
        VBox scoreBox = scene.getScorePanel();

        userDisplayText.setText(model.getPlayer());
        modificationBox.setVisible(false);

        scoreBox.getChildren().clear();

        Text textBestScores = new Text("Meilleurs Scores");
        textBestScores.getStyleClass().addAll("bold", "big");
        scoreBox.getChildren().add(textBestScores);
        List<Save> saves = model.getPlayerSaves();
        saves.sort(Collections.reverseOrder());
        for (int i = 0; i < Math.min(saves.size(), Save.MAX_NUMBER_OF_SAVE); i++) {
            Save save = saves.get(i);

            Text scoreText = new Text(save.getScore());
            scoreText.getStyleClass().add("bold");
            Text dateText = new Text(save.getDate());
            dateText.getStyleClass().add("bold");
            Text heureText = new Text(save.getHeure());
            heureText.getStyleClass().add("bold");
            HBox scores = new HBox(new Text((i + 1) + ". "), scoreText, new Text(" le "), dateText, new Text(" à "), heureText);
            scores.getStyleClass().add("tScores");
            scoreBox.getChildren().add(scores);
        }

        if (saves.size() == 0) {
            HBox box = new HBox(new Text("Aucune sauvegarde, jouez une partie pour en créer une !"));
            box.getStyleClass().add("tScores");
            scoreBox.getChildren().add(box);
        }

        stage.sizeToScene();
    }
}
