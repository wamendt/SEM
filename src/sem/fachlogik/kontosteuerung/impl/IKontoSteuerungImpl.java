package sem.fachlogik.kontosteuerung.impl;

import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class IKontoSteuerungImpl implements IKontoSteuerung{

    @Override
    public KontoGrenz getKonto(int id) {
        KontoGrenz konto = new KontoGrenz();
        konto.setEmailAddress("w.amendt@gmx.de");
        konto.setIMAPhost("imap.gmx.net");
        konto.setSMTPhost("smtp.gmx.net");
        konto.setPassWort("Packard1");
        return konto;
    }

    public boolean loggeKontoEin(KontoGrenz konto) throws IOException, SQLException{
        return true;
    }
    public boolean registriereKonto(KontoGrenz konto) throws IOException, SQLException{
        return true;
    }

    /*
    // Dasselbe Konto zurück??
    public KontoGrenz getKonto(KontoGrenz konto) throws IOException, SQLException{
        return konto;
    }
    */

    public boolean leoscheKonto(KontoGrenz konto) throws IOException, SQLException{
        return true;
    }

    public boolean loggeAus(KontoGrenz konto) throws IOException, SQLException{
        return true;
    }

    public ArrayList<KontoGrenz> getAlleKonten() throws IOException, SQLException{
        ArrayList<KontoGrenz> alleKonten = new ArrayList<KontoGrenz>();
        return alleKonten;
    }

    public boolean erstelleSignatur(KontoGrenz konto, String signatur) throws IOException, SQLException{
        return true;
    }

}