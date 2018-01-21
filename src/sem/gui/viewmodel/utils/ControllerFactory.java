package sem.gui.viewmodel.utils;

import javafx.fxml.FXMLLoader;
import sem.gui.viewmodel.hauptfenster.EmailListElementController;
import sem.gui.viewmodel.hauptfenster.HauptfensterController;
import sem.gui.viewmodel.hauptfenster.TagsController;
import sem.gui.viewmodel.menufenster.*;
import sem.gui.viewmodel.verbindungsfenster.VerbindungsfensterController;
import sem.gui.viewmodel.verfassungsfenster.VerfassungsfensterController;

import java.io.IOException;
import java.net.URL;

/**
 * Klasse zum Erstellen von Controllern fuer die Gui, es kann kein Exampler dieser Klasse selbst erstellt werden,
 * stattdessen sollen die statischen create Methoden verwendet werden.
 */
public class ControllerFactory {

    /**
     * Ein einzigartiges Hauptfenster Objekt, da das ganze Programm nur ein Hauptfenster haben darf.
     */
    private static HauptfensterController hauptfensterController;

    private ControllerFactory(){}

    /**
     * Generische HilfsMethode die , die Controller aus mit dem {@link FXMLLoader} laedt
     * @param url der pfad zur fxml datei, des Controllers
     * @param <T> der Generische Controller
     * @return den geladenen Controller
     */
    private static<T> T createController(URL url){
        FXMLLoader loader = new FXMLLoader(url);
        try {
            loader.load();
        } catch (IOException e) {
            //nicht moeglich hier zu landen
        }
        return loader.getController();
    }

    /**
     * Laedt ein Hauptfenster controller
     * @return den geladenen {@link HauptfensterController}
     */
    public static HauptfensterController createHauptfenster(){
        hauptfensterController = createController(HauptfensterController.class.getResource("../../view/fxml/Hauptfenster.fxml"));
        return hauptfensterController;
    }

    /**
     * Getter Methode fuer den HauptfensterController
     * @return den einzigartigen {@link HauptfensterController}ercontroller
     */
    public static HauptfensterController getHauptfensterController(){
        return hauptfensterController;
    }

    /**
     * Laedt einen EmailListElementController aus der FXML-Datei
     * @return den geladenen {@link EmailListElementController}
     */
    public static EmailListElementController createEmailListElement(){
        return createController(EmailListElementController.class.getResource( "../../view/fxml/EmailListElement.fxml"));
    }

    /**
     * Laedt einen TagsController aus der FXML-Datei
     * @return den geladenen {@link TagsController}
     */
    public static TagsController createTagsController(){
        return createController(EmailListElementController.class.getResource("../../view/fxml/TagElement.fxml"));
    }

    /**
     * Laedt einen MenuController aus der FXML-Datei
     * @return den geladenen {@link MenuController}
     */
    public static MenuController createMenuController(){
        return createController(MenuController.class.getResource("../../view/fxml/MenuFenster.fxml"));
    }

    /**
     * Laedt einen AlgemeineStatistikController aus der FXML-Datei
     * @return den geladenen {@link AllgemeineStatistikController}
     */
    public static AllgemeineStatistikController createAllgemeineStatistikController(){
        return createController(AllgemeineStatistikController.class.getResource("../../view/fxml/AllgemeineStatistikPane.fxml"));
    }

    /**
     * Laedt einen VerfassungsfensterController aus der FXML-Datei
     * @return den geladenen {@link VerfassungsfensterController}
     */
    public static VerfassungsfensterController createVerfassungsfensterController(){
        return createController(VerfassungsfensterController.class.getResource("../../view/fxml/Verfassungsfenster.fxml"));
    }

    /**
     * Laedt ein VerbindungsfensterController aus der FXML-Datei
     * @return den geladenen {@link VerbindungsfensterController}
     */
    public static VerbindungsfensterController createVerbindungsfensterController(){
        return createController(VerbindungsfensterController.class.getResource("../../view/fxml/Verbindungsfenster.fxml"));
    }

    /**
     * Laedt ein AssistenController aus der FXML-Datei
     * @return den geladenen {@link AssistentController}
     */
    public static AssistentController createAssistentController(){
        return createController(AssistentController.class.getResource("../../view/fxml/AssistentPane.fxml"));
    }

    /**
     * Laedt ein KontoEinstellungenController aus der FXML-Datei
     * @return den geladenen {@link KontoEinstellungenController}
     */
    public static KontoEinstellungenController createKontoEinstellungenController(){
        return createController(KontoEinstellungenController.class.getResource("../../view/fxml/KontoEinstellungenPane.fxml"));
    }

    /**
     * Laedt ein NeueRegelController aus der FXML-Datei
     * @return den geladenen {@link NeueRegelController}
     */
    public static NeueRegelController createNeueRegelController(){
         return createController(NeueRegelController.class.getResource("../../view/fxml/NeueRegelFenster.fxml"));
    }

    /**
     * Laedt ein KontoLoeschenController aus der FXML-Datei
     * @return den geladenen {@link KontoLoeschenController}
     */
    public static KontoLoeschenController createKontoLoeschenController(){
        return createController(KontoLoeschenController.class.getResource("../../view/fxml/KontoLoeschenFenster.fxml"));
    }
}
