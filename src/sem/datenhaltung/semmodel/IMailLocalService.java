package sem.datenhaltung.semmodel;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;

import java.io.File;
import java.util.ArrayList;

public interface IMailLocalService {

    //EMail-Ordner
    public ArrayList<String> getAlleOrdnerVonKonto(Konto konto);
    public String erstelleEMailOrdner(Konto konto, String pfad);
    public String loescheEMailOrdner(Konto konto, String pfad);
    public String getOrdnerByName(Konto konto, String name);

    //DB Tabellen
    public boolean erstelleEMailTabelle(Konto konto);
    public boolean loescheEMailTabelle(Konto konto);
    public boolean erstelleEMailEntwurfTabelle(Konto konto);
    public boolean loescheEMailEntwurfTabelle(Konto konto);
    public boolean erstelleAttachmentTabelle(Konto konto, String name);
    public boolean loescheAttachmentTabelle(Konto konto);

    //Aktionen mit Mails
    public boolean setzeTagsZurLokalenEMail(Konto konto, EMail email, String art);
    public boolean verschiebeEMail(Konto konto, String vonOrdner, String zuOrdner, EMail email);
    public boolean leiteWeiterEMail(EMail mail);
    public File fuegeAnhangZurEMailHinzu(EMail mail);
    public boolean sendeEmail(Konto konto, EMail email);

}
