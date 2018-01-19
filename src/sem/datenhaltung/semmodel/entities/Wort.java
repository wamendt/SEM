package sem.datenhaltung.semmodel.entities;

/**
 * Wort Entity Klasse, repraesentiert ein Wort das mit einem Tag (tid) verknuepft ist.
 */
public class Wort {
    private int wid;
    private String wort;
    private int tid;

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getWort() {
        return wort;
    }

    public void setWort(String wort) {
        this.wort = wort;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
