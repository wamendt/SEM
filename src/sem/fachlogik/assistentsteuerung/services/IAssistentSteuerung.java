package sem.fachlogik.assistentsteuerung.services;

import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Schnitstelle fuer die AssistentKomponente
 * Bietet Methoden an fuer die Verwaltung von Tags sowie zum Trainieren der Kuenstlichen Intelligenz
 */
public interface IAssistentSteuerung {

    /**
     * Gibt die Topicverteilung zu einer mit der instanceID gekenzeichneten EMAil,
     * Diese Funktion setzt vorraus der der Assistent bereits trainiert worden ist, und die Emails in der Db
     * analysoiert worden sind.
     */
    double[] getTagVerteilung(EMailGrenz email);

    /**
     * Holt einen Tag von der Datenbank
     * @param id der PrimaryKey des Tags
     * @return den gefunden Tag bei erfolg , sonst null
     */
    TagGrenz getTagById(int id);

    /**
     * Gibt die Anzahl der vorhanden Tags zurueck
     * @return
     */
    int getAnzahlTags();

    /**
     * Trainiert SEM mit den EMail instanzen aus der DB,
     * dabei werden Statistiken fuer jede EMail erstellt, sowie die passenden Tags
     * ausgewaehlt. Diese Methode erstellt ein komplett neues Model.
     */
    void trainiereSEM(int anzahlTags, double alphasum, double beta, int anzahlIterationen);

    /**
     * Holt alle Tags aus der Datenbank und konvertiert diese in eine Liste aus
     * TagGrenz objekten.
     * @return Die Liste der TagGrenzObjekte.
     * @throws IOException
     * @throws SQLException
     */
    ArrayList<TagGrenz> zeigeAlleTagsAn();

    /**
     * macht ein Update auf ein Tagobjekt
     * @param tag der zu updatende Tag
     * @return true bei erfolg, sonst false
     */
    boolean updateTagGrenz(TagGrenz tag);

    /**
     * Methode die ein weiteres Wort zur Stoplistehinzufuegt. Diese Woerter
     * werden dann vom Assistenten bei der Analyse ignoriert.
     * @param wort
     */
    void wortZurStoplisteHinzufuegen(String wort, int tid);


}
