package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.database.DBConnectionManager;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;



/**
 * Die generische Klasse DBCRUDTeamplate<T> enthaelt die Schablonenmethoden zum Suchen, Loeschen,
 * Veraendern und Einfuegen in eine Datenbank. Dafuer muss makeObject, in den konkreten Subklassen implementiert
 * werden.
 * makeObject muss in den konkreten Subklassen implementiert werden.
 * @param <T> Ein Entity - Objekt.
 */


public abstract class DBCRUDTeamplate<T> {

    /**
     * Erstelt ein entspraechendes Entity Objekt
     * @param rs das Anfrage Ergebnis
     * @return
     */
    protected abstract T makeObject(ResultSet rs) throws SQLException, IOException;


    /**
     * Schablonenmethode die alle T Obekte aus der Datanbank hohlt.
     * @param sql eine SQL SELECT Befehl bsp: SELECT * FROM email
     * @return eine Liste mit den Anfrageergebnissen, falls welche gefunden werden, sonst eine lehre Liste.
     * @throws SQLException
     * @throws IOException
     */
    protected ArrayList<T> query(String sql, Object... objects) throws SQLException, IOException {
        ArrayList<T> result = new ArrayList<>();
        Connection connection = DBConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if(objects != null){
            for(int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i-1]);
            }
        }

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
            result.add(makeObject(resultSet));

        return result;
    }

    /**
     * Schablonenmethode , fuer INSERT, UPDATE oder DELETE Befehle, beim Insert wird hier der schluessel nicht mit
     * erzeugt.
     * @param sql Ein SQL INSERT, UPDATE oder DELETE Befehl bsp: UPDATE email SET content=? where id=?
     * @param objects die Objects die eingefuegt werden sollen, diese muessen in genau der selben Reihenfolge in der
     *                 Liste enthaten sein, wie die Fragezeichen im SQL String.
     * @return die anzahl der veraenderten Zeilen in der Datenbank.
     * @throws IOException
     * @throws SQLException
     */
    protected int updateOrDelete(String sql, Object... objects)throws IOException, SQLException {
        Connection connection = null;
        connection = DBConnectionManager.getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute("PRAGMA foreign_keys=ON");
        PreparedStatement statement = connection.prepareStatement(sql);
        if(objects != null) {
            for (int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i - 1]);
            }
        }
        int ret = statement.executeUpdate();
        return ret;
    }

    /**
     * Schablonenmethode fuer SQL INSERT Befehle, mit rueckgabe des erzeugten Schluessels.
     * @param sql ein SQL INSERT INTO Befehl.
     * @param objects die Objects die eingefuegt werden sollen, diese muessen in genau der selben Reihenfolge in der
     *                 Liste enthaten sein, wie die Fragezeichen im SQL String.
     * @return den erzeugten Schluessel.
     * @throws IOException
     * @throws SQLException
     */
    protected int insertAndReturnKey(String sql, Object... objects) throws IOException, SQLException {
        int key = -1;
        Connection connection = null;
        connection = DBConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if(objects != null) {
            for (int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i-1]);
            }
        }
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()){
            key = resultSet.getInt(1);
        }
        return key;
    }



    /**
     * Soweit unterstueztzung von nur INSERT INTO tabname where id = x
     * wobei x irgend ein primarykey ist.
     * @param sql
     * @return
     * @throws IOException
     * @throws SQLException
     */
    protected T find(String sql) throws IOException, SQLException {
        //TODO schauen ob diese Methode nicht besser geloest werden kann... muss doch gehen ohne eine ganze liste zu holen??
        Connection connection = DBConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.getResultSet();
        T object = null;
        if (resultSet.next()){
            object = makeObject(resultSet);
        }

        return object;
    }

}
