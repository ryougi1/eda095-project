package database;


import java.util.Locale;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	private Button block;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Locale.setDefault(Locale.US);
		stage.setTitle("Block pallets");
		
		stage.getIcons().add(new Image("file:icon.jpg"));
		
		stage.setMinHeight(400);
		stage.setMinWidth(400);
		
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(20, 0, 20, 20));
		layout.setHgap(7);
		layout.setVgap(7);
		
		Label title = new Label("Select which cookie and during what time intervall to block.");
		
		startDate = new DatePicker();
		endDate = new DatePicker();
		
		RestrictiveTextField hour = new RestrictiveTextField();
		hour.prefWidth(1);
		hour.maxWidth(1);
		hour.setMaxLength(2);
		hour.setRestrict("[0-9]");
		RestrictiveTextField min = new RestrictiveTextField();
		min.setMaxLength(2);
		min.setRestrict("[0-9]");
		
		
		block = new Button("Block");
		Button cancel = new Button("Cancel");
		
		
		layout.add(new Label("Start "),0,1);
		layout.add(startDate, 1, 1);
		layout.add(hour, 2, 1);
		layout.add(new Label(":"), 3, 1);
		layout.add(min, 4, 1);
		layout.add(new Label("End "),0,2);
		layout.add(endDate, 1, 2);
//		layout.add(block, 2, 4);
//		layout.add(cancel, 3, 4);
		
		Scene scene = new Scene(layout, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
}
