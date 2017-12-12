package sem.fachlogik.assistentsteuerung.services;

import sem.fachlogik.grenzklassen.TagGrenz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IAssistentSteuerung {

    /**
     * Trainiert SEM mit den EMail instanzen aus der DB,
     * dabei werden Statistiken fuer jede EMail erstellt, sowie die passenden Tags
     * ausgewaehlt. Diese Methode erstellt ein komplett neues Model.
     */
    void trainiereSEM(int numTopics, double alphasum, double beta) throws IOException, SQLException;


    /**
     * Trainiert ein vorhandenes Modell, mit den Instanzen aus der DB.
     */
    void trainiereVorhandenSEM() throws IOException, SQLException;

    /**
     * Holt alle Tags aus der Datenbank und konvertiert diese in eine Liste aus
     * TagGrenz objekten.
     * @return Die Liste der TagGrenzObjekte.
     * @throws IOException
     * @throws SQLException
     */
    ArrayList<TagGrenz> zeigeAlleTagsAn() throws IOException, SQLException;

    boolean setzeTagNamen(String name);

    /**
     * Methode die ein weiteres Wort zur Stoplistehinzufuegt. Diese Woerter
     * werden dann vom Assistenten bei der Analyse ignoriert.
     * @param wort
     */
    void wortZurStoplisteHinzufuegen(String wort);

    void wortVonStoplisteEntfernen(String wort);

}
