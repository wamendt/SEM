package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.ICRUDWort;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDWort extends DBCRUDTeamplate<Wort> implements ICRUDWort {

    @Override
    protected Wort makeObject(ResultSet rs) throws SQLException, IOException {
        Wort w = new Wort();
        w.setWid(rs.getInt(1));
        w.setWort(rs.getString(2));
        w.setTid(rs.getInt(3));
        return w;
    }

    @Override
    public int createWort(Wort wort) throws IOException, SQLException {
        String sql = "INSERT INTO wort (wort, tid)" +
                "VALUES (?, ?)";
        return insertAndReturnKey(sql, wort.getWort(), wort.getTid());
    }

    @Override
    public Wort getWortById(int wid) throws IOException, SQLException {
        ArrayList<Wort> worts = query("SELECT * FROM wort WHERE wid=?", wid);
        return worts.size() > 0 ? worts.get(0) : null;
    }

    @Override
    public ArrayList<Wort> getAlleWoerter() throws IOException, SQLException {
        return query("SELECT * FROM wort");
    }

    @Override
    public ArrayList<Wort> getAlleWoerterMitTagId(int tid) throws IOException, SQLException {
        return query("SELECT * FROM wort WHERE tid = ?", tid);
    }

    @Override
    public boolean deleteWort(int wid) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROM wort WHERE wid = ?", wid);
        return ret == 1;
    }

    @Override
    public boolean deleteWoerterMitTagId(int tid) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROME wort WHERE tid= ?", tid);
        return ret == 1;
    }

    @Override
    public boolean updateWort(Wort wort) throws IOException, SQLException {
        int ret = updateOrDelete("UPDATE wort SET " +
                " wort = ? , tid = ? WHERE wid = ?", wort.getWort(), wort.getTid(), wort.getWid());
        return ret == 1;
    }


}
