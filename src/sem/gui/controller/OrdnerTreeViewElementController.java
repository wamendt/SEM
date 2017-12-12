package sem.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;

import java.io.IOException;
import java.util.ArrayList;

public class OrdnerTreeViewElementController extends Label {

    private String ordnerName;
    private ListView emailView;
    private KontoGrenz konto;
    private IMailSteuerung mailSteuerung;
    private WebView emailWebView;
    private Label labelVon, labelAn, labelDatum, labelBetreff;

    public OrdnerTreeViewElementController(KontoGrenz konto, String ordnerName, ListView emailView,
                                           IMailSteuerung mailSteuerung, WebView emailWebView,
                                           Label labelVon, Label labelAn, Label labelDatum, Label labelBetreff){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/OrdnerTreeViewElement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.ordnerName = ordnerName;
        this.emailView = emailView;
        this.mailSteuerung = mailSteuerung;
        this.konto = konto;
        this.emailWebView = emailWebView;
        this.labelAn = labelAn;
        this.labelVon = labelVon;
        this.labelDatum = labelDatum;
        this.labelBetreff = labelBetreff;
        this.setOnMouseClicked(event -> onMouseClicked(event));

        this.setText(ordnerName);
    }


    public void onMouseClicked(MouseEvent event){
        emailWebView.getEngine().loadContent("");
        emailView.getItems().clear();
        ArrayList<EMailGrenz> emails =  mailSteuerung.zeigeAlleEMailsAusOrdner(konto, ordnerName);

        for(EMailGrenz email : emails){
            System.out.println(email.getMid());
            emailView.getItems().add(new EmailListElementController(emailWebView, email, labelVon, labelAn, labelDatum, labelBetreff));
        }
    }
}
