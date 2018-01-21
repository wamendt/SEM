package sem.fachlogik.grenzklassen;

import sem.datenhaltung.semmodel.entities.Regel;

import java.util.ArrayList;

public class KontoGrenz {

    private int kid;
    private String userName;
    private String passWort;
    private String iMAPhost;
    private String sMTPhost;
    private int port;
    private ArrayList<Regel> regeln;


    public KontoGrenz(){}


    public int getKid() {
        return kid;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWort() {
        return passWort;
    }


    public String getIMAPhost() {
        return iMAPhost;
    }

    public String getSMTPhost() {
        return sMTPhost;
    }


    public int getPort() {
        return port;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWort(String passWort) {
        this.passWort = passWort;
    }

    public void setIMAPhost(String iMAPhost) {
        this.iMAPhost = iMAPhost;
    }

    public void setSMTPhost(String sMTPhost) {
        this.sMTPhost = sMTPhost;
    }


    public void setPort(int port) {
        this.port = port;
    }
}
