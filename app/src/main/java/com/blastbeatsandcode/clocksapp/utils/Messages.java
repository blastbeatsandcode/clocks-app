package com.blastbeatsandcode.clocksapp.utils;

import android.content.Context;
import android.widget.Toast;

/*
 * Utilities to quickly show messages to the user
 */
public class Messages {
    /*
     * Show a toast to the given context with the given string
     */
    public static void MakeToast (Context context, String str)
    {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
