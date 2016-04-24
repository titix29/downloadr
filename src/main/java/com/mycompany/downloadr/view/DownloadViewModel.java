package com.mycompany.downloadr.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DownloadViewModel {

	private StringProperty inputFileProp;
	private StringProperty outputFolderProp;

	public DownloadViewModel() {
		this.inputFileProp = new SimpleStringProperty();
		this.outputFolderProp = new SimpleStringProperty();
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
}
