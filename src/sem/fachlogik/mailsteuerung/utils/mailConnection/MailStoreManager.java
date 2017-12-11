package sem.fachlogik.mailsteuerung.utils.mailConnection;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class MailStoreManager {

    private static MailStoreManager mailStoreManager;
    private Session session;

    private MailStoreManager(){
    }

    public static MailStoreManager getStoreManager(){
        if(mailStoreManager == null){
            mailStoreManager = new MailStoreManager();
        }
        return mailStoreManager;
    }

    //Erstellt eine Imap - Verbindung und liefert das Store - Objekt zurück
    public Store setImapConnection(String host, String username, String password) throws NoSuchProviderException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.imaps.auth", "true");
        props.put("mail.imaps.startssl.enable", "true");
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", "993");


        javax.mail.Authenticator auth;
        auth = new javax.mail.Authenticator() {
            @Override
            public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        };

        this.session = Session.getInstance(props, auth);
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


    //Erstellt eine Imap - Verbindung und liefert das Store - Objekt zurück
    public Session setSmtpConnection(String host, String username, String password) throws NoSuchProviderException {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        javax.mail.Authenticator auth;
        auth = new javax.mail.Authenticator() {
            @Override
            public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        };

        this.session = Session.getInstance(props, auth);

        return session;
    }
}
