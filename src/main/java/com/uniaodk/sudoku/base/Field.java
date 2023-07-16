package com.uniaodk.sudoku.base;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Field {

	public static final int EMPTY = 0;

	private boolean isUpdatable;

	private int value;

	public Field() {
		this.isUpdatable = false;
		this.value = EMPTY;
	}

	public Field(int value) {
		setValue(value);
		this.isUpdatable = isEmpty();
	}

	public void setValue(int value) {
		boolean inRange = value >= 0 && value <= 9;;
		if (!inRange) throw new RuntimeException("Field is out of range [0-9]. 0 (zero) = Empty Field");
		this.value = value;
	}

	public boolean isEmpty() {
		return this.value == EMPTY;
	}

	public static Field[][] cloneMatrix(Field[][] fields) {
		Field[][] fieldsCloned = new Field[Sudoku.PUZZLE_SIZE][Sudoku.PUZZLE_SIZE];
		for (int row = 0; row < Sudoku.PUZZLE_SIZE; row++) {
			fieldsCloned[row] = new Field[Sudoku.PUZZLE_SIZE];
			for (int column = 0; column < Sudoku.PUZZLE_SIZE; column++) {
				Field field = new Field(fields[row][column].value);
				field.setUpdatable(fields[row][column].isUpdatable);
				fieldsCloned[row][column] = field;
			}
		}
		return fieldsCloned;
	}
}
