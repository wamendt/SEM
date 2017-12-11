package sem.gui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.mail.MessagingException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
/*
        IMailLocalService mailService = new IMailLocalServiceImpl();
        EMail email = new EMail();
        mailService.sendeEmail(null, email);
        */
    }


    public static void main(String[] args) throws MessagingException {

        /*
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");

        MailStoreManager storeManager = MailStoreManager.getStoreManager();

        Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());
        if(store != null){
            IMailServiceImplSingleton crudeMailServerService = new IMailServiceImplSingleton();
            Folder folder = (IMAPFolder) store.getFolder("INBOX");
            folder.addMessageCountListener(crudeMailServerService);
        }
        */
        launch(args);
    }
}
