package com.mycompany.downloadr.framework;

import com.google.common.eventbus.EventBus;

/**
 * Application context based on an event bus broadcasting events
 * 
 * @author xavier.castel@gmail.com
 *
 */
public class ViewModelContext {

	private final EventBus eventBus;
	
	public ViewModelContext() {
		this.eventBus = new EventBus();
	}
	
	/**
	 * Make o a subscriber of application events
	 * 
	 * @param o
	 */
	public void registerToEvents(Object o) {
		eventBus.register(o);
	}
	
	/**
	 * Indicates application is stopping
	 */
	public void stoppping() {
		eventBus.post(new DisposeEvent());
	}
	
	/**
	 * Event indicating application is closing
	 * @author xavier.castel@gmail.com
	 *
	 */
	public static class DisposeEvent {
		// marker class
	}
}
