package sem.fachlogik.mailsteuerung.services;

import sem.fachlogik.mailsteuerung.listener.MsgReceivedListener;
import sem.fachlogik.mailsteuerung.listener.MsgRemovedListener;

public interface IMailSteuerung {

    void addMsgReceivedListener(MsgReceivedListener msgReceivedListener);
    void removeMsgReceivedListener(MsgReceivedListener msgRemovedListener);

    void addMsgRemovedListener(MsgRemovedListener msgReceivedListener);
    void removeMsgRemovedListener(MsgRemovedListener msgRemovedListener);
}
