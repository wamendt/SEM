package sem.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VerbindungsfensterController implements Initializable{

    @FXML
    private Button btnConnect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnConnectAction(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Hauptfenster.fxml"));
        try {
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            btnConnect.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
