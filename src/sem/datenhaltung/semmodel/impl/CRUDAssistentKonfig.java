package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.datenhaltung.semmodel.entities.Regel;
import sem.datenhaltung.semmodel.services.ICRUDAssistentKonfig;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDAssistentKonfig extends DBCRUDTeamplate<AssistentKonfig> implements ICRUDAssistentKonfig{

    @Override
    protected AssistentKonfig makeObject(ResultSet rs) throws SQLException, IOException {
        AssistentKonfig konfig = new AssistentKonfig();

        konfig.setAlphaWert(rs.getDouble(1));
        konfig.setAnzahlDurchlauf(rs.getInt(2));
        konfig.setAnzahlTags(rs.getInt(3));
        konfig.setBetaWert(rs.getInt(4));
        konfig.setStopwordPfad(rs.getString(5));

        return konfig;
    }

    @Override
    public boolean createAssistentKonfig(AssistentKonfig konfig) throws IOException, SQLException{
        return true;
    }

    @Override
    public boolean deleteAssistentKonfig(int aid) throws IOException, SQLException{
        return true;
    }


    @Override
    public boolean updateAssistentKonfig(AssistentKonfig konfig) throws IOException, SQLException{
        return true;
    }


    @Override
    public AssistentKonfig getAssistentKonfig(int aid) throws IOException, SQLException{
        AssistentKonfig konfig = new AssistentKonfig();
        return konfig;
    }


    @Override
    public ArrayList<AssistentKonfig> getAlleAssistentKonfig() throws IOException, SQLException{
        ArrayList<AssistentKonfig> alleAssistentKonfig = new ArrayList<>();
        return alleAssistentKonfig;
    }


    @Override
    public boolean createRegel(Regel regel) throws IOException, SQLException{
        return true;
    }


    @Override
    public boolean deleteRegel(int rid) throws IOException, SQLException{
        return true;
    }


    @Override
    public boolean updateRegel(Regel regel) throws IOException, SQLException{
        return true;
    }


    @Override
    public Regel getRegelById(int rid) throws IOException, SQLException{
        Regel regel = new Regel();
        return regel;
    }


    @Override
    public ArrayList<Regel> getAlleRegeln() throws IOException, SQLException{
        ArrayList<Regel> alleRegeln = new ArrayList<Regel>();
        return alleRegeln;
    }


    @Override
    public boolean addRollback(int emailId, int tagId) throws IOException, SQLException{
        return true;
    }


    @Override
    public boolean deleteRollback() throws IOException, SQLException{
        return true;
    }


}
