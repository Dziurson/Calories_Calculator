package pl.edu.agh.student.calcalc.enums;

import android.app.Activity;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public enum OutputFileFormat implements IPropertyWithResource {
    KML(R.string.kml_filetype),
    GPX(R.string.gpx_filetype);

    private final int stringResourceId;

    OutputFileFormat(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    @Override
    public String getString(Activity context) {
        return context.getString(stringResourceId);
    }

}
