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
    public int createTag(Tag tag) {
        String sql = "INSERT INTO tag (name)" +
                "VALUES (?)";
        try {
            return insertAndReturnKey(sql, tag.getName());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Tag getTagById(int tid){
        ArrayList<Tag> tags;
        try {
            tags = query("SELECT * FROM tag WHERE tid=?", tid);
            return tags.size() > 0 ? tags.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Tag> getAlleTags(){
        try {
            return query("SELECT * FROM tag");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Tag>();
    }

    @Override
    public boolean deleteTag(int tid) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM tag WHERE tid = ?;", tid);
            return ret == 1;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int deleteAlleTags() {
        try {
            return  updateOrDelete("DELETE FROM tag");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public boolean updateTag(Tag tag) {
        int ret;
        try {
            ret = updateOrDelete("UPDATE tag" +
                            " SET name = ? WHERE tid = ?", tag.getName(), tag.getTid());
            return 1 == ret;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
