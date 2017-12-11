package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Adresse;
import sem.datenhaltung.semmodel.services.ICRUDAdresse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDAdresse extends DBCRUDTeamplate<Adresse> implements ICRUDAdresse{

    @Override
    protected Adresse makeObject(ResultSet rs) throws SQLException {
        Adresse adresse = new Adresse();
        adresse.setAid(rs.getInt(1));
        adresse.setAdresse(rs.getString(2));
        adresse.setName(rs.getString(3));
        adresse.setMid(rs.getInt(4));
        adresse.setZustand(rs.getString(5));
        return adresse;
    }

    @Override
    public int createAdresse(Adresse adresse) throws IOException, SQLException {
        String sql = "INSERT INTO adresse (adresse, name, mid, zustand)" +
                "VALUES ( ?, ?, ?, ?)";
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(adresse.getAdresse());
        objects.add(adresse.getName());
        objects.add(adresse.getMid());
        objects.add(adresse.getZustand());
        return insertAndReturnKey(sql, objects);
    }

    @Override
    public Adresse getAdresseById(int aid) throws IOException, SQLException {
        ArrayList<Adresse> adresses = query("SELECT * FROM adresse WHERE aid=?", aid);
        return adresses.size() > 0 ? adresses.get(0) : null;
    }

    @Override
    public ArrayList<Adresse> getAlleAdressen() throws IOException, SQLException {
        return query("SELECT * FROM adresse");
    }

    @Override
    public ArrayList<Adresse> getAlleCCAdressen(int mid) throws IOException, SQLException {
        return query("SELECT * FROM adresse WHERE zustand = ?, mid = ?", "cc", mid);
    }

    @Override
    public  ArrayList<Adresse> getAlleBCCAdressen(int mid) throws IOException, SQLException {
        return query("SELECT * FROM adresse WHERE zustand = ? mid = ?", "bcc", mid);
    }

    @Override
    public ArrayList<Adresse> getAlleEmpfaengerAdressen(int mid) throws IOException, SQLException {
        return query("SELECT * FROM adresse WHERE zustand = ?, mid = ? ", "e", mid);
    }

    @Override
    public boolean deleteAdresse(int aid) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROM adresse WHERE aid = ?", aid);
        return ret == 1;
    }

    @Override
    public boolean updateAdresse(Adresse adresse) throws IOException, SQLException {
        int ret = updateOrDelete("UPDATE adresse" +
                " SET adresse = ? , name = ?, mid = ?, zustand = ? " +
                "WHERE aid = ?", adresse.getAdresse(), adresse.getName()
                , adresse.getMid(), adresse.getZustand(), adresse.getAid());
        return ret == 1;
    }
}
