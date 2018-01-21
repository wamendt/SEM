package sem.gui.viewmodel.menufenster;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class NeueRegelController {
    private Parent parent ;

    @FXML
    private AnchorPane root;

    public Parent getParent() {
        return parent;
    }

    public AnchorPane getRoot() {
        return root;
    }
}
