package com.example.agc.aigoucai.bean;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class Home_bean {
    /**
     * code : 1
     * msg : 成功
     * data : {"id":"20","name":"北京快3","version":"1.0.0","catid":"72","channel":"","download_url":"","create_time":"0","update_time":"0","app_status":"0","app_url":"www.kjw666.com","extend_1_title":"","extend_1_status":"0","extend_1_url":"","extend_2_title":"","extend_2_status":"0","extend_2_url":"","menu":[{"catid":"73","catname":"最新资讯"}]}
     */

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * id : 20
         * name : 北京快3
         * version : 1.0.0
         * catid : 72
         * channel :
         * download_url :
         * create_time : 0
         * update_time : 0
         * app_status : 0
         * app_url : www.kjw666.com
         * extend_1_title :
         * extend_1_status : 0
         * extend_1_url :
         * extend_2_title :
         * extend_2_status : 0
         * extend_2_url :
         * menu : [{"catid":"73","catname":"最新资讯"}]
         */

        public String id;
        public String name;
        public String version;
        public String catid;
        public String channel;
        public String download_url;
        public String create_time;
        public String update_time;
        public String app_status;
        public String app_url;
        public String extend_1_title;
        public String extend_1_status;
        public String extend_1_url;
        public String extend_2_title;
        public String extend_2_status;
        public String extend_2_url;
        public List<MenuBean> menu;

        public static class MenuBean {
            /**
             * catid : 73
             * catname : 最新资讯
             */

            public String catid;
            public String catname;
        }
    }
}
