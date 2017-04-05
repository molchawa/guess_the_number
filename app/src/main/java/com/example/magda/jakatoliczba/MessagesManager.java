package com.example.magda.jakatoliczba;


import android.content.Context;
import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import android.widget.Toast;


public class MessagesManager {

    public static Toast getToast(Context context, int levelTime, String message) {
        if (levelTime == 0) { //short time of displaying
            return Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else { //long time of displaying
            return Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
    }


    public static AppCompatDialog getSimplyAlertDialog(String message, String title, String okButton,
                                                       String deniedButton, Context context,
                                                       DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style
                .Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(message);
        builder.setPositiveButton(okButton, onClickListener);
        builder.setNegativeButton(deniedButton, onClickListener);
        builder.setCancelable(false);
        AppCompatDialog tmpDialog = builder.create();
        tmpDialog.setCanceledOnTouchOutside(false);
        return tmpDialog;
    }


}