package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDTag extends DBCRUDTeamplate<Tag> implements ICRUDTag{
    private static final String TABLE_NAME = "tag";
    private static final String COLUMN_TID = "tid";
    private static final String COLUMN_NAME = "name";

    private static final String SQL_INSERT_TAG = "INSERT INTO tag(name) VALUES(?)";
    private static final String SQL_UPDATE_TAG = "UPDATE tag SET name = ? WHERE tid = ?";

    @Override
    protected Tag makeObject(ResultSet rs) throws SQLException{
        Tag tag = new Tag();
        tag.setTid(rs.getInt(COLUMN_TID));
        tag.setName(rs.getString(COLUMN_NAME));
        return tag;
    }

    @Override
    public int createTag(Tag tag) {
        try {
            int id = insertAndReturnKey(SQL_INSERT_TAG, tag.getName());
            tag.setTid(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Tag getTagById(int tid){
        ArrayList<Tag> tags;
        try {
            tags = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_TID), tid);
            return tags.size() > 0 ? tags.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Tag> getAlleTags(){
        try {
            return query(String.format(SQL_SELECT_FROM, TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteTag(int tid) {
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_TID), tid);
            return ret == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int deleteAlleTags() {
        try {
            return  updateOrDelete(String.format(SQL_DELETE_FROM, TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public boolean updateTag(Tag tag) {
        int ret;
        try {
            ret = updateOrDelete(SQL_UPDATE_TAG, tag.getName(), tag.getTid());
            return 1 == ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
