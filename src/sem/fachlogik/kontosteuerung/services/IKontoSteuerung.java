package sem.fachlogik.kontosteuerung.services;

import sem.fachlogik.grenzklassen.KontoGrenz;

public interface IKontoSteuerung {
    /**
     * holt ein Kontogrenzobjekt aus der datenbank
     * @param id
     * @return
     */
    KontoGrenz getKonto(int id);
}
