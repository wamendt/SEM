package sem.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import sem.fachlogik.grenzklassen.EMailGrenz;

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

    private EMailGrenz email;
    private Label labelVon, labelAn, labelDatum, labelBetreff;

    public EmailListElementController(WebView webView, EMailGrenz email, Label labelVon, Label labelAn, Label labelDatum, Label labelBetreff){
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
        this.labelBetreff = labelBetreff;

        webView.getEngine().setJavaScriptEnabled(true);
        this.setOnMouseClicked(event -> onMouseClicked (event) );


    }
    public void onMouseClicked(javafx.scene.input.MouseEvent event){

        labelVon.setText(email.getAbsender());
        labelAn.setText(email.getEmpfaenger().get(0));
        //labelDatum.setText(null);
        labelBetreff.setText(email.getBetreff());

        webView.getEngine().loadContent(email.getContentOriginal());
    }

}
