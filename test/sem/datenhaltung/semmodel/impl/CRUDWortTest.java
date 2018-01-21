package sem.datenhaltung.semmodel.impl;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDWort;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CRUDWortTest extends CRUDTest{

    private ICRUDWort classUnderTest = ICRUDManagerSingleton.getIcrudWordInstance();

    private void checkWort(Wort expected, Wort actual){
        assertEquals(expected.getWort(), actual.getWort());
        assertEquals(expected.getTid(), actual.getTid());
        assertEquals(expected.getWid(), actual.getWid());
    }
    /**
     * Erstellt ein Wort in die Datenbank. Anschliessend ist das Wort mit den korrekten Daten in der DB
     */
    @Test
    void createWort() {
        Wort wort = createWortObject(0);
        int key = classUnderTest.createWort(wort);

        Wort testWort = getWortVonDb(key);
        checkWort(wort, testWort);
    }

    /**
     * Es exestiert ein Wort in der Datenbank, welches geholt wird
     * Als Rueckgabe wird das Wort mit den korrekten Daten erwartet.
     */
    @Test
    void getWortById_00() {
        Wort wort = createWortObject(0);
        insertWort(wort);

        Wort testWort = classUnderTest.getWortById(wort.getWid());
        checkWort(wort, testWort);
    }

    /**
     * Es exestiert kein Wort in der Datenbank
     * Als Rueckgabe wird null erwartet
     */
    @Test
    void getWortById_01() {
        Wort wort = createWortObject(0);
        insertWort(wort);
        deleteWortVonDb(wort.getWid());

        Wort testWort = classUnderTest.getWortById(wort.getWid());
        assertEquals(null, testWort);
    }

    /**
     * Es Exestieren 2 Woerter in der DB, die geholt werden
     * Als Rueckgabe wird eine Liste mit diesen 2 Woertern erwartet
     */
    @Test
    void getAlleWoerter_00() {
        try{
            connection.createStatement().execute("DELETE FROM wort");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Wort w1 = createWortObject(0);
        Wort w2 = createWortObject(0);
        insertWort(w1);
        insertWort(w2);

        ArrayList<Wort> testList = classUnderTest.getAlleWoerter();
        assertEquals(2, testList.size());
        checkWort(w1, testList.get(0));
        checkWort(w2, testList.get(1));

    }


    /**
     * Es Exestieren  keine Woerter in der DB
     * Als Rueckgabe wird eine leere Liste erwartet
     */
    @Test
    void getAlleWoerter_01() {
        try{
            connection.createStatement().execute("DELETE FROM wort");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Wort> testList = classUnderTest.getAlleWoerter();
        assertEquals(0, testList.size());


    }

    /**
     * Es Exestiert 2 Woerter mit einem Tag in der Datenbank
     * Als Rueckgabe wird eine Liste erwartet mit diesen 2 Woertern
     */
    @Test
    void getAlleWoerterMitTagId_00() {
        try{
            connection.createStatement().execute("DELETE FROM wort");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Tag tag = createAndInsertTag();
        Wort w1 = createWortObject(tag.getTid());
        Wort w2 = createWortObject(tag.getTid());
        Wort w3 = createWortObject(0);
        insertWort(w1);
        insertWort(w2);
        insertWort(w3);

        ArrayList<Wort> testList = classUnderTest.getAlleWoerterMitTagId(tag.getTid());
        assertEquals(2, testList.size());
        checkWort(w1, testList.get(0));
        checkWort(w2, testList.get(1));
    }

    /**
     * Es Exestieren keine Woerter mit einem Tag in der Datenbank
     * Als Rueckgabe wird eine leere Liste erwartet
     */
    @Test
    void getAlleWoerterMitTagId_01() {
        try{
            connection.createStatement().execute("DELETE FROM wort");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Tag tag = createAndInsertTag();
        Wort w1 = createWortObject(tag.getTid());
        Wort w2 = createWortObject(tag.getTid());
        insertWort(w1);
        insertWort(w2);
        ArrayList<Wort> testList = classUnderTest.getAlleWoerterMitTagId(tag.getTid()+1); /*einzigartige tagid*/
        assertEquals(0, testList.size());
    }

    /**
     * Ein bereis vorhandes Wort in der Datenbank wird geloescht
     * Als Rueckgabe wird true erwartet und das Wort befindet sich nicht mehr in DB
     */
    @Test
    void deleteWort_00() {
        Wort wort = createWortObject(0);
        insertWort(wort);

        boolean test = classUnderTest.deleteWort(wort.getWid());
        Wort testWort = getWortVonDb(wort.getWid());
        assertEquals(true, test);
        assertEquals(null, testWort);
    }


    /**
     * kein vorhandes Wort in der Datenbank wird geloescht
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void deleteWort_01() {
        Wort wort = createWortObject(0);
        insertWort(wort);
        deleteWortVonDb(wort.getWid());

        boolean test = classUnderTest.deleteWort(wort.getWid());
        assertEquals(false, test);
    }

    /**
     * Es Exestieren 2 Woerter mit einem Tag in der Datenbank die geloescht werden,
     * Als Rueckgabe wird 2 erwartet und die Woerter befinden sich nicht mehr in der DB
     */
    @Test
    void deleteAlleWoerterMitTagId_00() {
        try{
            connection.createStatement().execute("DELETE FROM wort");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Tag tag = createAndInsertTag();
        Wort w1 = createWortObject(tag.getTid());
        Wort w2 = createWortObject(tag.getTid());
        insertWort(w1);
        insertWort(w2);

        int test = classUnderTest.deleteAlleWoerterMitTagId(tag.getTid());
        Wort testWort1 = getWortVonDb(w1.getWid());
        Wort testWort2 = getWortVonDb(w2.getWid());

        assertEquals(2, test);
        assertEquals(null, testWort1);
        assertEquals(null, testWort2);
    }

    /**
     * Es Exestieren keine Woerter mit einem Tag in der Datenbank die geloescht werden,
     * Als Rueckgabe wird 0 erwartet und die Woerter befinden sich noch in der DB
     */
    @Test
    void deleteAlleWoerterMitTagId_01() {
        try{
            connection.createStatement().execute("DELETE FROM wort");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Tag tag = createAndInsertTag();
        Wort w1 = createWortObject(tag.getTid());
        Wort w2 = createWortObject(tag.getTid());
        insertWort(w1);
        insertWort(w2);

        int test = classUnderTest.deleteAlleWoerterMitTagId(tag.getTid() + 1);

        Wort testWort1 = getWortVonDb(w1.getWid());
        Wort testWort2 = getWortVonDb(w2.getWid());

        assertEquals(0, test);
        checkWort(w1, testWort1);
        checkWort(w2, testWort2);
    }

    /**
     * Ein bereits in der DB vorhandes Wort wird geupdatet
     * Als Rueckgabe wird true erwartet und das Wort in der DB hat die korrekten Daten
     */
    @Test
    void updateWort_00() {
        Wort w = createWortObject(0);
        insertWort(w);

        w.setWort("UPDATE WORT");
        w.setTid(1235467);

        boolean test = classUnderTest.updateWort(w);
        Wort testWort = getWortVonDb(w.getWid());

        assertEquals(true, test);
        checkWort(w, testWort);
    }

    /**
     * Kein in der DB vorhandes Wort wird geupdatet
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void updateWort_01() {
        Wort w = createWortObject(0);
        insertWort(w);
        deleteWortVonDb(w.getWid());

        w.setWort("UPDATE WORT");
        w.setTid(1235467);

        boolean test = classUnderTest.updateWort(w);
        Wort testWort = getWortVonDb(w.getWid());

        assertEquals(false, test);
    }
}