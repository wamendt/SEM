package sem.gui.viewmodel.verbindungsfenster;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sem.fachlogik.assistentsteuerung.impl.IAssistentSteuerungImpl;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.kontosteuerung.impl.IKontoSteuerungImpl;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;
import sem.fachlogik.mailsteuerung.impl.IMailSteuerungImpl;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;
import sem.gui.viewmodel.hauptfenster.HauptfensterController;
import sem.gui.viewmodel.utils.ControllerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VerbindungsfensterController implements Initializable{

    @FXML
    private Button btnConnect;

    @FXML
    private Label labelStatus;

    @FXML
    private AnchorPane root;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField txtSmtp;

    @FXML
    private TextField txtImap;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPasswort;

    public AnchorPane getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressIndicator.setVisible(false);
    }
    private ArrayList<EMailGrenz> emails;

    @FXML
    public void btnConnectAction(ActionEvent event){
        labelStatus.setText("Importiere Emails...");
        IKontoSteuerung kontoSteuerung = new IKontoSteuerungImpl();
        IMailSteuerung mailSteuerung = new IMailSteuerungImpl();
        IAssistentSteuerung assistentSteuerung = new IAssistentSteuerungImpl();

        kontoSteuerung.loescheAlleKonten();
        mailSteuerung.loescheAlleEMails();

        KontoGrenz neuesKonto = new KontoGrenz();
        neuesKonto.setIMAPhost(txtImap.getText());
        neuesKonto.setSMTPhost(txtSmtp.getText());
        neuesKonto.setUserName(txtUsername.getText());
        neuesKonto.setPassWort(txtPasswort.getText());
        kontoSteuerung.registriereKonto(neuesKonto);

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                progressIndicator.setVisible(true);
                mailSteuerung.importEMails(neuesKonto);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HauptfensterController controller = ControllerFactory.createHauptfenster();
                        Stage stage = new Stage();
                        stage.setTitle("Smart Email Manager");
                        stage.setScene(new Scene(controller.getRoot()));
                        stage.show();
                        btnConnect.getScene().getWindow().hide();
                    }
                });
                return null;
            }
        };
        progressIndicator.progressProperty().bind(task.progressProperty());
        Thread thread  = new Thread(task);
        thread.start();
    }



}
