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
    boolean loggeKontoEin(KontoGrenz konto) throws IOException, SQLException;
    boolean registriereKonto(KontoGrenz konto) throws IOException, SQLException;
    KontoGrenz getKonto(int kid) throws IOException, SQLException;
    boolean leoscheKonto(KontoGrenz konto) throws IOException, SQLException;
    boolean loggeAus(KontoGrenz konto) throws IOException, SQLException;
    ArrayList<KontoGrenz> getAlleKonten() throws IOException, SQLException;
    boolean erstelleSignatur(KontoGrenz konto, String signatur) throws IOException, SQLException;

}
