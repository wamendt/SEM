package sem.gui.viewmodel.hauptfenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import sem.fachlogik.assistentsteuerung.impl.IAssistentSteuerungImpl;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.grenzklassen.EMailGrenz;

import sem.fachlogik.grenzklassen.TagGrenz;
import sem.fachlogik.kontosteuerung.impl.IKontoSteuerungImpl;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;
import sem.fachlogik.mailsteuerung.event.MsgReceivedEvent;
import sem.fachlogik.mailsteuerung.event.MsgRemovedEvent;
import sem.fachlogik.mailsteuerung.impl.IMailSteuerungImpl;
import sem.fachlogik.mailsteuerung.listener.MsgReceivedListener;
import sem.fachlogik.mailsteuerung.listener.MsgRemovedListener;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;
import sem.gui.viewmodel.utils.ControllerFactory;
import sem.gui.viewmodel.menufenster.MenuController;
import sem.gui.viewmodel.utils.TagClickedListener;
import sem.gui.viewmodel.verfassungsfenster.VerfassungsfensterController;

import java.net.URL;

import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class HauptfensterController implements Initializable, TagClickedListener, MsgReceivedListener, MsgRemovedListener {

    @FXML
    private Parent root;

    @FXML
    private Button buttonWeiterleiten, buttonSearch, buttonLoeschen, buttonMailantworten;

    @FXML
    private Button buttonNeue;

    @FXML
    private Button buttonMenu;
    

    @FXML
    private ListView<EMailGrenz> listViewEmails;

    @FXML
    private FlowPane flowPaneTags;

    @FXML
    private AnchorPane paneEmailAnzeige;

    @FXML
    private WebView webViewEmail;

    @FXML
    private Label labelAn, labelVon, labelDatum;

    @FXML
    private Label labelBetreff;

    @FXML
    private TreeView <String> treeViewOrdner;


    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane statistikPane;

    @FXML
    private PieChart grafEmailStatistik;

    @FXML private FlowPane flowPaneWoerter;

    @FXML
    private Label labelTagWoerter;


    private IMailSteuerung mailSteuerung = new IMailSteuerungImpl();
    private IKontoSteuerung kontoSteuerung = new IKontoSteuerungImpl();
    private IAssistentSteuerung assistentSteuerung = new IAssistentSteuerungImpl();

    public ArrayList<TagGrenz> clickedTags = new ArrayList<>();

    public void initFlowPaneTags(){
        flowPaneTags.getChildren().clear();
        statistikPane.setVisible(false);
        ArrayList <TagGrenz> tags = assistentSteuerung.zeigeAlleTagsAn();
        for (TagGrenz tag: tags){
            TagsController controller = ControllerFactory.createTagsController();
            controller.setLabel(tag.getName());
            controller.setTag(tag);
            flowPaneTags.getChildren().add(controller.getRoot());
            controller.setListener(this);
        }
    }

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

        treeViewOrdner.getSelectionModel().select(ordner.indexOf("INBOX") + 1);

        treeViewOrdner.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                listViewEmails.getItems().clear();
                paneEmailAnzeige.setVisible(false);
                statistikPane.setVisible(false);
                setButtonDisabled(true);
                showEmails();
            }
        });
    }


    private void initEmailListView(){
        ArrayList<EMailGrenz> emails = mailSteuerung.zeigeAlleEMailsAusOrdner(kontoSteuerung.getKonto(1),"INBOX");
        //ArrayList<EMailGrenz> emails = mailSteuerung.importEMails(kontoSteuerung.getKonto(1));
        ObservableList<EMailGrenz> observableEmails = FXCollections.observableArrayList(emails);

        listViewEmails.setItems(observableEmails);

        listViewEmails.setCellFactory(new Callback<ListView<EMailGrenz>, ListCell<EMailGrenz>>() {
            @Override
            public ListCell<EMailGrenz> call(ListView<EMailGrenz> param) {
                return new EmailListCell();
            }
        });

        listViewEmails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EMailGrenz>() {
            @Override
            public void changed(ObservableValue<? extends EMailGrenz> observable, EMailGrenz oldValue, EMailGrenz newValue) {
                if(newValue != null) {
                    paneEmailAnzeige.setVisible(true);
                    statistikPane.setVisible(true);
                    labelVon.setText(newValue.getAbsender());
                    labelBetreff.setText(newValue.getBetreff());
                    labelAn.setText(newValue.getEmpfaenger().get(0));
                    webViewEmail.getEngine().loadContent(newValue.getContentOriginal());
                    setButtonDisabled(false);

                    grafEmailStatistik.getData().clear();

                    flowPaneWoerter.setVisible(false);
                    labelTagWoerter.setVisible(false);

                    double[] tagVerteilung = assistentSteuerung.getTagVerteilung(newValue);
                    for(int i = 0; i < tagVerteilung.length; i++){
                        final int j = i;
                        if(tagVerteilung[i] > 0.001) {
                            PieChart.Data data = new PieChart.Data(assistentSteuerung.getTagById(i + 1).getName(), tagVerteilung[i]);
                            grafEmailStatistik.getData().add(data);
                            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                                flowPaneWoerter.getChildren().clear();
                                labelTagWoerter.setVisible(true);
                                for(String s : assistentSteuerung.getTagById(j+1).getWoerter())
                                    flowPaneWoerter.getChildren().add(new Label(s));
                                flowPaneWoerter.setVisible(true);
                            });
                        }
                    }
                }

            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEmailListView();
        initTreeViewOrdner();
        initFlowPaneTags();
        paneEmailAnzeige.setVisible(false);
        buttonMailantworten.setDisable(true);
        buttonLoeschen.setDisable(true);
        buttonWeiterleiten.setDisable(true);
        statistikPane.setVisible(false);
        mailSteuerung.addMsgReceivedListener(this);
        mailSteuerung.addMsgRemovedListener(this);

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
        MenuController controller = ControllerFactory.createMenuController();
        Scene scene = new Scene(controller.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    private void btnNeueEmailOnAction (ActionEvent event){
        createVerfassungsfenster();
    }

    @FXML
    private void btnAntwortenOnAction(ActionEvent event){
        VerfassungsfensterController controller = createVerfassungsfenster();
        EMailGrenz email = listViewEmails.getSelectionModel().getSelectedItem();

        StringBuilder sb = new StringBuilder();
        sb.append("<br><br><br> ________________________________________________________________________________ <br>" +
                "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><font face=\"Lucida Grande\" size=\"2\" " +
                "color=\"#989898\"><b>Bisheriger Nachrichtenverlauf: </b><br><br>");
        sb.append("</font></p></body></html>");
        sb.append(email.getContentOriginal());
        controller.getEmailInhalt().setHtmlText(sb.toString());
        controller.getTxtEmpfaenger().setText(email.getAbsender());
        controller.getTxtBetreff().setText("RE: " + email.getBetreff());

        sb = new StringBuilder();
        for(String cc : email.getCc()){
            if(!cc.isEmpty())
                sb.append(cc + " ; ");
        }
        controller.getTxtBCC().setText(sb.toString());

        sb = new StringBuilder();
        for(String bcc : email.getBcc()){
            if(!bcc.isEmpty())
                sb.append(bcc + " ;");
        }
        controller.getTxtCC().setText(sb.toString());

    }
    @FXML
    private void btnWeiterleitenOnAction(ActionEvent event){
        VerfassungsfensterController controller = createVerfassungsfenster();
        EMailGrenz email = listViewEmails.getSelectionModel().getSelectedItem();
        controller.getEmailInhalt().setHtmlText(email.getContentOriginal());
        controller.getTxtBetreff().setText("Fwd: " + email.getBetreff());
    }

    @FXML
    private void btnLoeschenOnAction(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Email Löschen");
        alert.setHeaderText("Email Löschen");
        alert.setContentText("Sicher das die Email gelöscht werden soll?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            EMailGrenz email = listViewEmails.getSelectionModel().getSelectedItem();
            mailSteuerung.weiseEinerEMailFlagZu(kontoSteuerung.getKonto(1), "geloescht", email);
            listViewEmails.getItems().remove(email);
        }
    }

    @FXML
    private void btnSearchOnAction(ActionEvent event){
        search();
    }

    @FXML
    private void searchFieldOnKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
           search();
        }
    }

    private void search(){
        ArrayList<EMailGrenz> emails = mailSteuerung.sucheEMail(searchField.getText());
        listViewEmails.setItems(FXCollections.observableArrayList(emails));
        paneEmailAnzeige.setVisible(false);
        statistikPane.setVisible(false);
        setButtonDisabled(true);
    }

    private VerfassungsfensterController createVerfassungsfenster(){
        VerfassungsfensterController controller = ControllerFactory.createVerfassungsfensterController();
        Scene scene = new Scene(controller.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        return controller;
    }

    private void setButtonDisabled(boolean status){
        buttonWeiterleiten.setDisable(status);
        buttonLoeschen.setDisable(status);
        buttonMailantworten.setDisable(status);
    }


    public Parent getRoot(){
        return root;
    }

    @Override
    public void tagClicked(TagGrenz tag) {
        statistikPane.setVisible(false);
        paneEmailAnzeige.setVisible(false);
        clickedTags.add(tag);
        showEmails();
    }

    @Override
    public void tagReleased(TagGrenz tag) {
        statistikPane.setVisible(false);
        paneEmailAnzeige.setVisible(false);
        clickedTags.remove(tag);
        showEmails();
    }

    public void showEmails(){
        String ordner = treeViewOrdner.getSelectionModel().getSelectedItem().getValue();
        if(ordner != null){
            ArrayList<EMailGrenz> emailsMitTag = new ArrayList<>();
            ArrayList<EMailGrenz> emailsAusOrdner = mailSteuerung.zeigeAlleEMailsAusOrdner(kontoSteuerung.getKonto(1), ordner);
            ArrayList<EMailGrenz> gefundeEmail = new ArrayList<>();
            if(clickedTags.isEmpty()){
                listViewEmails.setItems(FXCollections.observableArrayList(emailsAusOrdner));
            }else {
                for (TagGrenz tag : clickedTags) {
                    emailsMitTag.addAll(mailSteuerung.sucheEMailByTag(tag));
                }
                for (EMailGrenz email : emailsMitTag) {
                    for (EMailGrenz email2 : emailsAusOrdner) {
                        if (email2.getMid() == email.getMid()) {
                            gefundeEmail.add(email2);
                            break;
                        }
                    }
                }
                listViewEmails.setItems(FXCollections.observableArrayList(gefundeEmail));
            }
        }

    }

    @Override
    public void messageAngekommen(MsgReceivedEvent msgReceivedEvent) {
        if(treeViewOrdner.getSelectionModel().getSelectedItem().getValue().equals("INBOX")){
            listViewEmails.getItems().add(msgReceivedEvent.geteMailGrenz());
        }
        System.out.println(msgReceivedEvent.geteMailGrenz().getBetreff());
    }

    @Override
    public void messageGeloescht(MsgRemovedEvent msgRemovedEvent) {

    }
}








