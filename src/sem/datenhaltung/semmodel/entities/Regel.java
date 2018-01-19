package sem.datenhaltung.semmodel.entities;

/**
 * Regel Enetity Klasse
 *  Representiert eine Regel, die Der Anwender erstellen kann, Diese Regel sagt aus von welchem Ordner in welchem anderen
 *  Ordner die Emails verschoben werden sollen.
 */
public class Regel {
    private int rid;
    private String beschreibung;
    private String vonOrdner;
    private String zuOrdner;
    private int kid;
    private boolean isActive;

    public Regel(String beschreibung, String vonOrdner, String zuOrdner, int kid, boolean isActive) {
        this.beschreibung = beschreibung;
        this.vonOrdner = vonOrdner;
        this.zuOrdner = zuOrdner;
        this.kid = kid;
        this.isActive = isActive;
    }

    public Regel() { }

    public String getVonOrdner() {
        return vonOrdner;
    }

    public void setVonOrdner(String vonOrdner) {
        this.vonOrdner = vonOrdner;
    }

    public String getZuOrdner() {
        return zuOrdner;
    }

    public void setZuOrdner(String zuOrdner) {
        this.zuOrdner = zuOrdner;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
