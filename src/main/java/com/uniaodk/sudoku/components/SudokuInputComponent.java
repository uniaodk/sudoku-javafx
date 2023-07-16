package com.uniaodk.sudoku.components;

import java.util.stream.IntStream;
import com.uniaodk.sudoku.App;
import com.uniaodk.sudoku.base.Field;
import com.uniaodk.sudoku.base.Game;
import com.uniaodk.sudoku.base.Sudoku;
import com.uniaodk.sudoku.components.nodes.ButtonFieldNode;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class SudokuInputComponent extends Pane {

	private ButtonFieldNode button;

	private GridPane container;

	public SudokuInputComponent(ButtonFieldNode button) {
		this.button = button;
		container = new GridPane();
		container.setPrefSize(150, 200);
		container.setHgap(5);
		container.setVgap(5);
		container.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
		container.setPadding(new Insets(10));
		this.getChildren().add(container);
		buildButtons();
	}

	private void buildButtons() {
		for (int valueButton : IntStream.range(0, 9).toArray()) {
			int row = Sudoku.GRID_SIZE - Math.floorDiv(valueButton, Sudoku.GRID_SIZE);
			int column = valueButton % Sudoku.GRID_SIZE;
			Button button = new Button(String.valueOf(valueButton + 1));
			button.setPrefSize(50, 50);
			button.setFont(Font.font(App.FONT_FAMILY, FontWeight.MEDIUM, FontPosture.REGULAR, 18));
			button.setOnMouseClicked((mouseEvent) -> initOnMouseClicked(valueButton + 1));
			button.setStyle("-fx-cursor: hand;");
			container.add(button, column, row);
		}
		container.add(buildDeleteButton(), 1, 4);
	}

	private void initOnMouseClicked(int value) {
		this.button.getField().setValue(value);
		this.button.setText(this.button.getField().isEmpty() ? "" : String.valueOf(value));
		this.button.setSudokuInput(null);
		this.setOnMouseExited(null);
		GridPane parent = (GridPane) this.button.getParent();
		parent.getChildren().remove(this);
		if (Game.moves == 0) Game.startTimer();
		Game.addMove();
		Game.updateTextArea();
		Game.sudokuComponent.onFinishGame(button);
	}

	public static String DELETE_DEFAULT_STYLE = "-fx-base: #ff4757; -fx-cursor: hand;";

	public static String DELETE_HOVER_STYLE = "-fx-base: #ff5d69; -fx-cursor: hand;";

	private Button buildDeleteButton() {
		Image img = new Image("/assets/delete.png");
		ColorAdjust lightUp = new ColorAdjust();
		lightUp.setBrightness(1.0);
		ImageView view = new ImageView(img);
		view.setFitHeight(22);
		view.setEffect(lightUp);
		view.setPreserveRatio(true);
		Button deleteButton = new Button();
		deleteButton.setPrefSize(50, 50);
		deleteButton.setGraphic(view);
		deleteButton.setStyle(DELETE_DEFAULT_STYLE);
		deleteButton.setOnMouseEntered((mouseEvent) -> deleteButton.setStyle(DELETE_HOVER_STYLE));
		deleteButton.setOnMouseExited((mouseEvent) -> deleteButton.setStyle(DELETE_DEFAULT_STYLE));
		deleteButton.setOnMouseClicked((mouseEvent) -> initOnMouseClicked(Field.EMPTY));
		return deleteButton;
	}
}
