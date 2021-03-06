package sem.datenhaltung.semmodel.database;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Klasse die Eine Verbindung mit der Datenbank bereitstellt. Bietet ZugriffsMethoden fuer das Connection Objekt,
 * mit dem dann Sql Statements ausgefuehrt werden koennen.
 */
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
            //Scheint SQLLite spezifisch zu sein, dass mann auch mit nur einer verbindung zurecht kommt.
            //connection.close();
            //connection = null;
        }
    }

}
