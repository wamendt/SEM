package sem.fachlogik.grenzklassen;

import java.util.ArrayList;

public class EMailGrenz {
    private int mid;
    private String absender;
    private ArrayList<String> empfaenger;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private String betreff;
    private String inhalt;
    private String contentOriginal;
    private String zustand;
    private int messageID;
    private String ordner;
    private ArrayList<FileGrenz> files;

    public TagGrenz getTag() {
        return tag;
    }

    public void setTag(TagGrenz tag) {
        this.tag = tag;
    }

    private TagGrenz tag;

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

    public ArrayList<String> getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(ArrayList<String> empfaenger) {
        this.empfaenger = empfaenger;
    }

    public ArrayList<String> getCc() {
        return cc;
    }

    public void setCc(ArrayList<String> cc) {
        this.cc = cc;
    }

    public ArrayList<String> getBcc() {
        return bcc;
    }

    public void setBcc(ArrayList<String> bcc) {
        this.bcc = bcc;
    }

    public String getBetreff() {
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public ArrayList<FileGrenz> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileGrenz> files) {
        this.files = files;
    }

    public String getContentOriginal() {
        return contentOriginal;
    }

    public void setContentOriginal(String contentOriginal) {
        this.contentOriginal = contentOriginal;
    }

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
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
}
