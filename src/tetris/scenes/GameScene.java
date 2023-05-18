package tetris.scenes;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tetris.game.Audio;
import tetris.game.Shape;
import tetris.game.Model;
import tetris.game.Tetrimino;
import tetris.save.Save;

import java.util.ArrayList;
import java.util.Objects;

public class GameScene extends VBox {
    private final Save save;
    private final Text scoreField;
    private final Text completedLinesField;
    private final Text multiplicateurField;
    private final Scene scene;
    private final Audio audio;
    private final StackPane stackPane;
    private final Rectangle boardShade;
    private final BorderPane pauseCenter, gameOverCenter;
    private int linesCleared;
    private final ArrayList<Tetrimino> newTetriminos = new ArrayList<>();
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Tetrimino> stopped = new ArrayList<>();
    private final ArrayList<Tetrimino> nextShapeList = new ArrayList<>();
    private final GridPane grid;
    private final GridPane previewGrid;
    private final Button menuButton;
    private Timeline play;
    private Shape nextShape;

    public static final int COLUMNS = 10;
    public static final int LINES = 20;
    boolean right = false;
    boolean left = false;
    boolean down = false;

    Timeline handleDirection;
    int mult;

    public GameScene(Model model, Scene scene, Slider volumeSlider, Audio audio) {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());

        this.scene = scene;
        this.grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.getStyleClass().add("background");

        previewGrid = new GridPane();
        previewGrid.getStyleClass().add("grid");
        for (int i = 0; i < 4; i++) {
            previewGrid.add(new Rectangle(35, 35, Color.TRANSPARENT), i, 0);
        }
        previewGrid.setStyle("-fx-padding: 25 15 10 0;");

