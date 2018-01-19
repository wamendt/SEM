package sem.gui.viewmodel.utils;

import sem.fachlogik.grenzklassen.TagGrenz;

import java.util.ArrayList;

public interface TagClickedListener {
    void tagClicked(TagGrenz tag);
    void tagReleased(TagGrenz tag);
}
