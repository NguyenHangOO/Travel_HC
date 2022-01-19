package com.example.travel;

import java.io.Serializable;

public class CuisineAdmin implements Serializable {
     private String resourceImage;
     private String title;
     private int id_item;
     private int diadiem_id;

    public CuisineAdmin(String resourceImage, String title, int id_item, int diadiem_id) {
        this.resourceImage = resourceImage;
        this.title = title;
        this.id_item = id_item;
        this.diadiem_id = diadiem_id;
    }

    public String getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(String resourceImage) {
        this.resourceImage = resourceImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public int getDiadiem_id() {
        return diadiem_id;
    }

    public void setDiadiem_id(int diadiem_id) {
        this.diadiem_id = diadiem_id;
    }
}
