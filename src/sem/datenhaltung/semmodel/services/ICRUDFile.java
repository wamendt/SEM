package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.File;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDFile {
    int createFile(File file) throws IOException, SQLException;

    File getFileById(int fid) throws IOException, SQLException;

    ArrayList<File> getAlleWoerter() throws IOException, SQLException ;

    ArrayList<File> getAlleFilesMitEMailId(int mid) throws IOException, SQLException;

    boolean deleteFile(int fid) throws IOException, SQLException ;

    boolean updateFile(File file) throws IOException, SQLException ;
}
