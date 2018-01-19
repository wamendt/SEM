package sem.fachlogik.assistentsteuerung.services;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.fachlogik.assistentsteuerung.core.Assistent2;
import sem.fachlogik.assistentsteuerung.impl.IAssistentMailServiceImpl;
import sem.fachlogik.assistentsteuerung.impl.IAssistentSteuerungImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class IAssistentSteuerungTest {

    @org.junit.jupiter.api.Test
    void trainiereSEM() {
        IAssistentSteuerung steuerung = new IAssistentSteuerungImpl();
        steuerung.trainiereSEM(20, 0.01, 0.01, 2500);

        EMail testmail = new EMail();
        testmail.setInhalt("INHALT ZUM TESTEN");
        int id = ICRUDManagerSingleton.getIcrudMailInstance().createEMail(testmail);
        IAssistentMailService mailService = new IAssistentMailServiceImpl();
        Tag tag = mailService.analysiereEmail(testmail);
    }
}