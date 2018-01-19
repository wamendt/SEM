package sem.fachlogik.kontosteuerung.services;

import sem.fachlogik.grenzklassen.KontoGrenz;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IKontoSteuerung {

    boolean registriereKonto(KontoGrenz konto) ;
    KontoGrenz getKonto(int kid);
    boolean leoscheKonto(KontoGrenz konto);
    ArrayList<KontoGrenz> getAlleKonten() ;
    boolean erstelleSignatur(KontoGrenz konto, String signatur);


}
