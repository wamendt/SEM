package sem.fachlogik.mailsteuerung.services;

import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.fachlogik.mailsteuerung.listener.MsgReceivedListener;
import sem.fachlogik.mailsteuerung.listener.MsgRemovedListener;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IMailSteuerung {

    //Listener
    void addMsgReceivedListener(MsgReceivedListener msgReceivedListener);
    void removeMsgReceivedListener(MsgReceivedListener msgRemovedListener);
    void addMsgRemovedListener(MsgRemovedListener msgReceivedListener);
    void removeMsgRemovedListener(MsgRemovedListener msgRemovedListener);


    //Ordner
    boolean erstelleEMailOrdner(KontoGrenz kontoGrenz, String name);
    boolean loescheEMailOrdner(KontoGrenz kontoGrenz, String name);
    ArrayList<String> zeigeAlleOrdner(KontoGrenz kontoGrenz);  //x


    //E-Mails
    ArrayList<EMailGrenz> zeigeAlleEMails(KontoGrenz kontoGrenz) throws NoSuchProviderException;
    boolean weiseEinerEMailFlagZu(KontoGrenz kontoGrenz, String art, EMailGrenz eMailGrenz) throws MessagingException, IOException, SQLException;
    boolean loescheEMailAusPapierkorb(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz);
    EMailGrenz leseEMail(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz);
    boolean speicherEMailEntwurf(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz);
    boolean verschiebeEMail(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz, String vonOrdner, String zuOrdner);
    boolean sendeEMail(KontoGrenz kontoGrenz, EMailGrenz eMailGrenz);
    ArrayList<EMailGrenz> sucheEMail(String suche);
    ArrayList<EMailGrenz> importEMails(KontoGrenz kontoGrenz);
    ArrayList<EMailGrenz> importEMailsAusOrdner(KontoGrenz kontoGrenz, String ordnerName);
    ArrayList<EMailGrenz> zeigeAlleEMailsAusOrdner(KontoGrenz kontoGrenz, String ordnerName);  //x
    ArrayList<EMailGrenz> sucheEMailByTag(TagGrenz tagGrenz);  //x
}
