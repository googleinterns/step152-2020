package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import java.util.ArrayList;
import java.util.List;

public class Room {
    public static final String ROOM_ENTITY_NAME = "Room";
    public static final String TITLE_PROPERTY_KEY = "title";
    public static final String DESCRIPTION_PROPERTY_KEY = "description";
    public static final String HOST_PROPERTY_KEY = "host";
    public static final String FOLLOWERS_PROPERTY_KEY = "followers";
    public static final String TAGS_PROPERTY_KEY = "tags";
    public static final String LESSONS_PROPERTY_KEY = "lessons";

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

    @SuppressWarnings("unchecked")
    public void addTag(Tag tag) {
        if (this.entity.getProperty(Room.TAGS_PROPERTY_KEY) == null) {
            this.entity.setProperty(Room.TAGS_PROPERTY_KEY, new ArrayList<String>());
        }
        List<String> tags = (ArrayList<String>) this.entity.getProperty(Room.TAGS_PROPERTY_KEY);
        tags.add(tag.getTag());
    }
    
    @SuppressWarnings("unchecked")
    public void removeTag(Tag tag) {
        if (this.entity.getProperty(Room.TAGS_PROPERTY_KEY) == null) {
            return;
        }
        ArrayList<String> tags = (ArrayList<String>) this.entity.getProperty(Room.TAGS_PROPERTY_KEY);
        tags.remove(tag.getTag());
    }

    @SuppressWarnings("unchecked")
    public List<Tag> getAllTags() { 
        if (this.entity.getProperty(Room.TAGS_PROPERTY_KEY) == null) {
            return new ArrayList<Tag>();
        }
        ArrayList<String> tagStrings = (ArrayList<String>) this.entity.getProperty(Room.TAGS_PROPERTY_KEY);
        ArrayList<Tag> tags = new ArrayList<Tag>();
        for(String tagString : tagStrings) {
            tags.add(Tag.getTagFromString(tagString));
        }
        return tags;
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

    public String getDescription() {
        return (String) this.entity.getProperty(Room.DESCRIPTION_PROPERTY_KEY);
    }

    public void setDescription(String description) {
        this.entity.setProperty(Room.DESCRIPTION_PROPERTY_KEY, description);
    }

    @SuppressWarnings("unchecked")
    public List<Key> getAllFollowers() {
        if (this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY) == null) {
            return new ArrayList<Key>();
        }
        return (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
    }

    @SuppressWarnings("unchecked")
    public void addFollower(User follower) {
        if (this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY) == null) {
            this.entity.setProperty(Room.FOLLOWERS_PROPERTY_KEY, new ArrayList<Key>());
        }
        ArrayList<Key> followers = (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
        followers.add(follower.getUserKey());
        CachedInterestVector.addRoomUpdateCachedInterestVector(follower, this);
    }

    @SuppressWarnings("unchecked")
    public void removeFollower(User follower) {
        if (this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY) == null) {
            this.entity.setProperty(Room.FOLLOWERS_PROPERTY_KEY, new ArrayList<Key>());
        }
        ArrayList<Key> followers = (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
        followers.remove(follower.getUserKey());
        CachedInterestVector.removeRoomUpdateCachedInterestVector(follower, this);
    }

    @SuppressWarnings("unchecked")
    public List<Key> getAllLessons() { 
        if (this.entity.getProperty(Room.LESSONS_PROPERTY_KEY) == null) {
            return new ArrayList<Key>();
        }
        return (ArrayList<Key>) this.entity.getProperty(Room.LESSONS_PROPERTY_KEY); 
    }
    
    @SuppressWarnings("unchecked")
    public void addLesson(Lesson lesson) {
        if (this.entity.getProperty(Room.LESSONS_PROPERTY_KEY) == null) {
            this.entity.setProperty(Room.LESSONS_PROPERTY_KEY, new ArrayList<Key>());
        }
        ArrayList<Key> lessons = (ArrayList<Key>) this.entity.getProperty(Room.LESSONS_PROPERTY_KEY);
        lessons.add(lesson.getLessonKey());
    }

    @SuppressWarnings("unchecked")
    public void removeLesson(Lesson lesson) {
        if (this.entity.getProperty(Room.LESSONS_PROPERTY_KEY) == null) {
            return;
        }
        ArrayList<Key> lessons = (ArrayList<Key>) this.entity.getProperty(Room.LESSONS_PROPERTY_KEY);
        lessons.remove(lesson.getLessonKey());
    }

    @SuppressWarnings("unchecked")
    public boolean isFollowerInRoom(User follower) {
        ArrayList<Key> followers = (ArrayList<Key>) this.entity.getProperty(Room.FOLLOWERS_PROPERTY_KEY);
        return (!(followers.lastIndexOf(follower.getUserKey()) == -1));
    }

    public Entity getRoomEntity() {
        return this.entity;
    }

    public Key getRoomKey() {
        return this.entity.getKey();
    }
}