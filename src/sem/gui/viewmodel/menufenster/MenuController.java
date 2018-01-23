package sem.gui.viewmodel.menufenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import sem.gui.viewmodel.utils.ControllerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable{
    @FXML
    private SplitPane root;

    @FXML
    private AnchorPane paneAuswahlausMenu;

    @FXML
    private ListView <String> listMenuAssistent;


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMenuList();
    }

    public void initMenuList(){

        listMenuAssistent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (listMenuAssistent.getSelectionModel().getSelectedIndices().get(0)){
                    case 0:
                        paneAuswahlausMenu.getChildren().setAll(ControllerFactory.getAssistentController().getRoot());
                        break;
                    case 2:
                        AssistentController assistentController = ControllerFactory.createAssistentController();
                        assistentController.setParent(paneAuswahlausMenu);
                        assistentController.init();
                        paneAuswahlausMenu.getChildren().setAll(assistentController.getRoot());
                        break;
                    case 3:
                        KontoEinstellungenController kontoController = ControllerFactory.createKontoEinstellungenController();
                        kontoController.setParent(paneAuswahlausMenu);
                        kontoController.init();
                        paneAuswahlausMenu.getChildren().setAll(kontoController.getRoot());
                        break;
                }
            }
        });

    }

    public Parent getRoot(){
        return root;
    }



}
