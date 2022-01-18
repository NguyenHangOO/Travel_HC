package com.example.travel;

import java.io.Serializable;

public class Culinary implements Serializable {
     private String resourceImage;
     private String title;


    public Culinary(String resourceImage, String title) {
        this.resourceImage = resourceImage;
        this.title = title;
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
}
