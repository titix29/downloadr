package com.mycompany.downloadr.utils;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A ViewModel with a Service worker producing a T result
 * 
 * @param <T>	result type
 */
public abstract class BackgroundViewModel<T> {

	private final Service<T> srv;
	
	public BackgroundViewModel() {
		this.srv = new Service<T>() {

			@Override
			protected Task<T> createTask() {
				return backgroundTask();
			}
		};
	}
	
	protected abstract Task<T> backgroundTask();
	
	public void start() {
		// restart in case it has already been started
		srv.restart();
	}
	
	public boolean stop() {
		return srv.cancel();
	}
	
	public ReadOnlyBooleanProperty getRunningProperty() {
		return srv.runningProperty();
	}
	
	public ReadOnlyDoubleProperty getProgressProperty() {
		return srv.progressProperty();
	}
	
	public ReadOnlyStringProperty getMessageProperty() {
		return srv.messageProperty();
	}
}
