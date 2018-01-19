package sem.datenhaltung.semmodel.entities;


/**
 * Tag Entity Klasse, Representiert eine Kategorie zu der die Emails zugeordnet worden sind.
 */
public class Tag {
    private int tid;
    private String name;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
