package sem.datenhaltung.semmodel.database;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTag;

import java.io.IOException;
import java.sql.SQLException;

class CRUDTagTest {
    @Test
    void createTag() throws IOException, SQLException {
        Tag tag = new Tag();
        tag.setName("projekt");
        ICRUDTag crudtag = ICRUDManagerSingleton.getIcrudTagInstance();
        crudtag.createTag(tag);



    }

    @Test
    void getTagById() throws IOException, SQLException {
        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
        EMail eMail = icrudMail.getEMailById(123);
        ICRUDTag crudtag = ICRUDManagerSingleton.getIcrudTagInstance();
        Tag tag = crudtag.getTagById(1);
        eMail.setTid(tag.getTid());
        icrudMail.updateEMail(eMail);
    }

    @Test
    void getAlleTags() {
    }

    @Test
    void deleteTag() throws IOException, SQLException {
        ICRUDTag crudtag = ICRUDManagerSingleton.getIcrudTagInstance();
        crudtag.deleteTag(1);
    }

    @Test
    void updateTag() {
    }

}