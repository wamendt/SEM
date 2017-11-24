package sem.systemfunktionen.assistent.services;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;

public interface IAssistentKonfiguration {
    boolean setAssiKonfig(AssistentKonfig konfig);
    AssistentKonfig getAssiKonfig();
    boolean setModel(String pfad);
}
