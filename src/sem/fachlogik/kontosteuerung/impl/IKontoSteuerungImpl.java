package sem.fachlogik.kontosteuerung.impl;

import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.entities.Regel;
import sem.datenhaltung.semmodel.services.ICRUDKonto;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDRegel;
import sem.fachlogik.grenzklassen.GrenzklassenKonvertierer;
import sem.fachlogik.grenzklassen.KontoGrenz;
import sem.fachlogik.grenzklassen.RegelGrenz;
import sem.fachlogik.kontosteuerung.services.IKontoSteuerung;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class IKontoSteuerungImpl implements IKontoSteuerung{

    @Override
    public KontoGrenz getKontoById(int id) {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();
        Konto konto = icrudKonto.getKontoById(id);
        KontoGrenz kontoGrenz = null;
        if(konto != null){
            kontoGrenz = GrenzklassenKonvertierer.kontoZuKontoGrenz(konto);
        }
        return kontoGrenz;
    }

    @Override
    public KontoGrenz getKontoByUsername(String username) {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();
        Konto konto = icrudKonto.getKontoByUsername(username);
        KontoGrenz kontoGrenz = null;
        if(konto != null){
            kontoGrenz = GrenzklassenKonvertierer.kontoZuKontoGrenz(konto);
        }
        return kontoGrenz;
    }

    @Override
    public boolean registriereKonto(KontoGrenz konto) {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();

        if(icrudKonto.createKonto(GrenzklassenKonvertierer.kontoGrenzZuKonto(konto)) == -1)
            return false;
        return true;
    }

    @Override
    public boolean leoscheKonto(KontoGrenz konto){
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();
        if(icrudKonto.deleteKonto(konto.getKid()))
            return true;
        return false;
    }

    @Override
    public ArrayList<KontoGrenz> getAlleKonten() {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();

        ArrayList<KontoGrenz> alleKonten = new ArrayList<>();
        ArrayList<Konto> konten = icrudKonto.getAlleKonten();
        for(Konto k : konten){
            alleKonten.add(GrenzklassenKonvertierer.kontoZuKontoGrenz(k));
        }
        return alleKonten;
    }

    @Override
    public boolean erstelleSignatur(KontoGrenz konto, String signatur) {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();
        konto.setSignatur(signatur);
        Konto k = GrenzklassenKonvertierer.kontoGrenzZuKonto(konto);
        if(icrudKonto.updateKonto(k))
            return true;
        return false;
    }


    @Override
    public boolean erstelleRegel(KontoGrenz konto, RegelGrenz regel) {
        Regel r = GrenzklassenKonvertierer.regelGrenzZuRegel(regel);
        r.setKid(konto.getKid());
        ICRUDRegel icrudRegel = ICRUDManagerSingleton.getIcrudRegelInstance();
        return icrudRegel.createRegel(r) > 0;
    }

    @Override
    public boolean loescheAlleKonten() {
        ICRUDKonto icrudKonto = ICRUDManagerSingleton.getIcrudKontoInstance();
        return icrudKonto.loescheAlleKonten() > 0;
    }


}