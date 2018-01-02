package sem.gui.viewmodel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import sem.fachlogik.assistentsteuerung.impl.IAssistentSteuerungImpl;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.grenzklassen.EMailGrenz;

import sem.fachlogik.kontosteuerung.impl.IKontoSteuerungImpl;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;
import sem.fachlogik.mailsteuerung.impl.IMailSteuerungImpl;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class HauptfensterController implements Initializable{

    @FXML
    private Parent root;

    @FXML
    private Button buttonWeiterleiten, buttonSearch, buttonLoeschen, buttonMailantworten, buttonNeue;

    @FXML
    private Button buttonMenu, buttonMailoption;

    @FXML
    private Button btnAssistent;

    @FXML
    private ListView<EMailGrenz> listViewEmails;

    @FXML
    private FlowPane flowPaneTags;

    @FXML
    private WebView webViewEmail;

    @FXML
    private TreeView <String> treeViewOrdner;

    @FXML
    private Label labelAn, labelVon, labelDatum;

    @FXML
    private TextField searchField;

    @FXML
    private Label labelBetreff;

    private IMailSteuerung mailSteuerung = new IMailSteuerungImpl();
    private IKontoSteuerung kontoSteuerung = new IKontoSteuerungImpl();
    private IAssistentSteuerung assistentSteuerung = new IAssistentSteuerungImpl();



    private void initTreeViewOrdner(){

        TreeItem <String> root = new TreeItem<>("Root");
        TreeItem <String> konto = new TreeItem<>("konto");
        ArrayList<String> ordner = mailSteuerung.zeigeAlleOrdner(kontoSteuerung.getKonto(1));
        for (String s: ordner){
            konto.getChildren().add(new TreeItem<>(s));
        }
        root.getChildren().add(konto);
        treeViewOrdner.setRoot(root);
        treeViewOrdner.setShowRoot(false);
        konto.setExpanded(true);


        treeViewOrdner.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                listViewEmails.getItems().clear();
                webViewEmail.getEngine().loadContent("");
                labelAn.setText("");
                labelVon.setText("");
                labelBetreff.setText("");
                String ordner = newValue.getValue();
                ArrayList <EMailGrenz> emails = mailSteuerung.zeigeAlleEMailsAusOrdner(kontoSteuerung.getKonto(1),ordner);
                ObservableList<EMailGrenz> eMailGrenzs = FXCollections.observableArrayList(emails);
                listViewEmails.setItems(eMailGrenzs);
            }
        });
    }


    private void initEmailListView(){
        /*Alle Emails sollen beim Start geladen werden, dafuer werden Alle emails von der Mailsteuerung zu erst in
         * ein ObservableList getan damit javaFX damit weiter arbeiten kann*/
        ArrayList<EMailGrenz> emails = mailSteuerung.zeigeAlleEMails(kontoSteuerung.getKonto(1));
        ObservableList<EMailGrenz> observableEmails = FXCollections.observableArrayList(emails);

        /*Anschliessend werden diese Emails zur listViewEmails hinzugefuegt*/
        listViewEmails.setItems(observableEmails);

        /*Nun muessen wir sagen wie diese Emails angezeit werden sollen, dafuer definieren wir eine Cellfactory und
         * uebergeben ihr ein implementiertes CallbackInterface, fuer das Callbackinterface muessen wir nur die
         * Methode call implementieren, die uns hier ein neues Objekt von EmailListCell zurueckgibt*/
        listViewEmails.setCellFactory(new Callback<ListView<EMailGrenz>, ListCell<EMailGrenz>>() {
            @Override
            public ListCell<EMailGrenz> call(ListView<EMailGrenz> param) {
                return new EmailListCell();
            }
        });

        /*Nun muessen wir noch bestimmen was getan werden soll wenn wir ein Listenelement in der Listview selektieren
         * das machen wir mit einem selectionmodel, diesem fuegen wir ein implementieres ChangeListener-interface hinzu
         * mit der implementieren Methode changed, die immer dann aufgerufen wird sobald wir ein Listelement anklicken*/
        listViewEmails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EMailGrenz>() {
            @Override
            public void changed(ObservableValue<? extends EMailGrenz> observable, EMailGrenz oldValue, EMailGrenz newValue) {
                if(newValue != null) {
                    webViewEmail.getEngine().loadContent(newValue.getContentOriginal());
                    labelAn.setText(newValue.getEmpfaenger().get(0));
                    labelVon.setText(newValue.getAbsender());
                    labelBetreff.setText(newValue.getBetreff());
                }
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEmailListView();
    }

    @FXML
    private void btnOnMouseEntered(MouseEvent event){
        ((Button) event.getSource()).setStyle("-fx-background-color: lightgray");
    }

    @FXML
    private void btnOnMouseExited(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: transparent");
    }

    @FXML
    private void btnAssistentOnAction(ActionEvent event){
        AssistentMenuController controller = ControllerFactory.createAssistentMenuController();
        Scene scene = new Scene(controller.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public Parent getRoot(){
        return root;
    }
}








