package sem.datenhaltung.semmodel.crud.impl;

import sem.datenhaltung.semmodel.crud.services.ICRUDMail;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.template.DBMappingTemplate;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ICRUDMailImpl extends DBMappingTemplate<EMail> implements ICRUDMail {


    @Override
    protected EMail makeObject(ResultSet rs) {
        return null;
    }


    public ResultSet findEMail(){return null;}

    @Override
    public int erstelleEMail(EMail mail) throws IOException, SQLException {
        int returnedKey = -1;
        String sql = "INSERT INTO EMail (fromaddr, content) VALUES (?, ?);";
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("w.amendt@live.de");
        objects.add("Irgendein Inhalt");
        returnedKey = this.insertAndReturnKey(sql, objects);
        System.out.println("returnedKey: " + returnedKey);
        return returnedKey;
    }

    @Override
    public EMail loescheEMail(int id) {
        return null;
    }

    @Override
    public EMail getEMailByID(int id) {
        return null;
    }

    @Override
    public ArrayList<EMail> getAlleEMails() {
        return null;
    }

    @Override
    public boolean updateEMail(EMail mail) throws IOException, SQLException {
        String sql = "UPDATE EMail SET id = ? WHERE id = ?;";
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(3);
        objects.add(5);
        return this.updateOrDelete(sql, objects);
    }

    @Override
    public ArrayList<EMail> getAlleEMailsVomOrdner(String pfad) {
        return null;
    }
}
