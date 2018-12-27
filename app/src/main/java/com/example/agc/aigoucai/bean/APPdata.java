package com.example.agc.aigoucai.bean;

import java.io.Serializable;

public class APPdata  implements Serializable {

    private String b;  //品牌
    private String m; //型号
    private String s; //操作系统版本
    private String ip; //异常连接的ip
    private String bv;//使用的浏览器版本
    private String av;//app的版本号
    private String st;//请求链接花的时间
    private String err;//错误详情
    private String s_ip;//socket的服务器ip
    private String port;//端口
    private String applicationid;
    private String appvertion;

    public String getS_ip() {
        return s_ip;
    }

    public void setS_ip(String s_ip) {
        this.s_ip = s_ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public String getAv() {
        return av;
    }

    public void setAv(String av) {
        this.av = av;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }

    public String getAppvertion() {
        return appvertion;
    }

    public void setAppvertion(String appvertion) {
        this.appvertion = appvertion;
    }
}
