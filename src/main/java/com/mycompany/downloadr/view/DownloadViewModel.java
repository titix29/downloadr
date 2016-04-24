package com.mycompany.downloadr.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DownloadViewModel {

	private StringProperty inputFileProp;
	private StringProperty outputFolderProp;
	private StringProperty outputConsoleProp;

	public DownloadViewModel() {
		this.inputFileProp = new SimpleStringProperty();
		this.outputFolderProp = new SimpleStringProperty();
		this.outputConsoleProp = new SimpleStringProperty("");
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
	
	public void appendToConsole(String text) {
		outputConsoleProp.set(outputConsoleProp.get() + text + System.lineSeparator());
	}
	
	public StringProperty getOutputConsoleProp() {
		return outputConsoleProp;
	}
	
	
}
