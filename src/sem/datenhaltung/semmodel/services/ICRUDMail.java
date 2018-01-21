package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.EMail;
import java.util.ArrayList;

/**
 * Schnittstelle dessen Implementierungen den Zugriff auf Emails in der Datenbank ermoeglichen.
 * Mit Hilfe der Factory Klasse ICRUDManagerSingleton koennen Instancen der Implementierungen dieser Klasse erzeugt werden.
 * Beispiel:
 *  ICRUDMail crudmail = ICRUDManagerSingleton.getIcrudMailInstance();
 *
 */
public interface ICRUDMail {

    /**
     * Erstellt ein Email eintrag in der Datenbank und generiert dabei den PrimaryKey
     * auf der Email wird anschliessend dieser unter dem Atribut eid gesetzt.
     * @param email Die Email, die in die Datenbank eingetragen werden soll.
     * @return Den von der Datenbank erzeugten Schluessel bei erfolg, sonst -1;
     */
    int createEMail(EMail email);

    /**
     * Sucht nach einer EMail in der Datenbank mit der angegebenen Nummer.
     * @param mid der PrimaryKey der zu suchenden Email.
     * @return Die gefundene EMail , falls eine gefunden wird sonst null.
     */
    EMail getEMailById(int mid);

    /**
     * Sucht nach EMails in der Datenbank die mit einem Ordner verknuepft sind.
     * @param name der Ordnername mit dem die EMails verknuepft sind.
     * @return Wenn EMails gefunden werden dann eine Liste mit den EMails, sonst eine leere Liste;
     */
    ArrayList<EMail> getEMailByOrdner(int kid, String name);

    /**
     * Sucht nach EMails in der Datenbank die mit einem Tag verknuepft sind.
     * @param tid die TagId mit der die EMails verknuepft sind.
     * @return Wenn EMail gefunden werden dann eine Liste mit den EMails, sonst eine leere Liste;
     */
    ArrayList<EMail> getEMailByTag(int tid);

    /**
     * Sucht nach allen EMails in der Datenbank.
     * @return Wenn EMails gefunden werden dann eine Liste mit den EMails, sonst eine leere Liste;
     */
    ArrayList<EMail> getAlleEMails();

    /**
     * Loescht eine EMail aus der Datenbank.
     * @param mid der PrimaryKey der zuloeschenden EMail.
     * @return true bei erfolg, sonst false.
     */
    boolean deleteEMail(int mid);

    /**
     * Erneuert eine Vorhandene EMail aus der Datenbank. Die EMail muss einen PrimaryKey besitzen.
     * @param email die Email mit den erneuerten Attributen.
     * @return true bei erfolg, sonst false.
     */
    boolean updateEMail(EMail email);


    /**
     * Ueberprueft ob eine Email bereits in der Datenbank erxestiert, um dies zu machen, wird die id, der
     * betreff, der absender und der ordner zur ueberpruefung benoetigt.
     * @param id
     * @param betreff
     * @param absender
     * @param ordner
     * @return
     */
    EMail checkMessageInDB(int id, String betreff, String absender, String ordner);

    /**
     * Loescht alle EMails aus einem bestimmten Ordner.
     * @param ordner der Ordner aus dem die Emails gleoescht werden sollen.
     * @return die anzahl der geloeschten Objekte
     */
    int deleteEMailVomOrdner(int kid, String ordner) ;

    /**
     * Sucht nach einer Email mit dem Suchtwort suchwort, dabei wird ueberprueft ob das suchwort, im inhalt,
     * betreff, oder absender einer EMail auftaucht.
     * @param suchwort das zu suchende Wort
     * @return eine Liste der EMails, die gefunden wurden bei erfolg, sonst eine leere Liste.
     */
    ArrayList<EMail> searchEMail(String suchwort);


    //ArrayList<EMail> getAlleEMailsMitTagId(int tid);
    //EMail getEMailByMessageIDUndOrdner(int id, String ordner); /*BRAUCHT MAN DIE METHODE ??*/
    /*BRAUCHEN WIR DIE METHODEN ??? Oder Liefern wir nicht einfach schon die sqldatei nicht mit aus*/
    //    boolean createEMailTable();
//    boolean createFileTable() ;
//    boolean createAdresseTable();
//    boolean createTagTable() ;
//    boolean createWortTable()  ;
//    boolean deleteTable(String tableName);
}
