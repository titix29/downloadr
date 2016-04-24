package com.mycompany.downloadr;

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

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Downloadr");
		
		DownloadView view = new DownloadView();
		Scene scene = new Scene(view);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
