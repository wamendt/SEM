package sem.gui.viewmodel.verfassungsfenster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.kontosteuerung.impl.IKontoSteuerungImpl;
import sem.fachlogik.mailsteuerung.impl.IMailSteuerungImpl;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VerfassungsfensterController implements Initializable{
    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtEmpfaenger;

    @FXML
    private  TextField txtBetreff;

    @FXML
    private TextField txtCC;

    @FXML
    private  TextField txtBCC;

    @FXML
    private HTMLEditor emailInhalt;

    public HTMLEditor getEmailInhalt() {
        return emailInhalt;
    }

    public TextField getTxtEmpfaenger() {
        return txtEmpfaenger;
    }

    public TextField getTxtBetreff() {
        return txtBetreff;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public TextField getTxtCC() {
        return txtCC;
    }

    public TextField getTxtBCC() {
        return txtBCC;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void btnOnMouseEntered(MouseEvent event){
        ((Button) event.getSource()).setStyle("-fx-background-color:lightgray");
    }

    @FXML
    public void btnOnMouseExited(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: transparent");
    }

    @FXML
    public void btnSendenOnAction(ActionEvent event){
        EMailGrenz email = new EMailGrenz();
        email.setBetreff(txtBetreff.getText());
        email.setAbsender(new IKontoSteuerungImpl().getKontoById(1).getUserName());

        String [] empfaengerArr = txtEmpfaenger.getText().split(",");
        ArrayList <String> empfaengerList = new ArrayList<>();
        for (String s: empfaengerArr)
            empfaengerList.add(s);

        email.setEmpfaenger(empfaengerList);

        String[] ccArr =txtCC.getText().split(",");
        ArrayList ccList = new ArrayList();
        for (String s: ccArr)
            ccList.add(s);
        email.setCc(ccList);

        String[] bccArr =txtBCC.getText().split(",");
        ArrayList bccList = new ArrayList();
        for (String s: bccArr)
            bccList.add(s);
        email.setBcc(bccList);

        email.setInhalt(emailInhalt.getHtmlText());
        email.setFiles(new ArrayList<>());
        IMailSteuerung mailSteuerung = new IMailSteuerungImpl();
        mailSteuerung.sendeEMail(new IKontoSteuerungImpl().getKontoById(1),email);

    }
}
