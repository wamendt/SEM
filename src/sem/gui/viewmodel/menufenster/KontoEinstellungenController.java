package sem.gui.viewmodel.menufenster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sem.gui.viewmodel.utils.ControllerFactory;
import sem.gui.viewmodel.verbindungsfenster.VerbindungsfensterController;

import java.net.URL;
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
    private ComboBox choiceKonto;

    public void init(){
        root.prefWidthProperty().bind(((AnchorPane)parent).prefWidthProperty());
        root.prefHeightProperty().bind(((AnchorPane)parent).prefHeightProperty());

        root.prefWidthProperty().bind(((AnchorPane)parent).widthProperty());
        root.prefHeightProperty().bind(((AnchorPane)parent).heightProperty());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
}
