package sem.datenhaltung.semmodel.entities;

/**
 * Konto Entiy-Klasse
 */
public class Konto {

    private int kid;
    private String userName;
    private String passWort;
    private String iMAPhost;
    private String sMTPhost;
    private String emailAddress;
    private String signatur;
    private int port;

    //Konstruktor
    public Konto(){}


    public String getSignatur() {
        return signatur;
    }

    public void setSignatur(String signatur) {
        this.signatur = signatur;
    }

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

    public String getEmailAddress() {
        return emailAddress;
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

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }
}