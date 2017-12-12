package sem.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;

import java.io.IOException;
import java.util.ArrayList;

public class TagElementController extends Label {

    private IMailSteuerung mailSteuerung;
    private TagGrenz tagGrenz;
    private ListView emailView;
    private Label labelVon, labelAn, labelDatum;
    private WebView webView;
    private Label labelBetreff;

    public TagElementController(IMailSteuerung mailSteuerung, TagGrenz tagGrenz, ListView emailView, WebView webview,
                                Label labelVon, Label labelAn, Label labelDatum, Label labelBetreff){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/TagElement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setText(tagGrenz.getName());

        this.labelVon = labelVon;
        this.labelAn = labelAn;
        this.labelDatum = labelDatum;
        this.webView = webview;
        this.labelBetreff = labelBetreff;
        this.mailSteuerung = mailSteuerung;
        this.tagGrenz = tagGrenz;
        this.emailView = emailView;

        this.setOnMouseClicked(event -> onMouseClicked(event));
        this.setOnMouseEntered(event -> onMouseEntered(event));
        this.setOnMouseExited(event -> onMouseExited(event));
    }

    public void onMouseClicked(MouseEvent event){
        webView.getEngine().loadContent("");
        emailView.getItems().clear();
        ArrayList<EMailGrenz> emails = mailSteuerung.sucheEMailByTag(tagGrenz);

        for(EMailGrenz email : emails){
            emailView.getItems().add(new EmailListElementController(webView, email, labelVon, labelAn, labelDatum, labelBetreff));
        }
    }

    public void onMouseEntered(MouseEvent event){
        this.setStyle("-fx-background-color: lightgray");
    }

    public void onMouseExited(MouseEvent event){
        this.setStyle("-fx-background-color: transparent");
    }
}
