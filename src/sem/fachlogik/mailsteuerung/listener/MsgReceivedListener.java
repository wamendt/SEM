package sem.fachlogik.mailsteuerung.listener;

import sem.fachlogik.mailsteuerung.event.MsgReceivedEvent;

import java.util.EventListener;

public interface MsgReceivedListener extends EventListener {
    void messageAngekommen(MsgReceivedEvent msgReceivedEvent);
}
