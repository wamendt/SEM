package sem.datenhaltung.semmodel.entities;

public class TagVerteilung {
    private int tvid;
    private double verteilung;
    private int mid;

    public TagVerteilung() {
    }

    public TagVerteilung(double verteilung, int mid) {
        this.verteilung = verteilung;
        this.mid = mid;
    }


    public int getTvid() {
        return tvid;
    }

    public void setTvid(int tvid) {
        this.tvid = tvid;
    }

    public double getVerteilung() {
        return verteilung;
    }

    public void setVerteilung(double verteilung) {
        this.verteilung = verteilung;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
