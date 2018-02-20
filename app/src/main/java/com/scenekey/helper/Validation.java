package com.scenekey.helper;

import android.content.Context;
import android.widget.EditText;

import com.scenekey.R;

/**
 * Created by mindiii on 5/2/18.
 */

public class Validation {

    private Context context;

    public Validation(Context context) {
        this.context = context;
    }

    private String getString(EditText editText){
        return editText.getText().toString();
    }

    public boolean isFullNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
            editText.setError(context.getString(R.string.fullNameEmptyError));
            editText.requestFocus();
            return false;
        } else if (!(editText.length() >= 3)) {
            editText.setError(context.getString(R.string.fullNameLengthError));
            editText.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean isEmailValid(EditText editText) {
        if (getString(editText).isEmpty()){
            editText.setError(context.getString(R.string.emailEmptyError));
            editText.requestFocus();
            return false;
        }else{
            boolean bool = android.util.Patterns.EMAIL_ADDRESS.matcher(getString(editText)).matches();
            if (!bool) {
                editText.setError(context.getString(R.string.emailInvalidError));
                editText.requestFocus();
            }
            return bool;
        }

    }


    public boolean isPasswordValid(EditText editText) {
        if (getString(editText).isEmpty()) {
            editText.setError(context.getString(R.string.passEmptyError));
            editText.requestFocus();
            return false;
        } else if (editText.getText().length() >= 6) {
            editText.requestFocus();
            return true;
        } else {
            editText.setError(context.getString(R.string.passLengthError));
            editText.requestFocus();
            return false;
        }
    }


}
