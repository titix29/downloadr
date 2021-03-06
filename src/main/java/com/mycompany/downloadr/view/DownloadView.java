package com.mycompany.downloadr.view;

import java.io.File;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.google.common.base.StandardSystemProperty;
import com.mycompany.downloadr.framework.AbstractView;

public class DownloadView extends AbstractView<DownloadViewModel> {
	
	private GridPane container;
	
	@Override
	public Parent createUI() {
		container = new GridPane();
		container.setHgap(10);
		container.setVgap(10);
		container.setPadding(new Insets(20, 20, 20, 20));
		
		int row = 0;
		createFileRow(row++);
		createOutputFolderRow(row++);
		createActionRow(row++);
		createResultsRow(row++);
		
		return container;
	}
	
	private void createFileRow(int row) {
		container.add(new Label("Input file"), 0, row);
		
		final TextField fileT = new TextField();
		fileT.setEditable(false);
		fileT.setPrefWidth(250);
		fileT.textProperty().bindBidirectional(vm.getInputFileProp());
		container.add(fileT, 1, row);
		
		Button browse = new Button("...");
		FileChooser fileFC = new FileChooser();
		browse.setOnAction(ae -> {
			File f = fileFC.showOpenDialog(container.getScene().getWindow());
			fileT.setText(f.getAbsolutePath());
		});
		container.add(browse, 2, row);
	}
	
	private void createOutputFolderRow(int row) {
		container.add(new Label("Output folder"), 0, row);
		
		final TextField folderT = new TextField();
		folderT.setEditable(false);
		folderT.setPrefWidth(250);
		folderT.textProperty().bindBidirectional(vm.getOutputFolderProp());
		container.add(folderT, 1, row);
		
		Button browse = new Button("...");
		DirectoryChooser folderDC = new DirectoryChooser();
		folderDC.setInitialDirectory(Paths.get(StandardSystemProperty.JAVA_IO_TMPDIR.value()).toFile());
		browse.setOnAction(ae -> {
			File f = folderDC.showDialog(container.getScene().getWindow());
			folderT.setText(f.getAbsolutePath());
		});
		container.add(browse, 2, row);
	}
	
	private void createActionRow(int row) {
		GridPane rowGP = new GridPane();
		rowGP.setHgap(10);
		
		Button startB = new Button("Start");
		startB.setOnAction(this::startDownload);
		startB.disableProperty().bind(vm.getRunningProperty());
		rowGP.add(startB, 0, 0);
		
		Button stopB = new Button("Stop");
		stopB.setOnAction(this::stopDownload);
		stopB.disableProperty().bind(vm.getRunningProperty().not());
		rowGP.add(stopB, 1, 0);
		
		container.add(rowGP, 1, row, 2, 1);
	}
	
	private void createResultsRow(int row) {
		GridPane rowGP = new GridPane();
		rowGP.setHgap(10);
		
		TextArea consoleTA = new TextArea();
		consoleTA.setEditable(false);
		consoleTA.setPrefHeight(200);
		consoleTA.textProperty().bindBidirectional(vm.getOutputConsoleProp());
		consoleTA.textProperty().addListener((obs, val, oldVal) -> {
			// autoscroll on change : http://stackoverflow.com/questions/17799160/javafx-textarea-and-autoscroll
			consoleTA.setScrollTop(Double.MAX_VALUE);
		});
		rowGP.add(consoleTA, 0, 0);
		
		ProgressBar progress = new ProgressBar(100);
		progress.progressProperty().bind(vm.getProgressProperty());
		progress.visibleProperty().bind(vm.getRunningProperty());
		// TODO : manage width properly (using colspan)
		progress.setPrefWidth(500);
		rowGP.add(progress, 0, 1);
		
		Label statusL = new Label();
		statusL.textProperty().bind(vm.getMessageProperty());
		rowGP.add(statusL, 0, 2);
		
		container.add(rowGP, 0, row, 4, 1);
	}
	
	private void startDownload(@SuppressWarnings("unused") ActionEvent ae) {
		vm.start();
	}
	
	private void stopDownload(@SuppressWarnings("unused") ActionEvent ae) {
		if (!vm.stop()) {
			showError("Unable to cancel current worker");
		}
	}
	
}
