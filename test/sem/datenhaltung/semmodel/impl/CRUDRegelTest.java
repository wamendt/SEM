package sem.datenhaltung.semmodel.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CRUDRegelTest extends CRUDTest {

    private ICRUDRegel classUnderTest = ICRUDManagerSingleton.getIcrudRegelInstance();

    private void checkRegel(Regel expected, Regel actual){
        Assert.assertEquals(expected.getBeschreibung(), actual.getBeschreibung());
        Assert.assertEquals(expected.getKid(), actual.getKid());
        Assert.assertEquals(expected.getVonEmailAddress(), actual.getVonEmailAddress());
        Assert.assertEquals(expected.getZuOrdner(), actual.getZuOrdner());
        Assert.assertEquals(expected.getRid(), actual.getRid());
        Assert.assertEquals(expected.isActive(), actual.isActive());
    }

    /**
     * Erstellt eine Regel in der Datenbank und ueberprueft ob die Daten korrekt sind
     *
     */
    @Test
    void createRegel() {
        Regel regel = createRegelObject(0);
        int key = classUnderTest.createRegel(regel);

        Regel testRegel = getRegelVonDb(key);
        checkRegel(regel, testRegel);
    }

    /**
     * Loescht eine bereits vorhande Regel aus der Datenbank
     * Als Rueckgabe wird true erwartet und die Regel befindet sich nicht mehr in der Datenbank.
     */
    @Test
    void deleteRegel_00() {
        Regel regel = createRegelObject(0);
        insertRegel(regel);
        boolean test = classUnderTest.deleteRegel(regel.getRid());
        Regel testRegel = getRegelVonDb(regel.getRid());

        Assert.assertEquals(true, test);
        Assert.assertEquals(null, testRegel);
    }

    /**
     * Versucht eine nicht vorhanden Regel aus der Datenbank zu loeschen
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void deleteRegel_01() {
        Regel regel = createRegelObject(0);
        insertRegel(regel);
        deleteRegelVonDb(regel.getRid());

        boolean test = classUnderTest.deleteRegel(regel.getRid());

        Assert.assertEquals(false, test);
    }

    /**
     * Macht ein Update auf eine bereits in der DB vorhande Regel
     * Als Rueckgabe wird true erwartet und die Regel in der Db hat die korrekten Daten
     */
    @Test
    void updateRegel_00() {
        Regel regel = createRegelObject(0);
        insertRegel(regel);

        regel.setVonEmailAddress("UPDATE ORDER");
        regel.setZuOrdner("UPDATE ZUORDNER");
        regel.setKid(123);
        regel.setBeschreibung("UPDATE BESCHREIBUNG");
        regel.setActive(false);

        boolean test = classUnderTest.updateRegel(regel);
        Regel testRegel = getRegelVonDb(regel.getRid());

        assertEquals(true, test);
        checkRegel(regel, testRegel);
    }

    /**
     * Macht ein Update auf keine in der DB vorhande Regel
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void updateRegel_01() {
        Regel regel = createRegelObject(0);
        insertRegel(regel);
        deleteRegelVonDb(regel.getRid());

        regel.setVonEmailAddress("UPDATE ORDER");
        regel.setZuOrdner("UPDATE ZUORDNER");
        regel.setKid(123);
        regel.setBeschreibung("UPDATE BESCHREIBUNG");
        regel.setActive(false);

        boolean test = classUnderTest.updateRegel(regel);

        assertEquals(false, test);
    }

    /**
     * Eine Bereits vorhandene Regel wird aus der Datenbank geholt und ueberprueft ob die Daten korrekt sind.
     */
    @Test
    void getRegelById_00() {
        Regel regel = createRegelObject(0);
        insertRegel(regel);

        Regel testRegel = classUnderTest.getRegelById(regel.getRid());

        checkRegel(regel, testRegel);
    }

    /**
     * Es exestiert keine Regel in der Datenbank mit der angegebenen id, als Rueckgabe wird null erwartet
     */
    @Test
    void getRegelById_01() {
        Regel regel = createRegelObject(0);
        insertRegel(regel);
        deleteRegelVonDb(regel.getRid());

        Regel testRegel = classUnderTest.getRegelById(regel.getRid());
        assertEquals(null, testRegel);
    }

    /**
     *Es exestieren in der Datenbank bereis 2 regeln, die versucht werden zu holen.
     * Als Rueckgabe wird eine Liste mit diesen 2 Regeln erwartet.
     */
    @Test
    void getAlleRegeln_00() {
        try{
            connection.createStatement().execute("DELETE FROM regel");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Regel regel_1 = createRegelObject(0);
        Regel regel_2 = createRegelObject(1);
        insertRegel(regel_1);
        insertRegel(regel_2);

        ArrayList<Regel> testList = classUnderTest.getAlleRegeln();

        assertEquals(2, testList.size());
        checkRegel(regel_1, testList.get(0));
        checkRegel(regel_2, testList.get(1));
    }

    /**
     *Es exestieren keine regeln in der Datenbank, die versucht werden zu holen.
     * Als Rueckgabe wird eine leere Liste erwartet.
     */
    @Test
    void getAlleRegeln_01() {
        try{
            connection.createStatement().execute("DELETE FROM regel");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Regel> testList = classUnderTest.getAlleRegeln();

        assertEquals(0, testList.size());
    }

    /**
     * In der Datanbank exestiert ein Konto mit 2 zugehoerigen Regeln,
     * Als Rueckgabe wird eine Liste mit diesen 2 Regeln erwartet.
     */
    @Test
    void getRegelMitKontoId_00() {
        Konto konto =  createKontoObject();
        insertKonto(konto);
        Regel regel_1 = createRegelObject(konto.getKid());
        Regel regel_2 = createRegelObject(konto.getKid());
        insertRegel(regel_1);
        insertRegel(regel_2);

        ArrayList<Regel> testList = classUnderTest.getRegelMitKontoId(konto.getKid());

        assertEquals(2, testList.size());
        checkRegel(regel_1, testList.get(0));
        checkRegel(regel_2, testList.get(1));
    }
    /**
     * In der Datenbank exestieren keine Regeln mit den gesuchten Konto
     * Als Rueckgabe wird eine leere erwartet.
     */
    @Test
    void getRegelMitKontoId_01() {
        Konto konto =  createKontoObject();
        insertKonto(konto);
        Regel regel_1 = createRegelObject(konto.getKid());
        Regel regel_2 = createRegelObject(konto.getKid());
        insertRegel(regel_1);
        insertRegel(regel_2);

        ArrayList<Regel> testList = classUnderTest.getRegelMitKontoId(konto.getKid() + 1);/*Eine ID, die garantiert
        nicht exestiert*/

        assertEquals(0, testList.size());

    }
}