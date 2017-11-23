package sem.datenhaltung.semmodel.entities;

import java.util.ArrayList;

public class EMail {
    private int mid;
    private String absender;
    private ArrayList<String> empfaenger;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private String betref;
    private String inhalt;
    private String zustand;

    //Kontruktor
    public EMail(){}

    //Hier evtl weitere Kontruktoren, wenn nötig


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

    public String getBetref() {
        return betref;
    }

    public void setBetref(String betref) {
        this.betref = betref;
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
}