package com.mycompany.downloadr.framework;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Base class for all views
 * 
 * @author xavier.castel@gmail.com
 *
 * @param <VM>	ViewModel type
 */
public abstract class AbstractView<VM extends ViewModel> implements View<VM> {

	protected VM vm;

	@Override
	public void injectViewModel(VM viewModel) {
		this.vm = viewModel;
	}
	
	protected void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText(message);
		alert.show();
	}
}
