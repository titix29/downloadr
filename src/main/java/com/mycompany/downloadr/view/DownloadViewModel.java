package com.mycompany.downloadr.view;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mycompany.downloadr.services.DownloadServices;
import com.mycompany.downloadr.services.FileListingServices;
import com.mycompany.downloadr.services.SimpleFileListing;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DownloadViewModel {

	private final StringProperty inputFileProp;
	private final StringProperty outputFolderProp;
	private final StringProperty outputConsoleProp;
	
	private final Service<Void> worker;

	public DownloadViewModel() {
		this.inputFileProp = new SimpleStringProperty();
		this.outputFolderProp = new SimpleStringProperty();
		this.outputConsoleProp = new SimpleStringProperty("");
		this.worker = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return downloadTask();
			}
		};
	}
	
	public void startDownload() {
		// restart in case it has already been started
		worker.restart();
	}
	
	public boolean stopDownload() {
		return worker.cancel();
	}
	
	private Task<Void> downloadTask() {
		return new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				FileListingServices fileSrv = new SimpleFileListing();
				DownloadServices downloadSrv = new DownloadServices();
				fileSrv.init(Paths.get(inputFileProp.get()));
				
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
					
					downloaded = downloadSrv.download(url, Paths.get(outputFolderProp.get()));

					appendToConsole(String.format("File [%d/%d] downloaded at %s", i, nbFiles, downloaded));
					updateProgress(i, nbFiles);
				}
				
				return null;
			}
			
			@Override
			protected void failed() {
				String msg = String.format("Download failed : %s", getException().getMessage());
				appendToConsole(msg);
				updateProgress(1, 1);
			}
			
			@Override
			protected void cancelled() {
				// release any ressource
				appendToConsole("User cancelled task");
				updateMessage("");
			}
		};
	}
	
	public String getInputFile() {
		return inputFileProp.get();
	}

	public void setInputFile(String inputFile) {
		inputFileProp.set(inputFile);
	}

	public StringProperty getInputFileProp() {
		return inputFileProp;
	}
	
	public String getOutputFolder() {
		return outputFolderProp.get();
	}

	public void setOutputFolder(String outputFolder) {
		outputFolderProp.set(outputFolder);
	}

	public StringProperty getOutputFolderProp() {
		return outputFolderProp;
	}
	
	public String getOutputConsole() {
		return outputConsoleProp.get();
	}

	public void setOutputConsole(String outputConsole) {
		outputConsoleProp.set(outputConsole);
	}
	
	private void appendToConsole(String text) {
		outputConsoleProp.set(outputConsoleProp.get() + text + System.lineSeparator());
	}
	
	public StringProperty getOutputConsoleProp() {
		return outputConsoleProp;
	}

	public ReadOnlyBooleanProperty getRunningProperty() {
		return worker.runningProperty();
	}
	
	public ReadOnlyDoubleProperty getProgressProperty() {
		return worker.progressProperty();
	}
	
	public ReadOnlyStringProperty getMessageProperty() {
		return worker.messageProperty();
	}
}
