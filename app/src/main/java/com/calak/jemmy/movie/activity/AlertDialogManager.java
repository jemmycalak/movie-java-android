package com.calak.jemmy.movie.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.calak.jemmy.movie.R;

/**
 * Created by JEMMY CALAK on 1/20/2018.
 */

public class AlertDialogManager {

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.ic_action_bell : R.drawable.ic_action_bell);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, Context context) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("YA", okListener)
                .setNegativeButton("TIDAK", null)
                .setIcon(R.drawable.ic_action_bell)
                .create()
                .show();
    }

}
