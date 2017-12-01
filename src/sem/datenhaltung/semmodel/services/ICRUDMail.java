package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.EMail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDMail {
    int createEMail(EMail email) throws IOException, SQLException;
    EMail getEMailById(int mid) throws IOException, SQLException;
    ArrayList<EMail> getAlleEMails() throws IOException, SQLException;
    ArrayList<EMail> getAlleEMailsMitTagId(int tid) throws IOException, SQLException ;
    boolean deleteEMail(int mid) throws IOException, SQLException ;
    boolean updateEMail(EMail email) throws IOException, SQLException;
    public EMail getEMailByMessageInhalt(String content) throws IOException, SQLException;
}
