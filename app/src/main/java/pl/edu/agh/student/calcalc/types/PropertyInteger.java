package pl.edu.agh.student.calcalc.types;

import android.app.Activity;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public class PropertyInteger implements IPropertyWithResource {

    int value;
    private final int stringResourceIdWhenZero;

    public PropertyInteger(int value, int stringResourceIdWhenZero) {
        this.value = value;
        this.stringResourceIdWhenZero = stringResourceIdWhenZero;
    }
    @Override
    public String getString(Activity context) {
        return (value == 0) ? context.getString(stringResourceIdWhenZero) : ((Integer)value).toString();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
