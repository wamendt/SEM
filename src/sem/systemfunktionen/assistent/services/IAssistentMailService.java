package sem.systemfunktionen.assistent.services;

import sem.datenhaltung.semmodel.entities.Tag;
import sem.fachlogik.grenzklassen.EMailGrenz;

import java.util.ArrayList;

public interface IAssistentMailService {
    Tag analysiereEmail(EMailGrenz eMail);
    void trainiereSEM(ArrayList<EMailGrenz> emails);
}
