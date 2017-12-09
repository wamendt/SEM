package sem.datenhaltung.semmodel;


import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import java.util.ArrayList;

public interface IMailServerService {

    public Store getStore();

    //EMail-Ordner
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto) throws NoSuchProviderException;
    public boolean loeschEMailVomServer(Konto konto, EMail eMail);
    public boolean erstelleEMailOrdner(Konto konto, String pfad);
    public boolean loescheEMailOrdner(Konto konto, String pfad);

    //Aktionen mit Mails
    public boolean importiereAllEMails(Konto konto) throws MessagingException;
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) throws MessagingException;
    public boolean setzeTagsZurServerEMail(Konto konto, EMail email, String art) throws MessagingException;
    public boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad) throws MessagingException;

    //Listener
    public ArrayList<Folder> holeFolderFuerListener(Konto konto) throws MessagingException;
}
