package com.mycompany.downloadr.view;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class DownloadView extends GridPane {
	
	private DownloadViewModel model;
	
	public DownloadView() throws Exception {
		this.model = new DownloadViewModel();
		
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(20, 20, 20, 20));
		
		int row = 0;
		createFileRow(row++);
	}
	
	private void createFileRow(int row) {
		add(new Label("Input file"), 0, row);
		
		final TextField fileT = new TextField();
		fileT.setEditable(false);
		fileT.textProperty().bindBidirectional(model.getInputFileProp());
		add(fileT, 1, row);
		
		Button browse = new Button("...");
		FileChooser fileFC = new FileChooser();
		browse.setOnAction(ae -> {
			File f = fileFC.showOpenDialog(DownloadView.this.getScene().getWindow());
			fileT.setText(f.getAbsolutePath());
		});
		add(browse, 2, row);
	}
}
