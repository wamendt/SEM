package sem.fachlogik.mailsteuerung.listener;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import sem.datenhaltung.semmodel.entities.Konto;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.mailsteuerung.event.MsgReceivedEvent;

import java.util.ArrayList;

public class MessageCountListenManager {
    private IMAPStore store;
    private static Konto konto;

    private ArrayList<MsgReceivedListener> msgReceivedListeners = new ArrayList<>();

    public void addMsgReceivedListener(MsgReceivedListener listener){
        msgReceivedListeners.add(listener);
    }
    public MessageCountListenManager(Store store, Konto konto){
        this.store = (IMAPStore) store;
        MessageCountListenManager.konto = konto;
    }

    public void runListener(Folder folder) {
        try {
            if (!this.store.hasCapability("IDLE")) {
                throw new RuntimeException("IDLE not supported");
            }

            folder.addMessageCountListener(new MessageCountAdapter() {

                @Override
                public void messagesAdded(MessageCountEvent event) {
                    Message[] messages = event.getMessages();
                    int count = messages.length;

                    for (Message message : messages) {
                        try {
                            System.out.println("\nMessageCount: " + count + "\nMail Subject:- " + message.getSubject() + "\nMessageNumber: " + message.getMessageNumber());
                            for(MsgReceivedListener l : msgReceivedListeners){
                                l.messageAngekommen(new MsgReceivedEvent(this, new EMailGrenz()));
                            }
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent event){
                    System.out.println("Nachricht wurde im Ordner: " + folder.getFullName() + "gelöscht!\nAb hier Update in die Wege leiten!");
                }
            });

            IdleThread idleThread = new IdleThread(folder);
            idleThread.setDaemon(false);
            idleThread.start();

            //idleThread.join();
            // idleThread.kill(); //to terminate from another thread

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        finally {


            close(folder);
            close(store);
        }
        */
    }

    private static class IdleThread extends Thread {
        private final Folder folder;
        private volatile boolean running = true;

        IdleThread(Folder folder) {
            super();
            this.folder = folder;
        }

        public synchronized void kill() {
            if (!running)
                return;
            this.running = false;
        }

        @Override
        public void run() {
            while (running) {

                try {
                    ensureOpen(folder);
                    System.out.println("enter idle");
                    ((IMAPFolder) folder).idle();
                } catch (Exception e) {
                    // something went wrong
                    // wait and try again
                    e.printStackTrace();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        // ignore
                    }
                }

            }
        }
    }

    public static void close(final Folder folder) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (final Exception e) {
            // ignore
        }

    }

    public static void close(final Store store) {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (final Exception e) {
            // ignore
        }

    }

    static void ensureOpen(final Folder folder) throws MessagingException {

        if (folder != null) {
            Store store = folder.getStore();
            if (store != null && !store.isConnected()) {
                store.connect(konto.getUserName(), konto.getPassWort());
            }
        } else {
            throw new MessagingException("Unable to open a null folder");
        }

        if (folder.exists() && !folder.isOpen() && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
            System.out.println("open folder " + folder.getFullName());
            folder.open(Folder.READ_ONLY);
            if (!folder.isOpen())
                throw new MessagingException("Unable to open folder " + folder.getFullName());
        }

    }
}