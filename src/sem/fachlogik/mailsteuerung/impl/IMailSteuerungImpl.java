package sem.fachlogik.mailsteuerung.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.GrenzklassenKonvertierer;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.fachlogik.mailsteuerung.listener.MsgReceivedListener;
import sem.fachlogik.mailsteuerung.listener.MsgRemovedListener;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;
import sem.fachlogik.mailsteuerung.utils.impl.IMailServiceImpl;
import sem.fachlogik.mailsteuerung.utils.services.IMailService;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class IMailSteuerungImpl implements IMailSteuerung{

    // #################################################################################################################
    // ###########################################   /Hilfsmethoden   ##################################################
    // #################################################################################################################
    private ArrayList<EMailGrenz> convertEMailListToEMailGrenzList(ArrayList<EMail> eMails){
        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if(eMails.size() > 0){
            //Service anlegen
            //IMailService iMailService = IMailServiceImpl.getMailService();
            EMailGrenz eMailGrenz = new EMailGrenz();
            for (EMail eMail : eMails){
                eMailGrenz = GrenzklassenKonvertierer.eMailZuEMailGrenz(eMail);
                eMailGrenzList.add(eMailGrenz);
            }
        }
        return eMailGrenzList;
    }
    // #################################################################################################################
    // ###########################################   /Hilfsmethoden   ##################################################
    // #################################################################################################################

    // #################################################################################################################
    // ###############################################   Listener   ####################################################
    // #################################################################################################################
    @Override
    public void addMsgReceivedListener(MsgReceivedListener msgReceivedListener) {
        IMailServiceImpl.getReceivedListeners().add(msgReceivedListener);
    }

    @Override
    public void removeMsgReceivedListener(MsgReceivedListener msgReceivedListener) {
        IMailServiceImpl.getReceivedListeners().remove(msgReceivedListener);
    }

    @Override
    public void addMsgRemovedListener(MsgRemovedListener msgRemovedListener) {
        IMailServiceImpl.getRemovedListeners().add(msgRemovedListener);
    }

    @Override
    public void removeMsgRemovedListener(MsgRemovedListener msgRemovedListener) {
        IMailServiceImpl.getRemovedListeners().remove(msgRemovedListener);
    }
    // #################################################################################################################
    // ###############################################   /Listener   ###################################################
    // #################################################################################################################



    // #################################################################################################################
    // ################################################   Ordner   #####################################################
    // #################################################################################################################
    @Override
    public boolean erstelleEMailOrdner(KontoGrenz kontoGrenz, String name) {
        boolean ret = false;
        assert name != null;
        if(kontoGrenz != null && !name.equals("")){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //neuen Ordner erstellen und RückgabeTyp ermitteln
            ret = iMailService.erstelleEMailOrdner(konto, name);
        }
        return ret;
    }

    @Override
    public boolean loescheEMailOrdner(KontoGrenz kontoGrenz, String name) {
        boolean ret = false;
        if(kontoGrenz != null && name != null && !name.equals("")){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //neuen Ordner erstellen und RückgabeTyp ermitteln
            ret = iMailService.loescheEMailOrdner(konto, name);
        }
        return ret;
    }

    @Override
    public ArrayList<String> zeigeAlleOrdner(KontoGrenz kontoGrenz) {
        ArrayList<String> ordnerList = new ArrayList<>();
        if(kontoGrenz != null){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //neuen Ordner erstellen und RückgabeTyp ermitteln
            try {
                ordnerList = iMailService.getAlleOrdnerVonKonto(konto);
            }
            catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        return ordnerList;
    }

    @Override
    public int loescheAlleEMails() {
        return ICRUDManagerSingleton.getIcrudMailInstance().deleteAlleEMails();
    }
    // #################################################################################################################
    // ################################################   /Ordner   ####################################################
    // #################################################################################################################



    // #################################################################################################################
    // ################################################   E-Mail   #####################################################
    // #################################################################################################################
    @Override
    public ArrayList<EMailGrenz> zeigeAlleEMails(KontoGrenz kontoGrenz) {

        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if(kontoGrenz != null){
            ArrayList<EMail> eMailList;

            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            try{
                //Hole alle E-Mails aus der DB
                eMailList = iMailService.holeAlleEMails(konto);
                eMailGrenzList = convertEMailListToEMailGrenzList(eMailList);
            }
            catch (SQLException e) {
                System.out.println("SQLException wurde geworfen: " + e.getMessage());
            }
            catch (IOException e) {
                System.out.println("IOException wurde geworfen: " + e.getMessage());
            }
        }
        return eMailGrenzList;
    }

    @Override
    public boolean weiseEinerEMailFlagZu(KontoGrenz kontoGrenz, String art, EMailGrenz eMailGrenz) {
        boolean ret = false;
        assert art != null;
        if(kontoGrenz != null && !art.equals("") && eMailGrenz != null) {
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //Wandle EMailGrenz in E-Mail um
            EMail eMail = GrenzklassenKonvertierer.eMailGrenzZuEMail(eMailGrenz);

            try {
                ret = iMailService.setzeTagsZurServerEMail(konto, eMail, art);
            }
            catch (MessagingException e){
                System.out.println("MessagingException wurder geworfe: " + e.getMessage());
            }
            catch (IOException e){
                System.out.println("IOException wurder geworfe: " + e.getMessage());
            }
            catch (SQLException e){
                System.out.println("SQLException wurder geworfe: " + e.getMessage());
            }
        }
        return ret;
    }

    @Override
    public boolean loescheEMailAusPapierkorb(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz) {
        boolean ret = false;
        if (kontoGrenz != null && eMailGrenz != null){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //Wandle EMailGrenz in E-Mail um
            EMail eMail = GrenzklassenKonvertierer.eMailGrenzZuEMail(eMailGrenz);

            //E-Mail löschen
            ret = iMailService.loeschEMailVomServer(konto, eMail);
        }
        return ret;
    }

    @Override
    public boolean sendeEMail(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz) {
        boolean ret = false;
        if (kontoGrenz != null && eMailGrenz != null){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //Wandle EMailGrenz in E-Mail um
            EMail eMail = GrenzklassenKonvertierer.eMailGrenzZuEMail(eMailGrenz);

            //E-Mail senden
            try{
                ret = iMailService.sendeEmail(konto, eMail);
            }
            catch (NoSuchProviderException e){
                System.out.println("NoSuchProviderException wurde geworfen: " + e.getMessage());
            }
        }
        return ret;
    }

    @Override
    public boolean speicherEMailEntwurf(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz) {
        boolean ret = false;
        if (kontoGrenz != null && eMailGrenz != null){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //Wandle EMailGrenz in E-Mail um
            EMail eMail = GrenzklassenKonvertierer.eMailGrenzZuEMail(eMailGrenz);

            //E-Mail speichern
            try{
                ret = iMailService.speichereEMailImOrdner(konto, eMail, eMail.getOrdner());
            }
            catch (IOException e) {
                System.out.println("IOException wurde geworfen: " + e.getMessage());
            }
            catch (SQLException e) {
                System.out.println("SQLException wurde geworfen: " + e.getMessage());
            }
            catch (MessagingException e) {
                System.out.println("MessagingException wurde geworfen: " + e.getMessage());
            }
        }
        return ret;
    }

    @Override
    public boolean verschiebeEMail(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz, String vonOrdner, String zuOrdner) {
        boolean ret = false;
        assert vonOrdner != null;
        assert zuOrdner != null;
        if (kontoGrenz != null && eMailGrenz != null && !vonOrdner.equals("") && !zuOrdner.equals("")){
            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            //Wandle EMailGrenz in E-Mail um
            EMail eMail = GrenzklassenKonvertierer.eMailGrenzZuEMail(eMailGrenz);

            //E-Mail verschieben
            try{
                ret = iMailService.verschiebeEMail(konto, vonOrdner, zuOrdner, eMail);
            }
            catch (IOException e) {
                System.out.println("IOException wurde geworfen: " + e.getMessage());
            }
            catch (SQLException e) {
                System.out.println("SQLException wurde geworfen: " + e.getMessage());
            }
            catch (MessagingException e) {
                System.out.println("MessagingException wurde geworfen: " + e.getMessage());
            }
        }
        return ret;
    }

    @Override
    public ArrayList<EMailGrenz> sucheEMail(String suche) {
        assert suche != null;
        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if (!suche.equals("")){
            ArrayList<EMail> eMailList;

            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            try {
                eMailList = iMailService.sucheEMail(suche);

                if(eMailList.size() > 0){
                    //Wandle alle E-Mails in E-MailGrenz um
                    EMailGrenz eMailGrenz;
                    for(EMail eMail : eMailList){
                        eMailGrenz = GrenzklassenKonvertierer.eMailZuEMailGrenz(eMail);
                        // und in die Sammlung einfügen
                        if(eMailGrenz != null){
                            eMailGrenzList.add(eMailGrenz);
                        }
                    }
                }
            }
            catch (IOException e) {
                System.out.println("IOException wurde geworfen: " + e.getMessage());
            }
            catch (SQLException e) {
                System.out.println("SQLException wurde geworfen: " + e.getMessage());
            }
        }
        return eMailGrenzList;
    }

    @Override
    public ArrayList<EMailGrenz> importEMails(KontoGrenz kontoGrenz) {
        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if(kontoGrenz != null){
            ArrayList<EMail> eMailList = new ArrayList<>();
            ArrayList<String> ordnerList;

            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            try{
                //Alle Ordner des Kontos holen
                ordnerList = iMailService.getAlleOrdnerVonKonto(konto);

                //E-Mails holen
                if(ordnerList.size() > 0){
                    ArrayList<EMail> tmpEmailList = new ArrayList<>();

                    //gehe alle Ordner durch ...
                    for (String ornderName : ordnerList){
                        //hole von jedem Ordner alle E-Mails ...
                        tmpEmailList.clear();
                        tmpEmailList = iMailService.importiereAllEMailsvomOrdner(konto, ornderName);
                        if (tmpEmailList.size() > 0){
                            //und füge sie der Sammlung hinzu
                            eMailList.addAll(tmpEmailList);
                        }
                    }
                }
                //Wenn E-Mails existieren ...
                eMailGrenzList = convertEMailListToEMailGrenzList(eMailList);
            }
            catch (NoSuchProviderException e){
                System.out.println("NoSuchProviderException wurde geworfen: " + e.getMessage());
            }
            catch (MessagingException e) {
                System.out.println("MessagingException wurde geworfen: " + e.getMessage());
            }
        }
        return eMailGrenzList;
    }

    @Override
    public ArrayList<EMailGrenz> importEMailsAusOrdner(KontoGrenz kontoGrenz, String ordnerName) {
        assert ordnerName != null;
        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if(kontoGrenz != null && !ordnerName.equals("")){
            ArrayList<EMail> eMailList;

            //Service anlegen
            IMailService iMailService = IMailServiceImpl.getMailService();

            //KontoGrenz in Konto konvertieren
            Konto konto = GrenzklassenKonvertierer.kontoGrenzZuKonto(kontoGrenz);

            try{
                //hole alle E-Mails ...
                eMailList = iMailService.importiereAllEMailsvomOrdner(konto, ordnerName);

                //Wenn E-Mails existieren ...
                if (eMailList.size() > 0){
                    //Wandle die Liste in E-MailGrenz - Liste um
                    EMailGrenz eMailGrenz;
                    for (EMail eMail : eMailList){
                        eMailGrenz = GrenzklassenKonvertierer.eMailZuEMailGrenz(eMail);
                        eMailGrenzList.add(eMailGrenz);
                    }
                }
            }
            catch (NoSuchProviderException e){
                System.out.println("NoSuchProviderException wurde geworfen: " + e.getMessage());
            }
            catch (MessagingException e) {
                System.out.println("MessagingException wurde geworfen: " + e.getMessage());
            }
        }
        return eMailGrenzList;
    }

    @Override
    public ArrayList<EMailGrenz> zeigeAlleEMailsAusOrdner(KontoGrenz kontoGrenz, String ordnerName) {
        assert ordnerName != null;
        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if(kontoGrenz != null && !ordnerName.equals("")){
            ArrayList<EMail> eMailList;
            ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
            eMailList = icrudMail.getEMailByOrdner(kontoGrenz.getKid(), ordnerName);
            if(eMailList.size() > 0){
                //Service anlegen
                IMailService iMailService = IMailServiceImpl.getMailService();

                EMailGrenz eMailGrenz;
                for (EMail eMail : eMailList){
                    eMailGrenz = GrenzklassenKonvertierer.eMailZuEMailGrenz(eMail);
                    eMailGrenzList.add(eMailGrenz);
                }
            }
        }
        return eMailGrenzList;
    }

    @Override
    public ArrayList<EMailGrenz> sucheEMailByTag(TagGrenz tagGrenz) {
        int tid = -1;
        ArrayList<EMailGrenz> eMailGrenzList = new ArrayList<>();
        if(tagGrenz != null){
            tid = tagGrenz.getTid();
            if(tid > 0){
                ArrayList<EMail> eMailList;
                ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();

                eMailList = icrudMail.getEMailByTag(tid);
                eMailGrenzList = convertEMailListToEMailGrenzList(eMailList);
            }
        }
        return eMailGrenzList;
    }
    // #################################################################################################################
    // ################################################   /E-Mail   ####################################################
    // #################################################################################################################
}
