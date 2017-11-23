package sem.datenhaltung.assistentdaten.services;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.datenhaltung.semmodel.entities.Regel;
import sem.datenhaltung.semmodel.entities.Tag;

import java.util.ArrayList;

public interface IAssistentService {
    boolean createAssistentKonfig(AssistentKonfig konfig);
    boolean deleteAssistentKonfig(AssistentKonfig konfig);
    boolean updateAssistentKonfig(AssistentKonfig konfig);
    AssistentKonfig getAssistentKonfigById(int id);
    ArrayList<AssistentKonfig> getAlleAssistentKonfigs();

    boolean createRegel(Regel regel);
    boolean deleteRegel(Regel regel);
    boolean updateRegel(Regel regel);
    Regel getRegelById(int id);
    ArrayList<Regel> getAlleRegeln();
    boolean addRollback(int emailId, int tagId);
    boolean deleteRollback();
    ArrayList<Tag> getAlleTags();

}
