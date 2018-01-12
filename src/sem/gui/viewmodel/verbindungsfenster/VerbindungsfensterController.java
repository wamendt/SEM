package sem.gui.viewmodel.verbindungsfenster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sem.gui.viewmodel.hauptfenster.HauptfensterController;
import sem.gui.viewmodel.utils.ControllerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VerbindungsfensterController implements Initializable{

    @FXML
    private Button btnConnect;

    @FXML
    private AnchorPane root;

    public AnchorPane getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnConnectAction(ActionEvent event){
        HauptfensterController controller = ControllerFactory.createHauptfenster();
        Stage stage = new Stage();
        stage.setTitle("Smart Email Manager");
        stage.setScene(new Scene(controller.getRoot()));
        stage.show();
        btnConnect.getScene().getWindow().hide();

    }



}
