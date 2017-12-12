package sem.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.WebView;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.fachlogik.assistentsteuerung.impl.IAssistentSteuerungImpl;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.fachlogik.kontosteuerung.impl.IKonstoSteuerungImpl;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;
import sem.fachlogik.mailsteuerung.impl.IMailSteuerungImpl;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private TreeView <Label> treeviewOrdner;

    @FXML
    private Label labelAn, labelVon, labelDatum;

    private IMailSteuerung mailSteuerung = new IMailSteuerungImpl();
    private IKontoSteuerung kontoSteuerung = new IKonstoSteuerungImpl();
    private IAssistentSteuerung assistentSteuerung = new IAssistentSteuerungImpl();

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
    @FXML
    public void button_menuOnMouseExited(MouseEvent event){
        btn_OnMouseExited(button_menu);
    }
    @FXML
    private void btn_OnMouseEntered(Button button){
        button.setStyle("-fx-background-color: lightgray");
    }
    @FXML
    private void btn_OnMouseExited(Button button) {
        button.setStyle("-fx-background-color: transparent");
    }

    @FXML
    private void assistentTrainierenMenuOnAction(ActionEvent event){
        try {
            assistentSteuerung.trainiereSEM(20, 1.0, 0.1);
            flowPaneTags.getChildren().clear();
            ArrayList<TagGrenz> tags = assistentSteuerung.zeigeAlleTagsAn();
            for(TagGrenz tag : tags)
                flowPaneTags.getChildren().add(new TagElementController(mailSteuerung, tag, listViewEmails, webViewEmail,
                        labelVon, labelAn, labelDatum));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickOnOrdner(javafx.event.ActionEvent event) throws IOException {
        KontoGrenz kontoGrenz = kontoSteuerung.getKonto(1);
        ICRUDMail crudMail = ICRUDManagerSingleton.getIcrudMailInstance();
       listViewEmails.getItems().clear();
        try {
            ArrayList<EMail> emails = crudMail.getAlleEMails();
            for (EMail eMail : emails){
               // EmailListElementController emailListElementController =
                       // new EmailListElementController(webViewEmail,eMail,labelVon,labelAn,labelDatum);
               // listViewEmails.getItems().add(emailListElementController);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Tags
        try {
            flowPaneTags.getChildren().clear();
            ArrayList<TagGrenz> tags = assistentSteuerung.zeigeAlleTagsAn();
            for(TagGrenz tag : tags){
                flowPaneTags.getChildren().add(new TagElementController(mailSteuerung, tag, listViewEmails,
                        webViewEmail, labelVon, labelAn, labelDatum));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Ordnerstruktur
        ArrayList<String> ordner = mailSteuerung.zeigeAlleOrdner(kontoSteuerung.getKonto(1));

        TreeItem <Label> treeItemroot = new TreeItem<>(new OrdnerTreeViewRootElementController(kontoSteuerung.getKonto(1),
                listViewEmails,mailSteuerung,webViewEmail,labelVon,labelAn,labelDatum));

        for(String o: ordner){
                treeItemroot.getChildren().add(new TreeItem<>(new OrdnerTreeViewElementController(kontoSteuerung.getKonto(1),
                        o,listViewEmails,mailSteuerung, webViewEmail, labelVon, labelAn, labelDatum)));
                treeviewOrdner.setRoot(treeItemroot);

            }
        }






}

