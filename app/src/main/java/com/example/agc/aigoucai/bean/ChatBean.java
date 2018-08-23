package com.example.agc.aigoucai.bean;

import java.io.Serializable;

public class ChatBean implements Serializable{


    /**
     * Key : QQ
     * Code : qq
     * Value : 2500055788
     */

    private String Key;
    private String Code;
    private String Value;

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }
}
