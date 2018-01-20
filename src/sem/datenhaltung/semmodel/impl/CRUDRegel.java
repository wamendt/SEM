package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Regel;
import sem.datenhaltung.semmodel.services.ICRUDRegel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDRegel extends DBCRUDTeamplate<Regel> implements ICRUDRegel {

    /**
     * Erstelt ein entspraechendes Entity Objekt
     *
     * @param rs das Anfrage Ergebnis
     * @return
     */
    @Override
    protected Regel makeObject(ResultSet rs) throws SQLException, IOException {
       Regel regel = new Regel();
       regel.setActive(rs.getInt("isactive") == 1);
       return regel;
    }

    @Override
    public int createRegel(Regel regel) {
        return 0;
    }

    @Override
    public boolean deleteRegel(int rid) {
        return false;
    }

    @Override
    public boolean updateRegel(Regel regel) {
        return false;
    }

    @Override
    public Regel getRegelById(int rid) {
        return null;
    }

    @Override
    public ArrayList<Regel> getAlleRegeln() {
        return null;
    }

    @Override
    public ArrayList<Regel> getRegelMitKontoId(int kid) {
        return null;
    }
}
