package sem.fachlogik.kontosteuerung.impl;

import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.services.ICRUDKonto;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.fachlogik.grenzklassen.GrenzklassenKonvertierer;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class IKontoSteuerungImpl implements IKontoSteuerung{

    @Override
    public KontoGrenz getKonto(int id) {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();
        Konto konto = icrudKonto.getKontoById(id);
        KontoGrenz kontoGrenz = null;
        if(konto != null){
            kontoGrenz = GrenzklassenKonvertierer.KontoZuKontoGrenz(konto);
        }
        return kontoGrenz;
    }


    public boolean registriereKonto(KontoGrenz konto) {
        return true;
    }

    public boolean leoscheKonto(KontoGrenz konto){
        return true;
    }


    public ArrayList<KontoGrenz> getAlleKonten() {
        ArrayList<KontoGrenz> alleKonten = new ArrayList<KontoGrenz>();
        return alleKonten;
    }

    public boolean erstelleSignatur(KontoGrenz konto, String signatur) {
        return true;
    }

}