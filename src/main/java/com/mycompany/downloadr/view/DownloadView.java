package com.mycompany.downloadr.view;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
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
		createOutputFolderRow(row++);
		createActionRow(row++);
	}
	
	private void createFileRow(int row) {
		add(new Label("Input file"), 0, row);
		
		final TextField fileT = new TextField();
		fileT.setEditable(false);
		fileT.setPrefWidth(250);
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
	
	private void createOutputFolderRow(int row) {
		add(new Label("Output folder"), 0, row);
		
		final TextField folderT = new TextField();
		folderT.setEditable(false);
		folderT.setPrefWidth(250);
		folderT.textProperty().bindBidirectional(model.getOutputFolderProp());
		add(folderT, 1, row);
		
		Button browse = new Button("...");
		DirectoryChooser folderDC = new DirectoryChooser();
		browse.setOnAction(ae -> {
			File f = folderDC.showDialog(DownloadView.this.getScene().getWindow());
			folderT.setText(f.getAbsolutePath());
		});
		add(browse, 2, row);
	}
	
	private void createActionRow(int row) {
		GridPane rowGP = new GridPane();
		rowGP.setHgap(10);
		Button startB = new Button("Start");
		rowGP.add(startB, 0, 0);
		
		Button stopB = new Button("Stop");
		rowGP.add(stopB, 1, 0);
		
		add(rowGP, 1, row, 2, 1);
	}
}
