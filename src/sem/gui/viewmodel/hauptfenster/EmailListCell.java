package sem.gui.viewmodel.hauptfenster;


import javafx.scene.control.ListCell;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.gui.viewmodel.utils.ControllerFactory;

/**
 * Klasse die eine Zelle in der EmailListe repraesentiert.
 * Diese Klasse uberschreibt nur die geerbte Methode updateItem von ListCell, die beschreibt was getan werden soll,
 * wenn eine Zelle zur ListView hinzugefuegt wird.
 */
public class EmailListCell extends ListCell<EMailGrenz> {

    @Override
    protected void updateItem(EMailGrenz item, boolean empty){
        super.updateItem(item, empty);
        EmailListElementController controller = ControllerFactory.createEmailListElement();
        if(item != null) {
            setGraphic(controller.getRoot());
            controller.setLabelBetreff(item.getBetreff());
            controller.setLabelAbsender(item.getAbsender());
        }else{
            setGraphic(null);
        }
    }
}
