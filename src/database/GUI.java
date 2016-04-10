package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class GUI extends Application {

    private static Database db;
    private Label search;
    private Scene scene;
    private BorderPane border;
    private Stage primaryStage;
    private GridPane grid;
    private ScrollPane scroll;



//    public static void main(String[] args) {
//
//        try {
//            db = new Database();
//        } catch (ClassNotFoundException e){
//            e.printStackTrace();
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        launch(args);
//    }

//     public GUI (String[] args) throws ClassNotFoundException, SQLException {
//     db = new Database();
//     launch(args);
//     }

    @Override
    public void start(Stage primaryStage) throws Exception {
        db = new Database();

        Locale.setDefault(Locale.US);
        primaryStage.setTitle("Cookie Production");
        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(200);
        primaryStage.getIcons().add(new Image("file:icon.jpg"));

        border = new BorderPane();
        grid = new GridPane();
        scroll = new ScrollPane();
        scroll.setContent(grid);
        search = new Label("Search: ");
        HBox palletCreator = new HBox();

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Almond delight",
                        "Amneris",
                        "Berliner",
                        "Nut cookie",
                        "Nut ring",
                        "Tango"
                );
        final ComboBox comboBox = new ComboBox(options);
        Button createPallet = new Button("Create Pallet");

        createPallet.setOnAction(e -> {
            System.out.println(comboBox.getValue().toString());
            db.createPallet(comboBox.getValue().toString(), "Freezer");
        });

        palletCreator.getChildren().addAll(comboBox, createPallet);
        palletCreator.setAlignment(Pos.BOTTOM_RIGHT);
        palletCreator.setPadding(new Insets(10, 10, 10, 10));
        palletCreator.setSpacing(5);

        TextField searchField = new TextField();
        searchField.setPrefWidth(400);

        Button helpButton = new Button("?");
        helpButton.setShape(new Circle(1));
        helpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Label info = new Label();
                info.setText(
                        "Available search inputs: \n" + "*Search by cookie name by writing part of the cookies name. \n"
                                + "*Search by pallet barcode by writing the barcode number. \n"
                                + "*Search by cookie name and time by writing in the following format: \n"
                                + "\"cookieName,YYYY-MM-DD HH:MI:SS,YYYY-MM-DD HH:MI:SS\"\n"
                                + "where the first date is start date and the second is end date, no extra blankspaces is allowed.\n\n"
                                + "Rows marked in red are blocked pallets.\n\n"
                                + "Cookies currently in database: "
                                + db.getCookieList());
                scroll.setContent(info);
            }
        });

        Button blockButton = new Button("Block pallets");
        blockButton.setAlignment(Pos.CENTER_RIGHT);
        blockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            }
        });

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
        border.setCenter(scroll);

        border.setBottom(palletCreator);
        Scene scene = new Scene(border, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    drawInfoGrid(db.search(searchField.getText()));
                }
            }
        });


//        searchField.getOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent ke) {
//                if (ke.getCode().equals(KeyCode.ENTER)) {
//                    db.search(searchField.getText());
//                }
//            }
//        });
    }
    private void testInfoGrid() {

        String[][] input = new String[100][4];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                input[i][j] = "Hej hej hall�";
            }
        }
        drawInfoGrid(input);
    }

    private void drawInfoGrid(String[][] input) {

        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(8);
        grid.setVgap(10);
        boolean flag = false;
        Label l = null;

        if(input == null){
            l = new Label("No result found, please check your input");
            flag = true;
        } else if  (input.length == 0){
            l = new Label("No such cookies currently in store");
            flag = true;
        }
        if(flag){
            GridPane.setConstraints(l, 1, 0);
            grid.getChildren().add(l);
            scroll.setContent(grid);
            return;
        }

        Label Barcode = new Label("Barcode");
        Label Cookie = new Label("Cookie");
        Label Time = new Label("Time");
        Label Location = new Label("Location");
        GridPane.setConstraints(Barcode, 0, 0);
        GridPane.setConstraints(Time, 1, 0);
        GridPane.setConstraints(Cookie, 2, 0);
        GridPane.setConstraints(Location, 3, 0);
        grid.getChildren().addAll(Barcode, Cookie, Time, Location);

        if (input != null) {
            ArrayList<Integer> blockedPallets = db.getBlockedPallets();
            for (int i = 0; i < input.length; i++) {
                int barCode = Integer.parseInt(input[i][0]);
                for (int j = 0; j < input[0].length; j++) {
                    l = new Label(input[i][j]);
                    if(blockedPallets.contains(barCode)){
                        Pane p = new Pane();
                        p.getChildren().add(l);
                        p.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                        GridPane.setConstraints(p, j, i + 1);
                        grid.getChildren().add(p);
                    } else {
                        GridPane.setConstraints(l, j, i + 1);
                        grid.getChildren().add(l);
                    }
                }
            }
        }
        scroll.setContent(grid);
    }

}
