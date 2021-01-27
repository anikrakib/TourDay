package com.anikrakib.tourday.Utils;

import android.content.Context;
import android.content.Intent;

public class Share {
    public static void shareLink(Context context, String link){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.tourday.team/"+link);
        sendIntent.setType("text/plain");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }
}
