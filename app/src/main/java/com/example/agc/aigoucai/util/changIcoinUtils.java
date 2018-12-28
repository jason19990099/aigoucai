package com.example.agc.aigoucai.util;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;

import java.util.List;


public  class changIcoinUtils {
    private static ComponentName defaultComponent;
    private static ComponentName testComponent;
    private static PackageManager packageManager;


    /**
     * 显示一个 其他的全部隐藏
     * list最小个数为1
     */
    public  static void showOneDissOthers(Activity activity, String str1, List<String> list) {
        int size=list.size();
        for (int i=0;i<size;i++){
            if (list.get(i).equals(str1)){
                defaultComponent = new ComponentName(activity.getBaseContext(), str1);
                enableComponent(defaultComponent);
            }else{
                testComponent = new ComponentName(activity.getBaseContext(), list.get(i));
                disableComponent(testComponent);
            }
        }
    }

    /**
     * 同时多显示几个图标
     * @param activity
     * @param list
     */
    public static void addmore(Activity activity, List<String> list) {
        packageManager = activity.getApplicationContext().getPackageManager();
        int size=list.size();
        for (int i=0;i<size;i++){
            defaultComponent = new ComponentName(activity.getBaseContext(), String.valueOf(list.get(i)));  //拿到默认的组件
            enableComponent(defaultComponent);
        }
    }

    /**
     * 启用组件
     *
     * @param componentName
     */
    private  static void enableComponent(ComponentName componentName) {
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            //已经启用
            return;
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     *
     * @param componentName
     */
    private  static void disableComponent(ComponentName componentName) {
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            //已经禁用
            return;
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
