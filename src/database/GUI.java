package database;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.Image;





public class GUI extends Application{
	
	private Database db;
	
	public static void main(String[] args) {
		launch(args);
	}
	
//	public GUI(Database db){
//		this.db = db;
//		launch();
//	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Cookie Production");
		
		primaryStage.getIcons().add(new Image("file:icon.jpg"));
		
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(200);
		
		BorderPane border = new BorderPane();
		
		Label search = new Label("Search: ");
		
		TextField searchField = new TextField();
		searchField.setPrefWidth(400);
		
		Button helpButton = new Button("?");
		helpButton.setShape(new Circle(1));
		
		Button blockButton = new Button("Block pallets");
		blockButton.setAlignment(Pos.CENTER_RIGHT);
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.setPadding(new Insets(15, 12, 15, 12));
		hb.getChildren().addAll(search, searchField, helpButton);
		hb.setSpacing(5);
		
		HBox hbRight = new HBox();
		hbRight.setAlignment(Pos.CENTER_RIGHT);
		hbRight.getChildren().add(blockButton);
		hb.getChildren().add(hbRight);
		HBox.setHgrow(hbRight, Priority.ALWAYS);
		
		border.setTop(hb);
		Scene scene = new Scene(border, 800, 600);
		primaryStage.setScene(scene);;
		primaryStage.show();
		
		
	}
	

	
	
}
