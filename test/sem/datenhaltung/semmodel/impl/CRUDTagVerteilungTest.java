package sem.datenhaltung.semmodel.impl;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.TagVerteilung;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTagVerteilung;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CRUDTagVerteilungTest extends CRUDTest {

    private ICRUDTagVerteilung classUnderTest = ICRUDManagerSingleton.getIcrudTagVerteilungInstance();

    private void checkTagVerteilung(TagVerteilung expected, TagVerteilung actual){
        assertEquals(expected.getVerteilung(), actual.getVerteilung());
        assertEquals(expected.getMid(), actual.getMid());
        assertEquals(expected.getTvid(), actual.getTvid());
    }

    /**
     * Erstellt eine TagVerteilung in der DB
     * Anschliessend ist eine TagVerteilung mit den korrekten Daten in der DB vorhanden
     */
    @Test
    void createTagVerteilung() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);

        int key = classUnderTest.createTagVerteilung(tagVerteilung);
        TagVerteilung testTagVerteilung = getTagVerteilungVonDb(key);

        checkTagVerteilung(tagVerteilung, testTagVerteilung);
    }

    /**
     * Loescht eine beereit svorhandene Tagverteilung aus der DB
     * Als Rueckgabe wird true erwartet und die Tagverteilung befindet sich nicht mehr in der DB
     */
    @Test
    void deleteTagVerteilung_00() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung);

        boolean test = classUnderTest.deleteTagVerteilung(tagVerteilung.getTvid());
        TagVerteilung testTagVerteilung = getTagVerteilungVonDb(tagVerteilung.getTvid());

        assertEquals(true, test);
        assertEquals(null, testTagVerteilung);
    }

    /**
     * Versucht eine nichtvorhande Tagverteilung aus der DB zu loeschen
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void deleteTagVerteilung_01() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung);
        deleteTagVerteilungVonDb(tagVerteilung.getTvid());

        boolean test = classUnderTest.deleteTagVerteilung(tagVerteilung.getTvid());

        assertEquals(false, test);
    }

    /**
     * Es Exestieren bereits 2 TagVerteilungen in der Db die geloescht werden,
     * Als Rueckgabe wird eine 2 erwartet und die Tagverteilungen befinden sich nicht mehr in der DB.
     */
    @Test
    void deleteAlleTagVerteilungen() {
        try{
            connection.createStatement().execute("DELETE FROM tagverteilung");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TagVerteilung tagVerteilung_1 = createTagVerteilungObject(0);
        TagVerteilung tagVerteilung_2 = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung_1);
        insertTagVerteilung(tagVerteilung_2);

        int test = classUnderTest.deleteAlleTagVerteilungen();
        TagVerteilung testTagVerteilung_1 = getTagVerteilungVonDb(tagVerteilung_1.getTvid());
        TagVerteilung testTagVerteilung_2 = getTagVerteilungVonDb(tagVerteilung_2.getTvid());

        assertEquals(2, test);
        assertEquals(null, testTagVerteilung_1);
        assertEquals(null, testTagVerteilung_2);
    }

    /**
     * Auf eine Bereits in der DB vorhandene TagVerteilung wird ein Update durchgefuehrt
     * Als Rueckgabe wird true erwartet und die TagVerteilung hat in der DB die korrekten Daten.
     */
    @Test
    void updateTagVerteilung_00() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung);

        tagVerteilung.setVerteilung(1);
        tagVerteilung.setMid(1);

        boolean test = classUnderTest.updateTagVerteilung(tagVerteilung);
        TagVerteilung testTagVerteilung = getTagVerteilungVonDb(tagVerteilung.getTvid());

        assertEquals(true, test);
        checkTagVerteilung(tagVerteilung, testTagVerteilung);
    }

    /**
     * Auf eine nicht vorhandene TagVerteilung in der DB wird ein update durchgefuehrt
     * Als Rueckgabe wird false erwatet
     */
    @Test
    void updateTagVerteilung_01() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung);
        deleteTagVerteilungVonDb(tagVerteilung.getTvid());

        tagVerteilung.setVerteilung(1);
        tagVerteilung.setMid(1);

        boolean test = classUnderTest.updateTagVerteilung(tagVerteilung);

        assertEquals(false, test);
    }

    /**
     * Es exestieren bereits eine TagVerteilung in der Db, die geholt wird
     * Als Rueckgabe wird die TagVerteilung mit den Korrekten Daten erwartet
     */
    @Test
    void getTagVerteilungById_00() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung);

        TagVerteilung testTagVerteilung = classUnderTest.getTagVerteilungById(tagVerteilung.getTvid());
        checkTagVerteilung(tagVerteilung, testTagVerteilung);
    }

    /**
     * Es exestieren keine TagVerteilung in der Db, die geholt wird
     * Als Rueckgabe wird null erwartet
     */
    @Test
    void getTagVerteilungById_01() {
        TagVerteilung tagVerteilung = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung);

        TagVerteilung testTagVerteilung = classUnderTest.getTagVerteilungById(tagVerteilung.getTvid());
        checkTagVerteilung(tagVerteilung, testTagVerteilung);
    }

    /**
     * Es exestiert zwei TagVerteilung mit einer zugehoeringen Email
     * Als Rueckgabe wird eine Liste dieser 2 Tags erwartet und den Korrekten Daten
     */
    @Test
    void getTagVerteilungByEmailId_00() {
        try{
            connection.createStatement().execute("DELETE FROM tagverteilung");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        TagVerteilung tagVerteilung_1 = createTagVerteilungObject(eMail.getMid());
        TagVerteilung tagVerteilung_2 = createTagVerteilungObject(eMail.getMid());
        insertTagVerteilung(tagVerteilung_1);
        insertTagVerteilung(tagVerteilung_2);

        ArrayList<TagVerteilung> testList = classUnderTest.getTagVerteilungByEmailId(eMail.getMid());
        assertEquals(2, testList.size());
        checkTagVerteilung(tagVerteilung_1, testList.get(0));
        checkTagVerteilung(tagVerteilung_2, testList.get(1));

    }
    /**
     * Es exestieren keine TagVerteilungen mit der gesuchten Email
     * Als Rueckgabe wird eine leere Liste erwartet
     */
    @Test
    void getTagVerteilungByEmailId_01() {
        try{
            connection.createStatement().execute("DELETE FROM tagverteilung");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        TagVerteilung tagVerteilung_1 = createTagVerteilungObject(eMail.getMid());
        TagVerteilung tagVerteilung_2 = createTagVerteilungObject(eMail.getMid());
        insertTagVerteilung(tagVerteilung_1);
        insertTagVerteilung(tagVerteilung_2);

        ArrayList<TagVerteilung> testList = classUnderTest.getTagVerteilungByEmailId(eMail.getMid()+1);/*einzigartige mid*/
        assertEquals(0, testList.size());
    }

    /**
     * Es exestieren bereis 2 TagVerteilungen in der DB, die geholt werden
     * Als rueckgabe wird eine Liste mit diesen 2 TagVerteilungen erwartet mit den korrekten Daten
     */
    @Test
    void getAlleTagVerteilungen_00() {
        try{
            connection.createStatement().execute("DELETE FROM tagverteilung");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TagVerteilung tagVerteilung_1 = createTagVerteilungObject(0);
        TagVerteilung tagVerteilung_2 = createTagVerteilungObject(0);
        insertTagVerteilung(tagVerteilung_1);
        insertTagVerteilung(tagVerteilung_2);

        ArrayList<TagVerteilung> testList = classUnderTest.getAlleTagVerteilungen();
        assertEquals(2, testList.size());
        checkTagVerteilung(tagVerteilung_1, testList.get(0));
        checkTagVerteilung(tagVerteilung_2, testList.get(1));
    }

    /**
     * Es exestieren keineTagVerteilungen in der DB, die geholt werden
     * Als rueckgabe wird eine leere Liste erwartet
     */
    @Test
    void getAlleTagVerteilungen() {
        try{
            connection.createStatement().execute("DELETE FROM tagverteilung");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<TagVerteilung> testList = classUnderTest.getAlleTagVerteilungen();
        assertEquals(0, testList.size());
    }
}