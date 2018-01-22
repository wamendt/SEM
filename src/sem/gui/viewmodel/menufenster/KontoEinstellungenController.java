package sem.gui.viewmodel.menufenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.kontosteuerung.impl.IKontoSteuerungImpl;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;
import sem.gui.viewmodel.utils.ControllerFactory;
import sem.gui.viewmodel.verbindungsfenster.VerbindungsfensterController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class KontoEinstellungenController implements Initializable {

    private Parent parent ;

    @FXML
    private AnchorPane root;
    @FXML
    private Button btnKontoVerknuepfen;

    @FXML
    private Button btnRegelHinzufuegen;

    @FXML
    private TextArea txtSignatur;

    @FXML
    private ComboBox<KontoGrenz> comboKonto;

    @FXML
    private Button btnSignaturSpeichern;

    public void init(){
        root.prefWidthProperty().bind(((AnchorPane)parent).prefWidthProperty());
        root.prefHeightProperty().bind(((AnchorPane)parent).prefHeightProperty());
        root.prefWidthProperty().bind(((AnchorPane)parent).widthProperty());
        root.prefHeightProperty().bind(((AnchorPane)parent).heightProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IKontoSteuerung kontoSteuerung = new IKontoSteuerungImpl();
        comboKonto.getItems().addAll(FXCollections.observableArrayList(kontoSteuerung.getAlleKonten()));
        Callback<ListView<KontoGrenz>, ListCell<KontoGrenz>> cellCall = new Callback<ListView<KontoGrenz>, ListCell<KontoGrenz>>() {
            @Override
            public ListCell<KontoGrenz> call(ListView<KontoGrenz> param) {
                return new ListCell<KontoGrenz>(){
                    @Override
                    protected void updateItem(KontoGrenz item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getUserName());
                        }else{
                            setGraphic(null);
                        }
                    }
                };
            }
        };
        comboKonto.setCellFactory(cellCall);
        comboKonto.setButtonCell(cellCall.call(null));

        comboKonto.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KontoGrenz>() {
            @Override
            public void changed(ObservableValue<? extends KontoGrenz> observable, KontoGrenz oldValue, KontoGrenz newValue) {
                if (newValue!= null){
                    txtSignatur.setText(newValue.getSignatur());
                }
            }
        });


    }
    public AnchorPane getRoot(){
        return root;
    }

    public void setParent(Parent parent){
        this.parent = parent;
    }

    public Parent getParent() {
        return parent;
    }

    @FXML
    private void btnKontoVerknuepfenOnAction(ActionEvent event){
        VerbindungsfensterController controller = ControllerFactory.createVerbindungsfensterController();
        Scene scene = new Scene(controller.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    private void btnRegelHinzufuegenOnAction(ActionEvent event){
        NeueRegelController controller = ControllerFactory.createNeueRegelController();
        Scene scene = new Scene(controller.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    @FXML
    private void  btnKontoLoeschenOnAction(ActionEvent event){
        KontoLoeschenController controller = ControllerFactory.createKontoLoeschenController();
        Scene scene = new Scene(controller.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    @FXML
    private void btnSignaturSpeichernOnAction(ActionEvent event){
        IKontoSteuerung kontoSteuerung = new IKontoSteuerungImpl();
        KontoGrenz konto = comboKonto.getSelectionModel().getSelectedItem();
        String signatur = txtSignatur.getText();
        kontoSteuerung.erstelleSignatur(konto,signatur);
    }
}

