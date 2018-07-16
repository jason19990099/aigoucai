package com.example.agc.aigoucai.bean;

import java.util.List;

/**
 *  发送劫持数据
 */
public class DataSynevent {
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DataSynevent{" +
                "list=" + list +
                '}';
    }
}
