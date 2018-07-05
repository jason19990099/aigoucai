package com.example.mylibrary_socketutil;

import java.util.List;

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
