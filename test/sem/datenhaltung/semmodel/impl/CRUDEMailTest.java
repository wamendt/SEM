package sem.datenhaltung.semmodel.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;

import java.sql.SQLException;
import java.util.ArrayList;

class CRUDEMailTest extends CRUDTest {

    private static ICRUDMail classUnderTest = ICRUDManagerSingleton.getIcrudMailInstance();

    private void checkEmail(EMail expected, EMail actual){
        Assert.assertEquals(expected.getMid(), actual.getMid());
        Assert.assertEquals(expected.getBetreff(), actual.getBetreff());
        Assert.assertEquals(expected.getInhalt(), actual.getInhalt());
        Assert.assertEquals(expected.getTid(), actual.getTid());
        Assert.assertEquals(expected.getAbsender(), actual.getAbsender());
        Assert.assertEquals(expected.getCc(), actual.getCc());
        Assert.assertEquals(expected.getEmpfaenger(), actual.getEmpfaenger());
        Assert.assertEquals(expected.getContentOriginal(), actual.getContentOriginal());
        Assert.assertEquals(expected.getZustand(), actual.getZustand());
        Assert.assertEquals(expected.getOrdner(), actual.getOrdner());
        Assert.assertEquals(expected.getKid(), actual.getKid());
    }

    /**
     * Erstellt eine TestEMail in der Datenbank und ueberprueft ob die Daten korrekt sind
     */
    @Test
    void createEMail_00() {
        Tag testTag = createAndInsertTag();
        EMail email = createEMailObject(testTag.getTid(),0);
        int id = classUnderTest.createEMail(email);
        EMail testMail = getEMailVonDb(id);
        checkEmail(email, testMail);
    }


    /**
     * Eine Bereits in der Datenbank vorhande Email wird aus der DB geholt und ueberprueft ob die Daten korrekt sind.
     */
    @Test
    void getEMailById_00() {
        EMail eMail = createEMailObject(0, 0);
        insertEmail(eMail);

        EMail testEMail = classUnderTest.getEMailById(eMail.getMid());

        checkEmail(eMail, testEMail);
    }

