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
        int mid = rs.getInt(1);
        String betreff = rs.getString(2);
        String inhalt = rs.getString(3);
        int tid = rs.getInt(4);
        String absender = rs.getString(5);
        EMail email = new EMail();
        email.setMid(mid);
        email.setTid(tid);
        email.setBetref(betreff);
        email.setInhalt(inhalt);
        email.setAbsender(absender);
        return email;
    }

    @Override
    public int createEMail(EMail email) throws IOException, SQLException {
        String sql = "INSERT INTO email (betreff ,inhalt , tid, absender)" +
                "VALUES ( ?, ?, ?, ?)";

        return insertAndReturnKey(sql, email.getBetref());
    }

    @Override
    public EMail getEMailById(int mid) throws IOException, SQLException {
        ArrayList<EMail> eMails = query("SELECT * FROM email WHERE mid=?", mid);
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
                        "WHERE mid = ?", email.getBetref(), email.getInhalt()
                ,email.getTid(), email.getAbsender());
        return ret == 1? true : false;
    }
}
