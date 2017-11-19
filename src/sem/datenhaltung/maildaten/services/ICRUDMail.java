package sem.datenhaltung.maildaten.services;

import sem.datenhaltung.entities.EMail;
import sem.fachlogik.grenzklassen.EMailGrenz;

import java.util.ArrayList;

public interface ICRUDMail {
    public boolean createEMail (EMailGrenz mailGrenz);
    public boolean saveEMailEntwurf(EMailGrenz mailGrenz);
    public EMail deleteEMailEntwurfByID(int id);
    public EMail getEMailEntwurfByID(int id);
    public ArrayList<EMail> getAlleEMailEntwuerfe();
    public EMail updateEMailEntwurf(int ID);
}
