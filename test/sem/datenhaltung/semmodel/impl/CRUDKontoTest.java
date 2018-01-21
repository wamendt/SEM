package sem.datenhaltung.semmodel.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.services.ICRUDKonto;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;

import java.sql.SQLException;
import java.util.ArrayList;

class CRUDKontoTest extends CRUDTest{

    private ICRUDKonto classUnderTest = ICRUDManagerSingleton.getIcrudKontoInstance();

    private void checkKonto(Konto expected, Konto actual){
        Assert.assertEquals(expected.getKid(), actual.getKid());
        Assert.assertEquals(expected.getEmailAddress(), actual.getEmailAddress());
        Assert.assertEquals(expected.getPassWort(), actual.getPassWort());
        Assert.assertEquals(expected.getIMAPhost(), actual.getIMAPhost());
        Assert.assertEquals(expected.getSMTPhost(), actual.getSMTPhost());
        Assert.assertEquals(expected.getPort(), actual.getPort());
        Assert.assertEquals(expected.getUserName(), actual.getUserName());
        Assert.assertEquals(expected.getSignatur(), actual.getSignatur());
    }

    /**
     * Erstellt ein TestKonto in der Datenbank und ueberprueft ob die Daten korrekt sind
     */
    @Test
    void createKonto() {
        Konto konto = createKontoObject();

        int id = classUnderTest.createKonto(konto);

        Konto testkonto = getKontoVonDb(id);
        checkKonto(konto, testkonto);
    }

    /**
     * Ein bereits vorhandes Konto wird aus der Datenbank geloescht
     * Als Rueckgabe wird true erwartet und das Konto ist nicht mehr in der Datenbank
     */
    @Test
    void deleteKonto_00() {
        Konto konto = createKontoObject();
        insertKonto(konto);

        boolean test = classUnderTest.deleteKonto(konto.getKid());
        Konto testKonto = getKontoVonDb(konto.getKid());

        Assert.assertEquals(true, test);
        Assert.assertNull(testKonto);
    }

    /**
     * Ein nicht vorhandes Konto wird aus der Datenbank geloescht
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void deleteKonto_01() {
        Konto konto = createKontoObject();
        insertKonto(konto);
        deleteKontoVonDb(konto.getKid());

        boolean test = classUnderTest.deleteKonto(konto.getKid());

        Assert.assertEquals(false, test);
    }

    /**
     * Ein Konto mit falscher KID wird geloescht,
     * Als Rueckgabe wird false erwartet und Das Konto exestiert weriter in der DB
     */
    @Test
    void deleteKonto_02() {
        Konto konto = createKontoObject();
        insertKonto(konto);

        boolean test = classUnderTest.deleteKonto(konto.getKid() + 1); /*Eine ID die es garantiert nicht gibt*/
        Konto testKonto = getKontoVonDb(konto.getKid());

        Assert.assertEquals(false, test);
        checkKonto(konto, testKonto);

    }
    /**
     * Eine bereits vorhandenes Konto wird geupdatet
     * Als rueckgabe wird true erwartet und das Konto in der Datenbank hat die korrekten Daten
     */
    @Test
    void updateKonto_00() {
        Konto konto = createKontoObject();
        insertKonto(konto);

        konto.setEmailAddress("UPDATE EMAIL");
        konto.setPort(9876);
        konto.setSMTPhost("UPDATE SMTP");
        konto.setIMAPhost("UPDATE IMAP");
        konto.setPassWort("UPDATE PASSWORT");
        konto.setUserName("UPDATE USER");
        konto.setSignatur("UPDATE SIGNATUR");

        boolean test = classUnderTest.updateKonto(konto);
        Konto testKonto = getKontoVonDb(konto.getKid());

        Assert.assertEquals(true, test);
        checkKonto(konto, testKonto);
    }

    /**
     * Eine nicht vorhandenes Konto wird geupdatet
     * Als rueckgabe wird false erwartet
     */
    @Test
    void updateKonto_01(){
        Konto konto = createKontoObject();
        insertKonto(konto);
        deleteKontoVonDb(konto.getKid());

        konto.setEmailAddress("UPDATE EMAIL");
        konto.setPort(9876);
        konto.setSMTPhost("UPDATE SMTP");
        konto.setIMAPhost("UPDATE IMAP");
        konto.setPassWort("UPDATE PASSWORT");
        konto.setUserName("UPDATE USER");
        konto.setSignatur("UPDATE SIGNATUR");

        boolean test = classUnderTest.updateKonto(konto);

        Assert.assertEquals(false, test);
    }

    /**
     * Ein bereits in der Datenbank vorhandes Konto wird aus der DB geholt
     * und ueberprueft ob die Daten korrekt sind.
     */
    @Test
    void getKontoById_00() {
        Konto konto = createKontoObject();
        insertKonto(konto);

        Konto testKonto = classUnderTest.getKontoById(konto.getKid());

        checkKonto(konto, testKonto);
    }

    /**
     * Ein nicht in der Datenbank vorhandes Konto wird aus der DB geholt
     * Als Rueckgabe wird null erwartet
     */
    @Test
    void getKontoById_01() {
        Konto konto = createKontoObject();
        insertKonto(konto);
        deleteKontoVonDb(konto.getKid());

        Konto testKonto = classUnderTest.getKontoById(konto.getKid());

        Assert.assertEquals(null, testKonto);
    }


    /**
     * 2 Konten exestieren in der DB, die geholt werden
     * Als Rueckgabe wird eine Liste mit diesen 2 Konten erwartet.
     */
    @Test
    void getAlleKonten_00() {
        try {
            connection.createStatement().execute("DELETE FROM konto");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Konto konto_1 = createKontoObject();
        Konto konto_2 = createKontoObject();

        insertKonto(konto_1);
        insertKonto(konto_2);

        ArrayList<Konto> testList = classUnderTest.getAlleKonten();

        Assert.assertEquals(2, testList.size());
        checkKonto(konto_1, testList.get(0));
        checkKonto(konto_2, testList.get(1));
    }

    /**
     * kein Konto exestiert in der DB
     * Als Rueckgabe wird eine leere Liste erwartet.
     */
    @Test
    void getAlleKonten_01() {
        try {
            connection.createStatement().execute("DELETE FROM konto");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Konto> testList = classUnderTest.getAlleKonten();

        Assert.assertEquals(0, testList.size());
    }
}