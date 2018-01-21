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

    protected static final String SQL_SELECT_FROM_WHERE = "SELECT * FROM %s WHERE %s = ?";
    protected static final String SQL_SELECT_FROM = "SELECT * FROM %s ";
    protected static final String SQL_DELETE_FROM_WHERE = "DELETE FROM %s WHERE %s = ? ";
    protected static final String SQL_DELETE_FROM = "DELETE FROM %s";

    /**
     * Erstelt ein entspraechendes Entity Objekt
     * @param rs das Anfrage Ergebnis
     * @return
     */
    protected abstract T makeObject(ResultSet rs) throws SQLException;


    /**
     * Schablonenmethode die alle T Obekte aus der Datanbank hohlt.
     * @param sql eine SQL SELECT Befehl bsp: SELECT * FROM email
     * @return eine Liste mit den Anfrageergebnissen, falls welche gefunden werden, sonst eine lehre Liste.
     * @throws SQLException
     * @throws IOException
     */
    protected ArrayList<T> query(String sql, Object... objects) throws SQLException{
        ArrayList<T> result = new ArrayList<>();
        Connection connection = DBConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if(objects != null){
            for(int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i-1]);
            }
        }

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            result.add(makeObject(resultSet));
        }
        DBConnectionManager.closeConnection();
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
    protected int updateOrDelete(String sql, Object... objects)throws SQLException {
        Connection connection = null;
        int ret = -1;
        try {
            connection = DBConnectionManager.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys=ON");
            PreparedStatement statement = connection.prepareStatement(sql);
            if (objects != null) {
                for (int i = 1; i <= objects.length; i++) {
                    statement.setObject(i, objects[i - 1]);
                }
            }
            ret = statement.executeUpdate();
        }finally {
            DBConnectionManager.closeConnection();

        }
        return ret;
    }

    /**
     * Schablonenmethode fuer SQL INSERT Befehle, mit rueckgabe des erzeugten Schluessels.
     * @param sql ein SQL INSERT INTO Befehl.
     * @param objects die Objects die eingefuegt werden sollen, diese muessen in genau der selben Reihenfolge in der
     *                 Liste enthaten sein, wie die Fragezeichen im SQL String.
     * @return den erzeugten Schluessel bei Erfolg, sonst -1.
     * @throws IOException
     * @throws SQLException
     */
    protected int insertAndReturnKey(String sql, Object... objects) throws SQLException {
        int key = -1;
        Connection connection = null;
        try {
            connection = DBConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            if (objects != null) {
                for (int i = 1; i <= objects.length; i++) {
                    statement.setObject(i, objects[i - 1]);
                }
            }
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            }
            statement.clearParameters();
            statement.close();
        }finally {
            DBConnectionManager.closeConnection();
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
    protected T find(String sql) throws SQLException {
        //TODO schauen ob diese Methode nicht besser geloest werden kann... muss doch gehen ohne eine ganze liste zu holen??
        Connection connection = DBConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.getResultSet();
        T object = null;
        if (resultSet.next()){
            object = makeObject(resultSet);
        }
        preparedStatement.clearParameters();
        preparedStatement.close();
        DBConnectionManager.closeConnection();
        return object;
    }


    protected boolean create(String sql) throws SQLException {
        Connection connection = DBConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        boolean ret = preparedStatement.execute();
        preparedStatement.clearParameters();
        preparedStatement.close();
        DBConnectionManager.closeConnection();
        return ret;
    }
}
