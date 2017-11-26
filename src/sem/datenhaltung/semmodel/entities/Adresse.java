package sem.datenhaltung.semmodel.entities;

public class Adresse {

    private int aid;
    private String adresse;
    private String name;
    private int mid;
    private String zustand;


    public void setAid(int aid) {
        this.aid = aid;
    }
    public int getAid(){
        return aid;
    }
    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String addresse) {
        this.adresse = addresse;
    }
}
