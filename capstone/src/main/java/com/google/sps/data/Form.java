package com.google.sps.data;
import java.io.*;
import com.google.appengine.api.datastore.Entity;

public class Form {

    private String editURL;
    private String URL;
    private String id;
    // private boolean isPublished;

    public Form(String editURL, String URL, String id) {
        this.editURL = editURL;
        this.URL = URL;
        this.id = id;
        // this.isPublished = false;
    }

    public void unpublished(){
        this.isPublished = false;
    }

    // Getters
    public String getEditURL() {
        return this.editURL;
    }

    public String getURL() {
        return this.URL;
    }

    public String getID() {
        return this.id;
    }

    // public boolean isPublished() {
    //     return this.isPublished;
    // }

    // public void publish(){
    //     this.isPublished = true;
    // }

    // public void unpublished(){
    //     this.isPublished = false;
    // }
    
    public Entity toDatastoreEntity(){
        Entity formEntity = new Entity("Form");
        formEntity.setProperty("editURL", this.editURL);
        formEntity.setProperty("URL", this.URL);
        formEntity.setProperty("id", this.id);
        return formEntity;
    }
}