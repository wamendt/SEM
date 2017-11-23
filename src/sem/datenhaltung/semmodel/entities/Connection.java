package sem.datenhaltung.semmodel.entities;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Connection {
    private Konto konto;
    private Session session;


    //Konstruktor
    public Connection(Konto konto){
        this.konto = konto;
    }

    public static void setKonto(Konto konto) {
        final String username = "hikoelle@gmail.com";
        final String password = "rootroot";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
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



        } catch (MessagingException e) {

        }
    }
}