package com.anikrakib.tourday.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.anikrakib.tourday.R;

import java.util.Objects;

public class Loader {
    public static Dialog postDialog;

    public static void start(Context context){
        postDialog = new Dialog(context);
        postDialog.setContentView(R.layout.gif_view);
        postDialog.setCancelable(false);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        postDialog.show();
    }

    public static void off(){
        postDialog.dismiss();
    }
}
