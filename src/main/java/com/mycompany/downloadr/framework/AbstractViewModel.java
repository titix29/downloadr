package com.mycompany.downloadr.framework;

/**
 * Base class for all view models
 * 
 * @author xavier.castel@gmail.com
 *
 */
public abstract class AbstractViewModel implements ViewModel {

	@Override
	public void init(ViewModelContext context) {
		// subscribe to any event that can be raised
		context.registerToEvents(this);
	}
}
