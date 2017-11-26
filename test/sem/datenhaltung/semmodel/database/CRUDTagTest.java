package sem.datenhaltung.semmodel.database;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.impl.CRUDTag;

import java.io.IOException;
import java.sql.SQLException;

class CRUDTagTest {
    @Test
    void createTag() throws IOException, SQLException {
        Tag tag = new Tag();
        tag.setName("projekt");
        CRUDTag.getInstance().createTag(tag);

    }

    @Test
    void getTagById() {
    }

    @Test
    void getAlleTags() {
    }

    @Test
    void deleteTag() {
    }

    @Test
    void updateTag() {
    }

}