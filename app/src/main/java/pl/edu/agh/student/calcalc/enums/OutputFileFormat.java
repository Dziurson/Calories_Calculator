package pl.edu.agh.student.calcalc.enums;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public enum OutputFileFormat implements IPropertyWithResource {
    KML(R.string.kml_filetype),
    GPX(R.string.gpx_filetype);

    private final int stringResourceId;

    OutputFileFormat(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

}
