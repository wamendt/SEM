package sem.datenhaltung.semmodel;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import sem.datenhaltung.semmodel.impl.CRUDEMail;
import sem.datenhaltung.semmodel.mailConnection.MailStoreManager;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.*;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.io.*;
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

        //CRUDObjekt zur Überprüfung ob Mails bereits importiert wurden!
        CRUDEMail crudeMail = new CRUDEMail();
        EMail eMail = new EMail();

        int durchlauf = 1;
        long messageCounter = 1;

        try {
            //Store - Objekt holen
            MailStoreManager storeManager = new MailStoreManager();
            Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());
            if (store != null) {

                //Alle Ordner holen
                Folder[] folders = store.getDefaultFolder().list("*");

                //Ausgabe zur Kontrolle
                for (Folder folder : folders) {
                    if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                        System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                    }
                }

                long beginnMillisTotal = System.currentTimeMillis();
                long beginnSeconds = TimeUnit.MILLISECONDS.toSeconds(beginnMillisTotal);
                MimeMessage[] messages = null;

                do {
                    for (Folder folder : folders) {

                        //Ordner öfnnen
                        if(!folder.isOpen()){
                            folder.open((Folder.READ_ONLY));
                        }

                        //Im ersten Durchlauf werden alle gelesene Nachrichten geholt
                        if(durchlauf == 1) {
                            System.out.println("\nHole alle gelesenen Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        //Im zweiten Durchlauf werden alle ungelesene Nachrichten geholt
                        else if (durchlauf == 2){
                            System.out.println("\nHole alle ungelesenen Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        else if (durchlauf == 3){
                            System.out.println("\nHole alle gelöschte Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.DELETED), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        else if (durchlauf == 4){
                            System.out.println("\nHole alle entworfenen Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.DRAFT), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        else if (durchlauf == 5){
                            System.out.println("\nHole alle beantworteten Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.ANSWERED), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        else if (durchlauf == 6){
                            System.out.println("\nHole alle markierte Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.FLAGGED), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        else if (durchlauf == 7){
                            System.out.println("\nHole alle recent Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.RECENT), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }
                        else if (durchlauf == 8){
                            System.out.println("\nHole alle ungelesenen Nachrichten!");
                            messages = (MimeMessage[]) folder.search(new FlagTerm(new Flags(Flags.Flag.USER), false));
                            System.out.println("Nachrichtenanzahl: " + messages.length);
                        }

                        int i = 0;
                        for(MimeMessage message : messages){

                            //ID holen
                            String id = message.getMessageID();
                            String file = "";
                            try{
                                //Existiert die E-Mail bereits in der DB?
                                if(crudeMail.getEMailByMessageID(id) == null) {

                                    //Beginn Zeitmessung für die aktuelle Nachricht
                                    long startMillis = System.currentTimeMillis();
                                    long startSeconds = TimeUnit.MILLISECONDS.toSeconds(startMillis);

                                    //NachrichtenContent in PlainText umwandeln und HTML - Content entfernen
                                    String mailContent = getTextFromMessage(message);

                                    //Zeiterfassung
                                    long currentMillis = System.currentTimeMillis();
                                    long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);
                                    long messageTime = currentSeconds - startSeconds;
                                    long totalTime = currentSeconds - beginnSeconds;

                                    eMail.setBetref(message.getSubject());
                                    System.out.println("Betreff gesetzt!");

                                    eMail.setInhalt(mailContent);
                                    System.out.println("Inhalt gesetzt!");

                                    //Anfangs wird noch kein Tag gesetzt
                                    eMail.setTid(0);
                                    System.out.println("Tid gesetzt!");

                                    eMail.setAbsender(message.getFrom()[0].toString());
                                    System.out.println("Absender gesetzt!");

                                    if(InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)) != null){
                                        eMail.setCc(InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)));
                                        System.out.println("CC übernommen!");
                                    }
                                    else {
                                        eMail.setCc("");
                                        System.out.println("Leerer CC gesetzt!");
                                    }

                                    if(InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)) != null){
                                        eMail.setBcc(InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)));
                                        System.out.println("BCC übernommen!");
                                    }
                                    else {
                                        eMail.setBcc("");
                                        System.out.println("Leerer BCC gesetzt!");
                                    }

                                    if(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)) != null){
                                        eMail.setEmpfaenger(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
                                        System.out.println("Empfaenger übernommen!");
                                    }
                                    else {
                                        eMail.setEmpfaenger("");
                                        System.out.println("Leerer Empfaenger gesetzt!");
                                    }

                                    // MimeMessage in ByteArrayOutPutStream schreiben, von da aus in einen String umwandeln
                                    // und in die DB schreiben. So bleibt die Original-Mail erhalten!
                                    try{
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        message.writeTo(baos);
                                        eMail.setContentOriginal(baos.toString());
                                    }catch (Exception e){
                                        System.out.println("ByteArrayOutputStream wirft Exception: " + e.getMessage());
                                    }

                                    if(durchlauf == 1){
                                        eMail.setZustand("gelesen");
                                        System.out.println("Zustand auf gelesen gesetzt!");
                                    }
                                    else if(durchlauf == 2){
                                        eMail.setZustand("ungelesen");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }
                                    else if(durchlauf == 3){
                                        eMail.setZustand("geloescht");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }
                                    else if(durchlauf == 4){
                                        eMail.setZustand("entwurf");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }
                                    else if(durchlauf == 5){
                                        eMail.setZustand("beantwortet");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }
                                    else if(durchlauf == 6){
                                        eMail.setZustand("flagged");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }
                                    else if(durchlauf == 7){
                                        eMail.setZustand("recent");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }
                                    else if(durchlauf == 8){
                                        eMail.setZustand("user");
                                        System.out.println("Zustand auf ungelesen gesetzt!");
                                    }

                                    eMail.setMessageID(id);
                                    System.out.println("Message-ID gesetzt!");

                                    crudeMail.createEMail(eMail);

                                    //Ausgabe zur Kontrolle
                                    System.out.println(messageCounter + ". Nachricht wurde importiert!\nbenötigte Zeit für diese Nachricht: " + messageTime + "s\nbenötigte Zeit Total:" + totalTime + "s\n\n");
                                    messageCounter++;
                                }
                                else {
                                    System.out.println("\nE-Mail bereits in der DB!");
                                }
                            }catch (NullPointerException e){
                                System.out.println("NullPointerException wurde geworfen: " + e.getMessage());
                            }
                        }
                        folder.close(true);
                    }
                    durchlauf++;
                }while (durchlauf < 9);
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
        return false;
    }

    @Override
    public boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad) {
        return false;
    }
}
