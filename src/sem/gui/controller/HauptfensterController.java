package sem.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDAdresse;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTag;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class HauptfensterController implements Initializable{
    @FXML
    private Button button_weiterleiten, button_search, button_loeschen, button_mailantworten, button_neue;

    @FXML
    private Button button_option, button_menu, button_mailoption;

    @FXML
    private ListView <AnchorPane> listViewEmails;

    @FXML
    private FlowPane flowPaneTags;

    @FXML
    private WebView webViewEmail;

    @FXML
    private TreeView <String> treeview;

    @FXML
    private Label labelAn, labelVon, labelDatum;

    @FXML
    public void button_weiterleitenOnMauseEntered(MouseEvent event){
        btn_OnMouseEntered(button_weiterleiten);
    }
    @FXML
    public void button_weiterleitenOnMouseEntered(MouseEvent event){
        btn_OnMouseExited(button_weiterleiten);

    }
    @FXML
    public void button_mailantwortenOnMouseEntered(MouseEvent event){
        btn_OnMouseEntered(button_mailantworten);
    }
    @FXML
    public void button_mailantwortenOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_mailantworten);
    }
    @FXML
    public void button_searchOnMouseEntered(MouseEvent event){
        btn_OnMouseEntered(button_search);
    }
    @FXML
    public void button_searchOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_search);
    }
    @FXML
    public void button_loeschenOnMouseEntered(MouseEvent event){
        btn_OnMouseEntered(button_loeschen);
    }
    @FXML
    public void button_loeschenOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_loeschen);
    }
    @FXML
    public void button_optionOnMouseEntered(MouseEvent event){
        btn_OnMouseEntered(button_option);
    }
    @FXML
    public void button_optionOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_option);
    }
    @FXML
    public void button_neueOnMouseEntered(MouseEvent event){
        btn_OnMouseEntered(button_neue);
    }
    @FXML
    public void button_neueOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_neue);
    }
    @FXML
    public void button_menuOnMouseEntered(MouseEvent event){
        btn_OnMouseEntered(button_menu);
    }
    public void button_menuOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_menu);
    }
    private void btn_OnMouseEntered(Button button){
        button.setStyle("-fx-background-color: lightgray");
    }
    private void btn_OnMouseExited(Button button) {
        button.setStyle("-fx-background-color: transparent");
    }

    @FXML
    public void clickOnOrdner(javafx.event.ActionEvent event) throws IOException {
       ICRUDMail crudMail = ICRUDManagerSingleton.getIcrudMailInstance();
       listViewEmails.getItems().clear();
        try {
            ArrayList<EMail> emails = crudMail.getAlleEMails();
            for (EMail eMail : emails){
                EmailListElementController emailListElementController = new EmailListElementController(webViewEmail, eMail,labelVon,labelAn,labelDatum);
                listViewEmails.getItems().add(emailListElementController);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Tags
        ICRUDTag icrudTag = ICRUDManagerSingleton.getIcrudTagInstance();
        try {
            ArrayList <Tag> tags = icrudTag.getAlleTags();
            for (Tag t: tags){
                flowPaneTags.getChildren().add(new Label(t.getName()));
            }
            for (int i=0; i<50; i++){
                flowPaneTags.getChildren().add(new Label(Integer.toString(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Ordnerstruktur
        ICRUDAdresse icrudAdresse = ICRUDManagerSingleton.getIcrudAdresseInstance();
        TreeItem <String> treeItemroot = new TreeItem<>();

        treeview.setRoot(treeItemroot);

    }
}

