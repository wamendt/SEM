package sem.datenhaltung.semmodel.services;


import sem.datenhaltung.semmodel.entities.TagVerteilung;
import java.util.ArrayList;

/**
 * Schnittstelle dessen Implementierungen den Zugriff auf die Tagverteilungen in der Datenbank ermoeglichen.
 * Mit Hilfe der Factory Klasse ICRUDManagerSingleton koennen Instancen der Implementierungen dieser Klasse erzeugt werden.
 * Beispiel:
 *  ICRUDTagVerteilung crudTagVerteilung = ICRUDManagerSingleton.getIcrudTagVerteilungInstance();
 */
public interface ICRUDTagVerteilung {

    /**
     * Erstellt eine neue TagVerteilung in der Datenbank, dabeoi wird auf der tagVerteilung automatisch der PrimaryKey
     * gesetzt
     * @param tagVerteilung die zu erzeugende Tagverteilung
     * @return den erzeugten Schluessel bei erfolg sonst -1
     */
    int createTagVerteilung(TagVerteilung tagVerteilung);

    /**
     * Loescht eine TagVerteilung aus der Datenbank
     * @param tvid der PrimaryKey der TagVerteilung
     * @return true bei erfolg, sonst false
     */
    boolean deleteTagVerteilung(int tvid);

    /**
     * Loeschte Alle TagVerteilungen aus der Datenbank
     * @return die Anzahl der veraenderten Zeilen bei erfolg, sonst 0
     */
    int deleteAlleTagVerteilungen();

    /**
     * Macht ein Update auf eine bereits in der DB vorhanden TagVerteilung
     * @param tagVerteilung die zu updatende TagVerteilung
     * @return true bei erfolg, sonst false.
     */
    boolean updateTagVerteilung(TagVerteilung tagVerteilung);

    /**
     * Holt eine TagVerteilung aus der Datenbank
     * @param tvid der PrimaryKey der TagVerteilung
     * @return die gefundene TagVerteilung bei erfolg, sont false.
     */
    TagVerteilung getTagVerteilungById(int tvid);

    /**
     * Holt alle TagVerteilung aus der DB die zu einer Email zugeordnet wurden.
     * @param mid der PrimaryKey der EMail
     * @return eine Liste aller gefundenen TagVerteilungen bei erfolg, sonst eine leere Liste.
     */
    ArrayList<TagVerteilung> getTagVerteilungByEmailId(int mid);

    /**
     * Holt alle TagVerteilungen aus der DB.
     * @return eine Liste aller TagVerteilungen bei erfolg, sonst eine leere Liste.
     */
    ArrayList<TagVerteilung> getAlleTagVerteilungen() ;
}
