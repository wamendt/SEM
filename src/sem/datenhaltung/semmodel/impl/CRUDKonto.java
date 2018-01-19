package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDKonto;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDKonto extends DBCRUDTeamplate<Konto> implements ICRUDKonto {
    private static final String TABLE_NAME = "konto";
    private static final String COLUMN_KID = "kid";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORT = "passwort";
    private static final String COLUMN_IMAPHOST = "imaphost";
    private static final String COLUMN_SMTPHOST = "smpthost";
    private static final String COLUMN_EMAILADDRESS = "emailaddress";
    private static final String COLUMN_PORT = "port";
    private static final String COLUMN_SIGNATUR = "signatur";

    private static final String SQL_INSERT_KONTO = "INSERT INTO konto (UserName, EmailAddress, " +
            "IMAPhost, PassWort, Port, SMTPhost, signatur)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_KONTO = "UPDATE konto SET userName = ?, passwort = ?, imaphos = ?, smtphost = ?," +
            "emailaddress = ?, port = ?, signatur = ? WHERE kid = ?";

    @Override
    protected Konto makeObject(ResultSet rs) throws SQLException{
        Konto konto = new Konto();
        konto.setKid(rs.getInt(COLUMN_KID));
        konto.setUserName(rs.getString(COLUMN_USERNAME));
        konto.setEmailAddress(rs.getString(COLUMN_EMAILADDRESS));
        konto.setIMAPhost(rs.getString(COLUMN_IMAPHOST));
        konto.setPassWort(rs.getString(COLUMN_PASSWORT));
        konto.setPort(rs.getInt(COLUMN_PORT));
        konto.setSMTPhost(rs.getString(COLUMN_SMTPHOST));
        konto.setSignatur(rs.getString(COLUMN_SIGNATUR));
        return konto;
    }

    @Override
    public int createKonto(Konto konto){
        try {
            int id = insertAndReturnKey(SQL_INSERT_KONTO, konto.getUserName(), konto.getEmailAddress(),
                    konto.getIMAPhost(), konto.getPassWort(), konto.getPort(), konto.getSMTPhost(), konto.getSignatur());
            konto.setKid(id);
            return id;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteKonto(int kid){
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_KID),kid);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean updateKonto(Konto konto){
        int ret;
        try {
            ret = updateOrDelete(SQL_UPDATE_KONTO, konto.getUserName(), konto.getPassWort(),
                    konto.getIMAPhost(), konto.getSMTPhost(), konto.getEmailAddress(),
                    konto.getPort(), konto.getSignatur(), konto.getKid());
            return 1 == ret;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Konto getKontoById(int kid){
        ArrayList<Konto> kontos;
        try {
            kontos = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_KID), kid);
            return kontos.size() > 0 ? kontos.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Konto> getAlleKonten(){
        try {
            return query(String.format(SQL_SELECT_FROM, TABLE_NAME));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

}
