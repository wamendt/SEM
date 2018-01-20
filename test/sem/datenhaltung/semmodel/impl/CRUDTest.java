package sem.datenhaltung.semmodel.impl;

import org.apache.commons.mail.Email;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import sem.datenhaltung.semmodel.database.DBConnectionManager;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.entities.Tag;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Sammlung wiederverwertbarer Methoden fuer die CRUDTestKlassen
 */
public class CRUDTest {

    /**Connection Objekt fuer den zugrif auf die DatenBank*/
    protected Connection connection;
    /**Savepoint fuer den Rollback*/
    protected static Savepoint savepoint;


    /**
     * Erstellt eine Email Instanz
     * @param tid der Foreignkey eines Tag objektes, 0 wenn kein Tag zugeordnet werden soll
     * @return die erstellte Email
     */
    protected EMail createEMailObject(int tid){
        EMail email = new EMail();
        email.setInhalt("TEST INHALT");
        email.setTid(tid);
        email.setZustand("TEST ZUSTAND");
        email.setOrdner("TEST ORDNER");
        email.setBcc("TEST BCC");
        email.setCc("TEST CC");
        email.setEmpfaenger("TEST EMPFAENGER");
        email.setAbsender("TEST ABSENDER");
        email.setBetreff("TEST BETREFF");
        email.setContentOriginal("CONTENT ORG");
        email.setMessageID(12345);
        return email;
    }

    /**
     * Erstellt eine Konto-Instanz
     *
     * @return das erstellte Konto
     */
    protected Konto createKontoObject(){
        Konto konto = new Konto();
        konto.setUserName("TEST USERNAME");
        konto.setPassWort("TEST PASSWORT");
        konto.setIMAPhost("TEST IMAPHOST");
        konto.setSMTPhost("TEST SMTPHOST");
        konto.setEmailAddress("TEST EMAIL");
        konto.setPort(12345);
        konto.setSignatur("TEST SIG");
        return konto;
    }

    /**
     * Holt eine Email aus der Datenbank
     * @param mid der Primarykey der email
     * @return bei erfolg die gesuchte Email, sonst null
     */
    protected EMail getEMailVonDb(int mid){
        EMail eMail = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM email WHERE mid = ?");
            statement.setObject(1, mid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                eMail = new EMail();
                eMail.setMid(rs.getInt("mid"));
                eMail.setInhalt(rs.getString("inhalt"));
                eMail.setMessageID(rs.getInt("messageid"));
                eMail.setContentOriginal(rs.getString("contentoriginal"));
                eMail.setBetreff(rs.getString("betreff"));
                eMail.setAbsender(rs.getString("absender"));
                eMail.setTid(rs.getInt("tid"));
                eMail.setCc(rs.getString("cc"));
                eMail.setBcc(rs.getString("bcc"));
                eMail.setEmpfaenger(rs.getString("empfaenger"));
                eMail.setOrdner(rs.getString("ordner"));
                eMail.setZustand(rs.getString("zustand"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eMail;
    }
    /**
     * Holt ein Konto aus der Datenbank
     * @param kid der Primarykey d
     * @return bei erfolg die gesuchte Konto, sonst null
     */
    protected Konto getKontoVonDb(int kid){
        Konto konto = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM konto WHERE kid = ?");
            statement.setObject(1, kid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                konto = new Konto();
                konto.setUserName(rs.getString("username"));
                konto.setPassWort(rs.getString("passwort"));
                konto.setSignatur(rs.getString("signatur"));
                konto.setIMAPhost(rs.getString("imaphost"));
                konto.setSMTPhost(rs.getString("smtphost"));
                konto.setPort(rs.getInt("port"));
                konto.setEmailAddress(rs.getString("emailaddress"));
                konto.setKid(rs.getInt("kid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return konto;
    }

    /**
     * Loescht eine Email mit angegebenen PrimaryKey aus der Datenbank
     * @param id der PrimaryKey der Email
     */
    protected void deleteEMailVonDb(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM email WHERE mid = ?");
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loescht ein Konto mit angegebenen PrimaryKey aus der Datenbank
     * @param id der PrimaryKey des Kontos
     */
    protected void deleteKontoVonDb(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM konto WHERE kid = ?");
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loescht ein Tag mit angegebenem PrimaryKey aus der Datenbank
     * @param id der PrimaryKey des Tags
     */
    protected void deleteTagVonDb(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tag WHERE tid = ?");
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Methode zum erstellen einer Test Email, dieser wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initilazation ausgefuehrt werden
     * @param eMail die Email die eingefuegt werden soll
     */
    protected void insertEmail(EMail eMail){
        int key = insertAndReturnKey("INSERT INTO email ( betreff ,inhalt , tid, absender, cc, bcc," +
                        " empfaenger, contentOriginal, zustand, messageid, ordner) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                eMail.getBetreff(), eMail.getInhalt(), eMail.getTid(), eMail.getAbsender(), eMail.getCc(), eMail.getBcc(),
                eMail.getEmpfaenger(), eMail.getContentOriginal(), eMail.getZustand(), eMail.getMessageID(), eMail.getOrdner());
        eMail.setMid(key);
    }

    /**
     * Methode zum erstellen eines Test Kontos, dieses wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initilazation ausgefuehrt werden
     * @param konto das Konto was engefuegt werden soll.
     */
    protected void insertKonto(Konto konto){
        int key = insertAndReturnKey("INSERT INTO konto(username, passwort, imaphost, smtphost, emailaddress, port, signatur)" +
                "VALUES(?,?,?,?,?,?,?)", konto.getUserName(), konto.getPassWort(), konto.getIMAPhost(), konto.getSMTPhost(),
                konto.getEmailAddress(), konto.getPort(), konto.getSignatur());
        konto.setKid(key);
    }

    /**
     * Methode zum erstellen eines Test Tages, dieser wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initilazation ausgefuehrt werden
     * @return der erstellte Tag
     */
    protected Tag createAndInsertTag(){
        Tag tag = new Tag();
        tag.setName("TESTTAG");
        int key = insertAndReturnKey("INSERT INTO tag(name) VALUES(?)", tag.getName());
        tag.setTid(key);
        return tag;
    }

    /**
     * Methode zum ausfuehren von INSERT befehlen
     * @param sql der SQL Select befehl mit platzhaltern (?) fuer die Values
     * @param objects Objekte fuer die Platzhalter
     * @return den erzeugten PrimaryKey
     */
    protected int insertAndReturnKey(String sql, Object... objects){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (objects != null) {
                for (int i = 1; i <= objects.length; i++) {
                    statement.setObject(i, objects[i - 1]);
                }
            }
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return -1;
    }

    /**
     * Vor jedem start wird eine neue Verbindung mit der Datenbank geoffnet.
     * Und es wird ein savePoint gesetzt.
     */
    @BeforeEach
    void initBefore(){
        try {
            connection = DBConnectionManager.getConnection();
            savepoint = connection.setSavepoint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nach jedem Lauf wird die Verbindung wieder geschlossen und die Datenbank wird auf den
     * gesetzten Savepoint zurueckgesetzt.
     */
    @AfterEach
    void initAfter(){
        try {
            connection.rollback(savepoint);
            /*Verbindung sollte automatisch geschlossen werden*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
