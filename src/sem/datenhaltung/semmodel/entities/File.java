package sem.datenhaltung.semmodel.entities;

public class File {
    private int fid;
    private String pfad;
    private int mid;

    public int getMid() {
        return mid;
    }

    public void setMId(int mid) {
        this.mid = mid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getPfad() {
        return pfad;
    }

    public void setPfad(String pfad) {
        this.pfad = pfad;
    }
}
