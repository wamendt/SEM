package sem.datenhaltung.semmodel;


import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.util.ArrayList;

public interface IMailServerService {

    //EMail-Ordner
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto);
    public boolean loeschEMailVomServer(Konto konto, EMail eMail);
    public boolean erstelleEMailOrdner(Konto konto, String pfad);
    public boolean loescheEMailOrdner(Konto konto, String pfad);

    //Aktionen mit Mails
    public boolean importiereAllEMails(Konto konto) throws MessagingException;
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) throws MessagingException;
    public boolean setzeTagsZurServerEMail(Konto konto, EMail email, String art);
    public boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad) throws MessagingException;

}
