package sem.fachlogik.mailsteuerung.event;

import sem.fachlogik.grenzklassen.EMailGrenz;

import java.util.EventObject;

public class MsgReceivedEvent extends EventObject {
    private EMailGrenz eMailGrenz;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MsgReceivedEvent(Object source, EMailGrenz eMailGrenz) {
        super(source);
        this.eMailGrenz = eMailGrenz;
    }


    public EMailGrenz geteMailGrenz() {
        return eMailGrenz;
    }
}
