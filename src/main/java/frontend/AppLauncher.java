package frontend;

import backend.CanvasState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppLauncher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		CanvasState canvasState = new CanvasState(); // BackEnd

		// Root node of components
		MainFrame frame = new MainFrame(canvasState);
		Scene scene = new Scene(frame);

		primaryStage.setResizable(false);
		primaryStage.setTitle("Paint 1.0");
//		Image icon = new Image("/frontend/bucket.jpg");
//		primaryStage.getIcons().add(icon);

		// Add the Scene to the Stage
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event -> System.exit(0));
	}

}
