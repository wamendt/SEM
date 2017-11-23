package sem.datenhaltung.assistentdaten.impl;

import sem.datenhaltung.assistentdaten.services.IAssistentService;
import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.datenhaltung.semmodel.entities.Regel;
import sem.datenhaltung.semmodel.entities.Tag;

import java.util.ArrayList;

public class IAssistentServiceImpl implements IAssistentService {
    @Override
    public boolean createAssistentKonfig(AssistentKonfig konfig) {
        return false;
    }

    @Override
    public boolean deleteAssistentKonfig(AssistentKonfig konfig) {
        return false;
    }

    @Override
    public boolean updateAssistentKonfig(AssistentKonfig konfig) {
        return false;
    }

    @Override
    public AssistentKonfig getAssistentKonfigById(int id) {
        return null;
    }

    @Override
    public ArrayList<AssistentKonfig> getAlleAssistentKonfigs() {
        return null;
    }

    @Override
    public boolean createRegel(Regel regel) {
        return false;
    }

    @Override
    public boolean deleteRegel(Regel regel) {
        return false;
    }

    @Override
    public boolean updateRegel(Regel regel) {
        return false;
    }

    @Override
    public Regel getRegelById(int id) {
        return null;
    }

    @Override
    public ArrayList<Regel> getAlleRegeln() {
        return null;
    }

    @Override
    public boolean addRollback(int emailId, int tagId) {
        return false;
    }

    @Override
    public boolean deleteRollback() {
        return false;
    }

    @Override
    public ArrayList<Tag> getAlleTags() {
        return null;
    }
}
