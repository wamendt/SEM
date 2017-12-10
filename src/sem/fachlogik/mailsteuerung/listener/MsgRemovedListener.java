package sem.fachlogik.mailsteuerung.listener;

import sem.fachlogik.mailsteuerung.event.MsgRemovedEvent;

public interface MsgRemovedListener {
    void messageGeloescht(MsgRemovedEvent msgRemovedEvent);
}
