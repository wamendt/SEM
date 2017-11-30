package sem.datenhaltung.semmodel.entities;

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
    private String messageID;

    //Konstruktor
    public EMail(){}

    //Hier evtl weitere Kontruktoren, wenn nötig


    //Getter und Setter
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

    public void setBetref(String betreff) {
        this.betreff = betreff;
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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}