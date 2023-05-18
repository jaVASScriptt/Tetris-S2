package tetris.game;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import tetris.scenes.GameScene;

public class Tetrimino extends Parent{
	private int x;
	private int y;
	private final Color color;

	public Tetrimino(int X, int Y, Color color, boolean isBackground) {

		this.color = color;
		int size = 35;

		this.x = X;
		this.y = Y;

		Group cellGroup = new Group();

		Rectangle square = new Rectangle(size, size);
		Polygon topShade = new Polygon();
		Polygon bottomShade = new Polygon();

		double shadeSize = (double) size / 7.5;

		if (!isBackground) {
			topShade.setOpacity(0.5);
			topShade.setFill(Color.WHITE);
			topShade.getPoints().addAll(0.0, 0.0,
					(double) size, 0.0,
					(double) size - shadeSize, shadeSize,
					shadeSize, shadeSize,
					shadeSize, (double) size - shadeSize,
					0.0, (double) size);

			bottomShade.setOpacity(0.5);
			bottomShade.setFill(Color.BLACK);
			bottomShade.getPoints().addAll(0.0, (double) size,
					(double) size, (double) size,
					(double) size, 0.0,
					(double) size - shadeSize, shadeSize,
					(double) size - shadeSize, (double) size - shadeSize,
					shadeSize, (double) size - shadeSize);

			square.setFill(color);

			cellGroup.getChildren().addAll(square, topShade, bottomShade);
		} else {
			topShade.setOpacity(0.1);
			topShade.setFill(Color.BLACK);
			topShade.getPoints().addAll(0.0, 0.0,
					(double) size, 0.0,
					(double) size - shadeSize, shadeSize,
					shadeSize, shadeSize,
					shadeSize, (double) size - shadeSize,
					0.0, (double) size);

			bottomShade.setOpacity(0.25);
			bottomShade.setFill(Color.BLACK);
			bottomShade.getPoints().addAll(0.0, (double) size,
					(double) size, (double) size,
					(double) size, 0.0,
					(double) size - shadeSize, shadeSize,
					(double) size - shadeSize, (double) size - shadeSize,
					shadeSize, (double) size - shadeSize);

			Rectangle topRec = new Rectangle(size, size / 2.65);
			topRec.setOpacity(0.05);
			topRec.setFill(Color.WHITE);

			Arc halfCircle = new Arc((double) size / 2.0, (double) size / 2.0, (double) size / 2.0, (double) size / 8.0, 0.0f, 180.0f);
			halfCircle.setOpacity(0.05);
			halfCircle.setFill(Color.WHITE);
			halfCircle.setType(ArcType.ROUND);
			halfCircle.setRotate(180.0);

			square.setFill(color);
			square.setOpacity((55.0 / 62.0 - ((double) Y + 30.0) / ((double) GameScene.LINES + 50)));

			cellGroup.getChildren().addAll(square, topShade, bottomShade, halfCircle, topRec);
		}

		this.getChildren().add(cellGroup);
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
