package tetris.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tetris.game.Model;

import java.util.Objects;

public class HomeScene extends VBox {
    private final Text userDisplay;
    private final TextField userNameInput;
    private final HBox modificationPanel;
    private final VBox scorePanel;
    private final Button changeUserButton;
    private final Button confirmUserChangeButton;
    private final Button playButton;
    private final Button exitButton;
    private final Text lblVolume;
    private final HBox volumePanel;

    public HomeScene(Model model, Slider volumeSlider) {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        getStyleClass().add("background");

        this.userDisplay = new Text(model.getPlayer());
        userDisplay.getStyleClass().add("lbl");
        this.scorePanel = new VBox();
        this.changeUserButton = new Button("Modifier");
        changeUserButton.getStyleClass().add("btnUser");
        this.confirmUserChangeButton = new Button("Valider");
        confirmUserChangeButton.getStyleClass().add("btnUser");

        this.userNameInput = new TextField();
        userNameInput.setPromptText("Nouvel utilisateur");

        this.playButton = new Button("Jouer");
        playButton.getStyleClass().add("generalButton");

        this.exitButton = new Button("QUITTER");
        exitButton.getStyleClass().add("generalButton");

        this.modificationPanel = new HBox(userNameInput, confirmUserChangeButton);
        modificationPanel.setSpacing(10);
        modificationPanel.getStyleClass().add("spacer");
        modificationPanel.setAlignment(Pos.CENTER);
        modificationPanel.setVisible(false);

        ImageView userImage = new ImageView(new Image("file:icon/user.png"));
        Text lblUser = new Text("Utilisateur : ");
        lblUser.getStyleClass().add("lblBold");
        HBox userPanel = new HBox(userImage, lblUser, userDisplay, changeUserButton);
        userPanel.setSpacing(10);
        userPanel.getStyleClass().add("spacer");
        userPanel.setAlignment(Pos.CENTER);

        HBox userPane = new HBox(userPanel, modificationPanel);
        scorePanel.getStyleClass().add("vBoxScore");

        this.lblVolume = new Text("Volume : ");
        lblVolume.getStyleClass().add("lblBold");
        this.volumePanel = new HBox(lblVolume, volumeSlider);
        volumePanel.setStyle("-fx-padding: 30 0 15 10");

        VBox buttons = new VBox(playButton, exitButton, volumePanel);
        buttons.setAlignment(Pos.CENTER);

        this.getChildren().addAll(userPane, scorePanel, buttons);
    }

    public Text getUserDisplay() {
        return userDisplay;
    }

    public TextField getUserNameInput() {
        return userNameInput;
    }

    public HBox getModificationPanel() {
        return modificationPanel;
    }

    public VBox getScorePanel() {
        return scorePanel;
    }

    public Button getChangeUserButton() {
        return changeUserButton;
    }

    public Button getConfirmUserChangeButton() {
        return confirmUserChangeButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public void moveVolumeSlider(Slider volumeSlider) {
        volumePanel.getChildren().clear();
        volumePanel.getChildren().addAll(lblVolume, volumeSlider);
    }
}
