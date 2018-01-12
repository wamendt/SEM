package sem.gui.viewmodel.menufenster;

import javafx.scene.control.ListCell;
import sem.fachlogik.grenzklassen.TagGrenz;

public class TagListCell extends ListCell<TagGrenz> {

    @Override
    protected void updateItem(TagGrenz item, boolean empty) {
        super.updateItem(item, empty);
        if(item != null){
            System.out.println(item.getName());
            setText(item.getName());
        }else{
            setGraphic(null);
        }
    }
}
