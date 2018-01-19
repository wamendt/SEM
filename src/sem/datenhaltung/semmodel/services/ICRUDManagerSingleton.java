package sem.datenhaltung.semmodel.services;


import sem.datenhaltung.semmodel.impl.*;

public class ICRUDManagerSingleton {

    private static ICRUDWort icrudWortInstance;

    private static ICRUDTag icrudTagInstance;

    private static ICRUDFile icrudFileInstance;

    private static ICRUDTagVerteilung icrudTagVerteilung;

    private static ICRUDMail icrudMailInstance;

    private static ICRUDKonto icrudKontoInstance;

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
}
