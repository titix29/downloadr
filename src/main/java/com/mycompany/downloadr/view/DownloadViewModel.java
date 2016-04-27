package com.mycompany.downloadr.view;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mycompany.downloadr.services.DownloadServices;
import com.mycompany.downloadr.services.FileListingServices;
import com.mycompany.downloadr.services.SimpleFileListing;
import com.mycompany.downloadr.utils.BackgroundViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class DownloadViewModel extends BackgroundViewModel<Void> {

	private final StringProperty inputFileProp;
	private final StringProperty outputFolderProp;
	private final StringProperty outputConsoleProp;
	
	public DownloadViewModel() {
		this.inputFileProp = new SimpleStringProperty();
		this.outputFolderProp = new SimpleStringProperty();
		this.outputConsoleProp = new SimpleStringProperty("");
	}
	
	@Override
	protected Task<Void> backgroundTask() {
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

}
