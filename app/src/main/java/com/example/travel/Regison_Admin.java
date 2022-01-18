package com.example.travel;

import java.io.Serializable;

public class Regison_Admin implements Serializable {
     private String title;
     private int id;

    public Regison_Admin(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String toString()  {
        return this.getTitle();
    }
}
