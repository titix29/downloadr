package com.mycompany.downloadr.framework;

/**
 * A view model backing a View
 * 
 * @author xavier.castel@gmail.com
 *
 */
public interface ViewModel {

	/**
	 * Initialize this view model according to given application context
	 * @param context
	 */
	public void init(ViewModelContext context);
}
