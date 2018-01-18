package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Konto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDKonto{

    int createKonto(Konto konto);
    boolean deleteKonto(int kid);
    boolean updateKonto(Konto konto);
    Konto getKontoById(int kid) ;
    ArrayList<Konto> getAlleKonten() ;

}
