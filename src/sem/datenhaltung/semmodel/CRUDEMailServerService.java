package sem.datenhaltung.semmodel;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;

import java.util.ArrayList;

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
    public boolean importiereAllEMails(Konto konto) {
        return false;
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
