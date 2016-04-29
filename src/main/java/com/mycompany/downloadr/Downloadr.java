package com.mycompany.downloadr;

import com.mycompany.downloadr.framework.ViewFactory;
import com.mycompany.downloadr.framework.ViewModelContext;
import com.mycompany.downloadr.view.DownloadView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * On Eclipse, need to add access rule to javafx package (http://stackoverflow.com/questions/22812488/using-javafx-in-jre-8)
 * 
 * @author xavier.castel@gmail.com
 */
public class Downloadr extends Application {
	
	private ViewModelContext ctx;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Downloadr");
		
		this.ctx = new ViewModelContext();
		
		DownloadView view = ViewFactory.create(DownloadView.class, ctx);
		
		Scene scene = new Scene(view.createUI());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		ctx.stoppping();
		super.stop();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
