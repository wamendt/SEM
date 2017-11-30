package sem.fachlogik.grenzklassen;

import java.util.ArrayList;

public class TagGrenz {
    private int tid;
    private String name;
    private int numIndex;
    private ArrayList<String> woerter;

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

    public int getNumIndex() {
        return numIndex;
    }

    public void setNumIndex(int numIndex) {
        this.numIndex = numIndex;
    }

    public ArrayList<String> getWoerter() {
        return woerter;
    }

    public void setWoerter(ArrayList<String> woerter) {
        this.woerter = woerter;
    }
}
