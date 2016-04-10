package database;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TESTGUI extends Application {
	private Database db;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		db = new Database();
		
		primaryStage.setTitle("TEST");
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(200);

		BorderPane b = new BorderPane();

		Button helpButton = new Button("Block");
		helpButton.setShape(new Circle(1));
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				BlockStage block = null;
				try {
					block = new BlockStage(db);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(block != null) {
					block.show();					
				}
			}
		});
		
		b.setCenter(helpButton);
		Scene scene = new Scene(b, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
