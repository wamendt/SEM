package sem.datenhaltung.semmodel.crud.impl;

import sem.datenhaltung.semmodel.crud.services.IMailServerService;
import sem.datenhaltung.semmodel.entities.Connection;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.crud.services.IMailLocalService;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.template.DBMappingTemplate;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

public class CRUDEMailServerService extends DBMappingTemplate<EMail> implements IMailServerService {


    public CRUDEMailServerService(){

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
