package sem.datenhaltung.semmodel.entities;

import java.util.ArrayList;

public class Tag {
    private int tid;
    private String name;
    private int numIndenx;
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

    public int getNumIndenx() {
        return numIndenx;
    }

    public void setNumIndenx(int numIndenx) {
        this.numIndenx = numIndenx;
    }

    public ArrayList<String> getWoerter() {
        return woerter;
    }

    public void setWoerter(ArrayList<String> woerter) {
        this.woerter = woerter;
    }
}
