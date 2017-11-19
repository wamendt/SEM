package sem.fachlogik.grenzklassen;

import java.util.Date;

public class EMailGrenz {
    private String absender;
    private String empfaenger;
    private String ccEmpfaenger;
    private String betreff;
    private String inhalt;
    private Date erstellungsDatum;
    private boolean gelesen;


    //Kontruktor
    public EMailGrenz(){}

    //Hier evtl weitere Kontruktoren, wenn nötig


    //Getter
    public String getAbsender() {
        return absender;
    }

    public String getEmpfaenger() {
        return empfaenger;
    }

    public String getCcEmpfaenger() {
        return ccEmpfaenger;
    }

    public String getBetreff() {
        return betreff;
    }

    public String getInhalt() {
        return inhalt;
    }

    public Date getErstellungsDatum() {
        return erstellungsDatum;
    }

    public boolean getGelesen() {
        return gelesen;
    }


    //Setter
    public void setAbsender(String absender) {
        this.absender = absender;
    }

    public void setEmpfaenger(String empfaenger) {
        this.empfaenger = empfaenger;
    }

    public void setCcEmpfaenger(String ccEmpfaenger) {
        this.ccEmpfaenger = ccEmpfaenger;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public void setErstellungsDatum(Date erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }

    public void setGelesen(boolean gelesen) {
        this.gelesen = gelesen;
    }
}
