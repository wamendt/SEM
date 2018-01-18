package sem.datenhaltung.semmodel.database;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private static final String URL = "jdbc:sqlite:SEMDB.sqlite";  //"jdbc:sqlite:SEMDB" + username + ".sqlite"
    private static Connection connection;

    private DBConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        if(connection == null){
            SQLiteConfig config = new SQLiteConfig();
            config.setOpenMode(SQLiteOpenMode.FULLMUTEX);
            connection = DriverManager.getConnection(URL, config.toProperties());

        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if(connection != null) {
            connection.close();
            connection = null;
        }
    }
}
