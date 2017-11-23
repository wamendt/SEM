package sem.datenhaltung.maildaten.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.maildaten.services.ICRUDMail;
import sem.fachlogik.grenzklassen.EMailGrenz;

import java.util.ArrayList;

public class ICRUDMailImpl implements ICRUDMail{
    @Override
    public boolean createEMail(EMailGrenz mailGrenz) {
        return false;
    }

    @Override
    public boolean saveEMailEntwurf(EMailGrenz mailGrenz) {
        return false;
    }

    @Override
    public EMail deleteEMailEntwurfByID(int id) {
        return null;
    }

    @Override
    public EMail getEMailEntwurfByID(int id) {
        return null;
    }

    @Override
    public ArrayList<EMail> getAlleEMailEntwuerfe() {
        return null;
    }

    @Override
    public EMail updateEMailEntwurf(int ID) {
        return null;
    }
}
