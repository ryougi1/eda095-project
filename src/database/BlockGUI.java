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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Locale.setDefault(Locale.US);
		stage.setTitle("Block pallets");

		stage.getIcons().add(new Image("file:icon.jpg"));
		
		stage.setHeight(350);
		stage.setWidth(1400);
		stage.setResizable(false);
		
		BorderPane layout = new BorderPane();
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 0, 20, 20));
		grid.setHgap(7);
		grid.setVgap(7);

		Label title = new Label("Select which cookie and during what time intervall to block.");

		DatePicker startDate = new DatePicker();
		DatePicker endDate = new DatePicker();

		RestrictiveTextField startHour = new RestrictiveTextField();
		startHour.setMaxLength(2);
		startHour.setRestrict("[0-9]");
		startHour.setPromptText("Hour");
		RestrictiveTextField startMin = new RestrictiveTextField();
		startMin.setMaxLength(2);
		startMin.setRestrict("[0-9]");
		startMin.setPromptText("Minute");
		
		RestrictiveTextField endHour = new RestrictiveTextField();
		endHour.setMaxLength(2);
		endHour.setRestrict("[0-9]");
		endHour.setPromptText("Hour");
		RestrictiveTextField endMin = new RestrictiveTextField();
		endMin.setMaxLength(2);
		endMin.setRestrict("[0-9]");
		endMin.setPromptText("Minute");
		
		
		Button block = new Button("Block");
		Button cancel = new Button("Cancel");
		
		
		grid.add(new Label("Start "),0,1);
		grid.add(startDate, 1, 1);
		grid.add(startHour, 2, 1);
		grid.add(new Label(":"), 3, 1);
		grid.add(startMin, 4, 1);
		grid.add(new Label("End "),0,2);
		grid.add(endDate, 1, 2);
		grid.add(endHour, 2, 2);
		grid.add(new Label(":"), 3, 2);
		grid.add(endMin, 4, 2);
		
		HBox top = new HBox();
		top.setAlignment(Pos.CENTER);
		top.getChildren().add(title);
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setSpacing(15);
		hb.getChildren().addAll(block, cancel);
		
		layout.setTop(top);
		layout.setCenter(grid);
		layout.setBottom(hb);
		Scene scene = new Scene(layout, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
}


