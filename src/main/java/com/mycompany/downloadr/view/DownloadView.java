package com.mycompany.downloadr.view;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.google.common.base.StandardSystemProperty;
import com.mycompany.downloadr.services.DownloadServices;
import com.mycompany.downloadr.services.FileListingServices;
import com.mycompany.downloadr.services.SimpleFileListing;

public class DownloadView extends GridPane {
	
	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	
	private DownloadViewModel model;
	private Task<Void> downloadTask;
	
	private ProgressBar progressBar;
	
	public DownloadView() throws Exception {
		this.model = new DownloadViewModel();
		
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(20, 20, 20, 20));
		
		defineDownloadTask();
		
		int row = 0;
		createFileRow(row++);
		createOutputFolderRow(row++);
		createActionRow(row++);
		createResultsRow(row++);
	}
	
	private void defineDownloadTask() {
		// TODO : kill task when app is killed (maybe by inheriting Scene instead of Pane)
		this.downloadTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				FileListingServices fileSrv = new SimpleFileListing();
				DownloadServices downloadSrv = new DownloadServices();
				fileSrv.init(Paths.get(model.getInputFile()));
				
				URL url;
				Path downloaded;
				long nbFiles = fileSrv.getFilesCount();
				for (long i = 1; i <= nbFiles; i++) {
					if (isCancelled()) {
						// user cliked on stop
						break;
					}
					url = fileSrv.getFileAtIndex(i);
					updateMessage("Downloading file " + url);
					
					downloaded = downloadSrv.download(url, Paths.get(model.getOutputFolder()));

					model.appendToConsole(String.format("File [%d/%d] downloaded at %s", i, nbFiles, downloaded));
					updateProgress(i, nbFiles);
				}
				
				updateMessage("");
				
				return null;
			}
			
			@Override
			protected void failed() {
				String msg = String.format("Download failed : %s", getException().getMessage());
				model.appendToConsole(msg);
				updateProgress(1, 1);
			}
		};
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
		folderDC.setInitialDirectory(Paths.get(StandardSystemProperty.JAVA_IO_TMPDIR.value()).toFile());
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
		startB.setOnAction(this::startDownload); 
		rowGP.add(startB, 0, 0);
		
		Button stopB = new Button("Stop");
		stopB.setOnAction(this::stopDownload);
		rowGP.add(stopB, 1, 0);
		
		add(rowGP, 1, row, 2, 1);
	}
	
	private void createResultsRow(int row) {
		GridPane rowGP = new GridPane();
		rowGP.setHgap(10);
		
		TextArea consoleTA = new TextArea();
		consoleTA.setEditable(false);
		consoleTA.setPrefHeight(200);
		consoleTA.textProperty().bindBidirectional(model.getOutputConsoleProp());
		consoleTA.textProperty().addListener((obs, val, oldVal) -> {
			// autoscroll on change : http://stackoverflow.com/questions/17799160/javafx-textarea-and-autoscroll
			consoleTA.setScrollTop(Double.MAX_VALUE);
		});
		rowGP.add(consoleTA, 0, 0);
		
		ProgressBar progress = new ProgressBar(100);
		progress.progressProperty().bind(downloadTask.progressProperty());
		progress.setVisible(false);
		// TODO : manage width properly (using colspan)
		progress.setPrefWidth(500);
		rowGP.add(progress, 0, 1);
		this.progressBar = progress;
		
		Label statusL = new Label();
		statusL.textProperty().bind(downloadTask.messageProperty());
		rowGP.add(statusL, 0, 2);
		
		add(rowGP, 0, row, 4, 1);
	}
	
	private void startDownload(ActionEvent ae) {
		progressBar.setVisible(true);
		THREAD_POOL.submit(downloadTask);
	}
	
	private void stopDownload(ActionEvent ae) {
		// TODO
	}
}
