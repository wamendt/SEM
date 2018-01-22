package sem.gui.viewmodel;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sem.fachlogik.assistentsteuerung.core.Assistent2;
import sem.gui.viewmodel.hauptfenster.HauptfensterController;
import sem.gui.viewmodel.utils.ControllerFactory;
import sem.gui.viewmodel.verbindungsfenster.VerbindungsfensterController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
         VerbindungsfensterController controller = ControllerFactory.createVerbindungsfensterController();
        Parent root = controller.getRoot();
        primaryStage.setTitle("Smart Email Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
//
//        HauptfensterController hauptfensterController = ControllerFactory.createHauptfenster();
//
//        Parent root = hauptfensterController.getRoot();
//        primaryStage.setTitle("Smart Email Manager");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();


    }


    public static void main(String[] args) {
        Assistent2.getInstance().loadModel();
        launch(args);
    }
}
