package quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quiz.database.DatabaseHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scene/login.fxml"));
        primaryStage.setTitle("Quiz");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.createTables();
            }
        }).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