        this.audio = audio;
        this.save = model.createSave();
        this.linesCleared = 0;
        this.mult = 1;

        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < LINES; j++) {
                grid.add(new Tetrimino(i, j, Color.GRAY, true), i, j);
            }
        }

        HBox topPanel = new HBox(new Text("Tetris"));
        topPanel.getStyleClass().add("title");

        VBox bottomSidePanel = new VBox();
        bottomSidePanel.getStyleClass().add("background");
        bottomSidePanel.getStyleClass().add("lbl");

        Text volumeField = new Text("Volume :");
        volumeField.getStyleClass().add("lbl");
        bottomSidePanel.getChildren().addAll(volumeField, volumeSlider);

        scoreField = new Text(save.getScore());
        scoreField.getStyleClass().add("score");
        completedLinesField = new Text("0");
        completedLinesField.getStyleClass().add("score");
        multiplicateurField = new Text("x1");
        multiplicateurField.getStyleClass().add("score");

        Text lblScore = new Text("Score");
        lblScore.getStyleClass().add("lbl");
        Text lblCompletedLines = new Text("Lignes complétées");
        lblCompletedLines.getStyleClass().add("lbl");
        Text lblMultiplicateur = new Text("Multiplicateur");
        lblMultiplicateur.getStyleClass().add("lbl");

        Text lblNextForm = new Text("Pièce suivante");
        lblNextForm.getStyleClass().add("lbl");

        menuButton = new Button("Menu principal");
        menuButton.getStyleClass().add("btnMain");

        VBox topSidePanel = new VBox();
        topSidePanel.getStyleClass().add("background");
        topSidePanel.getChildren().addAll(menuButton, scoreField, lblScore, completedLinesField, lblCompletedLines, multiplicateurField, lblMultiplicateur, previewGrid, lblNextForm);

        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("background");
        borderPane.setTop(topSidePanel);
        borderPane.setBottom(bottomSidePanel);

        stackPane = new StackPane();
        ImageView pauseImg = new ImageView(new Image("file:icon/pause.png"));
        ImageView gameOverImg = new ImageView(new Image("file:icon/gameover.png"));

        pauseCenter = new BorderPane();
        pauseCenter.setCenter(pauseImg);

        gameOverCenter = new BorderPane();
        gameOverCenter.setCenter(gameOverImg);

        boardShade = new Rectangle(COLUMNS * 35 + 2 * (COLUMNS - 1), LINES * 35 + 2 * (LINES - 1));
        boardShade.setOpacity(0.5);
        boardShade.setFill(Color.BLACK);
        boardShade.toFront();

        stackPane.getChildren().addAll(grid);

        HBox main = new HBox();
        main.getStyleClass().add("background");
        main.setPadding(new Insets(5.0));
        main.setSpacing(25.0);

        main.getChildren().addAll(stackPane, borderPane);

        this.getChildren().addAll(main);
        start();
    }

    public Save getSave() {
        return save;
    }

    public void addTetrimino(Tetrimino ajt) {
        grid.add(ajt, ajt.getX(), ajt.getY());
        newTetriminos.add(ajt);
    }

    public void removeTetrimino(Tetrimino enl) {

        grid.getChildren().remove(enl);
        newTetriminos.remove(enl);
    }

    public void addShape(Shape form) {
        shapes.add(form);

        for (int i = 0; i < 4; i++) {
            this.addTetrimino(form.getTetrimino(i));
        }

        nextShape = Shape.createFormes();
        nextShape(nextShape);
    }

    public void nextShape(Shape form) {
        for (Tetrimino x : nextShapeList) {
            previewGrid.getChildren().remove(x);
        }
        nextShapeList.clear();

        for (int i = 0; i < 4; i++) {
            nextShapeList.add(form.getTetrimino(i));
            previewGrid.add(form.getTetrimino(i), form.getTetrimino(i).getX() - 3, form.getTetrimino(i).getY());
        }
    }

    public void removeLine(int y) {
        for (Tetrimino tetrimino : stopped) {
            if (tetrimino.getY() == y) {
                grid.getChildren().remove(tetrimino);
            }
        }
    }

    public void stop() {
        for (int i = 0; i < 4; i++) {
            stopped.add(newTetriminos.get(0));
            this.lastShape().setCase(i, newTetriminos.get(0));
            removeTetrimino(newTetriminos.get(0));
        }

        for (int i = 1; i < 5; i++) {
            grid.add(stopped.get(stopped.size() - i), stopped.get(stopped.size() - i).getX(), stopped.get(stopped.size() - i).getY());
        }
    }

    public void clearLines() {
        int j;
        int lines = 0;
        int x;
        int y;
        Color color;
        int max = 0;

        ArrayList<Tetrimino> temp = new ArrayList<>();
        ArrayList<Tetrimino> temp2 = new ArrayList<>();

        for (int i = 0; i < LINES; i++) {
            j = 0;
            temp.clear();

            for (Tetrimino c : this.occupe()) {
                if (c.getY() == i) {
                    j++;
                    temp.add(c);
                }
            }
            if (j == COLUMNS) {
                if (i > max) {
                    max = i;
                }
                lines++;
                this.removeLine(i);
                for (Tetrimino c : temp) {
                    stopped.remove(c);
                }
            }
        }
        for (Tetrimino c : this.occupe()) {
            if (c.getY() < max) {
                temp2.add(c);
            }
        }

        for (Tetrimino c : stopped) {
            grid.getChildren().remove(c);
        }

        for (int i = 0; i < stopped.size(); i++) {
            x = stopped.get(i).getX();
            y = temp2.contains(stopped.get(i)) ? stopped.get(i).getY() + lines : stopped.get(i).getY();
            color = stopped.get(i).getColor();
            stopped.set(i, new Tetrimino(x, y, color, false));
        }

        for (Tetrimino tetrimino : stopped) {
            grid.add(tetrimino, tetrimino.getX(), tetrimino.getY());
        }

        linesCleared += lines;
        mult = 1 + linesCleared / 10;
        save.incrementScore(lines, mult);
        scoreField.setText(save.getScore());
        completedLinesField.setText(String.valueOf(linesCleared));
        multiplicateurField.setText("x" + mult);
    }

    public ArrayList<Tetrimino> nouvCases() {
        return newTetriminos;
    }

    public ArrayList<Tetrimino> occupe() {
        return stopped;
    }

    public Shape lastShape() {
        return shapes.get(shapes.size() - 1);
    }

    public void start() {
        Shape shape = Shape.createFormes();
        addShape(shape);
        Timeline tickSlow = new Timeline(new KeyFrame(Duration.millis(1000), ignored -> {
            if (!down) tick();
        }));
        tickSlow.setCycleCount(Timeline.INDEFINITE);
        tickSlow.play();

        Timeline tickFast = new Timeline(new KeyFrame(Duration.millis(100), ignored -> {
            if (down) tick();
        }));
        tickFast.setCycleCount(Timeline.INDEFINITE);
        tickFast.play();

        handleDirection = new Timeline(new KeyFrame(Duration.millis(100), ignored -> {
            if (right) this.lastShape().strafeRight(this);
            if (left) this.lastShape().strafeLeft(this);
        }));
        handleDirection.setCycleCount(Timeline.INDEFINITE);
        handleDirection.play();

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case Q -> left = true;
                case D -> right = true;
                case S -> down = true;
                case Z -> this.lastShape().turn(this);
                case F -> {
                    while (!this.lastShape().getDown()) {
                        this.lastShape().down(this);
                    }
                }
                case ESCAPE -> {
                    if (tickSlow.getStatus() == Animation.Status.RUNNING) {
                        stackPane.getChildren().addAll(boardShade, pauseCenter);
                        audio.stop();
                        tickSlow.pause();
                        handleDirection.pause();
                    } else {
                        stackPane.getChildren().removeAll(boardShade, pauseCenter);
                        audio.play();
                        tickSlow.play();
                        handleDirection.play();
                    }
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case Q -> left = false;
                case D -> right = false;
                case S -> down = false;
            }
        });

        play = new Timeline(new KeyFrame(Duration.millis(10), ignored -> {
            if (this.lastShape().getDown()) {
                handleDirection.stop();
                this.lastShape().putAgain();
                this.stop();
                this.clearLines();
                Shape f = occupe().isEmpty() ? Shape.createFormes() : nextShape;
                addShape(f);
                if (f.isDownOccupied(this)) {
                    play.stop();
                    handleDirection.stop();
                    tickSlow.stop();
                    tickFast.stop();
                    stackPane.getChildren().addAll(boardShade, gameOverCenter);
                    audio.stop();
                }
                handleDirection.play();
            }
        }));
        play.setCycleCount(Timeline.INDEFINITE);
        play.play();
    }

    public Button getMenuButton() {
        return menuButton;
    }

    private void tick() {
        this.lastShape().down(this);
    }
}
