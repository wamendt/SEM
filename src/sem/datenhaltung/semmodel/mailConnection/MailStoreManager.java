package sem.datenhaltung.semmodel.mailConnection;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class MailStoreManager {

    public MailStoreManager(){

    }

    //Erstellt eine Imap - Verbindung und liefert das Store - Objekt zurück
    public Store setImapConnection(String host, String username, String password) throws NoSuchProviderException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.imaps.auth", "true");
        props.put("mail.imaps.startssl.enable", "true");
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", "993");


        javax.mail.Authenticator auth = null;
        auth = new javax.mail.Authenticator() {
            @Override
            public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        Store store = session.getStore("imaps");
        try {
            System.out.println("Connecting to IMAP server: ");
            store.connect(host, 993, username, password);
            System.out.println("Connected!");
        } catch (Exception e) {
            e.getStackTrace();
        }

        return store;
    }
}
