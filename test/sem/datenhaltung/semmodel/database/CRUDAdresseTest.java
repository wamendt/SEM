package sem.datenhaltung.semmodel.database;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Adresse;
import sem.datenhaltung.semmodel.impl.CRUDAdresse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

class CRUDAdresseTest {




    @Test
    void createAdresse() throws IOException, SQLException {
        CRUDAdresse crudAdresse = CRUDAdresse.getInstance();
        Adresse a = new Adresse();
        a.setAdresse("adresse");
        a.setName("name");
        a.setZustand("cc");

        int ret = crudAdresse.createAdresse(a);
        System.out.println(ret);
    }

    @Test
    void getAdresseById() throws IOException, SQLException {
        CRUDAdresse crudAdresse = CRUDAdresse.getInstance();
        Adresse a = crudAdresse.getAdresseById(1);
        System.out.println(a.getName());
    }


    @Test
    void getAlleAdressen() throws IOException, SQLException {
        ArrayList<Adresse> adresses = CRUDAdresse.getInstance().getAlleAdressen();
        for(Adresse a : adresses){
            System.out.println(a.getAid());
        }
    }

    @Test
    void deleteAdresse() throws IOException, SQLException {
        CRUDAdresse crudAdresse = CRUDAdresse.getInstance();
        crudAdresse.deleteAdresse(1);
    }



    @Test
    void updateAdresse() throws IOException, SQLException {
        Adresse a = CRUDAdresse.getInstance().getAdresseById(2);
        a.setName("geaenderter name");
        a.setAdresse("23");

        System.out.println(CRUDAdresse.getInstance().updateAdresse(a));
    }

}