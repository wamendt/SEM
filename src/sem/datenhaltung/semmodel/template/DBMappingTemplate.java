package sem.datenhaltung.semmodel.template;

import sem.datenhaltung.semmodel.databasemanager.SEMModelManager;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * Die generische Klasse DBMappingTemplate<T> enthaelt die Methoden insertAndReturnKey, delete, update, find, findAll und
 * insertAndReturnKey sowie die abstrakte Methode makeObject , die in konkreten
 * Subklassen implementiert werden muss. query , update und insertAndReturnKey nutzen
 * einen SQL-Befehl, der als Aufrufparameter mitgegeben wird. query gibt ein List -
 * Objekt zurück, dessen Eintraege mittels makeObject anwendungsabhaengig (siehe
 * EmailBroker ) erzeugt werden. fuer Einzelheiten siehe JDBC Dokumentation
 * Die Methode insertAndReturnKey enthaelt eine Besonderheit. Der zweite Parameter der
 * Statement -Methode executeUpdate bestimmt, dass der von der Datenbank automatisch
 * generierte Schluessel ueber die Statement -Methode getGeneratedKeys zur Verfuegung
 * gestellt wird
 * @param <T>
 */


public abstract class DBMappingTemplate<T> {

    protected abstract T makeObject(ResultSet rs);

    protected ArrayList<T> findAll(String sql) throws SQLException, IOException {
        ArrayList<T> result = new ArrayList<>();
        Connection connection = null;
        connection = SEMModelManager.getConnection();
        try(Statement stmt = connection.createStatement()){
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()){
                result.add(makeObject(resultSet));
            }
        }
        return result;
    }


    protected boolean updateOrDelete(String sql, ArrayList<Object> wertList)throws IOException, SQLException {
        Connection connection = null;
        connection = SEMModelManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i <= wertList.size(); i++){
            statement.setObject(i, wertList.get(i - 1));
        }
        statement.executeUpdate();
        return true;
    }

    protected int insertAndReturnKey(String sql, ArrayList<Object> wertList) throws IOException, SQLException {
        int key = -1;
        Connection connection = null;
        connection = SEMModelManager.getConnection();
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

    protected T find(String sql) throws IOException, SQLException {
        Connection connection = SEMModelManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.getResultSet();
        T object = null;
        if (resultSet.next()){
            object = makeObject(resultSet);
        }

        return object;
    }
}
