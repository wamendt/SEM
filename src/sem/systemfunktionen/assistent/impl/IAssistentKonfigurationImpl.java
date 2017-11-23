package sem.systemfunktionen.assistent.impl;

import sem.systemfunktionen.assistent.services.IAssistentKonfiguration;
import sem.datenhaltung.semmodel.entities.*;

public class IAssistentKonfigurationImpl implements IAssistentKonfiguration {
    @Override
    public boolean setAssiKonfig(AssiKonfig konfig) {
        return false;
    }

    @Override
    public AssiKonfig getAssiKonfig() {
        return null;
    }

    @Override
    public boolean setModel(String pfad) {
        return false;
    }
}
