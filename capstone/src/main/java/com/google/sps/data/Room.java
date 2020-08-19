package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import java.util.ArrayList;
import java.util.List;
import com.google.sps.service.DatabaseService;

public class Room {
    public static final String ROOM_ENTITY_NAME = "Room";
    public static final String TITLE_PROPERTY_KEY = "title";
    public static final String DESCRIPTION_PROPERTY_KEY = "description";
    public static final String HOST_PROPERTY_KEY = "host";
    public static final String FOLLOWERS_PROPERTY_KEY = "followers";

    private Entity entity;

    public Room(Entity entity) {
        this.entity = entity;
    }

    public Room(User host, String title, String description) { 
        this.entity = new Entity(Room.ROOM_ENTITY_NAME);
        this.entity.setProperty(Room.TITLE_PROPERTY_KEY, title);
        this.entity.setProperty(Room.DESCRIPTION_PROPERTY_KEY, description);
        this.entity.setProperty(Room.HOST_PROPERTY_KEY, host.getUserKey());
    }

    public Entity getRoomEntity() {
        return this.entity;
    }

    public Key getHost() {
        return (Key) this.entity.getProperty(Room.HOST_PROPERTY_KEY);
    }

    public void setHost(User host) { 
        this.entity.setProperty(Room.HOST_PROPERTY_KEY, host.getUserKey());
    }

    public String getTitle() {
        return (String) this.entity.getProperty(Room.TITLE_PROPERTY_KEY);
    }

    public void setTitle(String title){
        this.entity.setProperty(Room.TITLE_PROPERTY_KEY, title);
    }

    @SuppressWarnings("unchecked")
    public List<Key> getAllFollowers() {
        return (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
    }
    
    @SuppressWarnings("unchecked")
    public void addFollower(User follower) {
        if (this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY) == null) {
            this.entity.setProperty(Room.FOLLOWERS_PROPERTY_KEY, new ArrayList<Key>());
        }
        ArrayList<Key> followers = (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
        followers.add(follower.getUserKey());
    }

    @SuppressWarnings("unchecked")
    public void removeFollower(User follower) {
        if (this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY) == null) {
            this.entity.setProperty(Room.FOLLOWERS_PROPERTY_KEY, new ArrayList<Key>());
        }
        ArrayList<Key> followers = (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
        followers.remove(follower.getUserKey());
    }

    @SuppressWarnings("unchecked")
    public boolean isFollowerInRoom(User follower) {
        ArrayList<Key> followers = (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
        return (!(followers.lastIndexOf(follower.getUserKey()) == -1));
    }
}