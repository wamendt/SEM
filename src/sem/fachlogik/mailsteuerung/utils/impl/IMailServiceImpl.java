package sem.fachlogik.mailsteuerung.utils.impl;

import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDTag;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.FileGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.fachlogik.mailsteuerung.event.MsgReceivedEvent;
import sem.fachlogik.mailsteuerung.listener.MsgReceivedListener;
import sem.fachlogik.mailsteuerung.listener.MsgRemovedListener;
import sem.fachlogik.mailsteuerung.utils.services.IMailService;
import sem.fachlogik.mailsteuerung.utils.mailConnection.MailStoreManager;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.*;
import javax.activation.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.io.*;

import org.jsoup.Jsoup;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.entities.File;

public class IMailServiceImpl implements IMailService, MessageCountListener {

    //Add und Remove von Strg-Klassen
    private static IMailServiceImpl mailService;
    private static ArrayList<MsgReceivedListener> receivedListeners = new ArrayList<>();
    private static ArrayList<MsgRemovedListener> removedListeners = new ArrayList<>();
    private static MailStoreManager storeManager;
    private Store store;

    public IMailServiceImpl(){
        storeManager = MailStoreManager.getStoreManager();
    }

    public static IMailServiceImpl getMailService() {
        if (mailService == null){
            mailService = new IMailServiceImpl();
        }
        return mailService;
    }

    // #################################################################################################################
    // ###############################################   Listener   ####################################################
    // #################################################################################################################


    @Override
    public void messagesAdded(MessageCountEvent messageCountEvent) {
        System.out.println("Neue Message erhalten");
    }

    @Override
    public void messagesRemoved(MessageCountEvent messageCountEvent) {
        System.out.println("Message gelöscht");
    }

    private void notifyMsgReceivedListener(MsgReceivedEvent msg){
        for(MsgReceivedListener listener : receivedListeners){
            listener.messageAngekommen(msg);
        }
    }
    // #################################################################################################################
    // ###############################################   /Listener   ###################################################
    // #################################################################################################################

    // #################################################################################################################
    // ############################################   Hilfsfunktionen   ################################################
    // #################################################################################################################


    public static ArrayList<MsgReceivedListener> getReceivedListeners() {
        return receivedListeners;
    }

    public static ArrayList<MsgRemovedListener> getRemovedListeners() {
        return removedListeners;
    }



