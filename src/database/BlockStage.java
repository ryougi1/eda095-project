package database;

import java.sql.SQLException;
import java.util.Locale;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BlockStage extends Stage {
	private DatePicker startDate;
	private DatePicker endDate;


	public BlockStage(final Database db) throws Exception {
		Locale.setDefault(Locale.US);
		setTitle("Block pallets");

		getIcons().add(new Image("file:icon.jpg"));

		setHeight(350);
		setWidth(1400);
		setResizable(false);

		BorderPane layout = new BorderPane();

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 0, 20, 20));
		grid.setHgap(7);
		grid.setVgap(7);

		Label title = new Label(
				"Select which cookie and during what time intervall to block.");

		startDate = new DatePicker();
		endDate = new DatePicker();

		final RestrictiveTextField startHour = new RestrictiveTextField();
		startHour.setMaxLength(2);
		startHour.setRestrict("[0-9]");
		startHour.setPromptText("Hour");
		final RestrictiveTextField startMin = new RestrictiveTextField();
		startMin.setMaxLength(2);
		startMin.setRestrict("[0-9]");
		startMin.setPromptText("Minute");

		final RestrictiveTextField endHour = new RestrictiveTextField();
		endHour.setMaxLength(2);
		endHour.setRestrict("[0-9]");
		endHour.setPromptText("Hour");
		final RestrictiveTextField endMin = new RestrictiveTextField();
		endMin.setMaxLength(2);
		endMin.setRestrict("[0-9]");
		endMin.setPromptText("Minute");

		grid.add(new Label("Start "), 0, 1);
		grid.add(startDate, 1, 1);
		grid.add(startHour, 2, 1);
		grid.add(new Label(":"), 3, 1);
		grid.add(startMin, 4, 1);
		grid.add(new Label("End "), 0, 2);
		grid.add(endDate, 1, 2);
		grid.add(endHour, 2, 2);
		grid.add(new Label(":"), 3, 2);
		grid.add(endMin, 4, 2);

		Button block = new Button("Block");
		block.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert warning = new Alert(AlertType.WARNING);
				warning.setContentText("Invalid Date or Time input!");
				Alert success = new Alert(AlertType.INFORMATION);
				success.setContentText("Pallets successfully blocked!");
				boolean invalid = false;
				
				String startDateTime = new String();
				int startH = -1;
				int startM = -1;
				if (!startHour.getText().trim().isEmpty()) {
					startH = Integer.parseInt(startHour.getText());
				} else {
					invalid = true;
				}
				if (!startMin.getText().trim().isEmpty()) {
					startM = Integer.parseInt(startMin.getText());
				} else {
					invalid = true;
				}
				if (startDate.getValue() != null && startH >= 0 && startH <= 23
						&& startM >= 0 && startM <= 59) {
					String startHour;
					String startMin;
					if (startH < 10) {
						startHour = "0" + startH;
					} else {
						startHour = "" + startH;
					}
					if (startM < 10) {
						startMin = "0" + startM;
					} else {
						startMin = "" + startM;
					}
					startDateTime = startDate.getValue().toString()
							+ " " + startHour + ":" + startMin;
				} else {
					invalid = true;
				}
				String endDateTime = new String();
				int endH = -1;
				int endM = -1;
				if (!endHour.getText().trim().isEmpty()) {
					endH = Integer.parseInt(endHour.getText());
				} else {
					invalid = true;
				}
				if (!endMin.getText().trim().isEmpty()) {
					endM = Integer.parseInt(endMin.getText());
				} else {
					invalid = true;
				}
				if (endDate.getValue() != null && endH >= 0 && endH <= 23
						&& endM >= 0 && endM <= 59) {
					String endHour;
					String endMin;
					if (endH < 10) {
						endHour = "0" + endH;
					} else {
						endHour = "" + endH;
					}
					if (endM < 10) {
						endMin = "0" + endM;
					} else {
						endMin = "" + endM;
					}
					endDateTime = endDate.getValue().toString()
							+ " " + endHour + ":" + endMin;
				} else {
					invalid = true;
				}
				if(!invalid && startDate.getValue().isBefore(endDate.getValue())) {
					db.blockPalletByTime(startDateTime, endDateTime);	
					success.showAndWait();
					close();
				} else if(!invalid && startDate.getValue().equals(endDate.getValue()) && startH < endH) {
					db.blockPalletByTime(startDateTime, endDateTime);	
					success.showAndWait();
					close();
				} else if(!invalid && startDate.getValue().equals(endDate.getValue()) && startH == endH && startM <= endM) {
					db.blockPalletByTime(startDateTime, endDateTime);	
					success.showAndWait();
					close();
				} else {
					warning.showAndWait();
				}
			}
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				close();
			}
		});

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
		setScene(scene);
	}
}

