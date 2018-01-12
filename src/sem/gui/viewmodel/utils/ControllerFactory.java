package sem.gui.viewmodel.utils;

import javafx.fxml.FXMLLoader;
import sem.gui.viewmodel.hauptfenster.EmailListElementController;
import sem.gui.viewmodel.hauptfenster.HauptfensterController;
import sem.gui.viewmodel.hauptfenster.TagsController;
import sem.gui.viewmodel.menufenster.AllgemeineStatistikController;
import sem.gui.viewmodel.menufenster.AssistentController;
import sem.gui.viewmodel.menufenster.MenuController;
import sem.gui.viewmodel.verbindungsfenster.VerbindungsfensterController;
import sem.gui.viewmodel.verfassungsfenster.VerfassungsfensterController;

import java.io.IOException;

public class ControllerFactory {
    private ControllerFactory(){}

    private static HauptfensterController hauptfensterController;

    public static HauptfensterController createHauptfenster(){
        FXMLLoader loader = new FXMLLoader(HauptfensterController.class.getResource("../../view/fxml/Hauptfenster.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hauptfensterController = loader.getController();
        return loader.getController();
    }

    public static HauptfensterController getHauptfensterController(){
        return hauptfensterController;
    }

    public static EmailListElementController createEmailListElement(){
        FXMLLoader loader = new FXMLLoader(EmailListElementController.class.getResource("../../view/fxml/EmailListElement.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static TagsController createTagsController(){
        FXMLLoader loader = new FXMLLoader(EmailListElementController.class.getResource("../../view/fxml/TagElement.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static MenuController creatMenuController(){
      FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("../../view/fxml/MenuFenster.fxml"));
      try {
          loader.load();
      } catch (IOException e){
          e.printStackTrace();
      }
        return loader.getController();
    }

    public static AllgemeineStatistikController createAllgemeineStatistikController(){
        FXMLLoader loader =  new FXMLLoader(AllgemeineStatistikController.class.getResource("../../view/fxml/AllgemeineStatistikPane.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static VerfassungsfensterController createVerfassungsfensterController(){
        FXMLLoader loader = new FXMLLoader(VerfassungsfensterController.class.getResource("../../view/fxml/Verfassungsfenster.fxml"));
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static VerbindungsfensterController createVerbindungsfensterController(){
        FXMLLoader loader = new FXMLLoader(VerbindungsfensterController.class.getResource("../../view/fxml/Verbindungsfenster.fxml"));
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static AssistentController createAssistentController(){
        FXMLLoader loader = new FXMLLoader(VerbindungsfensterController.class.getResource("../../view/fxml/AssistentPane.fxml"));
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return loader.getController();
    }
}
