package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Regel;

import java.util.ArrayList;

public interface ICRUDRegel {
    int createRegel(Regel regel);
    boolean deleteRegel(int rid);
    boolean updateRegel(Regel regel);
    Regel getRegelById(int rid) ;
    ArrayList<Regel> getAlleRegeln();
    ArrayList<Regel> getRegelMitKontoId(int kid);
}
