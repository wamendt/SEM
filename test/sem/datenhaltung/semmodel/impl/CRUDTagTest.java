package sem.datenhaltung.semmodel.impl;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CRUDTagTest extends CRUDTest{

    private ICRUDTag classUnderTest = ICRUDManagerSingleton.getIcrudTagInstance();

    private void checkTag(Tag expected, Tag actual){
        assertEquals(expected.getTid(), actual.getTid());
        assertEquals(expected.getName(), actual.getName());
    }
    /**
     * Erstellt ein Tag in der DatenBank
     * Ein Tag wird in der Datenbak erstellt und besitzt die korrekt Daten
     */
    @Test
    void createTag() {
        Tag tag = createTagObject();

        int key =  classUnderTest.createTag(tag);
        Tag testTag = getTagVonDb(key);

        checkTag(tag, testTag);
    }

    /**
     * Es exestiert ein Tag in der DB und dieser Wird geholt
     * Als Rueckgabe wird der Tag mit den korrekten Daten erwartet
     */
    @Test
    void getTagById_00() {
        Tag tag = createAndInsertTag();

        Tag testTag = classUnderTest.getTagById(tag.getTid());
        checkTag(tag, testTag);
    }

    /**
     * Es exestiert kein Tag in der DB
     * Als Rueckgabe wird null erwartet
     */
    @Test
    void getTagById_01() {
        Tag tag = createAndInsertTag();
        deleteTagVonDb(tag.getTid());
        Tag testTag = classUnderTest.getTagById(tag.getTid());
        assertEquals(null, testTag);
    }

    /**
     * Es exestieren 2 Tags in der DB und diese werden geholt
     * Als Rueckgabe wird eine Liste mit diesen 2 Tags erwartet
     */
    @Test
    void getAlleTags_00() {
        try{
            connection.createStatement().execute("DELETE FROM tag");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Tag tag_1 = createAndInsertTag();
        Tag tag_2 = createAndInsertTag();

        ArrayList<Tag> testList = classUnderTest.getAlleTags();

        assertEquals(2, testList.size());
        checkTag(tag_1, testList.get(0));
        checkTag(tag_2, testList.get(1));
    }

    /**
     * Es exestieren keine Tags in der DB
     * Als Rueckgabe wird eine leere Liste erwartet
     */
    @Test
    void getAlleTags_01() {
        try{
            connection.createStatement().execute("DELETE FROM tag");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Tag> testList = classUnderTest.getAlleTags();

        assertEquals(0, testList.size());
    }

    /**
     * Ein bereits in der DB vohandener Tag wird geloescht
     * Als Rueckgabe wird True erwartet und der Tag befindet sich nicht mehr in der DB
     */
    @Test
    void deleteTag_00() {
        Tag tag = createAndInsertTag();

        boolean test = classUnderTest.deleteTag(tag.getTid());
        Tag testTag = getTagVonDb(tag.getTid());

        assertEquals(true, test);
        assertEquals(null, testTag);
    }

    /**
     * kein  in der DB vohandener Tag wird geloescht
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void deleteTag_01() {
        Tag tag = createAndInsertTag();
        deleteTagVonDb(tag.getTid());

        boolean test = classUnderTest.deleteTag(tag.getTid());

        assertEquals(false, test);

    }

    /**
     * In der DB exestieren 2 Tags, die werden geloescht
     * Als Rueckgabe wird 2 erwartet und die Tags befindet sich nicht mehr in der DB
     */
    @Test
    void deleteAlleTags_00() {
        try{
            connection.createStatement().execute("DELETE FROM tag");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Tag tag_1 = createAndInsertTag();
        Tag tag_2 = createAndInsertTag();

        int test = classUnderTest.deleteAlleTags();
        Tag testTag_1 = getTagVonDb(tag_1.getTid());
        Tag testTag_2 = getTagVonDb(tag_2.getTid());

        assertEquals(2, test);
        assertEquals(null, testTag_1);
        assertEquals(null, testTag_2);
    }

    /**
     * In der DB exestieren keine Tags, die werden geloescht
     * Als Rueckgabe wird 0 erwartet.
     */
    @Test
    void deleteAlleTags_01() {
        try {
            connection.createStatement().execute("DELETE FROM tag");
        } catch (SQLException e) {
            e.printStackTrace();

            int test = classUnderTest.deleteAlleTags();

            assertEquals(0, test);
        }
    }

    /**
     * Ein bereits vorhandener Tag wird geupdated
     * Als Rueckgabe wird true erwartet und der Tag hat in der DB die korrekten Daten
     */
    @Test
    void updateTag_00() {
        Tag tag = createAndInsertTag();
        tag.setName("UPDATE NAME");

        boolean test = classUnderTest.updateTag(tag);
        Tag testTag = getTagVonDb(tag.getTid());

        assertEquals(true, test);
        checkTag(tag, testTag);
    }

    /**
     * Kein Tag Exestiert in der DB und wird geupdatet
     * Als Rueckgabe wird false erwartet
     */
    @Test
    void updateTag_01() {
        Tag tag = createAndInsertTag();
        try{
            connection.createStatement().execute("DELETE FROM tag");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tag.setName("UPDATE NAME");

        boolean test = classUnderTest.updateTag(tag);

        assertEquals(false, test);
    }
}