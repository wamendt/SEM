package sem.systemfunktionen.assistent.services;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;

public interface IAssistentMailService {
    Tag analysiereEmail(EMail eMail);
}
