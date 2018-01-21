package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Wort;
import java.util.ArrayList;

/**
 * Schnittstelle dessen Implementierungen den Zugriff auf Woerter in der Datenbank ermoeglichen.
 * Mit Hilfe der Factory Klasse ICRUDManagerSingleton koennen Instancen der Implementierungen dieser Klasse erzeugt werden.
 * Beispiel:
 *  ICRUDWort crudwort = ICRUDManagerSingleton.getIcrudWortInstance();
 */
public interface ICRUDWort {

    /**
     * Erstellt ein Wort in der Datenbank, dabei wird der PrimaryKey automatisch beim Wort gesetzt
     * @param wort das zu erstellende WortObjekt
     * @return den erzeugten PrimaryKey bei erfolg, sonst -1
     */
    int createWort(Wort wort);

    /**
     * Holt ein Wort mit dem angegeben PrimaryKey aus der Datenbank
     * @param wid der PrimaryKey des Wortes
     * @return das gefundene Wort bei erfolg, sonst null
     */
    Wort getWortById(int wid);

    /**
     * Hollt alle Woerter aus der Datenbank
     * @return eine Liste aller gefundenen Woerter bei Erfolg, sonst eine leere Liste
     */
    ArrayList<Wort> getAlleWoerter();

    /**
     * Holt alle Woerter aus der DAtenbank die zu einem Tag gehoeren
     * @param tid der PrimaryKey eines Tags
     * @return eine Liste aller gefunden Woerter bei Erfolg, sonst eine leere Liste
     */
    ArrayList<Wort> getAlleWoerterMitTagId(int tid);

    /**
     * Loescht ein Wort aus der Datenbank
     * @param wid der PrimaryKey des Wortes
     * @return true bei Erfolg, sonst false
     */
    boolean deleteWort(int wid) ;

    /**
     * Loescht alle Woerter die zu einem Tag gehoeren
     * @param tid der PrimaryKey des Tags
     * @return die anzahl geloeschter Woerter bei erfolg, sonst 0
     */
    int deleteAlleWoerterMitTagId(int tid);

    /**
     * Macht ein Update auf ein bereits vorhandes Wort in der Datenbak
     * @param wort das zuupdatende Wort
     * @return true bei Erfolg, sonst false
     */
    boolean updateWort(Wort wort);
}
