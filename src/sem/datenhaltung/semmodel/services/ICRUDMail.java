package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.EMail;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDMail {
    int createEMail(EMail email);
    EMail getEMailById(int mid);
    ArrayList<EMail> getEMailByOrdner(String name);
    ArrayList<EMail> getEMailByTag(int tid);
    ArrayList<EMail> getAlleEMails();
    ArrayList<EMail> getAlleEMailsMitTagId(int tid);
    boolean deleteEMail(int mid);
    boolean updateEMail(EMail email);
    EMail getEMailByMessageIDUndOrdner(int id, String ordner);
    EMail checkMessageInDB(int id, String betreff, String absender, String ordner);
    boolean deleteEMailByFolder(String folder) ;
    boolean createEMailTable();
    boolean createFileTable() ;
    boolean createAdresseTable();
    boolean createTagTable() ;
    boolean createWortTable()  ;
    boolean deleteTable(String tableName);
    ArrayList<EMail> searchEMail(String suchwort);
}
