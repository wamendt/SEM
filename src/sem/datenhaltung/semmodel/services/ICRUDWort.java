package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Wort;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDWort {
    int createWort(Wort wort);
    Wort getWortById(int wid);
    ArrayList<Wort> getAlleWoerter();
    ArrayList<Wort> getAlleWoerterMitTagId(int tid);
    boolean deleteWort(int wid) ;
    boolean deleteAlleWoerterMitTagId(int tid);
    boolean updateWort(Wort wort);
}
