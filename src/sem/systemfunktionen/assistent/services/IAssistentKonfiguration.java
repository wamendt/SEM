package sem.systemfunktionen.assistent.services;

import sem.datenhaltung.semmodel.entities.*;

public interface IAssistentKonfiguration {
    boolean setAssiKonfig(AssiKonfig konfig);
    AssiKonfig getAssiKonfig();
    boolean setModel(String pfad);
}
