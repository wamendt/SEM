package sem.fachlogik.kontosteuerung.impl;

import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;

public class IKonstoSteuerungImpl implements IKontoSteuerung{

    @Override
    public KontoGrenz getKonto(int id) {
        KontoGrenz konto = new KontoGrenz();
        konto.setEmailAddress("w.amendt@gmx.de");
        konto.setIMAPhost("imap.gmx.net");
        konto.setSMTPhost("smtp.gmx.net");
        konto.setPassWort("Packard1");
        return konto;
    }
}
