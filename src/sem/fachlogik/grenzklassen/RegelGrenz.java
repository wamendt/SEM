package sem.fachlogik.grenzklassen;

public class RegelGrenz {
    private int rid;
    private String beschreibung;
    private String zuordner;
    private boolean isActive;
    private String vonemailaddress;

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

    public String getZuordner() {
        return zuordner;
    }

    public void setZuordner(String zuordner) {
        this.zuordner = zuordner;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getVonemailaddress() {
        return vonemailaddress;
    }

    public void setVonemailaddress(String vonemailaddress) {
        this.vonemailaddress = vonemailaddress;
    }
}
