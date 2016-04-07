package database;


import java.util.Locale;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BlockGUI extends Application {
	private DatePicker startDate;
	private DatePicker endDate;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Locale.setDefault(Locale.US);
		stage.setTitle("Block pallets");
		
		stage.getIcons().add(new Image("file:icon.jpg"));
		
		stage.setMinHeight(200);
		stage.setMinWidth(200);
		
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(20, 0, 20, 20));
		layout.setHgap(7);
		layout.setVgap(7);
		
		Label title = new Label("Select which cookie and during what time intervall to block.");
		
		startDate = new DatePicker();
		endDate = new DatePicker();
		
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER_LEFT);
		vb.getChildren().addAll(startDate, endDate);
		
		Scene scene = new Scene(layout, 800, 600);
		stage.setScene(scene);
		stage.show();

	}
	
}
