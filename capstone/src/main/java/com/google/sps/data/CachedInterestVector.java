package com.google.sps.data;
 
import com.google.sps.data.Room;
import com.google.sps.data.User;
import com.google.sps.data.Tag;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EmbeddedEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Iterable;
import java.lang.Math;
 
public class CachedInterestVector {

    public static HashMap<String, Double> embeddedEntityToHashMap(EmbeddedEntity embeddedVectorMap) {
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        for(String tag : embeddedVectorMap.getProperties().keySet()) {
            vectorHashMap.put(tag, (Double) embeddedVectorMap.getProperty(tag));
        }
        return vectorHashMap;
    }

    public static EmbeddedEntity hashMapToEmbeddedEntity(HashMap<String, Double> vectorHashMap){
        EmbeddedEntity embeddedVectorMap = new EmbeddedEntity();
        for(String tag : vectorHashMap.keySet()) {
            embeddedVectorMap.setProperty(tag, vectorHashMap.get(tag));
        }
        return embeddedVectorMap;
    }

    public static void denormalizeVectorHashMap(HashMap<String, Double> vectorHashMap, Double magnitude) {
        if (vectorHashMap.isEmpty()){
            return;
        }
        for(String tag : vectorHashMap.keySet()) {
            vectorHashMap.put(tag, (double) Math.round(((vectorHashMap.get(tag))*magnitude)));
        }
    }
 
    public static void addTagToDenormalizedVectorHashMap(HashMap<String, Double> vectorHashMap, List<Tag> tags) {
        for(Tag tag : tags) {
            if (vectorHashMap.get(tag.getTag()) == null) {
                vectorHashMap.put(tag.getTag(), 1.0d);
            } else {
                vectorHashMap.put(tag.getTag(), vectorHashMap.get(tag.getTag()) + 1);
            }
        }
    }

    public static void removeTagFromDenormalizedVectorHashMap(HashMap<String, Double> vectorHashMap, List<Tag> tags) {
        for(Tag tag : tags) {
            if (vectorHashMap.get(tag.getTag()) == null) {
                continue;
            } else {
                vectorHashMap.put(tag.getTag(), vectorHashMap.get(tag.getTag()) - 1);
                if(vectorHashMap.get(tag.getTag()) == 0.0){
                    vectorHashMap.remove(tag.getTag());
                }
            }
        }
    }
 
    public static Double magnitude(HashMap<String, Double> vectorHashMap) {
        double sum = 0;
        for (String tag : vectorHashMap.keySet()) {
            double value = vectorHashMap.get(tag);
            sum += Math.pow(value, 2.0);
        }
        return Math.sqrt(sum);
    }
 
    public static void renormalizeVectorHashMap(HashMap<String, Double> vectorHashMap, Double magnitude) {
        for(String tag : vectorHashMap.keySet()) {
            vectorHashMap.put(tag, ((vectorHashMap.get(tag))/magnitude));
        }
    }

    public static void addRoomUpdateCachedInterestVector(User user, Room room) {
        HashMap<String, Double> vectorHashMap = embeddedEntityToHashMap(user.getCachedInterestVector());
        denormalizeVectorHashMap(vectorHashMap, user.getMagnitude());
        addTagToDenormalizedVectorHashMap(vectorHashMap, room.getAllTags());
        
        Double newMagnitude = magnitude(vectorHashMap);
        renormalizeVectorHashMap(vectorHashMap, newMagnitude);

        user.setMagnitude(newMagnitude);
        user.setCachedInterestVector(hashMapToEmbeddedEntity(vectorHashMap));
    }

    public static void removeRoomUpdateCachedInterestVector(User user, Room room) {
        HashMap<String, Double> vectorHashMap = embeddedEntityToHashMap(user.getCachedInterestVector());
        denormalizeVectorHashMap(vectorHashMap, user.getMagnitude());
        removeTagFromDenormalizedVectorHashMap(vectorHashMap, room.getAllTags());
        
        Double newMagnitude = magnitude(vectorHashMap);
        renormalizeVectorHashMap(vectorHashMap, newMagnitude);

        user.setMagnitude(newMagnitude);
        user.setCachedInterestVector(hashMapToEmbeddedEntity(vectorHashMap));
    }
}