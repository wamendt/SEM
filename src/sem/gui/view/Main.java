package sem.gui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sem.datenhaltung.entities.EMail;
import sem.datenhaltung.maildaten.impl.IMailServiceImpl;
import sem.datenhaltung.maildaten.services.IMailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        IMailService mailService = new IMailServiceImpl();
        EMail email = new EMail();
        mailService.sendEmail(email);
    }


    public static void main(String[] args) {

        launch(args);
    }
}
