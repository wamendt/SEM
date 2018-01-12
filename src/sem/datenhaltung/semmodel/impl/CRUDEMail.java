package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.services.ICRUDMail;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDEMail extends DBCRUDTeamplate<EMail> implements ICRUDMail{


    @Override
    protected EMail makeObject(ResultSet rs) throws SQLException{
        EMail email = new EMail();
        email.setMid(rs.getInt("mid"));
        email.setBetref(rs.getString("betreff"));
        email.setInhalt(rs.getString("inhalt"));
        email.setTid(rs.getInt("tid"));
        email.setAbsender(rs.getString("absender"));
        email.setCc(rs.getString("cc"));
        email.setBcc(rs.getString("bcc"));
        email.setEmpfaenger(rs.getString("empfaenger"));
        email.setContentOriginal(rs.getString("contentOriginal"));
        email.setZustand(rs.getString("zustand"));
        email.setMessageID(rs.getInt("messageID"));
        email.setOrdner(rs.getString("ordner"));
        email.setInstanceID(rs.getInt("instanceid"));
        return email;
    }

    @Override
    public int createEMail(EMail email) {
        String sql = "INSERT INTO email (instanceid, betreff ,inhalt , tid, absender, cc, bcc, empfaenger, contentOriginal, zustand, messageID, ordner)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            return insertAndReturnKey(sql,email.getInstanceID(), email.getBetreff(), email.getInhalt(), email.getTid(), email.getAbsender(),
                    email.getCc(), email.getBcc(), email.getEmpfaenger(), email.getContentOriginal(), email.getZustand(),
                    email.getMessageID(), email.getOrdner());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public EMail getEMailById(int mid){
        ArrayList<EMail> eMails;
        try {
            eMails = query("SELECT * FROM email WHERE mid = ?", mid);
            return eMails.size() > 0 ? eMails.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public EMail getEMailbyInstanceID(int instanceID){
        ArrayList<EMail> eMails;
        try {
            eMails = query("SELECT e FROM email WHERE instnaceid = ?", instanceID);
            return eMails.size() > 0 ? eMails.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<EMail> getEMailByOrdner(String name){
        try {
            return query("SELECT * FROM email WHERE ordner = ?", name);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<EMail> getEMailByTag(int tid){
        try {
            return query("SELECT * FROM email WHERE tid = ?", tid);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public EMail getEMailByMessageIDUndOrdner(int id, String ordner){
        ArrayList<EMail> eMails;
        try {
            eMails = query("SELECT * FROM email WHERE messageID = ? AND betreff = ? AND absender = ? AND ordner = ?", id);
            return eMails.size() > 0 ? eMails.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EMail checkMessageInDB(int id, String betreff, String absender, String ordner) {
        ArrayList<EMail> eMails;
        try {
            eMails = query("SELECT * FROM email WHERE messageID = ? AND betreff = ? AND absender = ? AND ordner = ?", id, betreff, absender, ordner);
            return eMails.size() > 0 ? eMails.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<EMail> getAlleEMails(){
        try {
            return query("SELECT * FROM email");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<EMail> getAlleEMailsMitTagId(int tid){
        try {
            return query("SELECT * FROM email WHERE tid = ?", tid);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteEMail(int mid) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM email WHERE mid = ?", mid);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public ArrayList<EMail> searchEMail(String suchwort){
        try {
            return query("SELECT * FROM email WHERE betreff LIKE ? OR inhalt LIKE ?  ", "%" + suchwort + "%", "%" + suchwort + "%");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateEMail(EMail email)  {
        int ret = 0;
        try {
            ret = updateOrDelete("UPDATE email" +
                            " SET betreff = ? , inhalt = ?, tid = ?, absender = ? , instanceid = ?" +
                            "WHERE mid = ?", email.getBetreff(), email.getInhalt()
                    ,email.getTid(), email.getAbsender(), email.getInstanceID(), email.getMid());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean deleteEMailByFolder(String folder) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM email WHERE ordner = ?", folder);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean createEMailTable(){
        try {
            return create("CREATE TABLE IF NOT EXISTS email (mid INTEGER NOT NULL, betreff VARCHAR, inhalt TEXT, tid INTEGER, " +
                    "absender VARCHAR, cc TEXT, bcc TEXT, empfaenger TEXT, contentOriginal TEXT, " +
                    "zustand VARCHAR(50), ordner TEXT, messageID INTEGER);");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createFileTable(){
        try {
            return create("CREATE TABLE IF NOT EXISTS file (fid  INTEGER NOT NULL, pfad VARCHAR, mid  INTEGER);");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createAdresseTable() {
        try {
            return create("CREATE TABLE IF NOT EXISTS adresse (aid INTEGER NOT NULL, adresse VARCHAR, " +
                    "name VARCHAR, mid INTEGER, zustand VARCHAR);");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createTagTable(){
        try {
            return create("CREATE TABLE IF NOT EXISTS tag (tid INTEGER NOT NULL PRIMARY KEY, name VARCHAR);");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createWortTable() {
        try {
            return create("CREATE TABLE IF NOT EXISTS wort (wid INTEGER NOT NULL, wort VARCHAR, tid INTEGER NOT NULL);");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTable(String tableName){
        return false;
    }
}
