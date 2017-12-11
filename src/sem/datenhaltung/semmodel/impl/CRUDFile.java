package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.File;
import sem.datenhaltung.semmodel.services.ICRUDFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDFile extends DBCRUDTeamplate<File> implements ICRUDFile{


    @Override
    protected File makeObject(ResultSet rs) throws SQLException, IOException {
        File file = new File();

        file.setFid(rs.getInt(1));
        file.setPfad(rs.getString(2));
        file.setMId(rs.getInt(3));

        return file;
    }

    @Override
    public int createFile(File file) throws IOException, SQLException {
        String sql = "INSERT INTO file (pfad, mid)" +
                "VALUES (?, ?)";
        return insertAndReturnKey(sql, file.getPfad(), file.getMid());
    }

    @Override
    public File getFileById(int fid) throws IOException, SQLException {
        ArrayList<File> files = query("SELECT * FROM file WHERE fid=?", fid);
        return files.size() > 0 ? files.get(0) : null;
    }

    @Override
    public ArrayList<File> getAlleWoerter() throws IOException, SQLException {
        return query("SELECT * FROM file");
    }

    @Override
    public ArrayList<File> getAlleFilesMitEMailId(int mid) throws IOException, SQLException {
        return query("SELECT * FROM file WHERE mid = ?", mid);
    }

    @Override
    public boolean deleteFile(int fid) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROM file WHERE fid = ?", fid);
        return ret == 1;
    }

    @Override
    public boolean updateFile(File file) throws IOException, SQLException {
        int ret = updateOrDelete("UPDATE file" +
                " pfad = ? , mid = ? WHERE fid = ?",
                file.getPfad(), file.getMid(), file.getFid());
        return ret == 1;
    }

}
