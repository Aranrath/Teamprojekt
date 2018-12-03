package tp;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tp.model.Model;

public class Main extends Application {

	Model model;
	Presenter presenter;
	MainView mainView;

	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			generateMVP();
			
			primaryStage.setMaximized(true);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			Scene scene = new Scene(mainView, primaryScreenBounds.getWidth()*0.9, primaryScreenBounds.getHeight()*0.9);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private void generateMVP() {
		
		model = new Model();
		presenter = new Presenter (model, mainView);
		mainView = new MainView (presenter);
		
	}


	public static void main(String[] args) {
		launch(args);
	}

}
