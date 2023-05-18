package tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tetris.game.Audio;
import tetris.game.Model;
import tetris.game.UserModificationEventHandler;
import tetris.save.FileSystem;
import tetris.save.Parser;
import tetris.save.Save;
import tetris.scenes.GameScene;
import tetris.scenes.HomeScene;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Model model = new Model();
        stage.getIcons().add(new Image("file:icon/tetris.png"));
        stage.setTitle("Tetris");
        stage.setResizable(false);

        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setBlockIncrement(5);
        volumeSlider.setMinorTickCount(2);
        volumeSlider.setMajorTickUnit(20);
        volumeSlider.getStyleClass().add("sliderHome");

        HomeScene homeScene = new HomeScene(model, volumeSlider);
        Scene scene = new Scene(homeScene);

        List<Save> saves = model.getSaves();
        if(saves.size() > 0) {
            model.setPlayer(saves.get(saves.size() - 1).getName());
            UserModificationEventHandler.loadSaves(model, stage, homeScene);
        }

        Audio audio = new Audio("soundtrack.wav");

        homeScene.getExitButton().setOnAction(ignored -> stage.close());
        homeScene.getPlayButton().setOnAction(ignored -> {
            audio.setLoopPoint(Audio.GAME_LOOP);
            GameScene gameScene = new GameScene(model, scene, volumeSlider, audio);
            gameScene.getMenuButton().setOnAction(ign -> {
                Save.persist(model.getSaves(), model.getSave());
                FileSystem.save(Parser.stringify(model.getSaves()));
                UserModificationEventHandler.loadSaves(model, stage, homeScene);
                audio.play();
                homeScene.moveVolumeSlider(volumeSlider);
                volumeSlider.getStyleClass().remove("sliderGame");
                volumeSlider.getStyleClass().add("sliderHome");
                scene.setRoot(homeScene);
                stage.sizeToScene();
            });
            volumeSlider.getStyleClass().remove("sliderHome");
            volumeSlider.getStyleClass().add("sliderGame");
            scene.setRoot(gameScene);
            stage.sizeToScene();
        });
        homeScene.getChangeUserButton().setOnAction(ignored -> homeScene.getModificationPanel().setVisible(true));
        homeScene.getConfirmUserChangeButton().setOnAction(new UserModificationEventHandler(model, stage, homeScene));
        volumeSlider.valueProperty().addListener((observableValue, number, t1) -> audio.setVolume((Double) number));

        stage.setScene(scene);
        stage.show();
    }
}