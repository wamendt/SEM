package sem.gui.viewmodel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        HauptfensterController hauptfensterController = ControllerFactory.createHauptfenster();
        Parent root = hauptfensterController.getRoot();
        primaryStage.setTitle("Smart Email Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}