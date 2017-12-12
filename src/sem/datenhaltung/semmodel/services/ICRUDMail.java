package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.EMail;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDMail {
    int createEMail(EMail email) throws IOException, SQLException;
    EMail getEMailById(int mid) throws IOException, SQLException;
    ArrayList<EMail> getEMailByOrdner(String name) throws IOException, SQLException;
    ArrayList<EMail> getEMailByTag(int tid) throws IOException, SQLException;
    ArrayList<EMail> getAlleEMails() throws IOException, SQLException;
    ArrayList<EMail> getAlleEMailsMitTagId(int tid) throws IOException, SQLException ;
    boolean deleteEMail(int mid) throws IOException, SQLException ;
    boolean updateEMail(EMail email) throws IOException, SQLException;
    EMail getEMailByMessageIDUndOrdner(int id, String ordner) throws IOException, SQLException;
    EMail checkMessageInDB(int id, String betreff, String absender, String ordner) throws IOException, SQLException;
    boolean deleteEMailByFolder(String folder) throws IOException, SQLException ;
    boolean createEMailTable() throws IOException, SQLException ;
    boolean createFileTable() throws IOException, SQLException ;
    boolean createAdresseTable() throws IOException, SQLException ;
    boolean createTagTable() throws IOException, SQLException ;
    boolean createWortTable() throws IOException, SQLException ;
    boolean deleteTable(String tableName) throws IOException, SQLException ;
    ArrayList<EMail> searchEMail(String suchwort) throws IOException, SQLException;
}
