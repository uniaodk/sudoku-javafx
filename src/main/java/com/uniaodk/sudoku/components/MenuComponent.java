package com.uniaodk.sudoku.components;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.uniaodk.sudoku.base.Difficult;
import com.uniaodk.sudoku.base.Field;
import com.uniaodk.sudoku.base.Game;
import com.uniaodk.sudoku.base.Sudoku;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MenuComponent extends GridPane {

	private static final String TEXT_SAVE = "Save";

	private static final String TEXT_EDIT = "Edit";

	private ComboBox<Difficult> comboBoxDificult;

	private Field[][] fieldTemp = null;

	public MenuComponent() {
		setPrefHeight(REMAINING);
		setStyle("-fx-background-color: #ccc");
		initComponents();
	}

	private void initComponents() {
		TitleComponent labelMenu = new TitleComponent("Menu");
		Button buttonNewGame = new Button("New Game");
		Button buttonExit = new Button("Exit");
		CheckBox checkBoxSolution = new CheckBox("Show solution?");
		checkBoxSolution.setSelected(Game.showSolution);
		checkBoxSolution.setOnMouseClicked((mouseEvent) -> onClickSolution(checkBoxSolution));
		buttonNewGame.setOnMouseClicked((mouseEvent) -> Game.sudokuComponent.generateBoard(true));
		buttonExit.setOnMouseClicked((mouseEvent) -> Game.exit());
		VBox vBoxMenus = new VBox(buildDificultCombobox(), buildContainer(buttonNewGame), buildContainer(buttonExit),
				buildContainer(checkBoxSolution, "-fx-text-size : 13;"), createCustomLayout());
		vBoxMenus.setSpacing(20);
		add(labelMenu, 0, 0);
		add(vBoxMenus, 0, 1);
		GridPane.setHgrow(vBoxMenus, Priority.ALWAYS);
		GridPane.setVgrow(vBoxMenus, Priority.ALWAYS);
	}

	private void onClickSolution(CheckBox checkBoxSolution) {
		Game.showSolution = checkBoxSolution.isSelected();
		if (Game.showSolution) {
			this.fieldTemp = Field.cloneMatrix(Game.sudoku.getFields());
			Game.sudoku.setFields(Game.sudoku.getFieldsSolution());
			Game.sudokuComponent.generateBoard(false);
			return;
		}
		Game.sudoku.setFields(this.fieldTemp);
		Game.sudokuComponent.generateBoard(false);
	}

	private VBox createCustomLayout() {
		TitleComponent labelCustom = new TitleComponent("Custom");
		labelCustom.setPrefHeight(50);
		Game.labelErr = new Label("");
		Button buttonEditSave = new Button(TEXT_EDIT);
		TextArea textArea = createTextArea();
		Game.textArea = textArea;
		VBox vbox = new VBox(labelCustom, buildContainer(textArea, "-fx-text-size : 10;"),
				buildContainer(Game.labelErr, "-fx-cursor: none;  -fx-text-fill: red;"), buildContainer(buttonEditSave));
		vbox.setSpacing(5);
		buttonEditSave.setOnMouseClicked((mouseEvent) -> onClickedButtonEditSave(buttonEditSave, textArea));
		return vbox;
	}

	private TextArea createTextArea() {
		TextArea textArea = new TextArea(Game.sudoku.toString()) {

			@Override
			public void paste() {
				Clipboard clipboard = Clipboard.getSystemClipboard();
				if (clipboard.hasString()) {
					replaceSelection(clipboard.getString());
					Game.fixSudokuTextArea(this);
				}
			}
		};
		Game.fixSudokuTextArea(textArea);
		textArea.setPrefHeight(200);
		textArea.setDisable(true);
		return textArea;
	}

	private void onClickedButtonEditSave(Button button, TextArea textArea) {
		if (button.getText().equals(TEXT_EDIT)) {
			button.setText(TEXT_SAVE);
			textArea.setDisable(false);
			return;
		}
		try {
			Game.fixSudokuTextArea(textArea);
			int[][] matrix = converToMatrix(textArea.getText());
			Field[][] fields = Game.sudoku.convertToFields(matrix);
			if (!isValidFields(fields)) throw new RuntimeException(Sudoku.NOT_RESOLVABLE);
			Game.sudoku.setValidFields(fields);
			Game.sudokuComponent.generateBoard(false);
			button.setText(TEXT_EDIT);
			textArea.setDisable(true);
			Game.labelErr.setText("");
		} catch (Exception err) {
			Game.labelErr.setText(err.getMessage());
		}
	}

	private boolean isValidFields(Field[][] fields) {
		Sudoku sudoku = new Sudoku();
		sudoku.setFields(fields);
		for (int row = 0; row < Sudoku.PUZZLE_SIZE; row++) {
			for (int column = 0; column < Sudoku.PUZZLE_SIZE; column++) {
				if (!sudoku.isInputFieldValidEmpty(row, column, fields[row][column].getValue())) {
					return false;
				}
			}
		}
		return true;
	}

	private int[][] converToMatrix(String text) {
		text = text.replaceAll("_", "0").replaceAll("\t|\n| ", "");
		char[] textChars = text.toCharArray();
		int[][] matrix = new int[Sudoku.PUZZLE_SIZE][Sudoku.PUZZLE_SIZE];
		int countText = 0;
		for (int row = 0; row < Sudoku.PUZZLE_SIZE; row++) {
			matrix[row] = new int[Sudoku.PUZZLE_SIZE];
			for (int column = 0; column < Sudoku.PUZZLE_SIZE; column++) {
				int value = textChars.length > countText ? Character.getNumericValue(textChars[countText]) : 0;
				matrix[row][column] = value;
				countText++;
			}
		}
		return matrix;
	}

	private GridPane buildDificultCombobox() {
		comboBoxDificult = new ComboBox<>();
		comboBoxDificult.getSelectionModel().select(Game.sudoku.getDifficult());
		comboBoxDificult.getItems().addAll(Difficult.values());
		comboBoxDificult.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			Game.sudoku.setDifficult(newValue);
		});
		return buildContainer(comboBoxDificult);
	}

	private GridPane buildContainer(Control control, String... styles) {
		String stylesText = Arrays.asList(styles).stream().collect(Collectors.joining("; "));
		stylesText = stylesText.isEmpty() ? "-fx-font-size: 18;" : stylesText;
		GridPane container = new GridPane();
		container.setPadding(new Insets(0, 20, 0, 20));
		control.setStyle("-fx-font-family: \"Fira Code\"; -fx-cursor: hand;" + stylesText);
		control.setPrefWidth(REMAINING);
		container.add(control, 0, 0);
		GridPane.setVgrow(control, Priority.ALWAYS);
		GridPane.setVgrow(control, Priority.ALWAYS);
		container.setAlignment(Pos.CENTER);
		return container;
	}
}
