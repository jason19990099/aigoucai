package com.example.agc.aigoucai.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agc.aigoucai.R;


/**
 * Snackbar显示消息
 *
 * @author ares
 * @date 2015-12-21
 */
public class SB {
    private static View view;

    /**
     * 居中显示的short snackbar
     *
     * @param message
     */
    public static void showShortMessage(Context context, String message) {
        view = ((Activity) context).findViewById(android.R.id.content);
        if (view != null && !TextUtils.isEmpty(message)) {
            Snackbar sBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) sBar.getView();
            ((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            sBar.show();
            //文字居中显示
            View v = sBar.getView();
            TextView txtv = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    /**
     * 居中显示的short snackbar
     *
     * @param id
     */
    public static void showShortMessage(Context context, int id) {
        view = ((Activity) context).findViewById(android.R.id.content);
        if (view != null) {
            Snackbar sBar = Snackbar.make(view, context.getResources().getString(id), Snackbar.LENGTH_SHORT);
            Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) sBar.getView();
            ((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            sBar.show();
            //文字居中显示
            View v = sBar.getView();
            TextView txtv = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    /**
     * 居中显示的long snackbar
     *
     * @param message
     */
    public static void showLongMessage(Context context, String message) {
        view = ((Activity) context).findViewById(android.R.id.content);
        if (view != null && !TextUtils.isEmpty(message)) {
            sBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) sBar.getView();
            ((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            sBar.show();
            //文字居中显示
            View v = sBar.getView();
            TextView txtv = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    private static Snackbar sBar;

    /**
     * 带有按钮的 snackbar
     */
    public static void showActionMessage(Context context, String message, String butStr, View.OnClickListener onClickListener) {
        view = ((Activity) context).findViewById(android.R.id.content);
        if (view != null && !TextUtils.isEmpty(message) && !TextUtils.isEmpty(butStr)) {
            sBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) sBar.getView();
            ((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            //右边操作按钮
            ((Button) ve.findViewById(R.id.snackbar_action)).setTextColor(Color.parseColor("#FF840C"));
            sBar.setAction(butStr, onClickListener).show();
        }
    }

    /**
     * 关闭
     */
    public static void close() {
        if (sBar.isShown())
            sBar.dismiss();
    }
}
