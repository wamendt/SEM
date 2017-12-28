package sem.fachlogik.kontosteuerung.services;

import sem.fachlogik.grenzklassen.KontoGrenz;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IKontoSteuerung {
    /**
     * holt ein Kontogrenzobjekt aus der datenbank
     * @param kid
     * @return
     */
    boolean loggeKontoEin(KontoGrenz konto) ;
    boolean registriereKonto(KontoGrenz konto) ;
    KontoGrenz getKonto(int kid);
    boolean leoscheKonto(KontoGrenz konto);
    boolean loggeAus(KontoGrenz konto);
    ArrayList<KontoGrenz> getAlleKonten() ;
    boolean erstelleSignatur(KontoGrenz konto, String signatur);

}
