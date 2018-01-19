package sem.datenhaltung.semmodel.impl;

import org.junit.jupiter.api.Test;
import sem.datenhaltung.semmodel.entities.TagVerteilung;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;

import static org.junit.jupiter.api.Assertions.*;

class CRUDTagVerteilungTest {

    @Test
    void createTagVerteilung() {
        ICRUDManagerSingleton.getIcrudTagVerteilungInstance().createTagVerteilung(new TagVerteilung(0.5, 1));
    }

    @Test
    void deleteTagVerteilung() {
        ICRUDManagerSingleton.getIcrudTagVerteilungInstance().deleteTagVerteilung(40513);
    }

    @Test
    void deleteAlleTagVerteilungen() {
        ICRUDManagerSingleton.getIcrudTagVerteilungInstance().deleteAlleTagVerteilungen();
    }

    @Test
    void updateTagVerteilung() {
    }

    @Test
    void getTagVerteilungById() {
        TagVerteilung tagVerteilung = ICRUDManagerSingleton.getIcrudTagVerteilungInstance().getTagVerteilungById(59355);
        System.out.println(tagVerteilung.getVerteilung());
    }

    @Test
    void getTagVerteilungByEmailId() {
    }

    @Test
    void getAlleTagVerteilungen() {
    }
}