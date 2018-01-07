package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDTag {
    int createTag(Tag tag);
    Tag getTagById(int tid);
    ArrayList<Tag> getAlleTags();
    boolean deleteTag(int tid);
    int deleteAlleTags();
    boolean updateTag(Tag tag);
}
