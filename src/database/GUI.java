package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GUI extends Application {

    private static Database db;
    private Label search;
    private Scene scene;
    private BorderPane border;
    private Stage primaryStage;
    private GridPane grid;
    private ScrollPane scroll;



    public static void main(String[] args) {

        try {
            db = new Database();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        launch(args);
    }

//     public GUI (String[] args) throws ClassNotFoundException, SQLException {
//     db = new Database();
//     launch(args);
//     }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
                                + " *Search by cookie name and time by writing in the following format: \n"
                                + "\"cookieName,YYYY-MM-DD HH:MI:SS,YYYY-MM-DD HH:MI:SS\"\n"
                                + "where the first date is start date and the second is end date, no extra blankspaces is allowed.\n\n"
                                + "Rows marked in red are blocked pallets.\n"
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
        Scene scene = new Scene(border, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {

                    String[][] output = db.search(searchField.getText());

                    drawInfoGrid(output);




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
                input[i][j] = "Hej hej hallå";
            }
        }
        drawInfoGrid(input);
    }

    private void drawInfoGrid(String[][] input) {

        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(8);
        grid.setVgap(10);

        if(input == null){
            Label l = new Label("No result found, please check your input");
            GridPane.setConstraints(l, 1, 0);
            grid.getChildren().add(l);
            scroll.setContent(grid);
            return;
        }
        if(input.length == 0){
            Label l = new Label("No such cookies currently in store");
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
        GridPane.setConstraints(Cookie, 1, 0);
        GridPane.setConstraints(Time, 2, 0);
        GridPane.setConstraints(Location, 3, 0);
        grid.getChildren().addAll(Barcode, Cookie, Time, Location);

        if (input != null) {
            for (int i = 1; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    Label l = new Label(input[i][j]);
                    GridPane.setConstraints(l, j, i);
                    grid.getChildren().add(l);
                }
            }
        }


        scroll.setContent(grid);
    }

}
