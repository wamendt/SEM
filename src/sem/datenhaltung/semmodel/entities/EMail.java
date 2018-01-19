package sem.datenhaltung.semmodel.entities;

import java.util.ArrayList;

/**
 * Email Enetity Klasse
 */
public class EMail {
    private int mid;
    private String betreff;
    private String inhalt;
    private int tid;
    private String absender;
    private String cc;
    private String bcc;
    private String empfaenger;
    private String contentOriginal;
    private String zustand;
    private int messageID;
    private String ordner;
    private ArrayList<File> files;

    //Konstruktor
    public EMail(){}

    //Hier evtl weitere Kontruktoren, wenn nötig


    //Getter und Setter


    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getAbsender() {
        return absender;
    }

    public void setAbsender(String absender) {
        this.absender = absender;
    }

    public String getBetreff() {
        return betreff;
    }


    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(String empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getContentOriginal() {
        return contentOriginal;
    }

    public void setContentOriginal(String contentOriginal) {
        this.contentOriginal = contentOriginal;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getOrdner() {
        return ordner;
    }

    public void setOrdner(String ordner) {
        this.ordner = ordner;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
}