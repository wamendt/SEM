package sem.datenhaltung.semmodel.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import sem.datenhaltung.semmodel.database.DBConnectionManager;
import sem.datenhaltung.semmodel.entities.*;

import java.sql.*;

/**
 * Sammlung wiederverwertbarer Methoden fuer die CRUDTestKlassen
 */
public class CRUDTest {

    /**Connection Objekt fuer den zugrif auf die DatenBank*/
    protected Connection connection;
    /**Savepoint fuer den Rollback*/
    protected static Savepoint savepoint;


    /**
     * Erstellt eine Email Instanz.
     * @param tid der Foreignkey eines Tag objektes, 0 wenn kein Tag zugeordnet werden soll.
     * @return die erstellte Email
     */
    protected EMail createEMailObject(int tid, int kid){
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
        email.setKid(kid);
        return email;
    }

    /**
     * Erstellt eine Konto-Instanz
     *
     * @return das erstelte Konto
     */
    protected Konto createKontoObject(){
        Konto konto = new Konto();
        konto.setUserName("TEST USERNAME");
        konto.setPassWort("TEST PASSWORT");
        konto.setIMAPhost("TEST IMAPHOST");
        konto.setSMTPhost("TEST SMTPHOST");
        konto.setPort(12345);
        konto.setSignatur("TEST SIG");
        return konto;
    }

    /**
     * Erstellt eine Regel Instanz.
     * @param kid der Foreignkey eines Konto objektes, 0 wenn kein Konto zugeordnet werden soll.
     * @return die erstelte Regel
     */
    protected Regel createRegelObject(int kid){
        Regel regel = new Regel();
        regel.setActive(true);
        regel.setBeschreibung("TEST BESCHREIBUNG");
        regel.setKid(kid);
        regel.setZuOrdner("TEST ZUORDNER");
        regel.setVonEmailAddress("TEST VONORDNER");
        return regel;
    }

    /**
     * Erstellt eine Tag Instanz
     * @return den erstelten Tag
     */
    protected Tag createTagObject(){
        Tag tag = new Tag();
        tag.setName("TEST NAME");
        return tag;
    }

    /**
     * Erstellt eine TagVertelung Instanz
     * @param mid der ForeignKey einer Email, 0 wenn keine Email zugeordnet werden soll
     * @return die erstellte TagVerteilung
     */
    protected TagVerteilung createTagVerteilungObject(int mid){
        TagVerteilung tagVerteilung = new TagVerteilung();
        tagVerteilung.setVerteilung(0.01);
        tagVerteilung.setMid(mid);
        return tagVerteilung;
    }

