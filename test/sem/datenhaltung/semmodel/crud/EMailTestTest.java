package sem.datenhaltung.semmodel.crud;


import sem.datenhaltung.semmodel.entities.EMail;

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

        EMail cuT = new EMail();
        //cuT.insertEMail(null);
    }

}