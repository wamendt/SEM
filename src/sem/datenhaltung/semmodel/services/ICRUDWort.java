package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Wort;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDWort {
    int createWort(Wort wort) throws IOException, SQLException;
    Wort getWortById(int wid) throws IOException, SQLException;
    ArrayList<Wort> getAlleWoerter() throws IOException, SQLException;
    ArrayList<Wort> getAlleWoerterMitTagId(int tid) throws IOException, SQLException;
    boolean deleteWort(int wid) throws IOException, SQLException;
    boolean deleteAlleWoerterMitTagId(int tid) throws IOException, SQLException;
    boolean updateWort(Wort wort) throws IOException, SQLException;
}
