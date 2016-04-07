package database;


import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BlockGUI extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Block pallets");
		
		stage.getIcons().add(new Image("file:icon.jpg"));
		
		stage.setMinHeight(200);
		stage.setMinWidth(200);
		
		BorderPane border = new BorderPane();
		
		Label search = new Label("Select which cookie and during what time intervall to block.");
		

		
	}
	
}
