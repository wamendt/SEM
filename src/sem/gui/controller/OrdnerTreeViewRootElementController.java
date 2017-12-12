package sem.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;

import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.util.ArrayList;

public class OrdnerTreeViewRootElementController extends Label {

    private IMailSteuerung mailSteuerung;
    private ListView emailView;
    private KontoGrenz konto;
    private WebView webView;
    private Label labelVon, labelAn, labelDatum, labelBetreff;

    public OrdnerTreeViewRootElementController(KontoGrenz konto, ListView emailView, IMailSteuerung mailSteuerung,
                                               WebView webView, Label labelVon, Label labelAn , Label labelDatum, Label labelBetreff){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/OrdnerTreeViewElement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.mailSteuerung = mailSteuerung;
        this.emailView = emailView;
        this.konto = konto;
        this.webView = webView;
        this.labelAn = labelAn;
        this.labelVon = labelVon;
        this.labelDatum = labelDatum;
        this.labelBetreff = labelBetreff;
        this.setText(konto.getEmailAddress());

        this.setOnMouseClicked(event -> onMouseClicked(event));
    }

    public void onMouseClicked(MouseEvent event ){
        webView.getEngine().loadContent("");
        try {
            ArrayList<EMailGrenz> emails = mailSteuerung.zeigeAlleEMails(konto);
            emailView.getItems().clear();
            for(EMailGrenz email : emails){
                emailView.getItems().add(new EmailListElementController(webView, email,labelVon,labelAn,labelDatum, labelBetreff ));
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
}
