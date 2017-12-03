package sem.fachlogik.assistentsteuerung.impl;

import org.junit.jupiter.api.Test;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.assistentsteuerung.core.Assistent2;

import java.io.IOException;
import java.sql.SQLException;

class IAssistentSteuerungImplTest {
    @Test
    void trainiereVorhandenSEM() throws IOException, SQLException {
        Assistent2 assistent = Assistent2.getInstance();
        //assistent.loadModel();
        //assistent.setNumIterations(1000);
        //assistent.saveModel();

         //assistent.makeModel(10,1.0,0.01);
        //assistent.saveModel();
        IAssistentSteuerung assistentSteuerung = new IAssistentSteuerungImpl();
        assistentSteuerung.trainiereSEM(10,1.0,0.01);
        //assistent.setNumTopics(15);
        //assistent.saveModel();

       // assistentSteuerung.trainiereVorhandenSEM();
    }

}