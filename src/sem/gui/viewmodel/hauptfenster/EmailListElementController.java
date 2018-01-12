package sem.gui.viewmodel.hauptfenster;

import javafx.fxml.FXML;

import javafx.scene.Parent;
import javafx.scene.control.Label;



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
