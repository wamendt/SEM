package sem.gui.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

public class EmailListElementController {

    @FXML
    private Parent root;
    @FXML
    private Label labelAbsender;
    @FXML
    private Label labelBetreff;




    public void setLabelAbsender(String absender){
        labelAbsender.setText(absender);
    }
    public void setLabelBetreff(String betreff){
        labelBetreff.setText(betreff);
    }

    public Parent getRoot(){
        return root;
    }
}
