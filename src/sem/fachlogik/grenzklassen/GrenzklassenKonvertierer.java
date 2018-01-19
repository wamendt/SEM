package sem.fachlogik.grenzklassen;

import sem.datenhaltung.semmodel.entities.*;
import sem.datenhaltung.semmodel.services.ICRUDFile;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTag;
import sem.datenhaltung.semmodel.services.ICRUDWort;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klasse Zum konvertieren der Datenbank eintraege in Grenzobjekte, auch die Aussoziationen der einzelnen
 * Objekte zu einander werden hier hergestellt.
 */
public class GrenzklassenKonvertierer {

    private GrenzklassenKonvertierer(){}

    public static KontoGrenz KontoZuKontoGrenz(Konto konto) {
        KontoGrenz kontoGrenz = new KontoGrenz();
        kontoGrenz.setUserName(konto.getUserName());
        kontoGrenz.setPassWort(konto.getPassWort());
        kontoGrenz.setIMAPhost(konto.getIMAPhost());
        kontoGrenz.setSMTPhost(konto.getSMTPhost());
        kontoGrenz.setEmailAddress(konto.getEmailAddress());
        kontoGrenz.setPort(konto.getPort());
        return kontoGrenz;
    }


    public static Konto KontoGrenzZuKonto(KontoGrenz kontoGrenz) {
        Konto konto = new Konto();
        konto.setUserName(kontoGrenz.getUserName());
        konto.setPassWort(kontoGrenz.getPassWort());
        konto.setIMAPhost(kontoGrenz.getIMAPhost());
        konto.setSMTPhost(kontoGrenz.getSMTPhost());
        konto.setEmailAddress(kontoGrenz.getEmailAddress());
        konto.setPort(kontoGrenz.getPort());
        return konto;
    }

    
    public static EMailGrenz EMailZuEMailGrenz(EMail email){
        EMailGrenz eMailGrenz = null;
        Tag tag = ICRUDManagerSingleton.getIcrudTagInstance().getTagById(email.getTid());
        eMailGrenz = new EMailGrenz();
        if(tag != null){
            eMailGrenz.setTag(GrenzklassenKonvertierer.tagZuTagGrenz(tag));
        }
        ArrayList<String> empfaengerList = new ArrayList<>();
        empfaengerList.add(email.getAbsender());
        eMailGrenz.setAbsender(email.getAbsender());
        eMailGrenz.setEmpfaenger(empfaengerList);
        eMailGrenz.setOrdner(email.getOrdner());
        eMailGrenz.setInhalt(email.getInhalt());
        eMailGrenz.setContentOriginal(email.getContentOriginal());
        eMailGrenz.setOrdner(email.getOrdner());
        eMailGrenz.setBetreff(email.getBetreff());
        eMailGrenz.setMid(email.getMid());

        ArrayList<File> files = ICRUDManagerSingleton.getIcrudFileInstance().getAlleFilesMitEMailId(email.getMid());
        ArrayList<FileGrenz> fileGrenzs = new ArrayList<>();
        if(files.size() > 0){
            for(File f : files){
                fileGrenzs.add(GrenzklassenKonvertierer.FileZuFileGrenz(f));
            }
            eMailGrenz.setFiles(fileGrenzs);
        }

        //TODO BCC und CC konvertieren, fuers erste nur eine lehre liste
        eMailGrenz.setBcc(new ArrayList<>());
        eMailGrenz.setCc(new ArrayList<>());

        //TODO brauchen wir noch einen zustand und msgid?
        eMailGrenz.setZustand(email.getZustand());
        eMailGrenz.setMessageID(email.getMessageID());

        return eMailGrenz;
    }

    //TODO
    public static EMail EMailGrenzZuEMail(EMailGrenz eMailGrenz){
        EMail eMail = new EMail();
        eMail.setMid(eMailGrenz.getMid());
        eMail.setBetref(eMailGrenz.getBetreff());
        eMail.setInhalt(eMailGrenz.getInhalt());
        if(eMailGrenz.getTag() != null){
            eMail.setTid(eMailGrenz.getTag().getTid());
        }
        eMail.setAbsender(eMailGrenz.getAbsender());

        String ccList = explodeAdresses(eMailGrenz.getCc());
        eMail.setCc(ccList);

        String bccList = explodeAdresses(eMailGrenz.getBcc());
        eMail.setBcc(bccList);

        String empfaengerList = explodeAdresses(eMailGrenz.getEmpfaenger());
        eMail.setEmpfaenger(empfaengerList);

        eMail.setContentOriginal(eMailGrenz.getContentOriginal());
        eMail.setZustand(eMailGrenz.getZustand());
        eMail.setMessageID(eMailGrenz.getMessageID());
        eMail.setOrdner(eMailGrenz.getOrdner());

        try{
            //if(eMail.getFiles().size() > 0) {
            if(eMail.getFiles() != null) {
                ArrayList<File> fileList = new ArrayList<>();
                for (FileGrenz fileGrenz : eMailGrenz.getFiles()) {
                    File file = FileGrenzZuFile(fileGrenz);
                    fileList.add(file);
                }
                eMail.setFiles(fileList);
            }
        }
        catch (NullPointerException e){
            System.out.println("NullPointerException wurde geworfen in IMailSteuerung, getEMail(): " + e.getMessage());
        }
        return eMail;
    }

    public static File FileGrenzZuFile(FileGrenz fileGrenz) {
        File file = new File();
        file.setFid(fileGrenz.getFid());
        file.setMId(fileGrenz.getMid());
        file.setPfad(fileGrenz.getPfad());
        file.setName(fileGrenz.getName());
        return file;
    }


    public static FileGrenz FileZuFileGrenz(File file) {
        FileGrenz fileGrenz = new FileGrenz();
        fileGrenz.setFid(file.getFid());
        fileGrenz.setMId(file.getMid());
        fileGrenz.setPfad(file.getPfad());
        fileGrenz.setName(file.getName());
        return fileGrenz;
    }

    public static TagGrenz tagZuTagGrenz(Tag tag){
        ICRUDWort crudword = ICRUDManagerSingleton.getIcrudWordInstance();
        
        TagGrenz tagGrenz = new TagGrenz();
        ArrayList<Wort> woerter = crudword.getAlleWoerterMitTagId(tag.getTid());
        ArrayList<String> stringWoerter = new ArrayList<>();
        for(Wort w : woerter){
            stringWoerter.add(w.getWort());
        }
        tagGrenz.setWoerter(stringWoerter);
        tagGrenz.setTid(tag.getTid());
        tagGrenz.setName(tag.getName());

        return tagGrenz;
    }

    public static Tag tagGrenzZuTag(TagGrenz tagGrenz){
        Tag tag = new Tag();
        tag.setName(tagGrenz.getName());
        tag.setTid(tagGrenz.getTid());

        return tag;
    }


    //Zusatzfunktionen
    private static String explodeAdresses(ArrayList<String> adresses) {
        String adressString = "";
        if(adresses.size() > 0){
            for (String adress: adresses){
                adressString = adressString.concat(adress + ";");
            }
        }
        return adressString;
    }

    public ArrayList<String> implodeAdresses(String adresses) {
        ArrayList<String> adressList = null;
        if(adresses != null){
            String[] adressArray = adresses.split(";");
            adressList = new ArrayList<>();
            adressList.addAll(Arrays.asList(adressArray));
        }
        return adressList;
    }
 
}
