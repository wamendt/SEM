package sem.fachlogik.assistentsteuerung.services;

import sem.fachlogik.grenzklassen.TagGrenz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IAssistentSteuerung {

    /**
     * Gibt die Topicverteilung zu einer mit der instanceID gekenzeichneten EMAil,
     * Diese Funktion setzt vorraus der der Assistent bereits trainiert worden ist, und die Emails in der Db
     * analysoiert worden sind.
     */
    double[] getTagVerteilung(int instanceId);

    TagGrenz getTagById(int id);

    int getAnzahlTags();

    /**
     * Trainiert SEM mit den EMail instanzen aus der DB,
     * dabei werden Statistiken fuer jede EMail erstellt, sowie die passenden Tags
     * ausgewaehlt. Diese Methode erstellt ein komplett neues Model.
     */
    void trainiereSEM(int anzahlTags, double alphasum, double beta, int anzahlIterationen);



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
    ArrayList<TagGrenz> zeigeAlleTagsAn();

    boolean updateTagGrenz(TagGrenz tag);

    /**
     * Methode die ein weiteres Wort zur Stoplistehinzufuegt. Diese Woerter
     * werden dann vom Assistenten bei der Analyse ignoriert.
     * @param wort
     */
    void wortZurStoplisteHinzufuegen(String wort, int tid);

    void wortVonStoplisteEntfernen(String wort);

}
