package sem.datenhaltung.semmodel.database;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Adresse;
import sem.datenhaltung.semmodel.services.ICRUDAdresse;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

class CRUDAdresseTest {




    @Test
    void createAdresse() throws IOException, SQLException {
        ICRUDAdresse crudAdresse = ICRUDManagerSingleton.getIcrudAdresseInstance();
        Adresse a = new Adresse();
        a.setAdresse("adresse");
        a.setName("name");
        a.setZustand("cc");

        int ret = crudAdresse.createAdresse(a);
        System.out.println(ret);
    }

    @Test
    void getAdresseById() throws IOException, SQLException {
        ICRUDAdresse crudAdresse = ICRUDManagerSingleton.getIcrudAdresseInstance();

        Adresse a = crudAdresse.getAdresseById(1);
        System.out.println(a.getName());
    }


    @Test
    void getAlleAdressen() throws IOException, SQLException {
        ArrayList<Adresse> adresses = ICRUDManagerSingleton
                .getIcrudAdresseInstance()
                .getAlleAdressen();

        for(Adresse a : adresses){
            System.out.println(a.getAid());
        }
    }

    @Test
    void deleteAdresse() throws IOException, SQLException {
        ICRUDAdresse crudAdresse = ICRUDManagerSingleton.getIcrudAdresseInstance();

        crudAdresse.deleteAdresse(1);
    }



    @Test
    void updateAdresse() throws IOException, SQLException {
        Adresse a = ICRUDManagerSingleton.getIcrudAdresseInstance().getAdresseById(2);

        a.setName("geaenderter name");
        a.setAdresse("23");

        System.out.println( ICRUDManagerSingleton.getIcrudAdresseInstance().updateAdresse(a));
    }

}