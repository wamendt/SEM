package sem.datenhaltung.semmodel.database;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.impl.CRUDWort;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

class CRUDWortTest {
    @Test
    void createWort() throws IOException, SQLException {
        Wort test = new Wort();
        test.setWort("tagidtest");
        test.setTid(1);
        CRUDWort.getInstance().createWort(test);
    }

    @Test
    void getWortById() throws IOException, SQLException {
        Wort test = CRUDWort.getInstance().getWortById(1);
        System.out.println(test.getWort());
    }

    @Test
    void getAlleWoerter() throws IOException, SQLException {
        ArrayList<Wort> woerter = CRUDWort.getInstance().getAlleWoerter();
        for (Wort w : woerter){
            System.out.println(w.getWort());
        }
    }

    @Test
    void getAlleWoerterMitTagId() throws IOException, SQLException {
        ArrayList<Wort> test = CRUDWort.getInstance().getAlleWoerterMitTagId(1);

        for(Wort w : test){
            System.out.println(w.getWort());
        }
    }

    @Test
    void deleteWort() throws IOException, SQLException {
        CRUDWort.getInstance().deleteWort(1);
    }

    @Test
    void updateWort() throws IOException, SQLException {
        Wort test = CRUDWort.getInstance().getWortById(2);
        System.out.println(test.getWid());
        test.setWort("updateWort");
        System.out.println(CRUDWort.getInstance().updateWort(test));
    }

}