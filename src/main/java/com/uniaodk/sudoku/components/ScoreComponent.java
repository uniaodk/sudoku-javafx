package com.uniaodk.sudoku.components;

import com.uniaodk.sudoku.base.Game;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;

@Getter
public class ScoreComponent extends GridPane {

	private Label labelMoves;

	private Label labelTimePlaying;

	public ScoreComponent() {
		Game.score = this;
		initComponents();
	}

	private void initComponents() {
		this.labelMoves = new Label("0");
		this.labelTimePlaying = new Label("00:00:00");
		setMinHeight(200);
		setStyle("-fx-background-color: #bbb");
		setPrefWidth(REMAINING);
		setPadding(new Insets(0, 20, 0, 20));
		add(new TitleComponent("Score"), 0, 0);
		add(buildComponent("Moves:", labelMoves), 0, 1);
		add(buildComponent("Time:", labelTimePlaying), 0, 2);
	}

	private HBox buildComponent(String labelName, Label labelScore) {
		Label label = new Label(labelName);
		label.setPrefWidth(80);
		HBox hBox = new HBox(20, label, labelScore);
		hBox.setStyle("-fx-font-size: 20;");
		GridPane.setHgrow(hBox, Priority.ALWAYS);
		return hBox;
	}
}
