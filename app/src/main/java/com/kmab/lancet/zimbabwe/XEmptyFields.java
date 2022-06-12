package com.kmab.lancet.zimbabwe;

import android.widget.EditText;

public class XEmptyFields {

    public XEmptyFields() {
    }

    public boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().equals("");
    }
}
