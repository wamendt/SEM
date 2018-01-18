package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDKonto;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDKonto extends DBCRUDTeamplate<Konto> implements ICRUDKonto {

    @Override
    protected Konto makeObject(ResultSet rs) throws SQLException{
        Konto konto = new Konto();

        konto.setKid(rs.getInt(1));
        konto.setUserName(rs.getString(2));
        konto.setEmailAddress(rs.getString(3));
        konto.setAccountAt(rs.getString(4));
        konto.setIMAPhost(rs.getString(5));
        konto.setPassWort(rs.getString(6));
        konto.setPort(rs.getInt(7));
        konto.setSMTPhost(rs.getString(8));

        return konto;
    }

    @Override
    public int createKonto(Konto konto){
        String sql = "INSERT INTO konto (UserName, EmailAddress, AccountAt, " +
                "IMAPhost, PassWort, Port, SMTPhost)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            int id = insertAndReturnKey(sql, konto.getUserName(), konto.getEmailAddress(), konto.getAccountAt(),
                    konto.getIMAPhost(), konto.getPassWort(), konto.getPort(), konto.getSMTPhost());
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
            ret = updateOrDelete("DELETE FROM konto WHERE kid = ?", kid);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean updateKonto(Konto konto){
        int ret;
        try {
            ret = updateOrDelete("UPDATE konto" +
                    " SET userName = ?, passwort = ?, accountat = ?, imaphos = ?, smtphost = ?," +
                    "emailaddress = ?, port = ? WHERE kid = ?", konto.getUserName(), konto.getPassWort(),
                    konto.getAccountAt(), konto.getIMAPhost(), konto.getSMTPhost(), konto.getEmailAddress(),
                    konto.getPort(), konto.getKid());
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
            kontos = query("SELECT * FROM konto WHERE kid=?", kid);
            return kontos.size() > 0 ? kontos.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Konto> getAlleKonten(){
        try {
            return query("SELECT * FROM konto");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

}
