package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Tag;
import java.util.ArrayList;

/**
 * Schnittstelle dessen Implementierungen den Zugriff auf Tags in der Datenbank ermoeglichen.
 * Mit Hilfe der Factory Klasse ICRUDManagerSingleton koennen Instancen der Implementierungen dieser Klasse erzeugt werden.
 * Beispiel:
 *  ICRUDTag crudtag = ICRUDManagerSingleton.getIcrudTagInstance();
 * un
 */
public interface ICRUDTag {
    /**
     * Erstellt einen Tag in der Datenbank, der PrimaryKey wird beim Tag mitgesetzt
     * @param tag der zu erzeugende Tag
     * @return den erzeugten PrimaryKey bei erfolg, sonst -1
     */
    int createTag(Tag tag);

    /**
     * Holt einen Tag aus der Datenbank mit dem zugehoerigen PrimaryKey
     * @param tid der PrimaryKey des Tags
     * @return den gefunden Tag bei erfolg, sonst null
     */
    Tag getTagById(int tid);

    /**
     * Liefert alle Tags aus der Datenbank
     * @return eine Liste mit allen Tags bei erfolg, sonst eine leere Liste
     */
    ArrayList<Tag> getAlleTags();

    /**
     * Loescht ein Tag aus der Datenbank mit angegeben PrimaryKey
     * @param tid der PrimaryKey des Tags
     * @return true bei erfolg, sonst false
     */
    boolean deleteTag(int tid);

    /**
     * loescht alle Tags aus der Datenbank
     * @return die Anzahl der veraenderten Zeilen bei erfolg, sonst 0
     */
    int deleteAlleTags();

    /**
     * Macht ein Update auf einen bereits in der DB vorhanden Tag
     * @param tag der zu updatende Tag
     * @return true bei erfolg, sonst false
     */
    boolean updateTag(Tag tag);
}
