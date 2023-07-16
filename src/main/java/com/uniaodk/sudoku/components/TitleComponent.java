package com.uniaodk.sudoku.components;

import com.uniaodk.sudoku.components.nodes.TitleLableNode;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TitleComponent extends GridPane {

	public TitleComponent(String title) {
		TitleLableNode label = new TitleLableNode(title);
		GridPane.setHgrow(label, Priority.ALWAYS);
		GridPane.setVgrow(label, Priority.ALWAYS);
		setHalignment(label, HPos.CENTER);
		setPrefSize(USE_COMPUTED_SIZE, 100);
		this.getChildren().add(label);
	}
}
