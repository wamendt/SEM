package sem.fachlogik.mailsteuerung.utils.services;


import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.File;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.FileGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IMailService {
    //EMail-Ordner
    ArrayList<String> getAlleOrdnerVonKonto(Konto konto) throws NoSuchProviderException;
    boolean loeschEMailVomServer(Konto konto, EMail eMail);
    boolean erstelleEMailOrdner(Konto konto, String pfad);
    boolean loescheEMailOrdner(Konto konto, String pfad);
    Folder getOrdnerByName(Konto konto, String name) throws MessagingException;

    //Aktionen mit Mails
    boolean importiereAllEMails(Konto konto) throws MessagingException;
    ArrayList<EMail> importiereAllEMailsvomOrdner(Konto konto, String ordnerName) throws MessagingException;
    boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email) throws MessagingException, IOException, SQLException;
    boolean setzeTagsZurServerEMail(Konto konto, EMail email, String art) throws MessagingException, IOException, SQLException;
    boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad) throws MessagingException, IOException, SQLException;
    boolean sendeEmail(Konto konto, EMail email) throws NoSuchProviderException;
    ArrayList<EMail> holeAlleEMails(Konto konto) throws IOException, SQLException;
    ArrayList<EMail> sucheEMail(String suchwort) throws IOException, SQLException;


    //Listener
    ArrayList<Folder> holeFolderFuerListener(Konto konto) throws MessagingException;
}
