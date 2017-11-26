package sem.datenhaltung.semmodel.services;


import sem.datenhaltung.semmodel.impl.*;

public class ICRUDManagerSingleton {

    private static ICRUDWort icrudWortInstance;

    private static ICRUDTag icrudTagInstance;

    private static ICRUDFile icrudFileInstance;

    private static ICRUDAdresse icrudAdresseInstance;

    private static ICRUDMail icrudMailInstance;

    private ICRUDManagerSingleton(){}

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

    public static ICRUDAdresse getIcrudAdresseInstance(){
        if(icrudAdresseInstance == null)
            icrudAdresseInstance = new CRUDAdresse();
        return  icrudAdresseInstance;
    }

    public static ICRUDMail getIcrudMailInstance(){
        if(icrudMailInstance == null)
            icrudMailInstance = new CRUDEMail();
        return icrudMailInstance;
    }
}