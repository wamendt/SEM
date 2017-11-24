package sem.datenhaltung.semmodel.crud.services;


import sem.datenhaltung.semmodel.entities.Connection;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;

import java.util.ArrayList;

public interface IMailServerService {

    //EMail-Ordner
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto);
    public boolean loeschEMailVomServer(Konto konto, EMail eMail);
    public String erstelleEMailOrdner(Konto konto, String pfad);
    public String loescheEMailOrdner(Konto konto, String pfad);

    //Aktionen mit Mails
    public boolean sendeEmail(Konto konto, EMail email);
    public boolean importiereAllEMails(Konto konto);
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email);
    public boolean setzeTagsZurServerEMail(Konto konto, EMail email, String art);
    public boolean speichereEMailImOrdner(Konto konto, EMail eMail, String pfad);

}
