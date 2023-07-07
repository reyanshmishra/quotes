package com.motivational.quotes.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.motivational.quotes.R;

/**
 * Created by reyansh on 12/17/17.
 */

public class LoaderDialog extends Dialog {


    public LoaderDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loader_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.5f);
        setCancelable(false);
    }

}
