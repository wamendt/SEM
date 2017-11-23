package sem.datenhaltung.semmodel.entities;

public class Assistent {
    private static Assistent assistentInstance = new Assistent();
    private String tag;
    private String topic;
    private String regel;

    //Getter für Singleton-Instanz
    public static Assistent getInstance() {
        return assistentInstance;
    }

    //Konstruktor
    private Assistent() {
    }


    //Getter
    public String getTag() {
        return tag;
    }

    public String getTopic() {
        return topic;
    }

    public String getRegel() {
        return regel;
    }


    //Setter
    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setRegel(String regel) {
        this.regel = regel;
    }
}