package sem.gui.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class TagsController {
    @FXML
    private Label root;

    public Label getRoot() {
        return root;
    }

    public void setLabel(String text) {
        this.root.setText(text);
    }
}
