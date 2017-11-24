package sem.datenhaltung.semmodel.crud.services;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.fachlogik.grenzklassen.EMailGrenz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDMail {
    public int erstelleEMail (EMail mail) throws IOException, SQLException;
    public EMail loescheEMail(int id);
    public EMail getEMailByID(int id);
    public ArrayList<EMail> getAlleEMails();
    public boolean updateEMail(EMail mail) throws IOException, SQLException;
    public ArrayList<EMail> getAlleEMailsVomOrdner(String pfad);
}
