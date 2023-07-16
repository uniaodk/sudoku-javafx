package com.uniaodk.sudoku.components.nodes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.uniaodk.sudoku.App;
import com.uniaodk.sudoku.base.Field;
import com.uniaodk.sudoku.base.Game;
import com.uniaodk.sudoku.base.Sudoku;
import com.uniaodk.sudoku.components.SudokuInputComponent;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ButtonFieldNode extends Button {

	public static final String DEFAULT_STYLE = "-fx-cursor: hand; -fx-text-weight: light; -fx-background-color: white; -fx-opacity: 0.6;";

	public static final String SELF_HOVER_STYLE = "-fx-cursor: hand; -fx-background-color: #89C4F4;";

	public static final String HOVER_STYLE = "-fx-cursor: hand; -fx-background-color: #dcdcdc; -fx-opacity: 1;";

	public static final String DISABLED_STYLE = "-fx-cursor: null; -fx-text-fill: #333; -fx-background-color: #c6c6c6;";

	public static final String HIGHLIGHT_NUMBER = "-fx-text-weight: bold; -fx-text-fill: black; -fx-background-color: #89C4F4;";

	private Field field;

	private SudokuInputComponent sudokuInput;

	private int row, column;

	public ButtonFieldNode(Field field, int row, int column) {
		this.field = field;
		this.row = row;
		this.column = column;
		GridPane.setValignment(this, VPos.CENTER);
		GridPane.setHalignment(this, HPos.CENTER);
		setText(field.isEmpty() ? "" : String.valueOf(field.getValue()));
		setPrefSize(60, 60);
		setStyle(DEFAULT_STYLE + getDisabledStyle());
		setFont(Font.font(App.FONT_FAMILY, FontWeight.LIGHT, 25));
		setOnMouseEntered((mouseEvent) -> initOnMouse(HOVER_STYLE));
		setOnMouseExited((mouseEvent) -> initOnMouse(DEFAULT_STYLE));
		setOnMouseClicked((mouseEvent) -> initOnMouseClicked());
	}

	private void initOnMouseClicked() {
		if (Objects.nonNull(sudokuInput) || !field.isUpdatable() || Game.showSolution) return;
		sudokuInput = new SudokuInputComponent(this);
		GridPane gridPane = (GridPane) getParent();
		gridPane.add(sudokuInput, column, row);
		sudokuInput.setOnMouseExited((mouseEvent) -> {
			if (!gridPane.getChildren().contains(sudokuInput)) return;
			gridPane.getChildren().remove(sudokuInput);
			sudokuInput = null;
		});
		sudokuInput.setTranslateZ(1);
		sudokuInput.setTranslateX(-50);
		sudokuInput.setTranslateY(-190);
	}

	public void initOnMouse(String style) {
		if (Objects.isNull(getParent())) return;
		List<ButtonFieldNode> buttons = listButtons();
		setStyleRow(buttons, style);
		setStyleColumn(buttons, style);
		setStyleGrid(buttons, style);
		String selfStyle = HOVER_STYLE.equals(style) ? SELF_HOVER_STYLE : style;
		setStyle(selfStyle + getDisabledStyle());
		setStyleNumbersValues(buttons);
	}

	private List<ButtonFieldNode> listButtons() {
		GridPane parent = (GridPane) getParent();
		ObservableList<Node> children = parent.getChildren();
		return Arrays.asList(children.toArray(new Node[children.size()]))
				.stream()
				.filter(node -> node instanceof ButtonFieldNode)
				.map(node -> (ButtonFieldNode) node)
				.toList();
	}

	private void setStyleNumbersValues(List<ButtonFieldNode> buttons) {
		for (int rowIndex = 0; rowIndex < Sudoku.PUZZLE_SIZE; rowIndex++) {
			for (int columnIndex = 0; columnIndex < Sudoku.PUZZLE_SIZE; columnIndex++) {
				ButtonFieldNode button = getButton(buttons, rowIndex, columnIndex);
				boolean isHighlightNumber = Objects.nonNull(button) && !field.isEmpty() && button.getField().getValue() == field.getValue();
				if (isHighlightNumber) {
					button.setStyle(button.getStyle() + HIGHLIGHT_NUMBER);
					continue;
				}
				button.setStyle(button.getStyle().replaceAll(HIGHLIGHT_NUMBER, ""));
			}
		}
	}

	private String getDisabledStyle() {
		return !field.isUpdatable() ? DISABLED_STYLE : "";
	}

	private void setStyleRow(List<ButtonFieldNode> buttons, String style) {
		for (int columnIndex = 0; columnIndex < Sudoku.PUZZLE_SIZE; columnIndex++) {
			ButtonFieldNode button = getButton(buttons, row, columnIndex);
			if (Objects.isNull(buttons)) continue;
			button.setStyle(style + button.getDisabledStyle());
		}
	}

	private void setStyleColumn(List<ButtonFieldNode> buttons, String style) {
		for (int rowIndex = 0; rowIndex < Sudoku.PUZZLE_SIZE; rowIndex++) {
			ButtonFieldNode button = getButton(buttons, rowIndex, column);
			if (Objects.isNull(buttons)) continue;
			button.setStyle(style + button.getDisabledStyle());
		}
	}

	private void setStyleGrid(List<ButtonFieldNode> buttons, String style) {
		int startRow = Math.floorDiv(row, Sudoku.GRID_SIZE) * Sudoku.GRID_SIZE;
		int startColumn = Math.floorDiv(column, Sudoku.GRID_SIZE) * Sudoku.GRID_SIZE;
		for (int rowIndex = startRow; rowIndex < (startRow + Sudoku.GRID_SIZE); rowIndex++) {
			for (int columnIndex = startColumn; columnIndex < (startColumn + Sudoku.GRID_SIZE); columnIndex++) {
				ButtonFieldNode button = getButton(buttons, rowIndex, columnIndex);
				if (Objects.isNull(buttons)) continue;
				button.setStyle(style + button.getDisabledStyle());
			}
		}
	}

	private ButtonFieldNode getButton(List<ButtonFieldNode> buttons, int row, int column) {
		for (ButtonFieldNode button : buttons) {
			if (GridPane.getColumnIndex(button) == column && GridPane.getRowIndex(button) == row) return button;
		}
		return null;
	}
}
