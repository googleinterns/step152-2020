package com.google.sps.service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.sps.data.User;
import com.google.sps.data.Form;
import com.google.sps.data.Content;
import com.google.sps.data.Video;
import com.google.sps.data.Image;
import com.google.sps.data.Room;
import com.google.sps.data.Lesson;
import java.lang.IllegalArgumentException;

public class DatabaseService {
    public static User getUser(Key key) throws EntityNotFoundException { 
        return new User(DatastoreServiceFactory.getDatastoreService().get(key));
    }

    public static Room getRoom(Key key) throws EntityNotFoundException {
        return new Room(DatastoreServiceFactory.getDatastoreService().get(key));  
    }

    public static Lesson getLesson(Key key) throws EntityNotFoundException {
        Entity entity = DatastoreServiceFactory.getDatastoreService().get(key);
        switch((String) entity.getProperty(Lesson.TYPE_PROPERTY_KEY)) {
            case Lesson.TYPE_FORM:
                return new Form(DatastoreServiceFactory.getDatastoreService().get(key));
            case Lesson.TYPE_VIDEO:
                return new Video(DatastoreServiceFactory.getDatastoreService().get(key));
            case Lesson.TYPE_IMAGE:
                return new Image(DatastoreServiceFactory.getDatastoreService().get(key));
            case Lesson.TYPE_CONTENT:
                return new Content(DatastoreServiceFactory.getDatastoreService().get(key));
            default:
                throw new IllegalArgumentException(); 
        }
    }

    public static void save(Entity entity) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(entity);
    }  

    public static void delete(Key key) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.delete(key);
    }
}