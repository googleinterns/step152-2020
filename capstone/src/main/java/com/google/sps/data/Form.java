package com.google.sps.data;
import java.io.*;

//TODO implement isPublished()

/**public Entity toDatastoreEntity(){
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("content", content);
        return commentEntity;
    }*/

public class Form {

    private String editURL;
    private String URL;
    private int id;

    public Form(String editURL, String URL, int id) {
        this.editURL = editURL;
        this.URL = URL;
        this.id = id;
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
        // TODO
    }
}