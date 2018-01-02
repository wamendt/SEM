package sem.gui.viewmodel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AssistentMenuController implements Initializable{
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
                        paneAuswahlausMenu.getChildren().setAll(ControllerFactory.createAllgemeineStatistikController().getRoot());
                        break;
                }
            }
        });

    }

    public Parent getRoot(){
        return root;
    }



}
