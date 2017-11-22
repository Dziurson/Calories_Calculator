package pl.edu.agh.student.calcalc.enums;

import android.app.Activity;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public enum UserGender implements IPropertyWithResource {
    GENDER_MALE(R.string.gender_male),
    GENDER_FEMALE(R.string.gender_female);

    private final int stringResourceId;

    UserGender(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    @Override
    public String getString(Activity context) {
        return context.getString(stringResourceId);
    }
}
