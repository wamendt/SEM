package sem.datenhaltung.semmodel.crud.impl;

import sem.datenhaltung.semmodel.databasemanager.ConnectionManager;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * Die generische Klasse DBMappingTemplate<T> enthaelt die Schablonenmethoden zum Suchen, Loeschen,
 * Veraendern und Einfuegen in eine Datenbank. Dafuer muss makeObject, in den konkreten Subklassen implementiert
 * werden.
 * makeObject muss in den konkreten Subklassen implementiert werden.
 * @param <T> Ein Entity - Objekt.
 */


public abstract class DBMappingTemplate<T> {

    /**
     * Erstelt ein entspraechendes Entity Objekt
     * @param rs das Anfrage Ergebnis
     * @return
     */
    protected abstract T makeObject(ResultSet rs);

    /**
     * Schablonenmethode die alle T Obekte aus der Datanbank hohlt.
     * @param sql eine SQL SELECT Befehl bsp: SELECT * FROM email
     * @return eine Liste mit den Anfrageergebnissen, falls welche gefunden werden, sonst eine lehre Liste.
     * @throws SQLException
     * @throws IOException
     */
    protected ArrayList<T> findAll(String sql) throws SQLException, IOException {
        ArrayList<T> result = new ArrayList<>();
        Connection connection = ConnectionManager.getConnection();
        try(Statement stmt = connection.createStatement()){
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()){
                result.add(makeObject(resultSet));
            }
        }
        return result;
    }

    /**
     * Schablonenmethode , fuer INSERT, UPDATE oder DELETE Befehle, beim Insert wird hier der schluessel nicht mit
     * erzeugt.
     * @param sql Ein SQL INSERT, UPDATE oder DELETE Befehle bsp: UPDATE email SET content=(?) where id= (?)
     * @param wertList die Objects die eingefuegt werden sollen, diese muessen in genau der selben Reihenfolge in der
     *                 Liste enthaten sein, wie die Fragezeichen im SQL String.
     * @return die anzahl der veraenderten Zeilen in der Datenbank.
     * @throws IOException
     * @throws SQLException
     */
    protected int updateOrDelete(String sql, ArrayList<Object> wertList)throws IOException, SQLException {
        Connection connection = null;
        connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i <= wertList.size(); i++){
            statement.setObject(i, wertList.get(i - 1));
        }
        int ret = statement.executeUpdate();
        return ret;
    }

    /**
     * Schablonenmethode fuer SQL INSERT Befehle, mit rueckgabe des erzeugten Schluessels.
     * @param sql ein SQL INSERT INTO Befehl.
     * @param wertList die Objects die eingefuegt werden sollen, diese muessen in genau der selben Reihenfolge in der
     *                 Liste enthaten sein, wie die Fragezeichen im SQL String.
     * @return den erzeugten Schluessel.
     * @throws IOException
     * @throws SQLException
     */
    protected int insertAndReturnKey(String sql, ArrayList<Object> wertList) throws IOException, SQLException {
        int key = -1;
        Connection connection = null;
        connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i <= wertList.size(); i++){
            statement.setObject(i, wertList.get(i - 1));
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
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.getResultSet();
        T object = null;
        if (resultSet.next()){
            object = makeObject(resultSet);
        }

        return object;
    }
}
