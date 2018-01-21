package sem.datenhaltung.semmodel.impl;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;

public class CreateKonto {

    @Test
    void createNewKonto(){
        Konto konto = new Konto();
        konto.setPassWort("bmw318i");
        konto.setIMAPhost("imap.mail.ru");
        konto.setSMTPhost("smtp.mail.ru");
        konto.setSignatur("Meine Sig");
        konto.setUserName("h4x9r@bk.ru");
        ICRUDManagerSingleton.getIcrudKontoInstance().createKonto(konto);
    }
}
