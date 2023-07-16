package com.uniaodk.sudoku.base;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import com.uniaodk.sudoku.components.ScoreComponent;
import com.uniaodk.sudoku.components.SudokuComponent;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Game {

	public static final int ONE_SECOND = 1000;

	public static Sudoku sudoku = new Sudoku();

	private static Timer timer = new Timer();

	public static ScoreComponent score = null;

	public static SudokuComponent sudokuComponent = null;

	public static TextArea textArea = null;

	public static int moves = 0;

	public static boolean showSolution = false;

	public static Label labelErr = null;

	/*
	 * In seconds
	 */
	public static int timePlaying = 0;

	public static void stopTimer() {
		timer.cancel();
	}

	public static void exit() {
		timer.cancel();
		Platform.exit();
	}

	public static void startTimer() {
		timePlaying = 0;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(() -> {
					timePlaying++;
					score.getLabelTimePlaying().setText(getTime());
				});
			}
		}, ONE_SECOND, ONE_SECOND);
	}

	public static void addMove() {
		moves++;
		score.getLabelMoves().setText(String.valueOf(moves));
	}

	private static String getTime() {
		int seconds = timePlaying % 60;
		int minutes = timePlaying / 60;
		int hour = minutes / 60;
		minutes %= 60;
		return String.format("%02d:%02d:%02d", hour, minutes, seconds);
	}

	public static void newGame() {
		sudoku.generateValidFields();
		sudoku.hideFields();
		moves = 0;
		timePlaying = 0;
		timer.cancel();
		if (Objects.nonNull(textArea)) updateTextArea();
		if (Objects.isNull(score)) return;
		score.getLabelMoves().setText("0");
		score.getLabelTimePlaying().setText("00:00:00");
	}

	public static void updateTextArea() {
		textArea.setText(sudoku.toString());
		fixSudokuTextArea(textArea);
		labelErr.setText("");
	}

	public static void fixSudokuTextArea(TextArea textArea) {
		String sudokuText = textArea.getText();
		StringBuilder sudokuBuilder = new StringBuilder("");
		int column = 1, row = 0;
		for (char character : sudokuText.toCharArray()) {
			boolean isUnderscore = character == '_';
			boolean lineSeparator = column != 0 && column % 9 == 0;
			boolean tabulation = column != 0 && column % 3 == 0;
			boolean isValidNumber = (int) character >= 49 && (int) character <= 57;
			boolean isZero = (int) character == 48;
			if (isValidNumber || isZero || isUnderscore) {
				sudokuBuilder.append(isZero ? "_" : character).append(lineSeparator ? "" : " ");
				if (lineSeparator && row < 8) {
					row++;
					sudokuBuilder.append(System.lineSeparator());
					if (row % 3 == 0) sudokuBuilder.append(System.lineSeparator());
				}
				if (!lineSeparator && tabulation) sudokuBuilder.append('\t');
				column++;
			}
		}
		textArea.setText(sudokuBuilder.toString());
	}
}
