package com.uniaodk.sudoku.base;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sudoku {

	public static final int PUZZLE_SIZE = 9;

	public static final int GRID_SIZE = 3;

	public static final String NOT_RESOLVABLE = "Soduko is not resolvable!";

	public static final int TOTAL_FIELDS = PUZZLE_SIZE * PUZZLE_SIZE;

	private Field[][] fields;

	private Field[][] fieldsSolution;

	private Random random;

	private Difficult difficult;

	public Sudoku() {
		this(Difficult.NORMAL);
	}

	public Sudoku(Difficult difficult) {
		this.difficult = difficult;
		this.random = new Random();
		this.fields = new Field[PUZZLE_SIZE][PUZZLE_SIZE];
		cleanPuzzle();
	}

	public void cleanPuzzle() {
		this.fieldsSolution = fields;
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			fields[row] = new Field[PUZZLE_SIZE];
			for (int column = 0; column < PUZZLE_SIZE; column++) {
				fields[row][column] = new Field();
			}
		}
	}

	public void setValidFields(Field[][] fields) {
		Field[][] fieldsCloned = Field.cloneMatrix(fields);
		if (!solveFields(fields)) throw new RuntimeException(NOT_RESOLVABLE);
		this.fieldsSolution = fields;
		this.fields = fieldsCloned;
	}

	public boolean solveFields(Field[][] fields) {
		try {
			return solveEmptyFields(fields);
		} catch (StackOverflowError stackOverflowError) {
			System.err.println(NOT_RESOLVABLE);
			return false;
		}
	}

	public Field[][] convertToFields(int[][] puzzle) {
		if (puzzle.length != PUZZLE_SIZE) throw new RuntimeException("Must has 9 rows to solve.");
		Field[][] fields = new Field[PUZZLE_SIZE][PUZZLE_SIZE];
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			if (puzzle[row].length != PUZZLE_SIZE) throw new RuntimeException("Must has 9 columns to solve.");
			fields[row] = new Field[PUZZLE_SIZE];
			for (int column = 0; column < PUZZLE_SIZE; column++) {
				fields[row][column] = new Field(puzzle[row][column]);
			}
		}
		return fields;
	}

	public boolean generateValidFields() {
		cleanPuzzle();
		boolean solved = solveEmptyFields(fields);
		this.fieldsSolution = Field.cloneMatrix(fields);
		return solved;
	}

	private void fixFieldsUpdatable() {
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			for (int column = 0; column < PUZZLE_SIZE; column++) {
				boolean isUpdatable = this.fields[row][column].isUpdatable();
				this.fieldsSolution[row][column].setUpdatable(isUpdatable);
			}
		}
	}

	public boolean isValid() {
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			for (int column = 0; column < PUZZLE_SIZE; column++) {
				Field field = fields[row][column];
				if (field.isEmpty() || !isInputColumnValid(row, column, field.getValue())) return false;
			}
		}
		return true;
	}

	public boolean isInputFieldValid(int row, int column, int value) {
		return isInputColumnValid(row, column, value)
				&& isInputRowValid(row, column, value)
				&& isInputGridValid(row, column, value);
	}

	public boolean isInputFieldValidEmpty(int row, int column, int value) {
		if (value == Field.EMPTY) return true;
		return isInputFieldValid(row, column, value);
	}

	public boolean hasEmptyField() {
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			for (int column = 0; column < PUZZLE_SIZE; column++) {
				if (fields[row][column].isEmpty()) return true;
			}
		}
		return false;
	}

	public void hideFields() {
		int countHideFields = 0;
		do {
			int randomRow = random.nextInt(0, PUZZLE_SIZE);
			int randomColumn = random.nextInt(0, PUZZLE_SIZE);
			if (fields[randomRow][randomColumn].isEmpty() || !hasMinimunFieldsFilledInGrid(1, randomRow, randomColumn)) continue;
			fields[randomRow][randomColumn].setValue(Field.EMPTY);
			fields[randomRow][randomColumn].setUpdatable(true);
			countHideFields++;
		} while (countHideFields != getHideFields(difficult));
		fixFieldsUpdatable();
	}

	private boolean solveEmptyFields(Field[][] fields) {
		this.fields = fields;
		int row = 0;
		int column = 0;
		for (int fieldIndex = 0; fieldIndex < TOTAL_FIELDS; fieldIndex++) {
			row = Math.floorDiv(fieldIndex, PUZZLE_SIZE);
			column = fieldIndex % PUZZLE_SIZE;
			if (fields[row][column].isEmpty()) {
				if (fillEmptyField(fields, row, column)) return true;
				break;
			}
		}
		fields[row][column].setValue(Field.EMPTY);
		return false;
	}

	private int getHideFields(Difficult difficult) {
		if (Difficult.EASY.equals(difficult)) return 20;
		if (Difficult.NORMAL.equals(difficult)) return 40;
		if (Difficult.HARD.equals(difficult)) return 55;
		return 72;
	}

	private boolean hasMinimunFieldsFilledInGrid(int fieldsFilled, int row, int column) {
		int startRow = Math.floorDiv(row, GRID_SIZE) * GRID_SIZE;
		int startColumn = Math.floorDiv(column, GRID_SIZE) * GRID_SIZE;
		int countFieldFilled = 0;
		for (int rowIndex = startRow; rowIndex < (startRow + GRID_SIZE); rowIndex++) {
			for (int columnIndex = startColumn; columnIndex < (startColumn + GRID_SIZE); columnIndex++) {
				if (!fields[rowIndex][columnIndex].isEmpty()) countFieldFilled++;
			}
		}
		return countFieldFilled > fieldsFilled;
	}

	private boolean fillEmptyField(Field[][] fields, int row, int column) {
		List<Integer> rangeValues = IntStream.range(0, 10).boxed().collect(Collectors.toList());
		while (!rangeValues.isEmpty()) {
			int value = rangeValues.remove(random.nextInt(0, rangeValues.size()));
			if (isInputFieldValid(row, column, value)) {
				fields[row][column].setValue(value);
				if ((!hasEmptyField() && isValid()) || solveEmptyFields(fields)) return true;
			}
		}
		return false;
	}

	private boolean isInputRowValid(int row, int column, int value) {
		for (int columnIndex = 0; columnIndex < PUZZLE_SIZE; columnIndex++) {
			if (columnIndex == column) continue;
			if (value == fields[row][columnIndex].getValue()) return false;
		}
		return true;
	}

	private boolean isInputColumnValid(int row, int column, int value) {
		for (int rowIndex = 0; rowIndex < PUZZLE_SIZE; rowIndex++) {
			if (rowIndex == row) continue;
			if (value == fields[rowIndex][column].getValue()) return false;
		}
		return true;
	}

	private boolean isInputGridValid(int row, int column, int value) {
		int startRow = Math.floorDiv(row, GRID_SIZE) * GRID_SIZE;
		int startColumn = Math.floorDiv(column, GRID_SIZE) * GRID_SIZE;
		for (int rowIndex = startRow; rowIndex < (startRow + GRID_SIZE); rowIndex++) {
			for (int columnIndex = startColumn; columnIndex < (startColumn + GRID_SIZE); columnIndex++) {
				if (rowIndex == row && columnIndex == column) continue;
				if (value == fields[rowIndex][columnIndex].getValue()) return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			if (row != 0 && (row % 3) == 0) stringBuilder.append(System.lineSeparator());
			Object[] fieldValues = Arrays.asList(fields[row]).stream().map(field -> field.isEmpty() ? "_" : field.getValue()).toArray();
			stringBuilder.append(String.format("%s %s %s\t%s %s %s\t%s %s %s", fieldValues));
			stringBuilder.append((row != PUZZLE_SIZE - 1) ? "\n" : "");
		}
		return stringBuilder.toString();
	}
}
