package com.uniaodk.sudoku.components;

import com.uniaodk.sudoku.base.Field;
import com.uniaodk.sudoku.base.Game;
import com.uniaodk.sudoku.base.Sudoku;
import com.uniaodk.sudoku.components.nodes.ButtonFieldNode;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class SudokuComponent extends GridPane {

	private GridPane sudokuBoard;

	public SudokuComponent() {
		Game.sudokuComponent = this;
		VBox.setVgrow(this, Priority.ALWAYS);
		this.setAlignment(Pos.CENTER);
		generateBoard(true);
	}

	private void buildSudokuBoard() {
		this.sudokuBoard = new GridPane();
		GridPane.setHalignment(this.sudokuBoard, HPos.CENTER);
		GridPane.setValignment(this.sudokuBoard, VPos.CENTER);
		this.sudokuBoard.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
		this.sudokuBoard.setPrefSize(600, 600);
		this.sudokuBoard.setAlignment(Pos.CENTER);
		this.sudokuBoard.setGridLinesVisible(true);
	}

	private void buildSudokuFields() {
		for (int row = 0; row < Sudoku.PUZZLE_SIZE; row++) {
			this.sudokuBoard.getColumnConstraints().add(new ColumnConstraints(65));
			this.sudokuBoard.getRowConstraints().add(new RowConstraints(65));
			for (int column = 0; column < Sudoku.PUZZLE_SIZE; column++) {
				Field field = Game.sudoku.getFields()[row][column];
				ButtonFieldNode button = new ButtonFieldNode(field, row, column);
				this.sudokuBoard.add(button, column, row);
			}
		}
		this.sudokuBoard.add(buildHorizontalLine(-33), 0, 0);
		this.sudokuBoard.add(buildHorizontalLine(-33), 0, 3);
		this.sudokuBoard.add(buildHorizontalLine(-33), 0, 6);
		this.sudokuBoard.add(buildHorizontalLine(33), 0, 8);
		this.sudokuBoard.add(buildVerticalLine(-3), 0, 4);
		this.sudokuBoard.add(buildVerticalLine(-3), 3, 4);
		this.sudokuBoard.add(buildVerticalLine(-3), 6, 4);
		this.sudokuBoard.add(buildVerticalLine(62), 8, 4);
	}

	public void onFinishGame(ButtonFieldNode button) {
		if (!Game.sudoku.isValid()) return;
		Game.stopTimer();
		Alert alert = new Alert(AlertType.INFORMATION, "Congratulations!");
		alert.setHeaderText(null);
		alert.setTitle("Sudoku");
		alert.showAndWait();
		generateBoard(true);
	}

	public void generateBoard(boolean isNewGame) {
		this.getChildren().remove(this.sudokuBoard);
		if (isNewGame) Game.newGame();
		buildSudokuBoard();
		buildSudokuFields();
		this.add(this.sudokuBoard, 0, 0);
	}

	private Line buildHorizontalLine(double translateY) {
		Line line = new Line(683, 0, 100, 0);
		line.setTranslateX(-2);
		line.setTranslateY(translateY);
		line.setStrokeWidth(5);
		return line;
	}

	private Line buildVerticalLine(double translateX) {
		Line line = new Line(0, 684, 0, 99);
		line.setTranslateX(translateX);
		line.setTranslateY(1);
		line.setStrokeWidth(5);
		return line;
	}
}
