package sem.datenhaltung.semmodel.services;

import sem.datenhaltung.semmodel.entities.Tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ICRUDTag {
    int createTag(Tag tag) throws IOException, SQLException;
    Tag getTagById(int tid) throws IOException, SQLException ;
    ArrayList<Tag> getAlleTags() throws IOException, SQLException ;
    boolean deleteTag(int tid) throws IOException, SQLException ;
    boolean updateTag(Tag tag) throws IOException, SQLException ;
}
