package sem.datenhaltung.semmodel.databasemanager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SEMModelManager {
    private static final String URL = "jdbc:sqlite:SEMDB.sqlite";  //"jdbc:sqlite:SEMDB" + username + ".sqlite"
    private static Connection connection;

    private SEMModelManager() {
    }

    public static Connection getConnection() throws SQLException, IOException {
        if(connection == null){
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static boolean closeConnection() throws SQLException {
        boolean ret = false;
        if(connection != null) {
            connection.close();
            connection = null;
            ret = true;
        }
        return ret;
    }
}
