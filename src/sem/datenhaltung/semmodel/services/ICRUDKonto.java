package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Konto;
import java.util.ArrayList;
/**
 * Schnittstelle dessen Implementierungen den Zugriff auf Kontos in der Datenbank ermoeglichen.
 * Mit Hilfe der Factory Klasse ICRUDManagerSingleton koennen Instancen der Implementierungen dieser Klasse erzeugt werden.
 * Beispiel:
 *  ICRUKonto crudkonto = ICRUDManagerSingleton.getIcrudKontoInstance();
 *
 */
public interface ICRUDKonto{

    /**
     * Erstellt ein Konto Objekt in der Datenbank, der erzeugte PrimaryKey wird dierekt beim Konto gesetzt
     * @param konto das Zu erstellende Konto
     * @return den erzeugten PrimaryKey bei erfolg, sonst -1;
     */
    int createKonto(Konto konto);

    /**
     * Loescht ein Konto aus der Datenbank.
     * @param kid der PrimaryKey des Kontos.
     * @return true bei erfolg, sonst false.
     */
    boolean deleteKonto(int kid);

    /**
     * Fuehrt ein Update bei einem Bestehenden Konto durch
     * @param konto das zu Updaatende Konto, mit einem PrimaryKey der in der Datenbank bereits vorhanden ist
     * @return true bei erfolg, sonst false
     */
    boolean updateKonto(Konto konto);

    /**
     * Holt ein Konto mit angegeben PrimaryKey aus der Datenbank
     * @param kid der PrimaryKey des Kontos welches geholt werden soll.
     * @return Das gesuchte Konto bei erfolg, sonst null
     */
    Konto getKontoById(int kid) ;

    /**
     * Liefert eine Liste aller Konten aus der Datenbank
     * @return eine Liste mit den Konten bei erfolg, sonst eine leere Liste
     */
    ArrayList<Konto> getAlleKonten() ;

}
