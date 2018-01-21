package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.File;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDFile extends DBCRUDTeamplate<File> implements ICRUDFile{


    @Override
    protected File makeObject(ResultSet rs) throws SQLException{
        File file = new File();
        file.setFid(rs.getInt(1));
        file.setPfad(rs.getString(2));
        file.setMId(rs.getInt(3));
        return file;
    }

    @Override
    public int createFile(File file){
        String sql = "INSERT INTO file (pfad, mid)" +
                "VALUES (?, ?)";

        try {
            int id =  insertAndReturnKey(sql, file.getPfad(), file.getMid());
            file.setMId(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public File getFileById(int fid){
        ArrayList<File> files;
        try {
            files = query("SELECT * FROM file WHERE fid=?", fid);
            return files.size() > 0 ? files.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<File> getAlleWoerter() {
        try {
            return query("SELECT * FROM file");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<File> getAlleFilesMitEMailId(int mid) {
        try {
            return query("SELECT * FROM file WHERE mid = ?", mid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteFile(int fid) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM file WHERE fid = ?", fid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean updateFile(File file)  {
        int ret = 0;
        try {
            ret = updateOrDelete("UPDATE file" +
                    " pfad = ? , mid = ? WHERE fid = ?",
                    file.getPfad(), file.getMid(), file.getFid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

}
