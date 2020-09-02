package com.google.sps.data;
 
import com.google.sps.data.Room;
import com.google.sps.data.User;
import com.google.sps.data.Tag;
import com.google.sps.service.DatabaseService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.FetchOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.lang.Iterable;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.lang.Iterable;
 
public class RecommenderAlgorithm {

    public static List<Key> recommendRooms(User user, Integer amount) {
        RecommenderAlgorithm recommender = null;

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter tagFilter = new FilterPredicate(User.USER_ID_PROPERTY_KEY, FilterOperator.NOT_EQUAL, user.getId());
        Query query = new Query(User.USER_ENTITY_NAME).setFilter(tagFilter);
        PreparedQuery results = datastore.prepare(query);
        Iterable<Entity> allOtherUsers = results.asIterable();

        TreeMap<Key, Double> distances = RecommenderAlgorithm.getDistanceMaps(user, allOtherUsers);
        TreeMap sorted = sortByValues(distances);

        List<Key> rooms = new ArrayList<>();
        for (Map.Entry<Key, Double> entry : distances.entrySet()) {
            rooms = recommender.findSimilarRooms(user, entry.getKey(), rooms);
        }

        if (rooms.size() < amount) {
            return rooms;
        }

        return rooms.subList(0, amount);
    }

    private static TreeMap<Key, Double> getDistanceMaps(User user, Iterable<Entity> allOtherUsers) {
        TreeMap<Key, Double> userDistancePairs = new TreeMap<>();
        for (Entity otherUser : allOtherUsers) {
            Double distance = RecommenderAlgorithm.distanceBetween(user.getCachedInterestVector(), (EmbeddedEntity) otherUser.getProperty(User.TAGS_PROPERTY_KEY));
            userDistancePairs.put(otherUser.getKey(), distance);
        }
        return userDistancePairs;
    }

    public static Double distanceBetween(EmbeddedEntity userTagMap, EmbeddedEntity otherTagMap) {
        double sum = 0;
        double delta = 0;
        for (Tag tag : Tag.CATEGORY_TAGS) {
            if (userTagMap.getProperty(tag.getTag()) == null && otherTagMap.getProperty(tag.getTag()) == null) {
                continue;
            } else if (userTagMap.getProperty(tag.getTag()) == null) {
                delta = (Double) otherTagMap.getProperty(tag.getTag());
            } else if (otherTagMap.getProperty(tag.getTag()) == null) {
                delta = (Double) userTagMap.getProperty(tag.getTag());
            } else {
                delta = (Double) userTagMap.getProperty(tag.getTag()) - (Double) otherTagMap.getProperty(tag.getTag());
            }
            sum += Math.pow(delta, 2.0);
        }
        return Math.sqrt(sum);
    }

    private static List<Key> findSimilarRooms(User user, Key key, List<Key> rooms) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter userRoomFilter = new FilterPredicate(Room.FOLLOWERS_PROPERTY_KEY, FilterOperator.NOT_EQUAL, key);
        Query userRoomQuery = new Query(Room.ROOM_ENTITY_NAME).setFilter(userRoomFilter);
        PreparedQuery results = datastore.prepare(userRoomQuery);

        if (results.countEntities() == 0) {
            return new ArrayList<>();
        }

        for (Entity result : results.asIterable()) {
            Room room = new Room(result);
            if (!room.getAllFollowers().contains(user.getUserKey()) && !rooms.contains(room.getRoomKey())) {
                rooms.add(room.getRoomKey());
            }
        }
        return rooms;
    }

    private static <K, V extends Comparable<V>> TreeMap<K, V> 
    sortByValues(final TreeMap<K, V> map) {
        Comparator<K> valueComparator = 
                new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = 
                    map.get(k1).compareTo(map.get(k2));
                if (compare == 0) 
                    return 1;
                else 
                    return compare;
            }
        };
    
        TreeMap<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
}