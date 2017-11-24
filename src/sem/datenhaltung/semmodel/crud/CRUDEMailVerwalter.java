package sem.datenhaltung.semmodel.crud;

import sem.datenhaltung.semmodel.entities.EMail;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDEMailVerwalter extends DBMappingTemplate<EMail> {
    private static CRUDEMailVerwalter ourInstance = new CRUDEMailVerwalter();

    public static CRUDEMailVerwalter getInstance() {
        return ourInstance;
    }

    private CRUDEMailVerwalter() {
    }

    @Override
    protected EMail makeObject(ResultSet rs) {
        return null;
    }

    public void updateEMail(EMail emailTest) throws IOException, SQLException {
        String sql = "UPDATE EMail SET id = ? WHERE id = ?;";
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(3);
        objects.add(5);
        this.updateOrDelete(sql, objects);
    }

    public int insertEMail(EMail eMailTest) throws IOException, SQLException {
        int returnedKey = -1;
        String sql = "INSERT INTO EMail (fromaddr, content) VALUES (?, ?);";
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("w.amendt@live.de");
        objects.add("Irgendein Inhalt");
        returnedKey = this.insertAndReturnKey(sql, objects);
        System.out.println("returnedKey: " + returnedKey);
        return returnedKey;
    }

    public ResultSet findEMail(){return null;}
}
