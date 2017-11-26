package sem.systemfunktionen.assistent.impl;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.systemfunktionen.assistent.services.IAssistentKonfiguration;

public class IAssistentKonfigurationImpl implements IAssistentKonfiguration {
    @Override
    public boolean setAssiKonfig(AssistentKonfig konfig) {
        return false;
    }

    @Override
    public AssistentKonfig getAssiKonfig() {
        return null;
    }

    @Override
    public boolean setModel(String pfad) {
        return false;
    }
}
