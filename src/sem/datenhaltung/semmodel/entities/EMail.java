package sem.datenhaltung.semmodel.entities;

public class EMail {
    private int mid;
    private String absender;
    private String betref;
    private String inhalt;
    private String zustand;
    private int tid;

    //Konstruktor
    public EMail(){}

    //Hier evtl weitere Kontruktoren, wenn nötig


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