package sem.datenhaltung.semmodel.services;


import sem.datenhaltung.semmodel.entities.TagVerteilung;

import java.util.ArrayList;

public interface ICRUDTagVerteilung {
    int createTagVerteilung(TagVerteilung tagVerteilung);
    boolean deleteTagVerteilung(int tvid);
    boolean deleteAlleTagVerteilungen();
    boolean updateTagVerteilung(TagVerteilung tagVerteilung);
    TagVerteilung getTagVerteilungById(int tvid);
    ArrayList<TagVerteilung> getTagVerteilungByEmailId(int mid);
    ArrayList<TagVerteilung> getAlleTagVerteilungen() ;
}
