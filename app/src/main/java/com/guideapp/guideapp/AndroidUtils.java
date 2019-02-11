package com.guideapp.guideapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
public class AndroidUtils {

    public static void showKeyboard(Context context, EditText target) {
        if (context != null && target != null) {
            target.requestFocus();
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }



}
