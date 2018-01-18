package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.TagVerteilung;
import sem.datenhaltung.semmodel.services.ICRUDTagVerteilung;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUDTagVerteilung extends DBCRUDTeamplate<TagVerteilung> implements ICRUDTagVerteilung {
    /**
     * Erstelt ein entspraechendes Entity Objekt
     *
     * @param rs das Anfrage Ergebnis
     * @return
     */
    @Override
    protected TagVerteilung makeObject(ResultSet rs) throws SQLException, IOException {
        TagVerteilung tagVerteilung = new TagVerteilung();
        tagVerteilung.setTvid(rs.getInt(1));
        tagVerteilung.setVerteilung(rs.getDouble(2));
        tagVerteilung.setVerteilung(rs.getInt(3));
        return tagVerteilung;
    }

    @Override
    public int createTagVerteilung(TagVerteilung tagVerteilung) {
        String sql = "INSERT INTO tagverteilung (verteilung,mid) VALUES(?,?)";
        try {
            int id = insertAndReturnKey(sql, tagVerteilung.getVerteilung(), tagVerteilung.getMid());
            tagVerteilung.setTvid(id);
            return id;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteTagVerteilung(int tvid) {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM tagverteilung WHERE tvid = ?;", tvid);
            return ret == 1;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAlleTagVerteilungen() {
        int ret = 0;
        try {
            ret = updateOrDelete("DELETE FROM tagverteilung");
            return ret == 1;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTagVerteilung(TagVerteilung tagVerteilung) {
        int ret;
        try {
            ret = updateOrDelete("UPDATE tagverteilung" +
                    " SET verteilung = ?, mid = ? WHERE tvid = ?", tagVerteilung.getVerteilung()
                    , tagVerteilung.getMid(), tagVerteilung.getTvid());
            return 1 == ret;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public TagVerteilung getTagVerteilungById(int tvid) {
        ArrayList<TagVerteilung> tagverteilungen;
        try {
            tagverteilungen = query("SELECT * FROM tagverteilung WHERE tvid=?", tvid);
            return tagverteilungen.size() > 0 ? tagverteilungen.get(0) : null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<TagVerteilung> getTagVerteilungByEmailId(int mid) {
        ArrayList<TagVerteilung> tagVerteilungen;
        try {
            tagVerteilungen = query("SELECT * FROM tagverteilung WHERE mid=?", mid);
            return tagVerteilungen;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<TagVerteilung> getAlleTagVerteilungen() {
        ArrayList<TagVerteilung> tagVerteilungen;
        try {
            tagVerteilungen = query("SELECT * FROM tagverteilung");
            return tagVerteilungen;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
