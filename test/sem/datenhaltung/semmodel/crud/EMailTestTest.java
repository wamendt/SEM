package sem.datenhaltung.semmodel.crud;


import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.impl.CRUDEMail;

import java.io.IOException;
import java.sql.SQLException;

class EMailTestTest {
    @org.junit.jupiter.api.Test
    void updateEMail() throws IOException, SQLException {

/*
            Connection con = SEMModelManager.getConnection();

            Statement stmt = con.createStatement();
            String sql = "create table EMail " +
                    "(id int," +
                    "fromaddr varchar not null," +
                    "content text not null)";
            stmt.executeUpdate(sql);
            stmt.close();
*/

        CRUDEMail cuT = new CRUDEMail();
        EMail email = new EMail();
        /*email.setBetref("TestBetreff");
        email.setInhalt("Dies ist ein beliebiger Inhalt");
        email.setTid(1);
        email.setAbsender("w.amendt@live.de");
        email.setCc("beispielCC@hotmail.de");
        email.setBcc("beispielBCC@hotmail.de");
        email.setEmpfaenger("empfaenger@hotmail.de");
        email.setContentOriginal("<h1>Dies ist ein HTML-Inhalt</h1>");
        email.setZustand("testZustand");
        email.setMessageID("<kjncownpncqpqicnpncükjbcqboiqpoqpncqpnpqncpqj28zw98dhepdnp29uq@google.com>");
        //cuT.createEMail(email);*/

        email = cuT.getEMailById(1);

        System.out.println("mid: " + email.getMid());
        System.out.println("Betreff: " + email.getBetreff());
        System.out.println("Inhalt: " + email.getInhalt());
        System.out.println("tid: " + email.getTid());
        System.out.println("Absender: " + email.getAbsender());
        System.out.println("CC: " + email.getCc());
        System.out.println("BCC: " + email.getBcc());
        System.out.println("Empfänger: " + email.getEmpfaenger());
        System.out.println("ContentOriginal: " + email.getContentOriginal());
        System.out.println("Zustand: " + email.getZustand());
        System.out.println("MessageID: " + email.getMessageID() + "\n");
    }

}