package sem.datenhaltung.maildaten.services;

import sem.datenhaltung.entities.Connection;
import sem.datenhaltung.entities.EMail;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.KontoGrenz;

import java.util.ArrayList;

public interface IMailService {
    public boolean sendEmail(EMail email);
    public boolean importAllEMails(KontoGrenz kontoGrenz, Connection connection);
    public ArrayList<EMailGrenz> getAllEmails(KontoGrenz kontoGrenz);
    public ArrayList<String> getAllFoldersFromAccount(KontoGrenz kontoGrenz);
    public String getFolderByID(int id, String name);
    public boolean createEMailTable(String path);
    public boolean deleteEMailTable(String path);
    public boolean createEMailEntwurfTable(String path);
    public boolean deleteEMailEntwurfTable(String path);
    public boolean forwardEMail(EMailGrenz mailGrenz);

}