    /**
     * Erstellt einen Wort Instanz
     * @param tid der Foreignkey eines Tags, 0 wenn kein Tag zugeordnet werden soll
     * @return das erstellte Wort
     */
    protected Wort createWortObject(int tid){
        Wort wort = new Wort();
        wort.setWort("TEST WORT");
        wort.setTid(tid);
        return wort;
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
                eMail.setKid(rs.getInt("kid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eMail;
    }
    /**
     * Holt ein Konto aus der Datenbank
     * @param kid der Primarykey
     * @return bei erfolg das gesuchte Konto, sonst null
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
                konto.setKid(rs.getInt("kid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return konto;
    }

    /**
     * Holt eine Regel aus der Datenbank
     * @param rid der Primarykey
     * @return bei erfolg die gesuchte Regel, sonst null
     */
    protected Regel getRegelVonDb(int rid){
        Regel regel = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM regel WHERE rid = ?");
            statement.setObject(1, rid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                regel = new Regel();
                regel.setRid(rs.getInt("rid"));
                regel.setZuOrdner(rs.getString("zuordner"));
                regel.setVonEmailAddress(rs.getString("vonemailaddress"));
                regel.setKid(rs.getInt("kid"));
                regel.setBeschreibung(rs.getString("beschreibung"));
                regel.setActive(rs.getInt("isactive") == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regel;
    }

    /**
     * Holt einen Tag von der Datenbank
     * @param tid der primaryKey des Tags
     * @return den gefunden Tag bei erfolg, sonst null
     */
    protected Tag getTagVonDb(int tid){
        Tag tag = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tag WHERE tid = ?");
            statement.setObject(1, tid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                tag = new Tag();
                tag.setTid(rs.getInt("tid"));
                tag.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

    /**
     * Holt eine Tagverteilung aus der Datenbank
     * @param tvid der primaryKey der Tagverteilung
     * @return die gefundene Tagverteilulng bei erfolg, sons null.
     */
    protected TagVerteilung getTagVerteilungVonDb(int tvid){
        TagVerteilung tagVerteilung = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tagverteilung WHERE tvid = ?");
            statement.setObject(1, tvid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                tagVerteilung = new TagVerteilung();
                tagVerteilung.setTvid(rs.getInt("tvid"));
                tagVerteilung.setMid(rs.getInt("mid"));
                tagVerteilung.setVerteilung(rs.getDouble("verteilung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tagVerteilung;
    }

    /**
     * Holt ein Wort aus der Datenbank
     * @param wid der PrimaryKey des wortes
     * @return das gefundene Wort bei erfolg, sonst null
     */
    protected Wort getWortVonDb(int wid){
        Wort wort = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM wort WHERE wid = ?");
            statement.setObject(1, wid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
               wort = new Wort();
               wort.setTid(rs.getInt("tid"));
               wort.setWort(rs.getString("wort"));
               wort.setWid(rs.getInt("wid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wort;
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
     * Loescht eine Regel mit angegebenen PrimaryKey aus der Datenbank
     * @param id der PrimaryKey der Regel
     */
    protected void deleteRegelVonDb(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM regel WHERE rid = ?");
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
     * Loescht eine TagVerteilung mit angegebenen PrimaryKey aus der dAtenbank
     * @param tvid der PrimaryKey der TagVerteilung
     */
    protected void deleteTagVerteilungVonDb(int tvid){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tagverteilung WHERE tvid = ?");
            statement.setInt(1,tvid);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loescht ein Wort aus der Datenbank mit angegebene PrimaryKey.
     * @param wid der PrimaryKey des Wortes
     */
    protected void deleteWortVonDb(int wid){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM wort WHERE wid = ?");
            statement.setInt(1,wid);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Methode zum erstellen einer Test Email, dieser wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initialisierung ausgefuehrt werden
     * @param eMail die Email die eingefuegt werden soll
     */
    protected void insertEmail(EMail eMail){
        int key = insertAndReturnKey("INSERT INTO email ( betreff ,inhalt , tid, absender, cc, bcc," +
                        " empfaenger, contentOriginal, zustand, messageid, ordner, kid) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                eMail.getBetreff(), eMail.getInhalt(), eMail.getTid(), eMail.getAbsender(), eMail.getCc(), eMail.getBcc(),
                eMail.getEmpfaenger(), eMail.getContentOriginal(), eMail.getZustand(), eMail.getMessageID(), eMail.getOrdner(),
                eMail.getKid());
        eMail.setMid(key);
    }

    /**
     * Methode zum erstellen eines Test Kontos, dieses wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initialisierung ausgefuehrt werden
     * @param konto das Konto was engefuegt werden soll.
     */
    protected void insertKonto(Konto konto){
        int key = insertAndReturnKey("INSERT INTO konto(username, passwort, imaphost, smtphost, port, signatur)" +
                "VALUES(?,?,?,?,?,?)", konto.getUserName(), konto.getPassWort(), konto.getIMAPhost(), konto.getSMTPhost(),
                konto.getPort(), konto.getSignatur());
        konto.setKid(key);
    }

    /**
     * Methode zum erstellen einer Test-Regel, dieses wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initilasierung ausgefuehrt werden
     * @param regel die Regel die eingefuegt werden soll.
     */
    protected void insertRegel(Regel regel){
        int key = insertAndReturnKey("INSERT INTO regel(beschreibung, vonemailaddress, zuordner, isactive, kid)" +
                        "VALUES(?,?,?,?,?)", regel.getBeschreibung(), regel.getVonEmailAddress(), regel.getZuOrdner(),
                regel.isActive() ? 1: 0, regel.getKid());
        regel.setRid(key);
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
     * Methode zum erstellen einer Test TagVerteilung, dieser wird auch in die Datenbank hinzugefuegt
     * und der Primary Key wird gesetzt
     * Darf erst nach der initilazation ausgefuehrt werden
     * @param  tagVerteilung die zu erstellende Tagverteilung
     * @return der erzeugte PrimaryKey
     */
    protected int insertTagVerteilung(TagVerteilung tagVerteilung){
        int key = insertAndReturnKey("INSERT INTO tagverteilung(verteilung, mid) VALUES(?,?)",
                tagVerteilung.getVerteilung(), tagVerteilung.getMid());
        tagVerteilung.setTvid(key);
        return key;
    }

    /**
     * Methode zum erstellen eines Test WOrtes, dieses wird auch in die Datenbank hinzugefuegt udn der PrimaryuKey wird
     * gesetzt. Darf erst nach der initialisation ausgefuehrt werden
     * @param wort das zu erstellende wort
     * @return den erzeugeten Schluessel
     */
    protected int insertWort(Wort wort){
        int key = insertAndReturnKey("INSERT INTO wort(wort, tid) VALUES(?,?)",
                wort.getWort(), wort.getTid());
        wort.setWid(key);
        return key;
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
