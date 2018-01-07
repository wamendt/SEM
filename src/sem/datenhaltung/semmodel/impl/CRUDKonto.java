package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Konto;
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
            return insertAndReturnKey(sql, konto.getUserName(), konto.getEmailAddress(), konto.getAccountAt(),
                    konto.getIMAPhost(), konto.getPassWort(), konto.getPort(), konto.getSMTPhost());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteKonto(int kid){
        return true;
    }

    @Override
    public boolean updateKonto(Konto konto){
        return true;
    }

    @Override
    public Konto getKontoById(int kid){
        Konto k = new Konto();
        return k;
    }

    @Override
    public ArrayList<Konto> getAlleKonto(){
        ArrayList<Konto> alleKonten= new ArrayList<Konto>();
        return alleKonten;
    }

}
