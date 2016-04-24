package com.mycompany.downloadr.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DownloadViewModel {

	private StringProperty inputFileProp;

	public DownloadViewModel() {
		this.inputFileProp = new SimpleStringProperty();
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
	
	
}
