package sem.systemfunktionen.assistent.impl;

import sem.systemfunktionen.assistent.services.IAssistentKonfiguration;
import sem.datenhaltung.semmodel.entities.*;

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
