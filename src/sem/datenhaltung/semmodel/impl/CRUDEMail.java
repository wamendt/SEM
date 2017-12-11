package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.services.ICRUDMail;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDEMail extends DBCRUDTeamplate<EMail> implements ICRUDMail{


    @Override
    protected EMail makeObject(ResultSet rs) throws SQLException, IOException {
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
        return email;
    }

    @Override
    public int createEMail(EMail email) throws IOException, SQLException {
        String sql = "INSERT INTO email (betreff ,inhalt , tid, absender, cc, bcc, empfaenger, contentOriginal, zustand, messageID, ordner)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return insertAndReturnKey(sql, email.getBetreff(), email.getInhalt(), email.getTid(), email.getAbsender(),
                email.getCc(), email.getBcc(), email.getEmpfaenger(), email.getContentOriginal(), email.getZustand(),
                email.getMessageID(), email.getOrdner());
    }

    @Override
    public EMail getEMailById(int mid) throws IOException, SQLException {
        ArrayList<EMail> eMails = query("SELECT * FROM email WHERE mid = ?", mid);
        return eMails.size() > 0 ? eMails.get(0) : null;
    }

    public EMail getEMailByMessageIDUndOrdner(int id, String ordner) throws IOException, SQLException {
        ArrayList<EMail> eMails = query("SELECT * FROM email WHERE messageID = ?", id);
        return eMails.size() > 0 ? eMails.get(0) : null;
    }

    @Override
    public ArrayList<EMail> getAlleEMails() throws IOException, SQLException {
        return query("SELECT * FROM email");
    }

    @Override
    public ArrayList<EMail> getAlleEMailsMitTagId(int tid) throws IOException, SQLException {
        return query("SELECT * FROM email WHERE tid = ?", tid);
    }

    @Override
    public boolean deleteEMail(int mid) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROM email WHERE mid = ?", mid);
        return ret == 1;
    }

    @Override
    public ArrayList<EMail> searchEMail(String suchwort) throws IOException, SQLException {
        int mid = Integer.parseInt(suchwort);
        int tid = Integer.parseInt(suchwort);
        int messageID = Integer.parseInt(suchwort);
        ArrayList<EMail> eMails = query("SELECT * FROM email WHERE mid = ? OR betreff = ? OR inhalt = ? OR tid = ?" +
                "OR absender = ? OR cc = ? OR bcc = ? OR empfaenger = ? OR contentOriginal = ? OR zustand = ?" +
                "OR ordner = ? OR messageID = ?", mid, suchwort, suchwort, tid, suchwort, suchwort, suchwort, suchwort,
                suchwort, suchwort, suchwort, messageID);
        return eMails;
    }

    @Override
    public boolean updateEMail(EMail email) throws IOException, SQLException {
        int ret = updateOrDelete("UPDATE email" +
                        " SET betreff = ? , inhalt = ?, tid = ?, absender = ? " +
                        "WHERE mid = ?", email.getBetreff(), email.getInhalt()
                ,email.getTid(), email.getAbsender(), email.getMid());
        return ret == 1;
    }

    @Override
    public boolean deleteEMailByFolder(String folder) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROM email WHERE ordner = ?", folder);
        return ret == 1;
    }

    @Override
    public boolean createEMailTable() throws IOException, SQLException {
        boolean ret = false;
        ret = create("CREATE TABLE IF NOT EXISTS email (mid INTEGER NOT NULL, betreff VARCHAR, inhalt TEXT, tid INTEGER, " +
                        "absender VARCHAR, cc TEXT, bcc TEXT, empfaenger TEXT, contentOriginal TEXT, " +
                        "zustand VARCHAR(50), ordner TEXT, messageID INTEGER);");
        return ret;
    }

    @Override
    public boolean createFileTable() throws IOException, SQLException {
        boolean ret = false;
        ret = create("CREATE TABLE IF NOT EXISTS file (fid  INTEGER NOT NULL, pfad VARCHAR, mid  INTEGER);");
        return ret;
    }

    @Override
    public boolean createAdresseTable() throws IOException, SQLException {
        boolean ret = false;
        ret = create("CREATE TABLE IF NOT EXISTS adresse (aid INTEGER NOT NULL, adresse VARCHAR, " +
                "name VARCHAR, mid INTEGER, zustand VARCHAR);");
        return ret;
    }

    @Override
    public boolean createTagTable() throws IOException, SQLException {
        boolean ret = false;
        ret = create("CREATE TABLE IF NOT EXISTS tag (tid INTEGER NOT NULL PRIMARY KEY, name VARCHAR);");
        return ret;
    }

    @Override
    public boolean createWortTable() throws IOException, SQLException {
        boolean ret = false;
        ret = create("CREATE TABLE IF NOT EXISTS wort (wid INTEGER NOT NULL, wort VARCHAR, tid INTEGER NOT NULL);");
        return ret;
    }

    @Override
    public boolean deleteTable(String tableName) throws IOException, SQLException{
        boolean ret = false;

        return ret;
    }
}
