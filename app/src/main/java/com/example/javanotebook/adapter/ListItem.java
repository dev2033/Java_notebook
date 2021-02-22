package com.example.javanotebook.adapter;

import java.io.Serializable;

// Serializable нужен для того чтобы передать в интент класс полностью
public class ListItem implements Serializable {
    /*Получение всех данных от одного объекта*/
    private String title;
    private String desc;
    private String uri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
