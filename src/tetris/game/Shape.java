package tetris.game;

import javafx.scene.paint.Color;
import tetris.scenes.GameScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class Shape {
    public static final Color[] COLORS = {
            Color.rgb(248, 121, 41),
            Color.rgb(11, 165, 223),
            Color.rgb(192, 58, 180),
            Color.rgb(135, 212, 47),
            Color.rgb(215, 23, 53),
            Color.rgb(44, 87, 220),
            Color.rgb(251, 187, 49)
    };

    ArrayList<Tetrimino> tetriminos = new ArrayList<>();
    private int actualTurnState = 0;

    public void addTetrimino(int x1, int x2, int x3, int x4, int y1, int y2, int y3, int y4, Color color) {
        tetriminos.add(new Tetrimino(x1, y1, color, false));
        tetriminos.add(new Tetrimino(x2, y2, color, false));
        tetriminos.add(new Tetrimino(x3, y3, color, false));
        tetriminos.add(new Tetrimino(x4, y4, color, false));
    }

    public Tetrimino getTetrimino(int i) {
        return tetriminos.get(i);
    }

    public void setCase(int i, Tetrimino tetrimino) {
        tetriminos.set(i, tetrimino);
    }

    public void incrementTurnState() {
        if (actualTurnState == 3) {
            actualTurnState = 0;
        } else {
            actualTurnState++;
        }
    }

    public void down(GameScene plateau) {
        if (!isDownOccupied(plateau)) {
            for (int i = 0; i < 4; i++) {
                plateau.removeTetrimino(plateau.nouvCases().get(0));
                plateau.lastShape().getTetrimino(i).setY(plateau.lastShape().getTetrimino(i).getY() + 1);
                plateau.addTetrimino(plateau.lastShape().getTetrimino(i));
            }
        } else {
            plateau.lastShape().finishDown();
        }
    }

    public void strafeRight(GameScene plateau) {
        if (!isRightOccupied(plateau)) {
            for (int i = 0; i < 4; i++) {
                plateau.removeTetrimino(plateau.nouvCases().get(0));
                plateau.lastShape().getTetrimino(i).setX(plateau.lastShape().getTetrimino(i).getX() + 1);
                plateau.addTetrimino(plateau.lastShape().getTetrimino(i));
            }
        }
    }

    public void strafeLeft(GameScene plateau) {

        if (!isLeftOccupied(plateau)) {
            for (int i = 0; i < 4; i++) {
                plateau.removeTetrimino(plateau.nouvCases().get(0));
                plateau.lastShape().getTetrimino(i).setX(plateau.lastShape().getTetrimino(i).getX() - 1);
                plateau.addTetrimino(plateau.lastShape().getTetrimino(i));
            }
        }
    }

    public void turn(GameScene grid) {
        ArrayList<Integer> coord = grid.lastShape().turn();
        int surplus = 0;

        if (!isUpOccupied(grid) && !isTurnPossible(coord, grid)) {
            for (int i = 0; i < 4; i++) {
                grid.removeTetrimino(grid.nouvCases().get(0));
                Tetrimino tetrimino = grid.lastShape().getTetrimino(i);
                tetrimino.setX(tetrimino.getX() + coord.get(i * 2));
                tetrimino.setY(tetrimino.getY() + coord.get(i * 2 + 1));

                if (tetrimino.getX() < 0 && tetrimino.getX() < surplus) {
                    surplus += -(tetrimino.getX());
                }
                if (tetrimino.getX() >= GameScene.COLUMNS && tetrimino.getX() > surplus) {
                    surplus -= (tetrimino.getX() - (GameScene.COLUMNS - 1));
                }
            }
            for (int i = 0; i < 4; i++) {
                grid.lastShape().getTetrimino(i).setX(grid.lastShape().getTetrimino(i).getX() + surplus);
                grid.addTetrimino(grid.lastShape().getTetrimino(i));
            }

            coord.clear();
            incrementTurnState();
            grid.lastShape().setTurnState(actualTurnState);
        }


    }

    public boolean isLeftOccupied(GameScene plateau) {
        for (int i = 0; i < 4; i++) {
            if (plateau.lastShape().getTetrimino(i).getX() - 1 < 0) {
                return true;
            }
            for (Tetrimino x : plateau.occupe()) {
                if (plateau.lastShape().getTetrimino(i).getY() == x.getY() && plateau.lastShape().getTetrimino(i).getX() - 1 == x.getX()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isRightOccupied(GameScene plateau) {

        for (int i = 0; i < 4; i++) {
            if (plateau.lastShape().getTetrimino(i).getX() + 1 > GameScene.COLUMNS - 1) {
                return true;
            }
            for (Tetrimino x : plateau.occupe()) {
                if (plateau.lastShape().getTetrimino(i).getY() == x.getY() && plateau.lastShape().getTetrimino(i).getX() + 1 == x.getX()) {
                    return true;
                }
            }
        }

        return false;
    }


    public boolean isDownOccupied(GameScene plateau) {

        for (int i = 0; i < 4; i++) {
            if (plateau.lastShape().getTetrimino(i).getY() + 1 > GameScene.LINES - 1) {
                return true;
            }
            for (Tetrimino x : plateau.occupe()) {
                if (plateau.lastShape().getTetrimino(i).getY() + 1 == x.getY() && plateau.lastShape().getTetrimino(i).getX() == x.getX()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isUpOccupied(GameScene plateau) {

        for (int i = 0; i < 4; i++) {
            if (plateau.lastShape().getTetrimino(i).getY() - 2 < 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isTurnPossible(ArrayList<Integer> list, GameScene plateau) {

        for (int i = 0; i < 4; i++) {
            for (Tetrimino x : plateau.occupe()) {
                if (plateau.lastShape().getTetrimino(i).getY() + list.get(i * 2 + 1) == x.getY() && plateau.lastShape().getTetrimino(i).getX() + list.get(i * 2) == x.getX()) {
                    return true;
                }
            }
        }

        return false;
    }


    int turnState = 0;
    boolean down;

    public static Shape createFormes() {
        int shape = (int) (Math.random() * (7));

        return switch (shape) {
            case 0 -> new LineShape();
            case 1 -> new SquareShape();
            case 2 -> new TShape();
            case 3 -> new ZShape();
            case 4 -> new SShape();
            case 5 -> new LShape();
            case 6 -> new ReversedLShape();
            default -> throw new IllegalStateException("Unexpected value: " + shape);
        };
    }

    public abstract ArrayList<Integer> turn();

    public void setTurnState(int i) {
        turnState = i;
    }

    public void finishDown() {
        down = true;
    }

    public void putAgain() {
        down = false;
    }

    public boolean getDown() {
        return this.down;
    }


    public Color randomColor() {
        return COLORS[(int) Math.floor(Math.random() * COLORS.length)];
    }

    public static class LineShape extends Shape {

        public LineShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(3, 4, 5, 6, 0, 0, 0, 0, color);
        }

        @Override
        public ArrayList<Integer> turn() {
            return switch (turnState) {
                case 0 -> new ArrayList<>(Arrays.asList(2, -2, 1, -1, 0, 0, -1, 1));
                case 1 -> new ArrayList<>(Arrays.asList(-2, 2, -1, 1, 0, 0, 1, -1));
                case 2 -> new ArrayList<>(Arrays.asList(1, 1, 0, 0, -1, -1, -2, -2));
                case 3 -> new ArrayList<>(Arrays.asList(-1, -1, 0, 0, 1, 1, 2, 2));
                default -> new ArrayList<>(Collections.emptyList());
            };
        }


    }

    public static class SquareShape extends Shape {

        public SquareShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(4, 4, 5, 5, 0, 1, 0, 1, color);
        }

        @Override
        public ArrayList<Integer> turn() {
            return new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
        }
    }

    public static class LShape extends Shape {

        public LShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(3, 4, 5, 5, 1, 1, 1, 0, color);

        }

        @Override
        public ArrayList<Integer> turn() {
            return switch (turnState) {
                case 0 -> new ArrayList<>(Arrays.asList(1, -1, 0, 0, -1, 1, 0, 2));
                case 1 -> new ArrayList<>(Arrays.asList(1, 1, 0, 0, -1, -1, -2, 0));
                case 2 -> new ArrayList<>(Arrays.asList(-1, 1, 0, 0, 1, -1, 0, -2));
                case 3 -> new ArrayList<>(Arrays.asList(-1, -1, 0, 0, 1, 1, 2, 0));
                default -> new ArrayList<>(Collections.emptyList());
            };
        }

    }

    public static class ReversedLShape extends Shape {

        public ReversedLShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(3, 3, 4, 5, 0, 1, 1, 1, color);

        }


        @Override
        public ArrayList<Integer> turn() {
            return switch (turnState) {
                case 0 -> new ArrayList<>(Arrays.asList(2, 0, 1, -1, 0, 0, -1, 1));
                case 1 -> new ArrayList<>(Arrays.asList(0, 2, 1, 1, 0, 0, -1, -1));
                case 2 -> new ArrayList<>(Arrays.asList(-2, 0, -1, 1, 0, 0, 1, -1));
                case 3 -> new ArrayList<>(Arrays.asList(0, -2, -1, -1, 0, 0, 1, 1));
                default -> new ArrayList<>(Collections.emptyList());
            };
        }
    }

    public static class SShape extends Shape {

        public SShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(3, 4, 4, 5, 1, 1, 0, 0, color);

        }


        @Override
        public ArrayList<Integer> turn() {
            return switch (turnState) {
                case 0 -> new ArrayList<>(Arrays.asList(2, 0, 1, -1, 0, 0, -1, -1));
                case 1 -> new ArrayList<>(Arrays.asList(-2, 0, -1, 1, 0, 0, 1, 1));
                case 2 -> new ArrayList<>(Arrays.asList(1, 1, 0, 0, -1, 1, -2, 0));
                case 3 -> new ArrayList<>(Arrays.asList(-1, -1, 0, 0, 1, -1, 2, 0));
                default -> new ArrayList<>(Collections.emptyList());
            };
        }

    }

    public static class TShape extends Shape {

        public TShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(3, 4, 5, 4, 1, 1, 1, 0, color);

        }


        @Override
        public ArrayList<Integer> turn() {
            return switch (turnState) {
                case 0 -> new ArrayList<>(Arrays.asList(1, -1, 0, 0, -1, 1, 1, 1));
                case 1 -> new ArrayList<>(Arrays.asList(1, 1, 0, 0, -1, -1, -1, 1));
                case 2 -> new ArrayList<>(Arrays.asList(-1, 1, 0, 0, 1, -1, -1, -1));
                case 3 -> new ArrayList<>(Arrays.asList(-1, -1, 0, 0, 1, 1, 1, -1));
                default -> new ArrayList<>(Collections.emptyList());
            };
        }

    }

    public static class ZShape extends Shape {

        public ZShape() {
            super();
            Color color = super.randomColor();
            this.addTetrimino(3, 4, 4, 5, 0, 0, 1, 1, color);

        }

        @Override
        public ArrayList<Integer> turn() {
            return switch (turnState) {
                case 0 -> new ArrayList<>(Arrays.asList(2, 0, 1, 1, 0, 0, -1, 1));
                case 1 -> new ArrayList<>(Arrays.asList(-2, 0, -1, -1, 0, 0, 1, -1));
                case 2 -> new ArrayList<>(Arrays.asList(1, -1, 0, 0, -1, -1, -2, 0));
                case 3 -> new ArrayList<>(Arrays.asList(-1, 1, 0, 0, 1, 1, 2, 0));
                default -> new ArrayList<>(Collections.emptyList());
            };
        }

    }
}
