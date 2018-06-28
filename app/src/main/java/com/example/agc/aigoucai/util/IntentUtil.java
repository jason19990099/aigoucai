package com.example.agc.aigoucai.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.agc.aigoucai.R;

/**
 * 跳转工具类
 * rights reserved.
 */
public class IntentUtil {

    /**
     * 跳转至指定activity.
     *
     * @param context   上下文环境
     * @param gotoClass 跳转至界面
     * @param gotoClass 是否关闭当前页面
     * @version 1.0
     * @updateInfo
     */
    public static void gotoActivity(Context context, Class<?> gotoClass, boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
        if (finishThis)
            ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 跳转并从底部弹出至指定activity.
     *
     * @param context   上下文环境
     * @param gotoClass 跳转至界面
     * @param gotoClass 是否关闭当前页面
     * @updateInfo
     */
    public static void gotoActivityFromButton(Context context, Class<?> gotoClass, boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
        if (finishThis)
            ((Activity) context).finish();
		((Activity) context).overridePendingTransition(R.anim.enter_exit_from_top, R.anim.enter_enter_from_botton);
    }

    /**
     * 携带传递数据跳转至指定activity
     *
     * @param context   上下文环境
     * @param gotoClass 跳转至界面
     * @param bundle    携带数据
     * @param gotoClass 是否关闭当前页面
     * @version 1.0
     * @updateInfo
     */
    public static void gotoActivity(Context context, Class<?> gotoClass, Bundle bundle, boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (finishThis)
            ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 携带传递数据跳转至指定activity
     *
     * @param context   上下文环境.
     * @param gotoClass 跳转至界面.
     */
    public static void gotoActivityToTop(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 携带传递数据跳转至指定activity,并关闭当前activity.
     *
     * @param context   上下文环境.
     * @param gotoClass 跳转至界面.
     * @param bundle    携带数据.
     */
    public static void gotoActivityToTop(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 单例模式跳转至指定activity
     *
     * @param context   上下文
     * @param gotoClass 目标activity
     * @version 1.0
     * @updateInfo
     */
    public static void gotoActivityToTopAndFinish(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 单例模式跳转并携带数据
     *
     * @param context   上下文
     * @param gotoClass 目标activity
     * @param bundle    携带数据
     * @version 1.0
     * @updateInfo
     */
    public static void gotoActivityToTopAndFinish(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 跳转至指定activity.并返回结果
     *
     * @param context     上下文环境.
     * @param gotoClass   目标界面
     * @param requestCode 请求码
     * @version 1.0
     */
    public static void gotoActivityForResult(Context context, Class<?> gotoClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 携带传递数据跳转至指定activity.
     *
     * @param context     上下文环境.
     * @param gotoClass   跳转至界面.
     * @param bundle      携带数据.
     * @param requestCode 请求码.
     * @version 1.0
     * @updateInfo
     */
    public static void gotoActivityForResult(Context context, Class<?> gotoClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 跳转到发送短信界面
     *
     * @param context  上下文
     * @param phoneNum 发送号码
     * @param content  短信内容
     * @version 1.0
     * @createTime 2015-4-23,上午11:41:46
     * @updateTime 2015-4-23,上午11:41:46
     * @createAuthor luming
     * @updateAuthor luming
     * @updateInfo
     */
    public static void gotoSendMsmActivity(Context context, String phoneNum, String content) {
        Uri uri = Uri.parse("smsto:" + phoneNum);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }


}
