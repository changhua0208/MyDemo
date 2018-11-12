package com.jch.videolib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {


    public static void showOneButtonDialog(Context mContext, CharSequence msgString,
                                           CharSequence btnString, boolean cancelable, final AlertDialog.OnClickListener positiveListener) {

        new AlertDialog.Builder(mContext)
                .setMessage(msgString)
                .setPositiveButton(btnString,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (positiveListener != null)
                                    positiveListener.onClick(dialog, which);
                                dialog.dismiss();

                            }
                        })
                .setCancelable(cancelable).show();
    }

    public static void showOneButtonDialog(Context mContext, int msgResId,
                                           int confirm, boolean cancelable, final AlertDialog.OnClickListener positiveListener) {

        new AlertDialog.Builder(mContext)
                .setMessage(msgResId)
                .setPositiveButton(confirm,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (positiveListener != null)
                                    positiveListener.onClick(dialog, which);
                                dialog.dismiss();

                            }
                        })
                .setCancelable(cancelable).show();
    }


    /**
     * 两个按钮的dialog
     *
     * @param context
     * @param msgResId
     * @param confirm
     * @param cancelId
     * @param cancelable
     * @param listener
     * @return
     */
    public static void showTwoButtonDialog(Context context, int msgResId,
                                             int confirm, int cancelId, final boolean cancelable, final AlertDialog.OnClickListener listener, final AlertDialog.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(msgResId)
                .setPositiveButton(confirm,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (listener != null)
                                    listener.onClick(dialog, which);
                                dialog.dismiss();

                            }
                        })
                .setNegativeButton(cancelId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelListener != null)
                            cancelListener.onClick(dialog, which);
                        dialog.dismiss();
                    }
                })
                .setCancelable(cancelable).show();
    }

    public static void showTwoButtonDialog(Context context, String msg,
                                           String confirm, String cancelId, final boolean cancelable, final AlertDialog.OnClickListener listener, final AlertDialog.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(confirm,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (listener != null)
                                    listener.onClick(dialog, which);
                                dialog.dismiss();

                            }
                        })
                .setNegativeButton(cancelId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelListener != null)
                            cancelListener.onClick(dialog, which);
                        dialog.dismiss();
                    }
                })
                .setCancelable(cancelable).show();
    }

}
