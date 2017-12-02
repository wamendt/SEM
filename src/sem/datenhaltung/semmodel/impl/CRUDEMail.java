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
        email.setMessageID(rs.getString("messageID"));
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

    public EMail getEMailByMessageID(String id) throws IOException, SQLException {
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
        return ret == 1? true : false;
    }

    @Override
    public boolean updateEMail(EMail email) throws IOException, SQLException {
        int ret = updateOrDelete("UPDATE email" +
                        " SET betreff = ? , inhalt = ?, tid = ?, absender = ? " +
                        "WHERE mid = ?", email.getBetreff(), email.getInhalt()
                ,email.getTid(), email.getAbsender());
        return ret == 1? true : false;
    }
}
