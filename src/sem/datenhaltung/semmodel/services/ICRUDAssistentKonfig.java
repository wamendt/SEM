package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.datenhaltung.semmodel.entities.Regel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDAssistentKonfig{

    boolean createAssistentKonfig(AssistentKonfig konfig) throws IOException, SQLException;
    boolean deleteAssistentKonfig(int aid) throws IOException, SQLException;
    boolean updateAssistentKonfig(AssistentKonfig konfig) throws IOException, SQLException;
    AssistentKonfig getAssistentKonfig(int aid) throws IOException, SQLException;
    ArrayList<AssistentKonfig> getAlleAssistentKonfig() throws IOException, SQLException;
    boolean createRegel(Regel regel) throws IOException, SQLException;
    boolean deleteRegel(int rid) throws IOException, SQLException;
    boolean updateRegel(Regel regel) throws IOException, SQLException;
    Regel getRegelById(int rid) throws IOException, SQLException;
    ArrayList<Regel> getAlleRegeln() throws IOException, SQLException;
    boolean addRollback(int emailId, int tagId) throws IOException, SQLException;
    boolean deleteRollback() throws IOException, SQLException;

}
