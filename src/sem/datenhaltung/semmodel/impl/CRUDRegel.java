package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Regel;
import sem.datenhaltung.semmodel.services.ICRUDRegel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * CRUD Klasse die die ICRUDRegel Schnitstelle implementiert.
 * Bietet Methoden fuer den Zugriff auf die Regeln aus der Datenbank.
 */
public class CRUDRegel extends DBCRUDTeamplate<Regel> implements ICRUDRegel {
    private static final String TABLE_NAME = "regel";
    private static final String COLUMN_RID = "rid";
    private static final String COLUMN_BESCHREIBUNG = "beschreibung";
    private static final String COLUMN_VONEMAILADDRESS = "vonemailaddress";
    private static final String COLUMN_ZUORDNER = "zuordner";
    private static final String COLUMN_ISACTIVE = "isactive";
    private static final String COLUMN_KID = "kid";

    private static final String SQL_INSERT_REGEL = "INSERT INTO regel (beschreibung, vonemailaddress, zuordner, isactive, kid)" +
            "VALUES (?,?,?,?,?)";
    private static final String SQL_UPDATE_REGEL = "UPDATE regel SET beschreibung = ?, vonemailaddress = ?, zuordner = ?, isactive = ?," +
            " kid = ? WHERE rid = ?";
    /**
     * Erstelt ein entspraechendes Entity Objekt
     *
     * @param rs das Anfrage Ergebnis
     * @return
     */
    @Override
    protected Regel makeObject(ResultSet rs) throws SQLException {
       Regel regel = new Regel();
       regel.setActive(rs.getInt(COLUMN_ISACTIVE) == 1);
       regel.setBeschreibung(rs.getString(COLUMN_BESCHREIBUNG));
       regel.setKid(rs.getInt(COLUMN_KID));
       regel.setRid(rs.getInt(COLUMN_RID));
       regel.setVonEmailAddress(rs.getString(COLUMN_VONEMAILADDRESS));
       regel.setZuOrdner(rs.getString(COLUMN_ZUORDNER));
       return regel;
    }

    @Override
    public int createRegel(Regel regel) {
        int isActive = regel.isActive() ? 1 : 0;
        try {
            int key = insertAndReturnKey(SQL_INSERT_REGEL, regel.getBeschreibung(), regel.getVonEmailAddress(), regel.getZuOrdner(),
                    isActive, regel.getKid());
            regel.setRid(key);
            return key;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteRegel(int rid) {
        try {
            int i = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_RID), rid);
            return i == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRegel(Regel regel) {
       try{
           int isActive = regel.isActive() ? 1:0;
           int i = updateOrDelete(SQL_UPDATE_REGEL, regel.getBeschreibung(), regel.getVonEmailAddress(), regel.getZuOrdner(),
                  isActive, regel.getKid(), regel.getRid() );
           return i == 1;
       } catch (SQLException e) {
           e.printStackTrace();
       }
        return false;
    }

    @Override
    public Regel getRegelById(int rid) {
        try{
            ArrayList<Regel> regeln = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_RID), rid);
            return regeln.size() > 0 ? regeln.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Regel> getAlleRegeln() {
        try{
            ArrayList<Regel> regeln = query(String.format(SQL_SELECT_FROM, TABLE_NAME));
            return regeln;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Regel> getRegelMitKontoId(int kid) {
        try{
            ArrayList<Regel> regeln = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_KID), kid);
            return regeln;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
