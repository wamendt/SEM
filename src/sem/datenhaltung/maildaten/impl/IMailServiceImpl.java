package sem.datenhaltung.maildaten.impl;

import sem.datenhaltung.entities.Connection;
import sem.datenhaltung.entities.EMail;
import sem.datenhaltung.maildaten.services.IMailService;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class IMailServiceImpl implements IMailService{


    public IMailServiceImpl(){

    }

    @Override
    public boolean sendEmail(EMail email){
        boolean ret = false;
        final String username = "hikoelle@gmail.com";
        final String password = "root(133)";

        System.out.println("Hallo");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("hikoelle@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("hikoelle@gmail.com"));
            message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("wamendt86@gmail.com"));
            message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("gromow@gmail.com"));
            message.setSubject("Eingangsmail von Kölle");
            message.setText("\n\n Name des Absenders: \n\n Emailadresse des Absenders: \n\nTelefonnummer des Absenders: \n\n Nachricht: ");

            Transport.send(message);
            System.out.println("Sende Email!");
            ret = true;
        } catch (MessagingException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
        return ret;
    }

    @Override
    public boolean importAllEMails(KontoGrenz kontoGrenz, Connection connection) {
        return false;
    }

    @Override
    public ArrayList<EMailGrenz> getAllEmails(KontoGrenz kontoGrenz) {
        return null;
    }

    @Override
    public ArrayList<String> getAllFoldersFromAccount(KontoGrenz kontoGrenz) {
        return null;
    }

    @Override
    public String getFolderByID(int id, String name) {
        return null;
    }

    @Override
    public boolean createEMailTable(String path) {
        return false;
    }

    @Override
    public boolean deleteEMailTable(String path) {
        return false;
    }

    @Override
    public boolean createEMailEntwurfTable(String path) {
        return false;
    }

    @Override
    public boolean deleteEMailEntwurfTable(String path) {
        return false;
    }

    @Override
    public boolean forwardEMail(EMailGrenz mailGrenz) {
        return false;
    }
}
