package com.example.magda.jakatoliczba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


public class MessagesManager {

    public static final int SNACK_BAR_TIME_LVL_SHORT = 0;
    public static final int SNACK_BAR_TIME_LVL_LONG = 1;
    public static final int SNACK_BAR_TIME_LVL_INDEFINITE = 2;

    @NonNull
    public static Snackbar getSimplySnackBar(View rootLayout, int levelTime, String message) {
        if (levelTime == 0) { //krótki czas trwania
            return Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT);
        } else if (levelTime == 1) { //długi czas trwania
            return Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG);
        } else { //niezdefiniowany czas trwania
            return Snackbar.make(rootLayout, message, Snackbar.LENGTH_INDEFINITE);
        }
    }

    public static Toast getToast(Context context, int levelTime, String message) {
        if (levelTime == 0) { //krótki czas wyświetlania
            return Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else { //długi czas wyświetlania
            return Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
    }

    public static Snackbar getSnackBar(View rootView, int levelTime, String message,
                                       String buttonText, Context context, View.OnClickListener onClickListener) {
        Snackbar tmpSnackbar;
        if (levelTime == 0) { //krótki czas trwania
            tmpSnackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        } else if (levelTime == 1) { //długi czas trwania
            tmpSnackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        } else { //niezdefiniowany czas trwania
            tmpSnackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE);
        }
        tmpSnackbar.setAction(buttonText, onClickListener);
        tmpSnackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        return tmpSnackbar;
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

    public static AppCompatDialog getCustomLayoutAlertDialog(int layout,String title, Context context) {
        AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        return dialog;
    }

    public static ProgressDialog getSimplyProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

}