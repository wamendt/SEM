package sem.fachlogik.assistentsteuerung.services;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;

public interface IAssistentMailService {
    Tag analysiereEmail(EMail email);
}
