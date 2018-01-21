package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.TagVerteilung;
import sem.datenhaltung.semmodel.services.ICRUDTagVerteilung;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDTagVerteilung extends DBCRUDTeamplate<TagVerteilung> implements ICRUDTagVerteilung {
    private static final String TABLE_NAME = "tagverteilung";
    private static final String COLUMN_TVID = "tvid";
    private static final String COLUMN_VERTEILUNG = "verteilung";
    private static final String COLUMN_MID = "mid";

    private static final String SQL_INSERT_TAG = "INSERT INTO tagverteilung (verteilung, mid) VALUES(?,?)";
    private static final String SQL_UPDATE_TAG = "UPDATE tagverteilung SET verteilung = ?, mid = ? WHERE tvid = ?";
    /**
     * Erstelt ein entspraechendes Entity Objekt
     *
     * @param rs das Anfrage Ergebnis
     * @return
     */
    @Override
    protected TagVerteilung makeObject(ResultSet rs) throws SQLException {
        TagVerteilung tagVerteilung = new TagVerteilung();
        tagVerteilung.setTvid(rs.getInt(COLUMN_TVID));
        tagVerteilung.setVerteilung(rs.getDouble(COLUMN_VERTEILUNG));
        tagVerteilung.setMid(rs.getInt(COLUMN_MID));
        return tagVerteilung;
    }

    @Override
    public int createTagVerteilung(TagVerteilung tagVerteilung) {
        try {
            int id = insertAndReturnKey(SQL_INSERT_TAG, tagVerteilung.getVerteilung(), tagVerteilung.getMid());
            tagVerteilung.setTvid(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteTagVerteilung(int tvid) {
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_TVID), tvid);
            return ret == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int deleteAlleTagVerteilungen() {
        try {
            return updateOrDelete(String.format(SQL_DELETE_FROM, TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean updateTagVerteilung(TagVerteilung tagVerteilung) {
        int ret;
        try {
            ret = updateOrDelete(SQL_UPDATE_TAG, tagVerteilung.getVerteilung()
                    , tagVerteilung.getMid(), tagVerteilung.getTvid());
            return 1 == ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public TagVerteilung getTagVerteilungById(int tvid) {
        ArrayList<TagVerteilung> tagverteilungen;
        try {
            tagverteilungen = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_TVID), tvid);
            return tagverteilungen.size() > 0 ? tagverteilungen.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<TagVerteilung> getTagVerteilungByEmailId(int mid) {
        ArrayList<TagVerteilung> tagVerteilungen;
        try {
            tagVerteilungen = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_MID), mid);
            return tagVerteilungen;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<TagVerteilung> getAlleTagVerteilungen() {
        ArrayList<TagVerteilung> tagVerteilungen;
        try {
            tagVerteilungen = query(String.format(SQL_SELECT_FROM, TABLE_NAME));
            return tagVerteilungen;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
