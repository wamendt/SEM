package sem.datenhaltung.semmodel;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.impl.CRUDEMail;

import javax.mail.MessagingException;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CRUDEMailServerServiceTest {
    @Test
    void getAlleOrdnerVonKonto() {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
        crudeMailServerService.getAlleOrdnerVonKonto(konto);
    }

    @Test
    void loeschEMailVomServer() throws IOException, SQLException {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
        CRUDEMail crudeMail = new CRUDEMail();
        EMail eMail = new EMail();
        eMail = crudeMail.getEMailByMessageID("<1511971491.5a1edaa31bdf6@swift.generated>");
        System.out.println("EMAIL-MessageID: " + eMail.getMessageID() + "\nBetreff: " + eMail.getBetreff());
        crudeMailServerService.loeschEMailVomServer(konto, eMail);
    }

    @Test
    void erstelleEMailOrdner() {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
        boolean test = crudeMailServerService.erstelleEMailOrdner(konto, "Test");
        System.out.println("Ordner erstellt? " + test);
    }

    @Test
    void loescheEMailOrdner() {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
        System.out.println("Gelöscht? " + crudeMailServerService.loescheEMailOrdner(konto, "Test"));
    }

    @Test
    void importiereAllEMails() throws MessagingException {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();

        crudeMailServerService.importiereAllEMails(konto);
    }

    @Test
    void verschiebeEMail() throws IOException, SQLException, MessagingException {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");

        CRUDEMail crudeMail = new CRUDEMail();
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
        String id = "<94eb2c03eb7a148396055e89bc00@google.com>";
        EMail eMail = new EMail();
        eMail = crudeMail.getEMailByMessageID(id);
        System.out.println("EMAIL-MessageID: " + eMail.getMessageID() + "\nBetreff: " + eMail.getBetreff());
        System.out.println("Gelöscht? " + crudeMailServerService.verschiebeEMail(konto, "[Gmail]/Alle Nachrichten", "[Gmail]/Gesendet", eMail));
    }

    @Test
    void setzeTagsZurServerEMail() {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
    }

    @Test
    void speichereEMailImOrdner() {
        Konto konto = new Konto();
        konto.setEmailAddress("wamendt86@gmail.com");
        konto.setPassWort("Packard1!");
        konto.setIMAPhost("imap.gmail.com");
        IMailServerService crudeMailServerService = new CRUDEMailServerService();
    }

}