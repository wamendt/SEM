package sem.systemfunktionen.assistent.services;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;

public interface IAssistentKonfiguration {
    boolean setAssistentKonfig(AssistentKonfig konfig);
    AssistentKonfig getAssistentKonfig();
    boolean setModel(String pfad);
    boolean saveModel(String pfad);
    boolean loadModel(String pfad);

}
