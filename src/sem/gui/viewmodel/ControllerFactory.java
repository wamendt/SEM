package sem.gui.viewmodel;

import com.sun.org.apache.xml.internal.utils.URI;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ControllerFactory {
    private ControllerFactory(){}

    public static HauptfensterController createHauptfenster(){
        FXMLLoader loader = new FXMLLoader(HauptfensterController.class.getResource("../view/fxml/Hauptfenster.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static EmailListElementController createEmailListElement(){
        FXMLLoader loader = new FXMLLoader(EmailListElementController.class.getResource("../view/fxml/EmailListElement.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static TagsController createTagsController(){
        FXMLLoader loader = new FXMLLoader(EmailListElementController.class.getResource("../view/fxml/TagElement.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public static AssistentMenuController createAssistentMenuController(){
      FXMLLoader loader = new FXMLLoader(AssistentMenuController.class.getResource("../view/fxml/AssistentMenuFenster.fxml"));
      try {
          loader.load();
      } catch (IOException e){
          e.printStackTrace();
      }
        return loader.getController();
    }

    public static AllgemeineStatistikController createAllgemeineStatistikController(){
        FXMLLoader loader =  new FXMLLoader(AllgemeineStatistikController.class.getResource("../view/fxml/AllgemeineStatistikPane.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

}