    //Methode um das Store-Objekt zu setzen und die Verbindung aufzubauen
    private Store setStore(Konto konto){
        store = null;
        try{
            store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());
        }catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        }
        return store;
    }


    //Methode um alle Ordnernamen des Kontos zu holen
    private ArrayList<String> getAlleOrdnerNamen(){
        ArrayList<String> ordnerList = new ArrayList<>();
        try{
            //Alle Ordner holen
            Folder[] folders = store.getDefaultFolder().list("*");

            //Ausgabe zur Kontrolle
            for (Folder folder : folders) {
                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    ordnerList.add(folder.getFullName());
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("NullPointerException wird geworfen: " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen: " + e.getMessage());
        }
        return ordnerList;
    }


    //Methode um eine EMail in einem Ordner als gelöscht setzen und diese anschliessend zu löschen
    private boolean markiereUndloescheMail(Folder folder, EMail eMail) {
        boolean ret = false;

        try{
            if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                System.out.println("Gefundener Ordner: " + folder.getFullName());

                //Ordner öfnnen
                if(!folder.isOpen()){
                    folder.open((Folder.READ_WRITE));
                }

                //Hole die Nachricht mit der MessageID
                Message message = folder.getMessage(eMail.getMessageID());

                if(message != null){
                    message.setFlag(Flags.Flag.DELETED, true);
                }

                //Ordner auslaufen lassen und schliessen
                folder.expunge();
                ret = true;
            }
            folder.close(true);
        }catch (MessagingException e){
            System.out.println("MessagingException wird geworfen: " + e.getMessage());
        }

        return ret;
    }


    //Methode um den Zustand (gelesen / ungelesen...) zu ermitteln und diesen der E-Mail zu setzen
    private EMail setZustand(EMail eMail, Message message) throws MessagingException {
        try{
            if (message.isSet(Flags.Flag.SEEN)) {
                eMail.setZustand("gelesen");
            }
            else if (!message.isSet(Flags.Flag.SEEN)) {
                eMail.setZustand("ungelesen");
            }
            else if(message.isSet(Flags.Flag.DRAFT)){
                eMail.setZustand("entworfen");
            }
            else if(message.isSet(Flags.Flag.DELETED)){
                eMail.setZustand("geloescht");
            }
        }catch (MessagingException e){
            System.out.println("MessagingException wurde geworfen: " + e.getMessage());
        }
        return eMail;
    }


    //Methode wandelt Multipart-Messages in Plaintext um
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


    public EMail checkEMailAndSetToDB(Folder folder, Message message, ICRUDMail crudeMail, int i, long beginnSeconds, int messageCounter){
        EMail eMail = null;
        try {
            //Beginn Zeitmessung für die aktuelle Nachricht
            long startMillis = System.currentTimeMillis();

            //Existiert die E-Mail bereits in der DB?
            if (crudeMail.getEMailByMessageIDUndOrdner(i, folder.getFullName()) == null) {
                eMail = new EMail();

                //Betreff setzen
                eMail.setBetref(message.getSubject());

                //NachrichtenContent in PlainText umwandeln und HTML - Content entfernen
                String mailContent = getTextFromMessage(message);
                eMail.setInhalt(mailContent);

                //Anfangs wird noch kein Tag gesetzt
                eMail.setTid(0);

                //Absender setzen
                eMail.setAbsender(message.getFrom()[0].toString());

                //CC setzen
                if (InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)) != null) {
                    eMail.setCc(InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)));
                } else {
                    eMail.setCc("");
                }

                //BCC setzen
                if (InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)) != null) {
                    eMail.setBcc(InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)));
                } else {
                    eMail.setBcc("");
                }

                //Empfänger setzen
                if (InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)) != null) {
                    eMail.setEmpfaenger(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
                } else {
                    eMail.setEmpfaenger("");
                }

                //Zustand der Email setzen
                eMail = setZustand(eMail, message);

                //Ordner setzen
                eMail.setOrdner(folder.getFullName());

                //Message-Content als Plain-Text umwandeln und in die Datenbank speichern
                String content = "";
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    message.writeTo(baos);
                    content = baos.toString();
                    eMail.setContentOriginal(content);
                } catch (Exception e) {
                    System.out.println("ByteArrayOutputStream wirft Exception: " + e.getMessage());
                }

                //MessageNumber setzen
                eMail.setMessageID(i);

                //E-Mail in der Datenbank hinterlegen
                crudeMail.createEMail(eMail);

                //End-Zeiterfassung anhalten
                long currentMillis = System.currentTimeMillis();
                long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);

                //und die benötigte Zeit berechnen
                long messageTime = currentMillis - startMillis;
                long totalTime = currentSeconds - beginnSeconds;

                //Ausgabe zur Kontrolle
                System.out.println(messageCounter + ". Nachricht wurde importiert!\nbenötigte Zeit für diese Nachricht: " + messageTime + "ms\nbenötigte Zeit Total:" + totalTime + "s\n\n");
            } else {
                //End-Zeiterfassung anhalten
                long currentMillis = System.currentTimeMillis();
                long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);

                //und die benötigte Zeit berechnen
                long messageTime = currentMillis - startMillis;
                long totalTime = currentSeconds - beginnSeconds;

                //Ausgabe zur Kontrolle
                System.out.println("\nE-Mail bereits in der DB!");
                System.out.println("benötigte Zeit für diese Nachricht: " + messageTime + "ms\nbenötigte Zeit Total:" + totalTime + "s\n\n");
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
        return eMail;
    }

    // #################################################################################################################
    // ############################################   /Hilfsfunktionen   ###############################################
    // #################################################################################################################


    @Override
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto){

        //Store - Objekt setzen
        setStore(konto);

        //Liste für die OrdnerNamen
        ArrayList<String> ordnerListe = getAlleOrdnerNamen();

        //Ausgabe zur Kontrolle
        for(String name : ordnerListe){
            System.out.println("Ordner: " + name);
        }

        return ordnerListe;
    }


    @Override
    public boolean loeschEMailVomServer(Konto konto, EMail eMail) {
        boolean ret = false;

        //Store - Objekt holen
        setStore(konto);

        try{
            //Den Ordner der Email holen
            Folder folder = store.getFolder(eMail.getOrdner());

            //EMail als gelöscht setzen und löschen
            if(folder != null){
                ret = markiereUndloescheMail(folder, eMail);
            }

            //Falls Löschung auf dem Server erfolgreich war,
            if(ret){
                ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
                ret = icrudMail.deleteEMail(eMail.getMid());
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


    @Override
    public boolean erstelleEMailOrdner(Konto konto, String ordnerName) {
        boolean isCreated = false;

        //Store - Objekt holen
        setStore(konto);

        try{
            //Default Ordner holen
            Folder folder = store.getDefaultFolder();

            //Neuen Ordner erstellen
            Folder newFolder = folder.getFolder(ordnerName);
            isCreated = newFolder.create(Folder.HOLDS_MESSAGES);
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Fehler beim Erstellen des Ordners: " + e.getMessage());
            e.printStackTrace();
            isCreated = false;
        }

        return isCreated;
    }


    @Override
    public boolean loescheEMailOrdner(Konto konto, String ordnerName) {
        boolean ret = false;

        //Store - Objekt holen
        setStore(konto);

        try{
            //Ordner holen
            Folder folder = store.getFolder(ordnerName);

            //Ordner öffnen
            if(!folder.isOpen()){
                folder.open((Folder.READ_WRITE));
            }

            if(Objects.equals(folder.getFullName(), ordnerName)){
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

                //E-Mail lokal löschen
                ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
                ret = icrudMail.deleteEMailByFolder(folder.getFullName());
            }

            folder.close(true);
        }
        catch (NullPointerException e){
            System.out.println("NullPointerException wurde geworfen: " + e.getMessage());
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception: " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


    @Override
    public boolean importiereAllEMails(Konto konto){
        boolean ret = false;

        //Store - Objekt holen
        setStore(konto);

        //CRUDObjekt zur Überprüfung ob Mails bereits importiert wurden!
        ICRUDMail crudeMail = ICRUDManagerSingleton.getIcrudMailInstance();
        EMail eMail = new EMail();

        long messageCounter = 1;

        try {
            //Alle Ordner holen
            ArrayList<String> ordnerList = getAlleOrdnerVonKonto(konto);

            //Starte die Zeitmessung
            long beginnMillisTotal = System.currentTimeMillis();
            long beginnSeconds = TimeUnit.MILLISECONDS.toSeconds(beginnMillisTotal);

            for (String name : ordnerList) {

                Folder folder = store.getFolder(name);

                //Ordner öfnnen
                if(!folder.isOpen()){
                    folder.open((Folder.READ_ONLY));
                }

                //Nachrichten des Ordners holen
                int messageCount = folder.getMessageCount();

                for(int i = 1; i <= messageCount; i++){

                    //Hole Nachricht anhand des Indexes
                    Message message = folder.getMessage(i);
                    try {
                        checkEMailAndSetToDB(folder, message, crudeMail, i, beginnSeconds, messageCount);
                        messageCounter++;
                    }
                    catch (NullPointerException e){
                        System.out.println("NullPointerException wurde geworfen: " + e.getMessage());
                    }
                }
                folder.close(true);
            }
            ret = true;
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
        return ret;
    }


    public ArrayList<EMail> importiereAllEMailsvomOrdner(Konto konto, String ordnerName) throws MessagingException{
        //Store - Objekt holen
        setStore(konto);

        //CRUDObjekt zur Überprüfung ob Mails bereits importiert wurden!
        ICRUDMail crudeMail = ICRUDManagerSingleton.getIcrudMailInstance();
        EMail eMail = null;
        ArrayList<EMail> eMailArrayList = new ArrayList<>();

        long messageCounter = 1;

        try {
            //Alle Ordner holen
            Folder folder = store.getFolder(ordnerName);

            //Starte die Zeitmessung
            long beginnMillisTotal = System.currentTimeMillis();
            long beginnSeconds = TimeUnit.MILLISECONDS.toSeconds(beginnMillisTotal);

            //Ordner öfnnen
            if(!folder.isOpen()){
                folder.open((Folder.READ_ONLY));
            }

            //Nachrichten des Ordners holen
            int messageCount = folder.getMessageCount();

            for(int i = 1; i <= messageCount; i++){

                //Hole Nachricht anhand des Indexes
                Message message = folder.getMessage(i);
                try {
                    eMail = checkEMailAndSetToDB(folder, message, crudeMail, i, beginnSeconds, messageCount);
                    if(eMail != null){
                        eMailArrayList.add(eMail);
                    }
                    messageCounter++;
                }
                catch (NullPointerException e){
                    System.out.println("NullPointerException wurde geworfen: " + e.getMessage());
                }
            }
            folder.close(true);

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
        return eMailArrayList;
    }


    @Override
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) throws MessagingException, IOException, SQLException {
        boolean ret = false;

        //Store-Objekt holen
        setStore(konto);

        //Alle Namen der verfügbaren Ordner holen
        ArrayList<String> ordnerListe = getAlleOrdnerVonKonto(konto);

        //Wenn mindestens zwei Ordner verfügbar sind, fortfahren
        if(ordnerListe.size() > 1){

            //Ordner-Objekte anlegen
            Folder folder = store.getDefaultFolder();
            Folder fromFolder = null;
            Folder toFolder = null;

            for(String ordnerName: ordnerListe){

                if(Objects.equals(vonOrdner, ordnerName)) {
                    //vonOrdner - Ordner holen
                    fromFolder = folder.getFolder(vonOrdner);
                }

                if(Objects.equals(zuOrdner, ordnerName)){
                    //vonOrdner - Ordner holen
                    toFolder = folder.getFolder(zuOrdner);
                }

                if (fromFolder != null && toFolder != null){
                    //Von-Ordner öfnnen
                    if(!fromFolder.isOpen()){
                        fromFolder.open((Folder.READ_WRITE));
                    }

                    //Zu-Ordner öfnnen
                    if(!toFolder.isOpen()){
                        toFolder.open((Folder.READ_WRITE));
                    }

                    Message message = fromFolder.getMessage(email.getMessageID());
                    Message[] messages = new Message[1];
                    messages[0] = message;

                    //Nachrichten in den ZielOrdner kopieren
                    folder.copyMessages(messages, toFolder);
                    ret = markiereUndloescheMail(folder, email);

                    //Nach der erfolgreichen Änderung auf dem Server nun auch lokal durchführen
                    if(ret){
                        email.setOrdner(zuOrdner);
                        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
                        ret = icrudMail.updateEMail(email);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public boolean setzeTagsZurServerEMail(Konto konto, EMail email, String art) throws MessagingException, IOException, SQLException {
        boolean ret = false;

        //Store-Objekt holen
        setStore(konto);

        //Den entsprechenden E-Mail Ordner holen
        Folder folder = store.getFolder(email.getOrdner());
        System.out.println("Ordner gefunden!: " + folder.getFullName());

        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }

        Message message = folder.getMessage(email.getMessageID());
        System.out.println("Zum Vergleich: \n" +
                "EMail DB - Betreff :   " + email.getBetreff() + "\n" +
                "EMail Server - Betreff:" + message.getSubject());

        System.out.println("Message ID nicht leer und identisch!");
        switch (art) {
            case "gelesen":
                //E-Mail als gelesen markieren
                message.setFlag(Flags.Flag.SEEN, true);
                email.setZustand("gelesen");
                ret = true;
                break;
            case "ungelesen":
                //E-Mail als ungelesen markieren
                message.setFlag(Flags.Flag.SEEN, false);
                email.setZustand("ungelesen");
                ret = true;
                break;
            case "entwurf":
                //MessageArray erstellen für den Kopiervorgang
                Message[] messages1 = new Message[1];
                Folder ToFolder = store.getFolder("Entwürfe");

                //Message als Entwurf setzen und ins Array übertragen
                message.setFlag(Flags.Flag.DRAFT, true);
                messages1[0] = message;

                //Message in den EntwurfsOrdner kopieren
                folder.copyMessages(messages1, ToFolder);

                //E-Mail vom alten Ordner löschen
                if(loeschEMailVomServer(konto, email)){
                    email.setZustand("entworfen");
                    ret = true;
                }
                else {
                    ret = false;
                }

                break;
            case "geloescht":
                //Message als gelöscht setzen und in das Array übertragen
                Message[] messages = new Message[1];
                message.setFlag(Flags.Flag.DELETED, true);
                messages[0] = message;

                //Papierkorb holen und die E-Mail in den Papierkorb verschieben
                Folder toFolder = store.getFolder("Gelöschte Elemente");
                folder.copyMessages(messages, toFolder);
                email.setOrdner("INBOX");

                //E-Mail vom alten Ordner löschen
                if(loeschEMailVomServer(konto, email)){
                    email.setZustand("geloescht");
                    ret = true;
                }
                else {
                    ret = false;
                }
                break;
        }
        folder.expunge();
        folder.close(true);

        //Zustand lokal ändern
        if(ret){
            ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
            ret = icrudMail.updateEMail(email);
        }
        return ret;
    }


    @Override
    public boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad) throws MessagingException, IOException, SQLException {
        boolean ret = false;

        //Store-Objekt holen
        setStore(konto);

        //Alle Namen der verfügbaren Ordner holen
        ArrayList<String> ordnerListe = getAlleOrdnerVonKonto(konto);

        //Wenn mindestens zwei Ordner verfügbar sind, fortfahren
        if(ordnerListe.size() > 1 && ordnerListe.contains(pfad)) {
            //Entsprechenden Ordner zum Speichern holen
            Folder folder = store.getFolder(pfad);

            if(folder != null){
                System.out.println("Ordner gefunden!: " + folder.getFullName());

                if(!folder.isOpen()){
                    folder.open(Folder.READ_WRITE);
                }

                try {
                    //Die zu speichernde Nachricht erstellen
                    MimeMessage message = new MimeMessage(Session.getInstance(System.getProperties()));

                    //Mit Daten füllen
                    message.setFrom(new InternetAddress(eMail.getAbsender()));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(eMail.getEmpfaenger()));
                    message.setSubject(eMail.getBetreff());

                    //MessageContent erstellen
                    MimeBodyPart content = new MimeBodyPart();

                    //Content füllen
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
                    ret = true;
                }
                catch (MessagingException ex) {
                    System.out.println("MessagingException wurde geworfen: " + ex.getMessage());
                }
            }
            else {
                ret = false;
            }

        }
        else {
            System.out.println("Der angegebene Ordner existiert nicht auf dem E-Mail - Server!");
            return false;
        }

        //Vorgang lokal umsetzen
        if(ret){
            int id = -1;
            ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
            id = icrudMail.createEMail(eMail);
            if(id != -1){
                ret = true;
            }
        }
        return false;
    }


    //Methode holt einen entsprechenden Ordner, der mit einem Listener überwacht werden soll
    @Override
    public ArrayList<Folder> holeFolderFuerListener(Konto konto) throws MessagingException {
        ArrayList<String> ordnerListe = getAlleOrdnerVonKonto(konto);
        ArrayList<Folder> folders = new ArrayList<>();
        Folder folder = null;
        if(ordnerListe.size() > 0){
            for(String ordnerName : ordnerListe){
                try{
                    folder = store.getFolder(ordnerName);
                    folders.add(folder);
                }
                catch (MessagingException e){
                    System.out.println("MessagingException wurde geworfen: " + e.getMessage());
                }
            }
        }
        return folders;
    }


    @Override
    public Folder getOrdnerByName(Konto konto, String name) {
        //Store-Objekt holen
        setStore(konto);
        Folder folder = null;
        try{
            folder = store.getFolder(name);
        }
        catch (MessagingException e){
            System.out.println("MessagingException wurde geworfen: " + e.getMessage());
        }
        return folder;
    }


    @Override
    public boolean sendeEmail(Konto konto, EMail email) {
        boolean ret = false;

        try {
            Session session = storeManager.setSmtpConnection(konto.getSMTPhost(), konto.getEmailAddress(), konto.getPassWort());

            String[] empfaengerList = email.getEmpfaenger().split(";");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getAbsender()));
            for (int i = 0; i < empfaengerList.length; i++){
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfaengerList[i]));
            }
            message.setSubject(email.getBetreff());

            //MessageContent erstellen
            //MimeBodyPart content = new MimeBodyPart();
            BodyPart messageBodyPart = new MimeBodyPart();

            //Content füllen
            messageBodyPart.setText(email.getInhalt());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Anhang hinzufügen
            if(email.getFiles().size() > 0){

                for(File file : email.getFiles()) {
                    //File holen und in DataSource zuweisen
                    messageBodyPart = new MimeBodyPart();
                    String filename = "/Users/wamendt/Desktop/photo_2017-10-21_14-12-43.jpg";
                    DataSource source = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(filename);
                    multipart.addBodyPart(messageBodyPart);
                }
                // Nachricht fertigbauen
                message.setContent(multipart);
                // store file
                //message.writeTo(new FileOutputStream(new File("c:/mail.eml")));
            }

            //Nachricht senden
            Transport.send(message);

            //Gesendete E-Mail lokal sichern
            email.setOrdner("Gesendet");

            //Message-Content als Plain-Text umwandeln und in die Datenbank speichern
            String content = "";
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                message.writeTo(baos);
                content = baos.toString();
                email.setContentOriginal(content);
            } catch (Exception e) {
                System.out.println("ByteArrayOutputStream wirft Exception: " + e.getMessage());
            }

            ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
            int id = -1;
            id = icrudMail.createEMail(email);
            if(id != -1){
                ret = true;
            }
        }
        catch (NoSuchProviderException e){
            System.out.println("NoSuchProviderException: " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException: " + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IOException: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return ret;
    }


    // #################################################################################################################
    // #################################################   Setter   ####################################################
    // #################################################################################################################
    @Override
    public EMailGrenz getEMailGrenz(EMail eMail) {
        EMailGrenz eMailGrenz = new EMailGrenz();
        eMailGrenz.setMid(eMail.getMid());
        eMailGrenz.setBetreff(eMail.getBetreff());
        eMailGrenz.setInhalt(eMail.getInhalt());

        Tag tag = null;
        TagGrenz tagGrenz = null;
        try{
            ICRUDTag icrudTag = ICRUDManagerSingleton.getIcrudTagInstance();
            tag = icrudTag.getTagById(eMail.getTid());
            if(tag != null){
                tagGrenz = getTagGrenz(tag);
                eMailGrenz.setTag(tagGrenz);
            }
        }
        catch (SQLException e){
            System.out.println("SQLException wird geworfen: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("IOException wird geworfen: " + e.getMessage());
        }
        eMailGrenz.setAbsender(eMail.getAbsender());

        ArrayList<String> ccList = implodeAdresses(eMail.getCc());
        eMailGrenz.setCc(ccList);

        ArrayList<String> bccList = implodeAdresses(eMail.getBcc());
        eMailGrenz.setBcc(bccList);

        ArrayList<String> empfaengerList = implodeAdresses(eMail.getEmpfaenger());
        eMailGrenz.setEmpfaenger(empfaengerList);

        eMailGrenz.setContentOriginal(eMail.getContentOriginal());
        eMailGrenz.setZustand(eMail.getZustand());
        eMailGrenz.setMessageID(eMail.getMessageID());
        eMailGrenz.setOrdner(eMail.getOrdner());

        if(eMail.getFiles().size() > 0) {
            ArrayList<FileGrenz> fileList = new ArrayList<>();
            for (File file : eMail.getFiles()) {
                FileGrenz fileGrenz = getFileGrenz(file);
                fileList.add(fileGrenz);
            }
            eMailGrenz.setFiles(fileList);
        }
        return eMailGrenz;
    }

    @Override
    public EMail getEMail(EMailGrenz eMailGrenz) {
        EMail eMail = new EMail();
        eMail.setMid(eMailGrenz.getMid());
        eMail.setBetref(eMailGrenz.getBetreff());
        eMail.setInhalt(eMailGrenz.getInhalt());
        eMail.setTid(eMailGrenz.getTag().getTid());
        eMail.setAbsender(eMail.getAbsender());

        String ccList = explodeAdresses(eMailGrenz.getCc());
        eMail.setCc(ccList);

        String bccList = explodeAdresses(eMailGrenz.getBcc());
        eMail.setBcc(bccList);

        String empfaengerList = explodeAdresses(eMailGrenz.getEmpfaenger());
        eMail.setEmpfaenger(empfaengerList);

        eMail.setContentOriginal(eMail.getContentOriginal());
        eMail.setZustand(eMail.getZustand());
        eMail.setMessageID(eMail.getMessageID());
        eMail.setOrdner(eMail.getOrdner());

        if(eMail.getFiles().size() > 0) {
            ArrayList<File> fileList = new ArrayList<>();
            for (FileGrenz fileGrenz : eMailGrenz.getFiles()) {
                File file = getFile(fileGrenz);
                fileList.add(file);
            }
            eMail.setFiles(fileList);
        }
        return eMail;
    }

    @Override
    public KontoGrenz getKontoGrenz(Konto konto) {
        KontoGrenz kontoGrenz = new KontoGrenz();
        kontoGrenz.setUserName(konto.getUserName());
        kontoGrenz.setPassWort(konto.getPassWort());
        kontoGrenz.setAccountAt(konto.getAccountAt());
        kontoGrenz.setIMAPhost(konto.getIMAPhost());
        kontoGrenz.setSMTPhost(konto.getSMTPhost());
        kontoGrenz.setEmailAddress(konto.getEmailAddress());
        kontoGrenz.setPort(konto.getPort());
        return kontoGrenz;
    }

    @Override
    public Konto getKonto(KontoGrenz kontoGrenz) {
        Konto konto = new Konto();
        konto.setUserName(kontoGrenz.getUserName());
        konto.setPassWort(kontoGrenz.getPassWort());
        konto.setAccountAt(kontoGrenz.getAccountAt());
        konto.setIMAPhost(kontoGrenz.getIMAPhost());
        konto.setSMTPhost(kontoGrenz.getSMTPhost());
        konto.setEmailAddress(kontoGrenz.getEmailAddress());
        konto.setPort(kontoGrenz.getPort());
        return konto;
    }

    @Override
    public TagGrenz getTagGrenz(Tag tag) {
        TagGrenz tagGrenz = new TagGrenz();
        tagGrenz.setTid(tag.getTid());
        tagGrenz.setName(tag.getName());
        tagGrenz.setNumIndex(tag.getNumIndex());
        tagGrenz.setWoerter(tag.getWoerter());
        return tagGrenz;
    }

    @Override
    public Tag getTag(TagGrenz tagGrenz) {
        Tag tag = new Tag();
        tag.setTid(tagGrenz.getTid());
        tag.setName(tagGrenz.getName());
        tag.setNumIndex(tagGrenz.getNumIndex());
        tag.setWoerter(tagGrenz.getWoerter());
        return null;
    }

    @Override
    public FileGrenz getFileGrenz(File file) {
        FileGrenz fileGrenz = new FileGrenz();
        fileGrenz.setFid(file.getFid());
        fileGrenz.setMId(file.getMid());
        fileGrenz.setPfad(file.getPfad());
        fileGrenz.setName(file.getName());
        return fileGrenz;
    }

    @Override
    public File getFile(FileGrenz fileGrenz) {
        File file = new File();
        file.setFid(fileGrenz.getFid());
        file.setMId(fileGrenz.getMid());
        file.setPfad(fileGrenz.getPfad());
        file.setName(fileGrenz.getName());
        return file;
    }

    @Override
    public String explodeAdresses(ArrayList<String> adresses) {
        String adressString = "";
        if(adresses.size() > 0){
            for (String adress: adresses){
                adressString = adressString.concat(adress + ";");
            }
        }
        return adressString;
    }

    @Override
    public ArrayList<String> implodeAdresses(String adresses) {
        ArrayList<String> adressList = null;
        if(adresses != null){
            String[] adressArray = adresses.split(";");
            adressList = new ArrayList<>();
            adressList.addAll(Arrays.asList(adressArray));
        }
        return adressList;
    }

    @Override
    public ArrayList<EMail> holeAlleEMails(Konto konto) {
        ArrayList<EMail> eMailList = new ArrayList<>();
        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
        try{
            eMailList = icrudMail.getAlleEMails();
        }
        catch (IOException e){
            System.out.println("IOException wurde geworfen: " + e.getMessage());
        }
        catch (SQLException e){
            System.out.println("SQLException wurde geworfen: " + e.getMessage());
        }
        return eMailList;
    }

    @Override
    public ArrayList<EMail> sucheEMail(String suchwort) {
        ArrayList<EMail> eMailArrayList = new ArrayList<>();

        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();

        try{
            eMailArrayList = icrudMail.searchEMail(suchwort);
        } catch (SQLException e) {
            System.out.println("SQLException wurde geworfen: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException wurde geworfen: " + e.getMessage());
        }
        return eMailArrayList;
    }
    // #################################################################################################################
    // ################################################   /Setter   ####################################################
    // #################################################################################################################
}