package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.ICRUDWort;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDWort extends DBCRUDTeamplate<Wort> implements ICRUDWort {

    @Override
    protected Wort makeObject(ResultSet rs) throws SQLException{
        Wort w = new Wort();
        w.setWid(rs.getInt(1));
        w.setWort(rs.getString(2));
        w.setTid(rs.getInt(3));
        return w;
    }

    @Override
    public int createWort(Wort wort){
        String sql = "INSERT INTO wort (wort, tid)" +
                "VALUES (?, ?)";
        try {
            int id =  insertAndReturnKey(sql, wort.getWort(), wort.getTid());
            wort.setWid(id);
            return id;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Wort getWortById(int wid) {
        ArrayList<Wort> worts;
        try {
            worts = query("SELECT * FROM wort WHERE wid=?", wid);
            return worts.size() > 0 ? worts.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Wort> getAlleWoerter()  {
        try {
            return query("SELECT * FROM wort");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Wort> getAlleWoerterMitTagId(int tid) {
        try {
            return query("SELECT * FROM wort WHERE tid = ?", tid);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteWort(int wid) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM wort WHERE wid = ?", wid);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean deleteAlleWoerterMitTagId(int tid) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM wort WHERE tid= ?", tid);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean updateWort(Wort wort) {
        int ret = 0;
        try {
            ret = updateOrDelete("UPDATE wort SET " +
                    " wort = ? , tid = ? WHERE wid = ?", wort.getWort(), wort.getTid(), wort.getWid());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }


}
