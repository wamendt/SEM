package sem.datenhaltung.semmodel.crud.impl;

import sem.datenhaltung.semmodel.crud.services.IMailLocalService;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.crud.services.ICRUDMail;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.template.DBMappingTemplate;
import sem.fachlogik.grenzklassen.EMailGrenz;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

public class CRUDEMailILocalService extends DBMappingTemplate<EMail> implements IMailLocalService{

    public CRUDEMailILocalService(){

    }

    @Override
    public boolean sendeEmail(Konto konto, EMail email){
        boolean ret = false;

        //Hier müssen die Daten vom Konto geladen werden
        final String username = "hikoelle@gmail.com";
        final String password = "root(133)";
        final String name = "E-Mail Sortierer";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Authenticator auth = null;
        auth = new javax.mail.Authenticator() {
            @Override
            public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("w.amendt@live.de"));
            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("w.amendt@live.de"));
            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("w.amendt@live.de"));
            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("w.amendt@live.de"));
            message.setSubject("Dies ist ein Betreff!");
            message.setText("\n\nTestinhalt!\n\n");

            Transport.send(message);
            ret = true;
        } catch (MessagingException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
        return ret;

    }

    @Override
    protected EMail makeObject(ResultSet rs) {
        return null;
    }

    @Override
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto) {
        return null;
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
    public String getOrdnerByName(Konto konto, String name) {
        return null;
    }

    @Override
    public boolean erstelleEMailTabelle(Konto konto) {
        return false;
    }

    @Override
    public boolean loescheEMailTabelle(Konto konto) {
        return false;
    }

    @Override
    public boolean erstelleEMailEntwurfTabelle(Konto konto) {
        return false;
    }

    @Override
    public boolean loescheEMailEntwurfTabelle(Konto konto) {
        return false;
    }

    @Override
    public boolean erstelleAttachmentTabelle(Konto konto, String name) {
        return false;
    }

    @Override
    public boolean loescheAttachmentTabelle(Konto konto) {
        return false;
    }

    @Override
    public boolean setzeTagsZurLokalenEMail(Konto konto, EMail email, String art) {
        return false;
    }

    @Override
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) {
        return false;
    }

    @Override
    public boolean leiteWeiterEMail(EMail mail) {
        return false;
    }

    @Override
    public File fuegeAnhangZurEMailHinzu(EMail mail) {
        return null;
    }


}
