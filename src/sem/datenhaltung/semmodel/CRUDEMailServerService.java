package sem.datenhaltung.semmodel;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import sem.datenhaltung.semmodel.impl.CRUDEMail;
import sem.datenhaltung.semmodel.mailConnection.MailStoreManager;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.*;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;


public class CRUDEMailServerService implements IMailServerService {


    public CRUDEMailServerService(){

    }


    @Override
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto){
        //Liste für die OrdnerNamen
        ArrayList<String> ordnerListe = new ArrayList<>();

        //Store - Objekt holen
        try{
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            //Alle Ordner holen
            Folder[] folders = store.getDefaultFolder().list("*");

            //Ausgabe zur Kontrolle
            for (Folder folder : folders) {
                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    System.out.println("Gefundener Ordner: " + folder.getFullName());
                    ordnerListe.add(folder.getFullName());
                }
            }
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        } catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen: " + e.getMessage());
        }
        return ordnerListe;
    }

    @Override
    public boolean loeschEMailVomServer(Konto konto, EMail eMail) {
        //Store - Objekt holen
        try{
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            //Alle Ordner holen
            Folder[] folders = store.getDefaultFolder().list("*");

            //Suchkriterien erstellen
            SearchTerm searchTerm = new MessageIDTerm(eMail.getMessageID());

            //Ausgabe zur Kontrolle
            for (Folder folder : folders) {
                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    System.out.println("Gefundener Ordner: " + folder.getFullName());

                    //Ordner öfnnen
                    if(!folder.isOpen()){
                        folder.open((Folder.READ_WRITE));
                    }

                    //Hole die Nachricht mit der MessageID
                    MimeMessage[] messages = (MimeMessage[]) folder.search(searchTerm);

                    for (MimeMessage message: messages){
                        if(Objects.equals(eMail.getMessageID(), message.getMessageID())){
                            System.out.println("Lösche EMAIL!");
                            message.setFlag(Flags.Flag.DELETED, true);
                            folder.expunge();
                            folder.close(true);
                            System.out.println("EMAIL gelöscht!");
                            return true;
                        }
                    }
                }
                folder.close(true);
            }
        }
        catch (NullPointerException e){
            System.out.println("NullPointerException wurde geworfen!" + e.getMessage());
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean erstelleEMailOrdner(Konto konto, String pfad) {
        boolean isCreated = false;
        //Store - Objekt holen
        try{
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            //Default Ordner holen
            Folder folder = store.getDefaultFolder();

            //Neuen Ordner erstellen
            Folder newFolder = folder.getFolder(pfad);
            isCreated = newFolder.create(Folder.HOLDS_MESSAGES);
            System.out.println("created: " + isCreated);


        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Error creating folder: " + e.getMessage());
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }

    @Override
    public boolean loescheEMailOrdner(Konto konto, String pfad) {
        //Store - Objekt holen
        try{
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            //Alle Ordner holen
            Folder[] folders = store.getDefaultFolder().list("*");

            //Ausgabe zur Kontrolle
            for (Folder folder : folders) {
                //Ordner öfnnen
                if(!folder.isOpen()){
                    folder.open((Folder.READ_WRITE));
                }

                if(Objects.equals(folder.getFullName(), pfad)){
                    //Hole alle Nachrichten des Ordners
                    Message[] messages = folder.getMessages();

                    //Setze Flags auf gelöscht
                    folder.setSubscribed(false);
                    Flags deleted = new Flags(Flags.Flag.DELETED);
                    folder.setFlags(messages, deleted, true);

                    //Ordner schliessen und auslaufen lassen
                    folder.expunge();
                    folder.close(true);

                    //Ordner letzendlich löschen
                    folder.delete(true);
                    System.out.println("Ordner gelöscht!");
                    return true;
                }
                folder.close(true);
            }
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        } catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean importiereAllEMails(Konto konto){
        /*
        * Hier müssen noch die FLags der gelesenen bzw. ungelesenen Nachrichten abgefragt werden und diese dementsprechend
        * in der DB einsetzen zu können!
        * */

        //CRUDObjekt zur Überprüfung ob Mails bereits importiert wurden!
        CRUDEMail crudeMail = new CRUDEMail();
        EMail eMail = new EMail();

        long messageCounter = 1;

        try {
            //Store - Objekt holen
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            if (store != null) {

                //Alle Ordner holen
                Folder[] folders = store.getDefaultFolder().list("*");

                long beginnMillisTotal = System.currentTimeMillis();
                long beginnSeconds = TimeUnit.MILLISECONDS.toSeconds(beginnMillisTotal);

                Message[] messages = null;

                for (Folder folder : folders) {

                    //Ordner öfnnen
                    if(!folder.isOpen()){
                        folder.open((Folder.READ_ONLY));
                    }

                    //Nachrichten des Ordners holen
                    messages = folder.getMessages();

                    for(Message message : messages){

                        System.out.println("Ordner: " + folder.getFullName());

                        String content = "";
                        String messageID;
                        int exist = 0;
                        try{
                            //Beginn Zeitmessung für die aktuelle Nachricht
                            long startMillis = System.currentTimeMillis();
                            long startSeconds = TimeUnit.MILLISECONDS.toSeconds(startMillis);

                            //NachrichtenContent in PlainText umwandeln und HTML - Content entfernen
                            String mailContent = getTextFromMessage(message);

                            //Existiert die E-Mail bereits in der DB?

                            if(crudeMail.getEMailByMessageID(mailContent) == null){

                                eMail.setBetref(message.getSubject());

                                eMail.setInhalt(mailContent);

                                //Anfangs wird noch kein Tag gesetzt
                                eMail.setTid(0);

                                eMail.setAbsender(message.getFrom()[0].toString());

                                if (InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)) != null) {
                                    eMail.setCc(InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)));
                                } else {
                                    eMail.setCc("");
                                }

                                if (InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)) != null) {
                                    eMail.setBcc(InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)));
                                } else {
                                    eMail.setBcc("");
                                }

                                if (InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)) != null) {
                                    eMail.setEmpfaenger(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
                                } else {
                                    eMail.setEmpfaenger("");
                                }

                                /*
                                if (durchlauf == 1) {
                                    eMail.setZustand("gelesen");
                                } else if (durchlauf == 2) {
                                    eMail.setZustand("ungelesen");
                                }
                                */

                                eMail.setContentOriginal(content);

                                eMail.setOrdner(folder.getFullName());

                                // MimeMessage in ByteArrayOutPutStream schreiben, von da aus in einen String umwandeln
                                // und in die DB schreiben. So bleibt die Original-Mail erhalten!
                                try {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    message.writeTo(baos);
                                    content = baos.toString();
                                } catch (Exception e) {
                                    System.out.println("ByteArrayOutputStream wirft Exception: " + e.getMessage());
                                }

                                message.getHeader("Message-Id");
                                Enumeration headers = message.getAllHeaders();

                                while (headers.hasMoreElements()) {
                                    Header h = (Header) headers.nextElement();
                                    String mID = h.getName();
                                    if(mID.contains("Message-ID")){
                                        System.out.println(h.getName() + ":" + h.getValue());
                                        messageID = h.getValue();
                                        eMail.setMessageID(messageID);
                                    }
                                }

                                crudeMail.createEMail(eMail);
                                //Zeiterfassung
                                long currentMillis = System.currentTimeMillis();
                                long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);

                                long messageTime = currentMillis - startMillis;
                                long totalTime = currentSeconds - beginnSeconds;

                                //Ausgabe zur Kontrolle
                                System.out.println(messageCounter + ". Nachricht wurde importiert!\nbenötigte Zeit für diese Nachricht: " + messageTime + "ms\nbenötigte Zeit Total:" + totalTime + "s\n\n");
                                messageCounter++;
                            }
                            else {
                                //Zeiterfassung
                                long currentMillis = System.currentTimeMillis();
                                long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);

                                System.out.println("\nE-Mail bereits in der DB!");
                                long messageTime = currentMillis - startMillis;
                                long totalTime = currentSeconds - beginnSeconds;
                                System.out.println("benötigte Zeit für diese Nachricht: " + messageTime + "ms\nbenötigte Zeit Total:" + totalTime + "s\n\n");
                            }

                        }catch (NullPointerException e){
                            System.out.println("NullPointerException wurde geworfen: " + e.getMessage());
                        }
                    }
                    folder.close(true);
                }
            }
        }
        catch (NoSuchProviderException e) {
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        }
        catch (FolderNotFoundException fe){
            System.out.println("FolderNotFoundException wurde geworfen: " + fe.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wurde geworfen: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Exception wurde geworfen: " + e.getMessage());
        }
        return false;
    }

    //Wandelt Multipart-Messages in Plaintext um
    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        }
        else if (message.isMimeType("multipart/*")) {
            String result = "";
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

            int count = mimeMultipart.getCount();

            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);

                if (bodyPart.isMimeType("text/plain")) {
                    result = result + "\n" + bodyPart.getContent();
                    break;  //without break same text appears twice in my tests
                }
                else if (bodyPart.isMimeType("text/html")) {
                    String html = (String) bodyPart.getContent();
                    result = result.concat("\n" + Jsoup.parse(html).text());
                }
            }
            return result;
        }
        return "";
    }


    @Override
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) throws MessagingException {
        //Alle Namen der verfügbaren Ordner holen
        ArrayList<String> ordnerListe = getAlleOrdnerVonKonto(konto);

        //Wenn mindestens zwei Ordner verfügbar sind, fortfahren
        if(ordnerListe.size() > 1){
            System.out.println("Ordnergröße > 1");
            //Store holen
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            //Ordner-Objekte anlegen
            Folder folder = store.getDefaultFolder();
            Folder fromFolder = null;
            Folder toFolder = null;

            for(String ordnerName: ordnerListe){
                if(Objects.equals(vonOrdner, ordnerName)){
                    //vonOrdner - Ordner holen
                    fromFolder = folder.getFolder(vonOrdner);
                    System.out.println("FromOrdner geholt!");
                }
                if(Objects.equals(zuOrdner, ordnerName)){
                    //vonOrdner - Ordner holen
                    toFolder = folder.getFolder(zuOrdner);
                    System.out.println("ToOrdner geholt!");

                }
                if (fromFolder != null && toFolder != null){
                    //Ordner öfnnen
                    if(!fromFolder.isOpen()){
                        fromFolder.open((Folder.READ_WRITE));
                        System.out.println("FromOrdner geöffnet!");

                    }

                    //Ordner öfnnen
                    if(!toFolder.isOpen()){
                        toFolder.open((Folder.READ_WRITE));
                        System.out.println("ToOrdner geöffnet!");

                    }

                    //Suchkriterien erstellen
                    SearchTerm searchTerm = new MessageIDTerm(email.getMessageID());

                    //Hole die Nachricht mit der MessageID
                    MimeMessage[] messages = (MimeMessage[]) fromFolder.search(searchTerm);

                    //Nachrichten in den ZielOrdner kopieren
                    fromFolder.copyMessages(messages, toFolder);
                    System.out.println("Nachricht kopiert");

                    if(loeschEMailVomServer(konto, email)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean setzeTagsZurServerEMail(Konto konto, EMail email, String art) {
        //Hier werden sämtliche Flags zu einer Email gesetzt, bisher werden diese in den andern Methoden gesetzt,
        //Dies wird ausgekappselt und hier umgesetzt!

        return false;
    }

    @Override
    public boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad) throws MessagingException {
        //Alle Namen der verfügbaren Ordner holen
        ArrayList<String> ordnerListe = getAlleOrdnerVonKonto(konto);

        //Wenn mindestens zwei Ordner verfügbar sind, fortfahren
        if(ordnerListe.size() > 1 && ordnerListe.contains(pfad)) {
            System.out.println("Ordnergröße > 1");
            //Store holen
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

            Folder folder = store.getFolder(pfad);
            System.out.println("Ordner gefunden!: " + folder.getFullName());

            if(!folder.isOpen()){
                folder.open(Folder.READ_WRITE);
            }

            try {
                MimeMessage message = new MimeMessage(Session.getInstance(System.getProperties()));
                message.setFrom(new InternetAddress(eMail.getAbsender()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(eMail.getEmpfaenger()));
                message.setSubject(eMail.getBetreff());
                // create the message part
                MimeBodyPart content = new MimeBodyPart();
                // fill message
                message.setText(eMail.getInhalt());
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(content);

                // add attachments
                /*f(eMail.getAttachment() != null){
                    for(File file : attachments) {
                        MimeBodyPart attachment = new MimeBodyPart();
                        DataSource source = new FileDataSource(file);
                        attachment.setDataHandler(new DataHandler(source));
                        attachment.setFileName(file.getName());
                        multipart.addBodyPart(attachment);
                    }
                    // integration
                    message.setContent(multipart);
                    // store file
                    message.writeTo(new FileOutputStream(new File("c:/mail.eml")));
                }*/

                message.setFlag(Flags.Flag.DRAFT, true);
                MimeMessage draftMessages[] = {message};
                folder.appendMessages(draftMessages);
                /*
                Message[] messages = new Message[1];
                messages[0] = message;
                defaultFolder.copyMessages(messages, folder);
                */
                return true;
            }
            catch (MessagingException ex) {
                System.out.println("MessagingException wurde geworfen: " + ex.getMessage());
            }
        }
        else {
            System.out.println("Der angegebene Ordner existiert nicht auf dem E-Mail - Server!");
            return false;
        }
        return false;
    }
}
