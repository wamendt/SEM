package sem.fachlogik.kontosteuerung.services;

import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.RegelGrenz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IKontoSteuerung {

    boolean registriereKonto(KontoGrenz konto) ;
    KontoGrenz getKontoById(int kid);
    KontoGrenz getKontoByUsername(String username);
    boolean leoscheKonto(KontoGrenz konto);
    ArrayList<KontoGrenz> getAlleKonten() ;
    boolean erstelleSignatur(KontoGrenz konto, String signatur);
    boolean erstelleRegel(KontoGrenz konto, RegelGrenz regel);
    boolean loescheAlleKonten();

}
