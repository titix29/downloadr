package com.mycompany.downloadr.framework;

import javafx.scene.Parent;

/**
 * Interface describing a JavaFX view backed by a ViewModel of type VM
 * 
 * @author xavier.castel@gmail.com
 *
 * @param <VM>
 */
public interface View<VM extends ViewModel> {
	
	/**
	 * Receive viewModel information
	 * @param viewModel
	 */
	void injectViewModel(VM viewModel);
	
	/**
	 * Creates the view and return its root node
	 * @return
	 */
	public Parent createUI();
}
