package pl.edu.agh.student.calcalc.enums;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IResourced;

public enum OutputFileFormat implements IResourced {
    KML(R.string.kml_filetype),
    GPX(R.string.gpx_filetype);

    private final int stringResourceId;

    OutputFileFormat(int stringResourceId_) {
        stringResourceId = stringResourceId_;
    }

    @Override
    public int getResourceId() {
        return stringResourceId;
    }

}
