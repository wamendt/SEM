package sem.fachlogik.mailsteuerung.impl;

import sem.fachlogik.mailsteuerung.listener.MsgReceivedListener;
import sem.fachlogik.mailsteuerung.listener.MsgRemovedListener;
import sem.fachlogik.mailsteuerung.services.IMailSteuerung;
import sem.fachlogik.mailsteuerung.utils.impl.IMailServiceImpl;
import sem.fachlogik.mailsteuerung.utils.services.IMailService;

public class IMailSteuerungImpl implements IMailSteuerung{

    @Override
    public void addMsgReceivedListener(MsgReceivedListener msgReceivedListener) {
        IMailServiceImpl.getReceivedListeners().add(msgReceivedListener);
    }

    @Override
    public void removeMsgReceivedListener(MsgReceivedListener msgReceivedListener) {
        IMailServiceImpl.getReceivedListeners().remove(msgReceivedListener);
    }

    @Override
    public void addMsgRemovedListener(MsgRemovedListener msgRemovedListener) {
        IMailServiceImpl.getRemovedListeners().add(msgRemovedListener);
    }

    @Override
    public void removeMsgRemovedListener(MsgRemovedListener msgRemovedListener) {
        IMailServiceImpl.getRemovedListeners().remove(msgRemovedListener);
    }
}
