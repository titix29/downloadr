package com.mycompany.downloadr.framework;

import java.lang.reflect.ParameterizedType;

import com.google.common.base.Throwables;

/**
 * An object producing views
 * 
 * @author xavier.castel@gmail.com
 *
 */
public class ViewFactory {

	@SuppressWarnings("unchecked")
	public static <V extends View<VM>, VM extends ViewModel> V create(Class<V> viewClass, ViewModelContext ctx) {
		V view = null;
		try {
			view = viewClass.newInstance();
			Class<VM> viewModelClass = (Class<VM>) ((ParameterizedType)viewClass.getGenericSuperclass()).getActualTypeArguments()[0];
			VM viewModel = viewModelClass.newInstance();
			viewModel.init(ctx);
			view.injectViewModel(viewModel);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		
		return view;
	}
}
