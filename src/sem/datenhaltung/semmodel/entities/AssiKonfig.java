package sem.datenhaltung.semmodel.entities;

public class AssiKonfig {
    private int aid;
    private int anzahlDurchlauf;
    private int anzahlTags;
    private double alphaWert;
    private double betaWert;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAnzahlDurchlauf() {
        return anzahlDurchlauf;
    }

    public void setAnzahlDurchlauf(int anzahlDurchlauf) {
        this.anzahlDurchlauf = anzahlDurchlauf;
    }

    public int getAnzahlTags() {
        return anzahlTags;
    }

    public void setAnzahlTags(int anzahlTags) {
        this.anzahlTags = anzahlTags;
    }

    public double getAlphaWert() {
        return alphaWert;
    }

    public void setAlphaWert(double alphaWert) {
        this.alphaWert = alphaWert;
    }

    public double getBetaWert() {
        return betaWert;
    }

    public void setBetaWert(double betaWert) {
        this.betaWert = betaWert;
    }
}
