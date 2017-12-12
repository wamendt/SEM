package sem.datenhaltung.semmodel.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDAdresse {
    int createAdresse(Adresse adresse) throws IOException, SQLException ;
    Adresse getAdresseById(int aid) throws IOException, SQLException;
    ArrayList<Adresse> getAlleAdressen() throws IOException, SQLException ;
    ArrayList<Adresse> getAlleCCAdressen(int mid) throws IOException, SQLException;
    ArrayList<Adresse> getAlleBCCAdressen(int mid) throws IOException, SQLException ;
    ArrayList<Adresse> getAlleEmpfaengerAdressen(int mid) throws IOException, SQLException ;
    boolean deleteAdresse(int aid) throws IOException, SQLException ;
    boolean updateAdresse(Adresse adresse) throws IOException, SQLException;
}
