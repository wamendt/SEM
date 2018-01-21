package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.ICRUDWort;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDWort extends DBCRUDTeamplate<Wort> implements ICRUDWort {
    private static final String TABLE_NAME = "wort";
    private static final String COLUMN_WID = "wid";
    private static final String COLUMN_WORT = "wort";
    private static final String COLUMN_TID = "tid";

    private static final String SQL_INSERT_WORT = "INSERT INTO wort(wort, tid) VALUES(?,?)";
    private static final String SQL_UPDATE_WORT = "UPDATE wort SET wort = ? , tid = ? WHERE wid = ?";

    @Override
    protected Wort makeObject(ResultSet rs) throws SQLException{
        Wort w = new Wort();
        w.setWid(rs.getInt(COLUMN_WID));
        w.setWort(rs.getString(COLUMN_WORT));
        w.setTid(rs.getInt(COLUMN_TID));
        return w;
    }

    @Override
    public int createWort(Wort wort){
        try {
            int id =  insertAndReturnKey(SQL_INSERT_WORT, wort.getWort(), wort.getTid());
            wort.setWid(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Wort getWortById(int wid) {
        ArrayList<Wort> worts;
        try {
            worts = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_WID), wid);
            return worts.size() > 0 ? worts.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Wort> getAlleWoerter()  {
        try {
            return query(String.format(SQL_SELECT_FROM, TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Wort> getAlleWoerterMitTagId(int tid) {
        try {
            return query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_TID), tid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteWort(int wid) {
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_WID), wid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public int deleteAlleWoerterMitTagId(int tid) {
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_TID), tid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public boolean updateWort(Wort wort) {
        int ret = 0;
        try {
            ret = updateOrDelete(SQL_UPDATE_WORT, wort.getWort(), wort.getTid(), wort.getWid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }


}
