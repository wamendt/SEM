package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.services.ICRUDTag;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDTag extends DBCRUDTeamplate<Tag> implements ICRUDTag{


    @Override
    protected Tag makeObject(ResultSet rs) throws SQLException, IOException {
        Tag tag = new Tag();
        tag.setTid(rs.getInt(1));
        tag.setName(rs.getString(2));
        return tag;
    }

    @Override
    public int createTag(Tag tag) throws IOException, SQLException {
        String sql = "INSERT INTO tag (name)" +
                "VALUES (?)";
        return insertAndReturnKey(sql, tag.getName());
    }

    @Override
    public Tag getTagById(int tid) throws IOException, SQLException {
        ArrayList<Tag> tags = query("SELECT * FROM tag WHERE tid=?", tid);
        return tags.size() > 0 ? tags.get(0) : null;
    }

    @Override
    public ArrayList<Tag> getAlleTags() throws IOException, SQLException {
        return query("SELECT * FROM tag");
    }

    @Override
    public boolean deleteTag(int tid) throws IOException, SQLException {
        int ret = updateOrDelete("DELETE FROM tag WHERE tid = ?;", tid);
        return ret == 1;
    }

    @Override
    public int deleteAlleTags() throws IOException, SQLException {
        return  updateOrDelete("DELETE FROM tag");
    }


    @Override
    public boolean updateTag(Tag tag) throws IOException, SQLException {
        int ret = updateOrDelete("UPDATE tag" +
                        " name = ? WHERE tid = ?", tag.getName(), tag.getTid());
        return ret == 1;
    }
}
