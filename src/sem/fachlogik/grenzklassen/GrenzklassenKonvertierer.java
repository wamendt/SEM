package sem.fachlogik.grenzklassen;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.File;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.ICRUDFile;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTag;
import sem.datenhaltung.semmodel.services.ICRUDWort;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klasse Zum konvertieren der Datenbank eintraege in Grenzobjekte, auch die Aussoziationen der einzelnen
 * Objekte zu einander werden hier hergestellt.
 */
public class GrenzklassenKonvertierer {

    private GrenzklassenKonvertierer(){}
    
    public static EMailGrenz eMailZuEMailGrenz(EMail email){
        EMailGrenz eMailGrenz = null;
        try {
            Tag tag = ICRUDManagerSingleton.getIcrudTagInstance().getTagById(email.getTid());
            eMailGrenz = new EMailGrenz();
            eMailGrenz.setTag(GrenzklassenKonvertierer.tagZuTagGrenz(tag));
            eMailGrenz.setOrdner(email.getOrdner());
            eMailGrenz.setInhalt(email.getInhalt());
            eMailGrenz.setContentOriginal(email.getContentOriginal());
            eMailGrenz.setOrdner(email.getOrdner());
            eMailGrenz.setBetreff(email.getBetreff());
            eMailGrenz.setMid(email.getMid());

            ArrayList<File> files = ICRUDManagerSingleton.getIcrudFileInstance().getAlleFilesMitEMailId(email.getMid());
            ArrayList<FileGrenz> fileGrenzs = new ArrayList<>();
            for(File f : files){
                fileGrenzs.add(GrenzklassenKonvertierer.FileZuFileGrenz(f));
            }
            eMailGrenz.setFiles(fileGrenzs);

            //TODO BCC und CC konvertieren, fuers erste nur eine lehre liste
            eMailGrenz.setBcc(new ArrayList<>());
            eMailGrenz.setCc(new ArrayList<>());

            //TODO brauchen wir noch einen zustand und msgid?
            eMailGrenz.setZustand(email.getZustand());
            eMailGrenz.setMessageID(email.getMessageID());



        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eMailGrenz;
    }

    //TODO
    public static EMail EMailGrenzZuEMail(EMailGrenz eMailGrenz){
        return null;
    }

    public static FileGrenz FileZuFileGrenz(File file){
        FileGrenz fileGrenz = new FileGrenz();
        fileGrenz.setFid(file.getFid());
       //TODO mid wird nur fuer die verwaltung in der db gebraucht, brauchen wir das in der steuerung noch?
        fileGrenz.setMId(file.getMid());
        fileGrenz.setName(file.getName());
        fileGrenz.setPfad(file.getPfad());
        return fileGrenz;
    }

    public static TagGrenz tagZuTagGrenz(Tag tag){
        ICRUDWort crudword = ICRUDManagerSingleton.getIcrudWordInstance();
        
        TagGrenz tagGrenz = null;
        try {
            ArrayList<Wort> woerter = crudword.getAlleWoerterMitTagId(tag.getTid());
            ArrayList<String> stringWoerter = new ArrayList<>();

            tagGrenz = new TagGrenz();
            for(Wort w : woerter){
                stringWoerter.add(w.getWort());
            }
            tagGrenz.setWoerter(stringWoerter);
            tagGrenz = new TagGrenz();
            tagGrenz.setTid(tag.getTid());
            tagGrenz.setName(tag.getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tagGrenz;
    }
    
 
}
