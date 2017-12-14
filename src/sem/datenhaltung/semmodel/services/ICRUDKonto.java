package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Konto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDKonto{

    int createKonto(Konto konto)throws IOException, SQLException;
    boolean deleteKonto(int kid) throws IOException, SQLException ;
    boolean updateKonto(Konto konto) throws IOException, SQLException ;
    Konto getKontoById(int kid) throws IOException, SQLException;
    ArrayList<Konto> getAlleKonto() throws IOException, SQLException ;

}
