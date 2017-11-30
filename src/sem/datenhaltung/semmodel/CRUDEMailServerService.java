package sem.datenhaltung.semmodel;

import sem.datenhaltung.semmodel.impl.CRUDEMail;
import sem.datenhaltung.semmodel.mailConnection.MailStoreManager;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.io.*;
import org.jsoup.Jsoup;


public class CRUDEMailServerService implements IMailServerService {


    public CRUDEMailServerService(){

    }


    @Override
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto) {
        return null;
    }

    @Override
    public boolean loeschEMailVomServer(Konto konto, EMail eMail) {
        return false;
    }

    @Override
    public String erstelleEMailOrdner(Konto konto, String pfad) {
        return null;
    }

    @Override
    public String loescheEMailOrdner(Konto konto, String pfad) {
        return null;
    }

    @Override
    public boolean sendeEmail(Konto konto, EMail email) {
        return false;
    }

    @Override
    public boolean importiereAllEMails(Konto konto) throws MessagingException {

        //CRUDObjekt zur Überprüfung ob Mails bereits importiert wurden!
        CRUDEMail crudeMail = new CRUDEMail();
        EMail eMail = new EMail();

        int durchlauf = 1;

        //Store - Objekt holen
        MailStoreManager storeManager = new MailStoreManager();
        Store store = storeManager.setImapConnection(konto.getIMAPhost(), konto.getEmailAddress(), konto.getPassWort());

        long messageCounter = 1;

        try {
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
                    }
                    durchlauf++;
                }while (durchlauf < 9);
            }
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
                    result = result + "\n" + Jsoup.parse(html).text();
                }
            }
            return result;
        }
        return "";
    }


    @Override
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) {
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
