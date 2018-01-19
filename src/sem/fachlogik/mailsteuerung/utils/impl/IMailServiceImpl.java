package sem.fachlogik.mailsteuerung.utils.impl;

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
    private static Store store;
    private boolean textIsHtml = false;

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


    private Multipart addAttachment(EMail eMail, Multipart multipart){
        try{
            if(eMail.getFiles() != null){
                for(File file : eMail.getFiles()) {
                    //File holen und in DataSource zuweisen
                    BodyPart messageBodyPart = new MimeBodyPart();
                    String filePath = file.getPfad();
                    DataSource source = new FileDataSource(filePath);
                    try {
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(file.getName());
                        multipart.addBodyPart(messageBodyPart);
                    } catch (MessagingException e) {
                        System.out.println("MessagingException wurde geworfen in IMailSteuerung, addAttachment(): " + e.getMessage());;
                    }
                }
            }
        }catch(NullPointerException e){
            System.out.println("NullpointerException wurde geworfen beim Einfügen von Anhängen: " + e.getMessage());
        }

        // store file
        //message.writeTo(new FileOutputStream(new File("c:/mail.eml")));
        return multipart;
    }


    private EMail getContentOriginal(Message message, EMail email){
        try {
            String content = getTextFromMessage(message);
            email.setContentOriginal(content);
        } catch (Exception e) {
            System.out.println("Exception wird in IMailSteuerung, getContentOriginal() geworfen: " + e.getMessage());
        }
        return email;
    }


    //Methode um das Store-Objekt zu setzen und die Verbindung aufzubauen
    private void setStore(Konto konto){
        if(store == null){
            try{
                store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());
            }catch (NoSuchProviderException e){
                System.out.println("StoreManager wirft Exception in IMailSteuerung, setStore(): : " + e.getMessage());
            }
        }
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
            System.out.println("NullPointerException wird geworfen in IMailSteuerung, getAlleOrdnerNamen(): " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen in IMailSteuerung, getAlleOrdnerNamen(): " + e.getMessage());
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
            System.out.println("MessagingException wird geworfen in IMailSteuerung, markiereUndloescheMail(): " + e.getMessage());
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
            System.out.println("MessagingException wurde geworfen in IMailSteuerung, setZustand(): " + e.getMessage());
        }
        return eMail;
    }

    /**
     * Return the primary text content of the message.
     */
    private String getTextFromMessage(Part p) throws
            MessagingException, IOException {
        try {
            if (p.isMimeType("text/*")) {
                String s = (String) p.getContent();
                textIsHtml = p.isMimeType("text/html");
                return s;
            }

            if (p.isMimeType("multipart/alternative")) {
                // prefer html text over plain text
                Multipart mp = (Multipart) p.getContent();
                String text = null;
                for (int i = 0; i < mp.getCount(); i++) {
                    Part bp = mp.getBodyPart(i);
                    if (bp.isMimeType("text/plain")) {
                        if (text == null)
                            text = getTextFromMessage(bp);
                        continue;
                    } else if (bp.isMimeType("text/html")) {
                        String s = getTextFromMessage(bp);
                        if (s != null)
                            return s;
                    } else {
                        return getTextFromMessage(bp);
                    }
                }
                return text;
            } else if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    String s = getTextFromMessage(mp.getBodyPart(i));
                    if (s != null)
                        return s;
                }
            }
        }
        catch(Exception e){
                return "NachrichtInhalt konnte nicht gelesen werden, da MessageType: IMAPMessage!";
            }
        return null;
    }


    //Methode wandelt Multipart-Messages in Plaintext um
    private String getPlainTextFromMessage(Message message){
        try{
            message = (MimeMessage) message;
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
                if(result.isEmpty()){
                    MimeMessage message1 = (MimeMessage) message;
                    try {
                        System.out.println("MIMEMessage: " + message1.getContent().toString() + " Type: " + message1.getContentType());
                        System.out.println("ImapMessages: ");

                        MimeMultipart part = (MimeMultipart)message.getContent();

                        for (int i = 0; i < part.getCount(); i++) {
                            MimeBodyPart bodyPart = (MimeBodyPart) part.getBodyPart(i);
                            System.out.println("bodyParty(" + i + "): " + bodyPart.getContent().toString());

                            if (bodyPart.isMimeType("text/plain")) {
                                result = result + "\n" + bodyPart.getContent();
                                break;  //without break same text appears twice in my tests
                            }
                            else if (bodyPart.isMimeType("text/html")) {
                                String html = (String) bodyPart.getContent();
                                result = result.concat("\n" + Jsoup.parse(html).text());
                            }
                        }
                    } catch (IOException | MessagingException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(result);
                }
                return result;
            }
        }
        catch (Exception e){
            return "";
        }
        return "";
    }


    private EMail checkEMailAndSetToDB(Folder folder, Message message, ICRUDMail crudeMail, int i, long beginnSeconds, int messageCounter){
        EMail eMail = new EMail();
        try {
            //Beginn Zeitmessung für die aktuelle Nachricht
            long startMillis = System.currentTimeMillis();

            //Existiert die E-Mail bereits in der DB?
            if (crudeMail.checkMessageInDB(i, message.getSubject(), message.getFrom()[0].toString(), folder.getFullName()) == null) {
                eMail = new EMail();

                //Betreff setzen
                eMail.setBetref(message.getSubject());

                //Anfangs wird noch kein Tag gesetzt
                eMail.setTid(0);

                //Absender setzen
                eMail.setAbsender(extractEmailAddress(message.getFrom()[0].toString()));

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

                eMail = getContentOriginal(message, eMail);

                //MessageNumber setzen
                eMail.setMessageID(i);

                //NachrichtenContent in PlainText umwandeln und HTML - Content entfernen
                String mailContent = getPlainTextFromMessage(message);
                eMail.setInhalt(mailContent);

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
                System.out.println("StoreManager wirft Exception in IMailSteuerung, checkEMailAndSetToDB(): " + e.getMessage());
            }
        catch (FolderNotFoundException fe){
                System.out.println("FolderNotFoundException wurde geworfen in IMailSteuerung, checkEMailAndSetToDB(): " + fe.getMessage());
            }
        catch (MessagingException e) {
                System.out.println("MessagingException wurde geworfen in IMailSteuerung, checkEMailAndSetToDB(): " + e.getMessage());
            }
        catch (Exception e) {
                System.out.println("Exception wurde geworfen in IMailSteuerung, checkEMailAndSetToDB(): " + e.getMessage());
            }
        return eMail;
    }


    /*Liefert im Erfolgsfall die reine und gültige E-Mail Adresse zurück, ansonsten einen leeren String*/
    private String extractEmailAddress(String address){
        String extractedAddress = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (!address.isEmpty()){
            boolean addressStarted = false;
            for (int i = 0; i < address.length(); i++){
                if(address.charAt(i) == '<'){
                    addressStarted = true;
                }
                else if (addressStarted && address.charAt(i) == '>'){
                    addressStarted = false;
                    break;
                }
                else if(addressStarted && address.charAt(i) != '<' && address.charAt(i) != '>'){
                    stringBuilder.append(address.charAt(i));
                }
            }
            String tmp = stringBuilder.toString();
            CharSequence cs1 = "@";
            CharSequence cs2 = ".";
            if(tmp.contains(cs1) && tmp.contains(cs2)){
                extractedAddress = tmp;
            }
        }
        return extractedAddress;
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
            System.out.println("NullPointerException wurde in IMailSteuerung, loeschEMailVomServer() geworfen!" + e.getMessage());
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft in IMailSteuerung, loeschEMailVomServer() Exception: " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wird geworfenin IMailSteuerung, loeschEMailVomServer() : " + e.getMessage());
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
            System.out.println("StoreManager wirft in IMailSteuerung, erstelleEMailOrdner() Exception: " + e.getMessage());
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Fehler beim Erstellen des Ordners in IMailSteuerung, erstelleEMailOrdner(): " + e.getMessage());
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
                ret = icrudMail.deleteEMailVomOrdner(folder.getFullName());
            }

            folder.close(true);
        }
        catch (NullPointerException e){
            System.out.println("NullPointerException wurde in IMailSteuerung, loescheEMailOrdner() geworfen: " + e.getMessage());
        }
        catch (NoSuchProviderException e){
            System.out.println("StoreManager wirft Exception in IMailSteuerung, loescheEMailOrdner() : " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wird geworfen in IMailSteuerung, loescheEMailOrdner() : " + e.getMessage());
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
                    }
                    catch (NullPointerException e){
                        System.out.println("NullPointerException wurde  in IMailSteuerung, importiereAllEMails() geworfen: " + e.getMessage());
                    }
                }
                folder.close(true);
            }
            ret = true;
        }
        catch (NoSuchProviderException e) {
            System.out.println("StoreManager in IMailSteuerung, importiereAllEMails() wirft Exception: " + e.getMessage());
        }
        catch (FolderNotFoundException fe){
            System.out.println("FolderNotFoundException wurde geworfen in IMailSteuerung, importiereAllEMails(): " + fe.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wurde geworfen in IMailSteuerung, importiereAllEMails(): " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Exception wurde geworfen in IMailSteuerung, importiereAllEMails(): " + e.getMessage());
        }
        return ret;
    }


    public ArrayList<EMail> importiereAllEMailsvomOrdner(Konto konto, String ordnerName) throws MessagingException{
        //Store - Objekt holen
        setStore(konto);

        //CRUDObjekt zur Überprüfung ob Mails bereits importiert wurden!
        ICRUDMail crudeMail = ICRUDManagerSingleton.getIcrudMailInstance();
        EMail eMail;
        ArrayList<EMail> eMailArrayList = new ArrayList<>();

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
                }
                catch (NullPointerException e){
                    System.out.println("NullPointerException wurde geworfen in IMailSteuerung, importiereAllEMailsvomOrdner(): " + e.getMessage());
                }
            }
            folder.close(true);

        }
        catch (NoSuchProviderException e) {
            System.out.println("StoreManager wirft Exception in IMailSteuerung, importiereAllEMailsvomOrdner(): " + e.getMessage());
        }
        catch (FolderNotFoundException fe){
            System.out.println("FolderNotFoundException wurde geworfen in IMailSteuerung, importiereAllEMailsvomOrdner(): " + fe.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wurde geworfen in IMailSteuerung, importiereAllEMailsvomOrdner(): " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Exception wurde geworfen in IMailSteuerung, importiereAllEMailsvomOrdner(): " + e.getMessage());
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
                    BodyPart messageBodyPart = new MimeBodyPart();

                    //Content füllen
                    messageBodyPart.setText(eMail.getInhalt());
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    // Anhang hinzufügen
                    if(eMail.getFiles().size() > 0){
                        message.setContent(addAttachment(eMail, multipart));
                    }

                    message.setFlag(Flags.Flag.DRAFT, true);
                    MimeMessage draftMessages[] = {message};
                    folder.appendMessages(draftMessages);
                    ret = true;
                }
                catch (MessagingException ex) {
                    System.out.println("MessagingException wurde geworfen in IMailSteuerung, speichereEMailImOrdner(): " + ex.getMessage());
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
        return ret;
    }


    //Methode holt einen entsprechenden Ordner, der mit einem Listener überwacht werden soll
    @Override
    public ArrayList<Folder> holeFolderFuerListener(Konto konto) throws MessagingException {
        ArrayList<String> ordnerListe = getAlleOrdnerVonKonto(konto);
        ArrayList<Folder> folders = new ArrayList<>();
        Folder folder;
        if(ordnerListe.size() > 0){
            for(String ordnerName : ordnerListe){
                try{
                    folder = store.getFolder(ordnerName);
                    folders.add(folder);
                }
                catch (MessagingException e){
                    System.out.println("MessagingException wurde geworfen in IMailSteuerung, holeFolderFuerListener(): " + e.getMessage());
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
            System.out.println("MessagingException wurde geworfen in IMailSteuerung, getOrdnerByName(): " + e.getMessage());
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
            for (String anEmpfaengerList : empfaengerList) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anEmpfaengerList));
            }
            message.setSubject(email.getBetreff());

            //MessageContent erstellen
            //MimeBodyPart content = new MimeBodyPart();
            BodyPart messageBodyPart = new MimeBodyPart();

            //Content füllen
            messageBodyPart.setContent(email.getInhalt(), "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(addAttachment(email, multipart));

            //Nachricht senden
            Transport.send(message);

            //Gesendete E-Mail lokal sichern
            email.setOrdner("Gesendet");

            //Message-Content als Plain-Text umwandeln und in die Datenbank speichern
            email = getContentOriginal(message, email);

            ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
            int id = -1;
            id = icrudMail.createEMail(email);
            if(id != -1){
                ret = true;
            }
        }
        catch (NoSuchProviderException e){
            System.out.println("NoSuchProviderException wurde geworfen in IMailSteuerung, sendeEmail(): " + e.getMessage());
        }
        catch (MessagingException e) {
            System.out.println("MessagingException wurde geworfen in IMailSteuerung, sendeEmail(): " + e.getMessage());
        }

        return ret;
    }

    @Override
    public ArrayList<EMail> holeAlleEMails(Konto konto) {
        ArrayList<EMail> eMailList = new ArrayList<>();
        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();

        eMailList = icrudMail.getAlleEMails();

        return eMailList;
    }

    @Override
    public ArrayList<EMail> sucheEMail(String suchwort) {
        ArrayList<EMail> eMailArrayList = new ArrayList<>();

        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();

        eMailArrayList = icrudMail.searchEMail(suchwort);

        return eMailArrayList;
    }
    // #################################################################################################################
    // ################################################   /Setter   ####################################################
    // #################################################################################################################
}