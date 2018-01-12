package sem.gui.viewmodel.hauptfenster;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sem.fachlogik.grenzklassen.TagGrenz;

public class TagsController {

    private boolean isClicked = false;

    private TagGrenz tag;

    private TagClickedListener listener;

    @FXML
    private Label root;

    @FXML
    private void rootOnMouseEntered(MouseEvent event){
        root.setScaleX(1.3);
        root.setScaleY(1.3);
    }

    @FXML
    private void rootOnMouseExited(MouseEvent event){
        root.setScaleX(1.0);
        root.setScaleY(1.0);
    }

    @FXML
    private void rootOnMouseClicked(MouseEvent event){
        if(!isClicked){
            root.setStyle("-fx-background-color: lightgrey");
            listener.tagClicked(tag);
            isClicked = true;
        }else{
            root.setStyle("-fx-background-color: white");
            listener.tagReleased(tag);
            isClicked = false;
        }
    }
    public Label getRoot() {
        return root;
    }


    public void setLabel(String text) {
        this.root.setText(text);
    }

    public void setListener(TagClickedListener listener) {
        this.listener = listener;
    }

    public void setTag(TagGrenz tag){
        this.tag = tag;
    }
}
