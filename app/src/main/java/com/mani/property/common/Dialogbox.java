package com.mani.property.common;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mani.property.R;


/**
 * Created by Mani on 31-07-2017.
 */

public class Dialogbox {
    public static ProgressDialog pgdialog;
    //strType=0 -finish
    //strType=1 -Restart the Activity
    //strType=2 -Dismiss dialog

    public static String alerts(final Context context, String Errormsg, final String strType) {
        try {

/*
            MaterialDialog dialog=new MaterialDialog.Builder(context)

                    .title("Richpigeons").titleGravity(GravityEnum.CENTER)

                    .customView(R.layout.custom_dialog, false)
                    .positiveText("OK").positiveColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    .canceledOnTouchOutside(false)
                    .cancelable(false)
                   *//* .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                            if (strType.equals("1")) {
                                Intent refresh = new Intent(context, context.getClass());
                                context.startActivity(refresh);
                                ((Activity) context).finish();
                            } else if (strType.equals("0")) {
                                ((Activity) context).finish();
                            }

                        }
                    })*//*

                    .show();*/


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialog.getWindow();
            dialog.setContentView(R.layout.custom_dialog);
            window.setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            TextView tvClose = (TextView) dialog.findViewById(R.id.tvOk);
            TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);

            tvMsg.setText(Errormsg);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (strType.equals("1")) {
                        Intent refresh = new Intent(context, context.getClass());
                        context.startActivity(refresh);
                        ((Activity) context).finish();
                    } else if (strType.equals("0")) {
                        ((Activity) context).finish();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Errormsg;

    }

    public static void showDialog(Context mContext, String strMessage) {
        try {
            if (pgdialog != null)
                if (pgdialog.isShowing())
                    pgdialog.dismiss();
            pgdialog = ProgressDialog.show(mContext, "", strMessage, true);
            pgdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pgdialog.setCancelable(false);
            pgdialog.show();
            pgdialog.setContentView(R.layout.custom_loading);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialog() {
        try {
            if (pgdialog.isShowing())
                pgdialog.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void keyboard(Activity context) {

        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            alerts(context, "Network Not Available! Please turn on Wifi or use mobile data.", "0");
            return false;
        }
        return true;
    }
}
