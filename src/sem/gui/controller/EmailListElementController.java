package sem.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.fachlogik.grenzklassen.EMailGrenz;

import javax.mail.internet.MimeMessage;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class EmailListElementController extends AnchorPane{
    @FXML
   private Label label_absender;
    @FXML
    private Label label_betref;
    @FXML
    private Label label_inhalt;
    @FXML
    private Label label_datum;

    private WebView webView;

    private EMail email;//TODO
    private Label labelVon, labelAn, labelDatum;

    public EmailListElementController(WebView webView, EMail email, Label labelVon, Label labelAn, Label labelDatum){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/EmailListElement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.email = email;
        this.webView = webView;
        this.label_absender.setText(email.getAbsender());
        this.label_betref.setText(email.getBetreff());
        this.labelAn = labelAn;
        this.labelVon = labelVon;
        this.labelDatum = labelDatum;
        String inhalt_string;
        webView.getEngine().setJavaScriptEnabled(true);
        this.setOnMouseClicked(event -> onMouseClicked (event) );


    }
    public void onMouseClicked(javafx.scene.input.MouseEvent event){

        labelVon.setText(email.getAbsender());
        labelAn.setText(email.getEmpfaenger());
        labelDatum.setText(null);

        webView.getEngine().loadContent(email.getInhalt());
    }

}
