package sem.datenhaltung.semmodel.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.database.DBConnectionManager;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;

import javax.xml.transform.Result;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class CRUDEMailTest {

    private static DBConnectionManager dbConnectionManager;
    private static Connection connection;
    private static Connection savepointBeforeAll;
    private static Savepoint savepoint;

    private static ICRUDMail classUnderTest = ICRUDManagerSingleton.getIcrudMailInstance();

    private static EMail testemail;
    private static Tag testTag;

    private EMail createEmail(){
        EMail eMail = new EMail();
        eMail.setTid(1);
        eMail.setInhalt("TESTINHALT");
        return null;
    }
    private static Tag tag(){
        Tag tag = new Tag();
        tag.setName("TESTTAG");
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tag(name) VALUE (?)");
            statement.executeUpdate()
            statement.executeUpdate("INSERT INTO tag (name) VALUES (" + tag.getName() + ")", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getResultSet();
            if(rs.next()){
                int id = rs.getInt(1);
                System.out.println(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

//    @BeforeAll
//    void init(){
//        try {
//            connection = DBConnectionManager.getConnection();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        testemail.setInhalt("TEST INHALT");
//        testemail.setTid();
////    }
////    /**
//     * Vor jedem start wird eine neue Verbindung mit der Datenbank geoffnet.
//     * Und es wird savePoint gesetzt und eine ne
//     */
////    @Before
////    void initBefore(){
////        try {
////            savepoint = connection.setSavepoint();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//    }

//    /**
//     * Nach jedem Lauf wird die Verbindung wieder geschlossen und die Datenbank wird auf den zurueckgesetzt.
//     */
//    @After
//    void initAfter(){
//        try {
//            connection.rollback(savepoint);
//            /*Verbindung sollte automatisch geschlossen werden*/
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Test
    void createTestTag(){
        try {
            connection = DBConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tag();
    }
    /**
     * Erstellt eine TestEMail in der Datenbank und ueberprueft ob die Daten korrekt sind
     */
    @Test
    void createEMail() {
        EMail email = new EMail();
        email.setInhalt("TEST INHALT");
    }

    @Test
    void getEMailById() {
    }

    @Test
    void getEMailByOrdner() {
    }

    @Test
    void getEMailByTag() {
    }

    @Test
    void checkMessageInDB() {
    }

    @Test
    void getAlleEMails() {
    }

    @Test
    void deleteEMail() {
    }

    @Test
    void searchEMail() {
    }

    @Test
    void updateEMail() {
    }

    @Test
    void deleteEMailVomOrdner() {
    }
}