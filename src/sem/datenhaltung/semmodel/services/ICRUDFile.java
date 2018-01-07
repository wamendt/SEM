package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.File;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDFile {
    int createFile(File file);

    File getFileById(int fid);

    ArrayList<File> getAlleWoerter();

    ArrayList<File> getAlleFilesMitEMailId(int mid);

    boolean deleteFile(int fid);

    boolean updateFile(File file);
}
