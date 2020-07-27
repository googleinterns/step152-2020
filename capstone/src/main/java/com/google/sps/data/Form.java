package com.google.sps.data;
import java.io.*;

public class Form {

    private String editURL;
    private String URL;
    private int id;
    private boolean isPublished;

    public Form(String editURL, String URL, int id) {
        this.editURL = editURL;
        this.URL = URL;
        this.id = id;
        this.isPublished = false;
    }

    public String getEditURL() {
        return this.editURL;
    }

    public String getURL() {
        return this.URL;
    }

    public int getID() {
        return this.id;
    }

    public boolean isPublished() {
        return this.isPublished;
    }

    public void publish(){
        this.isPublished = true;
    }

    public void unpublished(){
        this.isPublished = false;
    }
}