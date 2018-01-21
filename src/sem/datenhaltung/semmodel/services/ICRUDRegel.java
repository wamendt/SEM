package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Regel;
import java.util.ArrayList;

/**
 * Schnittstelle dessen Implementierungen den Zugriff auf Regeln in der Datenbank ermoeglichen.
 * Mit Hilfe der Factory Klasse ICRUDManagerSingleton koennen Instancen der Implementierungen dieser Klasse erzeugt werden.
 * Beispiel:
 *  ICRUDRegel crudregel = ICRUDManagerSingleton.getIcrudRegelInstance();
 */
public interface ICRUDRegel {

    /**
     * Erstellt eine Regel in der Datenbank, dabei wird der Primary Key automatisch auf der Regel gesezt
     * @param regel die zu erstellende Regel
     * @return der erzeugte PrimaryKey bei erfolg, sonst -1
     */
    int createRegel(Regel regel);

    /**
     * Loescht eine Regel aus der Datenbank
     * @param rid der PrimaryKey der Regel
     * @return true bei erfolg, sonst false
     */
    boolean deleteRegel(int rid);

    /**
     * Macht ein Update auf eine bereits in der DB vorhandene Regel
     * @param regel die zu updatetende Regel
     * @return true bei erfolg, sonst false
     */
    boolean updateRegel(Regel regel);


    /**
     * Holt eine Regel mit dem PrimaryKey id aus der Datenbank
     * @param rid der PrimaryKey der Regel
     * @return die gefundene Regel bei erfolg sonst null
     */
    Regel getRegelById(int rid) ;

    /**
     * Liefert eine Liste aller Regeln aus der Datenbank
     * @return eine Liste mit allen Regeln bei erfolg, sonst eine leere Liste
     */
    ArrayList<Regel> getAlleRegeln();

    /**
     * Liefert alle Regeln die zu einem bestimmten Konto gehoeren
     * @param kid der PrimaryKey des Kontos
     * @return eine Liste mit allen Regeln die zum Konto gehoerten bei erfolg, sonst eine leere Liste
     */
    ArrayList<Regel> getRegelMitKontoId(int kid);
}
