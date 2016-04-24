package com.mycompany.downloadr;

import com.mycompany.downloadr.services.DownloadServices;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * On Eclipse, need to add access rule to javafx package (http://stackoverflow.com/questions/22812488/using-javafx-in-jre-8)
 * 
 * @author xavier.castel@gmail.com
 */
public class Downloadr extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Downloadr");
		primaryStage.show();
		
		DownloadServices srv = new DownloadServices();
		srv.download(null, null);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
