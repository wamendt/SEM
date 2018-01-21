package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.services.ICRUDMail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * CRUD Klasse die die ICRUDMail Schnitstelle implementiert.
 * Bietet Methoden fuer den Zugriff auf die Emails aus der Datenbank.
 */
public class CRUDEMail extends DBCRUDTeamplate<EMail> implements ICRUDMail {
    private static final String TABLE_NAME = "email";
    private static final String COLUMN_MID = "mid";
    private static final String COLUMN_BETREFF = "betreff";
    private static final String COLUMN_INHALT = "inhalt";
    private static final String COLUMN_TID = "tid";
    private static final String COLUMN_ABSENDER = "absender";
    private static final String COLUMN_CC = "cc";
    private static final String COLUMN_BCC = "bcc";
    private static final String COLUMN_EMPFAENGER = "empfaenger";
    private static final String COLUMN_CONTENTORIGNAL = "contentoriginal";
    private static final String COLUMN_ZUSTAND = "zustand";
    private static final String COLUMN_MESSAGEID = "messageid";
    private static final String COLUMN_ORDNER = "ordner";
    private static final String COLUMN_KID = "kid";

    private static final String SQL_INSERT_EMAIL = "INSERT INTO email ( betreff ,inhalt , tid, absender, cc, bcc," +
            " empfaenger, contentOriginal, zustand, messageid, ordner, kid) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CHECK_DB = "SELECT * FROM email WHERE messageid = ? AND betreff = ? AND absender = ?";
    private static final String SQL_SEARCH_EMAIL = "SELECT * FROM email WHERE betreff LIKE ? OR inhalt LIKE ? OR absender LIKE ? ";
    private static final String SQL_UPDATE_EMAIL = "UPDATE email SET betreff = ? , inhalt = ?, tid = ?, absender = ?," +
            " cc = ?, bcc = ?, empfaenger = ?, contentoriginal = ?, zustand = ?, ordner = ?, messageid = ?, kid = ? WHERE mid = ?";
    private static final String SQL_SELECT_FROM_WHERE_AND = SQL_SELECT_FROM_WHERE + " AND %s = ?";
    private static final String SQL_DELETE_FROM_WHERE_AND = SQL_DELETE_FROM_WHERE + " AND %s = ?";

    @Override
    protected EMail makeObject(ResultSet rs) throws SQLException{
        EMail email = new EMail();
        email.setMid(rs.getInt(COLUMN_MID));
        email.setBetreff(rs.getString(COLUMN_BETREFF));
        email.setInhalt(rs.getString(COLUMN_INHALT));
        email.setTid(rs.getInt(COLUMN_TID));
        email.setAbsender(rs.getString(COLUMN_ABSENDER));
        email.setCc(rs.getString(COLUMN_CC));
        email.setBcc(rs.getString(COLUMN_BCC));
        email.setEmpfaenger(rs.getString(COLUMN_EMPFAENGER));
        email.setContentOriginal(rs.getString(COLUMN_CONTENTORIGNAL));
        email.setZustand(rs.getString(COLUMN_ZUSTAND));
        email.setMessageID(rs.getInt(COLUMN_MESSAGEID));
        email.setOrdner(rs.getString(COLUMN_ORDNER));
        email.setKid(rs.getInt(COLUMN_KID));
        return email;
    }

    @Override
    public int createEMail(EMail email) {
        try {
            int id = insertAndReturnKey(SQL_INSERT_EMAIL, email.getBetreff(), email.getInhalt(), email.getTid(), email.getAbsender(),
                    email.getCc(), email.getBcc(), email.getEmpfaenger(), email.getContentOriginal(), email.getZustand(),
                    email.getMessageID(), email.getOrdner(), email.getKid());
            email.setMid(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public EMail getEMailById(int mid){
        ArrayList<EMail> eMails;
        try {
            eMails = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_MID), mid);
            return eMails.size() > 0 ? eMails.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<EMail> getEMailByOrdner(int kid, String name){
        try {
            return query(String.format(SQL_SELECT_FROM_WHERE_AND, TABLE_NAME, COLUMN_KID, COLUMN_ORDNER), kid, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<EMail> getEMailByTag(int tid){
        try {
            return query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_TID), tid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    /*Wozu dann noch der Ordner*/
    public EMail checkMessageInDB(int id, String betreff, String absender, String ordner) {
        ArrayList<EMail> eMails;
        try {
            eMails = query(SQL_CHECK_DB, id, betreff, absender);
            return eMails.size() > 0 ? eMails.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<EMail> getAlleEMails(){
        try {
            return query(String.format(SQL_SELECT_FROM, TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteEMail(int mid) {
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_MID), mid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public int deleteAlleEMails() {
        int ret = 0;
        try{
            ret = updateOrDelete(String.format(SQL_DELETE_FROM, TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ArrayList<EMail> searchEMail(String suchwort){
        try {
            suchwort = "%" + suchwort + "%";
            return query(SQL_SEARCH_EMAIL, suchwort, suchwort , suchwort);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateEMail(EMail email)  {
        int ret = 0;
        try {
            ret = updateOrDelete(SQL_UPDATE_EMAIL, email.getBetreff(), email.getInhalt()
                    ,email.getTid(), email.getAbsender(), email.getCc(), email.getBcc(), email.getEmpfaenger(),
                    email.getContentOriginal(), email.getZustand(), email.getOrdner(), email.getMessageID(), email.getKid(),
                    email.getMid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public int deleteEMailVomOrdner(int kid, String ordner) {
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE_AND, TABLE_NAME, COLUMN_KID, COLUMN_ORDNER), kid, ordner);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /*BRAUCHT MAN DIE METHODE ??*/
//    public EMail getEMailByMessageIDUndOrdner(int id, String ordner){
//        ArrayList<EMail> eMails;
//        try {
//            eMails = query("SELECT * FROM email WHERE messageID = ? AND betreff = ? AND absender = ? AND ordner = ?", id);
//            return eMails.size() > 0 ? eMails.get(0) : null;
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    @Override
//    public ArrayList<EMail> getAlleEMailsMitTagId(int tid){
//        try {
//            return query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_TID), tid);
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }

    /*BRAUCHEN WIR DIE METHODEN ??? Oder Liefern wir nicht einfach schon die sqldatei nicht mit aus*/

//    @Override
//    public boolean createEMailTable(){
//        try {
//            return create("CREATE TABLE IF NOT EXISTS email (mid INTEGER NOT NULL, betreff VARCHAR, inhalt TEXT, tid INTEGER, " +
//                    "absender VARCHAR, cc TEXT, bcc TEXT, empfaenger TEXT, contentOriginal TEXT, " +
//                    "zustand VARCHAR(50), ordner TEXT, messageID INTEGER);");
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean createFileTable(){
//        try {
//            return create("CREATE TABLE IF NOT EXISTS file (fid  INTEGER NOT NULL, pfad VARCHAR, mid  INTEGER);");
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


//
//    @Override
//    public boolean createAdresseTable() {
//        try {
//            return create("CREATE TABLE IF NOT EXISTS adresse (aid INTEGER NOT NULL, adresse VARCHAR, " +
//                    "name VARCHAR, mid INTEGER, zustand VARCHAR);");
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean createTagTable(){
//        try {
//            return create("CREATE TABLE IF NOT EXISTS tag (tid INTEGER NOT NULL PRIMARY KEY, name VARCHAR);");
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean createWortTable() {
//        try {
//            return create("CREATE TABLE IF NOT EXISTS wort (wid INTEGER NOT NULL, wort VARCHAR, tid INTEGER NOT NULL);");
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean deleteTable(String tableName){
//        return false;
//    }
}
