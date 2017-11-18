package pl.edu.agh.student.calcalc.containers;

import android.app.Activity;

import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public class CustomInteger implements IPropertyWithResource {

    int value;

    public CustomInteger(int value) {
        this.value = value;
    }
    @Override
    public String getString(Activity context) {
        return ((Integer)value).toString();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
