package sem.systemfunktionen.assistent.impl;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.systemfunktionen.assistent.services.IAssistentKonfiguration;

public class IAssistentKonfigurationImpl implements IAssistentKonfiguration {


    @Override
    public boolean setAssistentKonfig(AssistentKonfig konfig) {
        return false;
    }

    @Override
    public AssistentKonfig getAssistentKonfig() {
        return null;
    }

    @Override
    public boolean setModel(String pfad) {
        return false;
    }

    @Override
    public boolean saveModel(String pfad) {
        return false;
    }

    @Override
    public boolean loadModel(String pfad) {
        return false;
    }
}
