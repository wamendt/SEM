package sem.datenhaltung.semmodel.services;


import sem.datenhaltung.semmodel.impl.*;

/**
 * Fabrik Klasse die die aktuelle Implementation der CRUDKlassen liefert
 *
 * Beispiel:
 *
 * ICRUDMail crudmail = ICRUDManagerSingleton.getIcrudMailInstance();
 *
 * nun koenen mit der crudmail Instanz zugriffe auf die Datenbank stattfinden
 */

public class ICRUDManagerSingleton {

    private static ICRUDWort icrudWortInstance;

    private static ICRUDTag icrudTagInstance;

    private static ICRUDFile icrudFileInstance;

    private static ICRUDTagVerteilung icrudTagVerteilung;

    private static ICRUDMail icrudMailInstance;

    private static ICRUDKonto icrudKontoInstance;

    private static ICRUDRegel icrudRegelInstance;

    private ICRUDManagerSingleton(){}

    public static ICRUDKonto getIcrudKontoInstance(){
        if(icrudKontoInstance == null){
            icrudKontoInstance = new CRUDKonto();
        }
        return icrudKontoInstance;
    }

    public static ICRUDTagVerteilung getIcrudTagVerteilungInstance(){
        if(icrudTagVerteilung == null){
            icrudTagVerteilung = new CRUDTagVerteilung();
        }
        return icrudTagVerteilung;
    }

    public static ICRUDWort getIcrudWordInstance(){
        if(icrudWortInstance == null){
            icrudWortInstance = new CRUDWort();
        }
        return  icrudWortInstance;
    }

    public static ICRUDTag getIcrudTagInstance(){
        if(icrudTagInstance == null)
            icrudTagInstance = new CRUDTag();
        return icrudTagInstance;
    }

    public  static ICRUDFile getIcrudFileInstance(){
        if(icrudFileInstance == null)
            icrudFileInstance = new CRUDFile();
        return icrudFileInstance;
    }


    public static ICRUDMail getIcrudMailInstance(){
        if(icrudMailInstance == null)
            icrudMailInstance = new CRUDEMail();
        return icrudMailInstance;
    }

    public static ICRUDRegel getIcrudRegelInstance(){
        if(icrudRegelInstance == null){
            icrudRegelInstance = new CRUDRegel();
        }
        return icrudRegelInstance;
    }
}