    /**
     * Eine Email die nicht in der Datenbank ist, wird geholt.
     * Als Rueckgabe wird null erwartet
     */
    @Test
    void getEMailById_01(){
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);
        deleteEMailVonDb(eMail.getMid());
        EMail testemail = classUnderTest.getEMailById(eMail.getMid());
        Assert.assertEquals(null, testemail);
    }

    /**
     * eine in der Datenbank bereis vorhande Emails mit einem ordner TEST ORDNER werden aus der datenbank geholt
     * Als Rueckgabe wird eine Liste mit einer Email mit den korrekten Daten erwartet
     */
    @Test
    void getEMailByOrdner_00() {
        Konto konto = createKontoObject();
        insertKonto(konto);
        EMail eMail1 = createEMailObject(0, konto.getKid());
        insertEmail(eMail1);

        ArrayList<EMail> testListe = classUnderTest.getEMailByOrdner(konto.getKid(), eMail1.getOrdner());
        Assert.assertEquals(1, testListe.size());
        checkEmail(testListe.get(0), eMail1);
    }

    /**
     * Keine Email in der Datenbank hat einen ordner TEST ORDNER
     * Als Rueckgabe wird eine Leere Liste erwartet
     */
    @Test
    void getEMailByOrdner_01(){

        ArrayList<EMail> testliste = classUnderTest.getEMailByOrdner(0,"TEST ORDNER");

        Assert.assertEquals(true, testliste.isEmpty());
    }


    /**
     * eine in der Datenbank bereis vorhande Email mit einem Tag werden aus der datenbank geholt
     * Als Rueckgabe wird eine Liste mit einer Email mit den korrekten Daten erwartet
     */
    @Test
    void getEMailByTag_00() {
        Tag tag = createAndInsertTag();
        EMail eMail = createEMailObject(tag.getTid(), 0);
        insertEmail(eMail);
        ArrayList<EMail> testlist = classUnderTest.getEMailByTag(tag.getTid());
        Assert.assertEquals(1, testlist.size());
        checkEmail(eMail, testlist.get(0));
    }

    /**
     * keine Email in der Datanbank hat den gesuchten Tag,
     * Als Rueckgabe wird eine leere Liste erwartet.
     */
    @Test
    void getEMailByTag_01() {
        Tag tag = createAndInsertTag();
        EMail eMail = createEMailObject(tag.getTid() + 1, 0);//FKey der garantiert noch nicht exesistiert.
        insertEmail(eMail);
        ArrayList<EMail> testlist = classUnderTest.getEMailByTag(tag.getTid());
        Assert.assertEquals(0, testlist.size());

    }

    /**
     *  Eine Email mit einer id, einer einem betreff TEST BETREFF , einem absender TEST ABSENDER
     *  und einem ordner TEST ORDNER exesitiert breits in der DB
     *  als Rueckgabe soll die Email gefunden werden mit den korrekten Daten
     */
    @Test
    void checkMessageInDB_00() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);
        EMail testMail = classUnderTest.checkMessageInDB(eMail.getMessageID(), eMail.getBetreff(), eMail.getAbsender(), eMail.getOrdner());

        Assert.assertNotNull(testMail);
        checkEmail(eMail, testMail);
    }

    /**
     *  Eine Email mit einer id, einer einem betreff TEST BETREFF , einem absender TEST ABSENDER
     *  und einem ordner TEST FALSCHER ORDNER exesitiert breits in der DB
     *  als Rueckgabe soll null kommen, da keine Email gefunden wurde
     */
    @Test
    void checkMessageInDB_01(){
        EMail eMail = createEMailObject(0,0);

        String ordnerOrg = eMail.getOrdner();
        eMail.setOrdner("TEST FALSCHER ORDNER");
        insertEmail(eMail);
        EMail testMail = classUnderTest.checkMessageInDB(eMail.getMid(), eMail.getBetreff(), eMail.getAbsender(), ordnerOrg);

        Assert.assertEquals(null, testMail);
    }

    /**
     *  Eine Email mit einer id, einer einem betreff TEST BETREFF , einem absender  TEST FALSCHER ABSENDER
     *  und einem ordner TEST ORDNER exesitiert breits in der DB
     *  als Rueckgabe soll null kommen, da keine Email gefunden wurde
     */
    @Test
    void checkMessageInDB_02(){
        EMail eMail = createEMailObject(0,0);

        String absenderOrg = eMail.getAbsender();
        eMail.setAbsender("TEST FALSCHER ABSENDER");
        insertEmail(eMail);

        EMail testMail = classUnderTest.checkMessageInDB(eMail.getMid(), eMail.getBetreff(), absenderOrg, eMail.getOrdner());

        Assert.assertEquals(null, testMail);
    }

    /**
     *  Eine Email mit einer id, einer einem betreff TEST FALSCHER BETREFF , einem absender  TEST ABSENDER
     *  und einem ordner TEST ORDNER exesitiert breits in der DB
     *  als Rueckgabe soll null kommen, da keine Email gefunden wurde, da nach TEST BETREFF gesucht wurde
     */
    @Test
    void checkMessageInDB_03(){
        EMail eMail = createEMailObject(0,0);

        String betreffOrg = eMail.getBetreff();
        eMail.setBetreff("TEST FALSCHER BETREFF");
        insertEmail(eMail);

        EMail testMail = classUnderTest.checkMessageInDB(eMail.getMid(), betreffOrg, eMail.getAbsender(), eMail.getOrdner());

        Assert.assertEquals(null, testMail);
    }

    /**
     * In der Datenbank exestieren 2 Email, Als Rueckgabe wird eine Liste mit diessen 2 Email erwartet
     */
    @Test
    void getAlleEMails_00() {
        try {
            connection.createStatement().execute("DELETE FROM email");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        EMail eMail_1 = createEMailObject(0,0);
        EMail eMail_2 = createEMailObject(0,0);

        insertEmail(eMail_1);
        insertEmail(eMail_2);

        ArrayList<EMail> testList = classUnderTest.getAlleEMails();

        Assert.assertEquals(2, testList.size());
        checkEmail(eMail_1, testList.get(0));
        checkEmail(eMail_2, testList.get(1));
    }

    /**
     * In der Datenbank exestieren keine Emails, Als Rueckgabe wird eine leere Liste erwartet
     */
    @Test
    void getAlleEMails_01() {
        try {
            connection.createStatement().execute("DELETE FROM email");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<EMail> testList = classUnderTest.getAlleEMails();

        Assert.assertEquals(0, testList.size());
    }

    /**
     * Eine Email exestiert bereits in der Datenbank und wird geloescht,
     * Als rueckgabe wird true zuruckgegeben und die Email exestiert nicht mehr in der Datenbank
     */
    @Test
    void deleteEMail_00() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        boolean test = classUnderTest.deleteEMail(eMail.getMid());

        EMail testEMail = getEMailVonDb(eMail.getMid());

        Assert.assertEquals(null, testEMail);
        Assert.assertEquals(true, test);
    }

    /**
     * Eine Email exestiert nicht in der Datenbank und wird geloescht,
     * Als rueckgabe wird false zuruckgegeben
     */
    @Test
    void deleteEMail_01() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);
        deleteEMailVonDb(eMail.getMid());

        boolean test = classUnderTest.deleteEMail(eMail.getMid());

        Assert.assertEquals(false, test);
    }

    /**
     * Eine Email wird nach absender gesucht
     * Es wird ein Liste mit einer Email zurueckgegeben mit den korrekte Daten;
     */
    @Test
    void searchEMail_00() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        ArrayList<EMail> testList = classUnderTest.searchEMail(eMail.getAbsender());

        Assert.assertEquals(1, testList.size());
        checkEmail(eMail, testList.get(0));
    }


    /**
     * Eine Email wird nach betreff gesucht
     * Es wird ein Liste mit einer Email zurueckgegeben mit den korrekte Daten;
     */
    @Test
    void searchEMail_01() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        ArrayList<EMail> testList = classUnderTest.searchEMail(eMail.getBetreff());

        Assert.assertEquals(1, testList.size());
        checkEmail(eMail, testList.get(0));
    }

    /**
     * Eine Email wird nach inhalt gesucht
     * Es wird ein Liste mit einer Email zurueckgegeben mit den korrekte Daten;
     */
    @Test
    void searchEMail_02() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        ArrayList<EMail> testList = classUnderTest.searchEMail(eMail.getInhalt());

        Assert.assertEquals(1, testList.size());
        checkEmail(eMail, testList.get(0));
    }

    /**
     * Eine Email wird gesucht, mit einem Suchwort der nicht vorhanden ist
     * Es wird eine leere Liste zurueckgegeben
     */
    @Test
    void searchEMail_03() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);

        ArrayList<EMail> testList = classUnderTest.searchEMail("NICHT VORHANDEN");

        Assert.assertEquals(0, testList.size());
    }

    /**
     * Eine bereits vorhandene Email wird geupdatet
     * Als rueckgabe wird true erwartet und die Email in der Datenbank hat die korrekten Daten
     */
    @Test
    void updateEMail_00() {
        Konto konto = createKontoObject();
        insertKonto(konto);
        EMail eMail = createEMailObject(0,konto.getKid());
        insertEmail(eMail);

        eMail.setBetreff("UPDATE BETREFF");
        eMail.setAbsender("UPDATE ABSENDER");
        eMail.setOrdner("UPDATE ORDNER");
        eMail.setZustand("UPDATE ZUSTAND");
        eMail.setEmpfaenger("UPDATE EMPFAENGER");
        eMail.setBcc("UPDATE BCC");
        eMail.setCc("UPDATE CC");
        eMail.setContentOriginal("UPDATE CONTENT");
        eMail.setInhalt("INHALT");
        eMail.setMessageID(987);

        boolean test = classUnderTest.updateEMail(eMail);

        EMail testMail = getEMailVonDb(eMail.getMid());

        Assert.assertEquals(true, test);
        checkEmail(eMail, testMail);

    }

    /**
     * Eine nicht vorhandene Email wird geupdatet
     * Als rueckgabe wird false erwartet
     */
    @Test
    void updateEMail_01() {
        EMail eMail = createEMailObject(0,0);
        insertEmail(eMail);
        deleteEMailVonDb(eMail.getMid());

        eMail.setBetreff("UPDATE BETREFF");
        eMail.setAbsender("UPDATE ABSENDER");
        eMail.setOrdner("UPDATE ORDNER");
        eMail.setZustand("UPDATE ZUSTAND");
        eMail.setEmpfaenger("UPDATE EMPFAENGER");
        eMail.setBcc("UPDATE BCC");
        eMail.setCc("UPDATE CC");
        eMail.setContentOriginal("UPDATE CONTENT");
        eMail.setInhalt("INHALT");
        eMail.setMessageID(987);

        boolean test = classUnderTest.updateEMail(eMail);

        Assert.assertEquals(false, test);

    }

    /**
     * 2 Emails sind bereits mit einem ordner TEST ORDNER in der Db vorhanden.
     * Als Rueckgabe wird 2 erwartet und die Emails sind nicht mehr in der Datenbank
     */
    @Test
    void deleteEMailVomOrdner_00() {
        EMail eMail_1 = createEMailObject(0,0);
        EMail eMail_2 = createEMailObject(0,0);
        insertEmail(eMail_1);
        insertEmail(eMail_2);

        int test = classUnderTest.deleteEMailVomOrdner(0,eMail_1.getOrdner());

        EMail testMail_1 = getEMailVonDb(eMail_1.getMid());
        EMail testMail_2 = getEMailVonDb(eMail_2.getMid());

        Assert.assertEquals(2, test);
        Assert.assertEquals(null, testMail_1);
        Assert.assertEquals(null, testMail_2);

    }

    /**
     * keine Emails bereits mit einem ordner TEST DELETE ORDNER in der Db vorhanden.
     * Als Rueckgabe wird 0 erwartet und die EMails sind immer noch in der datenbank;
     */
    @Test
    void deleteEMailVomOrdner_01() {
        EMail eMail_1 = createEMailObject(0,0);
        EMail eMail_2 = createEMailObject(0,0);
        insertEmail(eMail_1);
        insertEmail(eMail_2);

        int test = classUnderTest.deleteEMailVomOrdner(0,"TEST DELETE ORDNER");

        EMail testMail_1 = getEMailVonDb(eMail_1.getMid());
        EMail testMail_2 = getEMailVonDb(eMail_2.getMid());

        Assert.assertEquals(0, test);
        checkEmail(eMail_1, testMail_1);
        checkEmail(eMail_2, testMail_2);

    }
}