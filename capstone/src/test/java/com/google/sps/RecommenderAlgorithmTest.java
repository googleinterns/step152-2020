package com.google.sps;
 
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
 
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.Room;
import com.google.sps.data.User;
import com.google.sps.data.Tag;
import com.google.sps.data.RecommenderAlgorithm;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Key;
import com.google.sps.service.DatabaseService;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
 
@RunWith(JUnit4.class)
public final class RecommenderAlgorithmTest {
    
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    private static final List<Tag> TEST_CATEGORY_TAGS = Tag.CATEGORY_TAGS;

    private static User TEST_USER_SMITH;
    private static User TEST_USER_JOHN;
    private static User TEST_USER_BILL;
    private static User TEST_USER_SAM;
    private static User TEST_USER_ADAM;
    private static Room TEST_ROOM_POETRY;
    private static Room TEST_ROOM_RUN;
    private static Room TEST_ROOM_COOK; 
    private static Room TEST_ROOM_COOK_TWO;
    private static Room TEST_ROOM_SCIENCE;

    private static double epsilon = 0.00001;

    @Before
    public void setUp() {
        helper.setUp();
        TEST_USER_SMITH = new User("1.0234", "Smith");
        TEST_USER_JOHN = new User("2341.0", "John");
        TEST_USER_BILL = new User("341.02", "Bill");
        TEST_USER_SAM = new User("41.023", "Sam");
        TEST_USER_ADAM = new User("521.13", "Adam");
        DatabaseService.save(TEST_USER_SMITH.getUserEntity());
        DatabaseService.save(TEST_USER_JOHN.getUserEntity());
        DatabaseService.save(TEST_USER_BILL.getUserEntity());
        DatabaseService.save(TEST_USER_SAM.getUserEntity());
        DatabaseService.save(TEST_USER_ADAM.getUserEntity());
 
        TEST_ROOM_POETRY = new Room(TEST_USER_SMITH, "How to Teach Poetry", "Learn how to teach Poetry");
        TEST_ROOM_RUN = new Room(TEST_USER_JOHN, "Run Efficiently", "Improve your running experience");
        TEST_ROOM_COOK = new Room(TEST_USER_BILL, "Iron Chef Master Course", "Cook Better");
        TEST_ROOM_COOK_TWO = new Room(TEST_USER_SAM, "Iron Chef Master Course II", "Cook Better Part 2");
        TEST_ROOM_SCIENCE = new Room(TEST_USER_ADAM, "Physics I", "Understand the world");
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());
        DatabaseService.save(TEST_ROOM_SCIENCE.getRoomEntity());
    }
 
    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void calculateDistanceSameUserTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        EmbeddedEntity userTagMap = TEST_USER_SMITH.getCachedInterestVector();

        Double distances = RecommenderAlgorithm.distanceBetween(userTagMap, userTagMap);
        Assert.assertEquals(distances, 0.0, epsilon);
    }

    @Test
    public void calculateDistanceTwoUsersSameRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        EmbeddedEntity userTagMap = TEST_USER_SMITH.getCachedInterestVector();
        EmbeddedEntity otherTagMap = TEST_USER_JOHN.getCachedInterestVector();

        Double distances = RecommenderAlgorithm.distanceBetween(userTagMap, otherTagMap);
        Assert.assertEquals(distances, 0.0, epsilon);
    }

    @Test
    public void calculateDistanceTwoUsersDifferentRoomsSameTagTest() { 
        TEST_ROOM_COOK_TWO.addTag(Tag.COOKING);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        EmbeddedEntity userTagMap = TEST_USER_SMITH.getCachedInterestVector();
        EmbeddedEntity otherTagMap = TEST_USER_JOHN.getCachedInterestVector();

        Double distances = RecommenderAlgorithm.distanceBetween(userTagMap, otherTagMap);
        Assert.assertEquals(distances, 0.0, epsilon);
    }

    @Test
    public void calculateDistanceTwoUsersDifferentRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        EmbeddedEntity userTagMap = TEST_USER_SMITH.getCachedInterestVector();
        EmbeddedEntity otherTagMap = TEST_USER_JOHN.getCachedInterestVector();

        Double distances = RecommenderAlgorithm.distanceBetween(userTagMap, otherTagMap);
        Assert.assertEquals(distances, 1.4142135623730951, epsilon);
    }

    @Test
    public void calculateDistanceTwoUsersOneSimilarRoomTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        EmbeddedEntity userTagMap = TEST_USER_SMITH.getCachedInterestVector();
        EmbeddedEntity otherTagMap = TEST_USER_JOHN.getCachedInterestVector();

        Double distances = RecommenderAlgorithm.distanceBetween(userTagMap, otherTagMap);
        Assert.assertEquals(distances, 0.7653668647301795, epsilon);
    }

    @Test
    public void calculateDistanceTwoUsersTwoSimilarRoomsMultipleRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_POETRY.addTag(Tag.LITERATURE);
        TEST_ROOM_POETRY.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());

        EmbeddedEntity userTagMap = TEST_USER_SMITH.getCachedInterestVector();
        EmbeddedEntity otherTagMap = TEST_USER_JOHN.getCachedInterestVector();

        Double distances = RecommenderAlgorithm.distanceBetween(userTagMap, otherTagMap);
        Assert.assertEquals(distances, 0.6058108930553725, epsilon);
    }

    @Test
    public void calculateShortestDistanceMultipleUsersMultipleRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        TEST_ROOM_RUN.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_POETRY.addTag(Tag.LITERATURE);
        TEST_ROOM_POETRY.addFollower(TEST_USER_SMITH);
        TEST_ROOM_POETRY.addFollower(TEST_USER_JOHN);
        TEST_ROOM_POETRY.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());

        TEST_ROOM_COOK_TWO.addTag(Tag.COOKING);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());

        EmbeddedEntity smithTagMap = TEST_USER_SMITH.getCachedInterestVector();
        EmbeddedEntity johnTagMap = TEST_USER_JOHN.getCachedInterestVector();
        EmbeddedEntity samTagMap = TEST_USER_SAM.getCachedInterestVector();

        Double smithJohnDistance = RecommenderAlgorithm.distanceBetween(smithTagMap, johnTagMap);
        Double smithSamDistance = RecommenderAlgorithm.distanceBetween(smithTagMap, samTagMap);

        Assert.assertEquals(smithJohnDistance, 0.33820395745152554, epsilon);
    }

    /* ------------------------------------------------------------ */

    @Test
    public void recommendRoomsOneUserOneRoomTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);

        Assert.assertEquals(recommended, new ArrayList<>());
    }

    @Test
    public void recommendRoomsTwoUsersOneRecommendedRoomTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        List<Key> rooms = new ArrayList<>();
        rooms.add(TEST_ROOM_COOK.getRoomKey());

        Assert.assertEquals(recommended, rooms);
    }

    @Test
    public void recommendRoomsTwoUsersMultipleRecommendedRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_POETRY.addTag(Tag.LITERATURE);
        TEST_ROOM_POETRY.addTag(Tag.EDUCATION);
        TEST_ROOM_POETRY.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        List<Key> rooms = new ArrayList<>();
        rooms.add(TEST_ROOM_POETRY.getRoomKey());
        rooms.add(TEST_ROOM_COOK.getRoomKey());

        Assert.assertEquals(recommended, rooms);
    }

    @Test
    public void recommendRoomsMultipleUsersNoSimilarRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_POETRY.addTag(Tag.LITERATURE);
        TEST_ROOM_POETRY.addFollower(TEST_USER_ADAM);
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());

        TEST_ROOM_SCIENCE.addTag(Tag.EDUCATION);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_BILL);
        DatabaseService.save(TEST_ROOM_SCIENCE.getRoomEntity());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        List<Key> rooms = new ArrayList<>();
        rooms.add(TEST_ROOM_SCIENCE.getRoomKey());
        rooms.add(TEST_ROOM_POETRY.getRoomKey());
        rooms.add(TEST_ROOM_COOK.getRoomKey());

        Assert.assertEquals(recommended, rooms);
    }

    @Test
    public void recommendRoomsMultipleUsersMultipleRecommendedRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        TEST_ROOM_RUN.addFollower(TEST_USER_BILL);
        TEST_ROOM_RUN.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        TEST_ROOM_COOK.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_POETRY.addTag(Tag.LITERATURE);
        TEST_ROOM_POETRY.addTag(Tag.EDUCATION);
        TEST_ROOM_POETRY.addFollower(TEST_USER_JOHN);
        TEST_ROOM_POETRY.addFollower(TEST_USER_BILL);
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());

        TEST_ROOM_COOK_TWO.addTag(Tag.COOKING);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SAM);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_BILL);
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());

        TEST_ROOM_SCIENCE.addTag(Tag.EDUCATION);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_BILL);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_SCIENCE.getRoomEntity());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        List<Key> rooms = new ArrayList<>();
        rooms.add(TEST_ROOM_POETRY.getRoomKey());
        rooms.add(TEST_ROOM_COOK_TWO.getRoomKey());
        rooms.add(TEST_ROOM_SCIENCE.getRoomKey());
        rooms.add(TEST_ROOM_COOK.getRoomKey());

        Assert.assertEquals(recommended, rooms);
    }

    @Test
    public void recommendRoomsMultipleUsersSameRoomsTest() { 
        TEST_ROOM_RUN.addTag(Tag.FITNESS);
        TEST_ROOM_RUN.addFollower(TEST_USER_SMITH);
        TEST_ROOM_RUN.addFollower(TEST_USER_JOHN);
        TEST_ROOM_RUN.addFollower(TEST_USER_BILL);
        TEST_ROOM_RUN.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_RUN.getRoomEntity());

        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        TEST_ROOM_COOK.addFollower(TEST_USER_BILL);
        TEST_ROOM_COOK.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_POETRY.addTag(Tag.LITERATURE);
        TEST_ROOM_POETRY.addTag(Tag.EDUCATION);
        TEST_ROOM_POETRY.addFollower(TEST_USER_SMITH);
        TEST_ROOM_POETRY.addFollower(TEST_USER_JOHN);
        TEST_ROOM_POETRY.addFollower(TEST_USER_BILL);
        TEST_ROOM_POETRY.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_POETRY.getRoomEntity());

        TEST_ROOM_COOK_TWO.addTag(Tag.COOKING);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_JOHN);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_BILL);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());

        TEST_ROOM_SCIENCE.addTag(Tag.EDUCATION);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_SMITH);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_JOHN);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_BILL);
        TEST_ROOM_SCIENCE.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_SCIENCE.getRoomEntity());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        Assert.assertEquals(recommended, new ArrayList<>());
    }

    @Test
    public void recommendRoomsMultipleUsersNoTagsTest() { 
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_BILL);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());

        List<Key> rooms = new ArrayList<>();
        rooms.add(TEST_ROOM_COOK_TWO.getRoomKey());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        Assert.assertEquals(recommended, rooms);
    }

    @Test
    public void recommendRoomsMultipleUsersSameTagsTest() { 
        TEST_ROOM_COOK.addTag(Tag.COOKING);
        TEST_ROOM_COOK.addFollower(TEST_USER_SMITH);
        TEST_ROOM_COOK.addFollower(TEST_USER_JOHN);
        DatabaseService.save(TEST_ROOM_COOK.getRoomEntity());

        TEST_ROOM_COOK_TWO.addTag(Tag.COOKING);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_BILL);
        TEST_ROOM_COOK_TWO.addFollower(TEST_USER_SAM);
        DatabaseService.save(TEST_ROOM_COOK_TWO.getRoomEntity());

        List<Key> rooms = new ArrayList<>();
        rooms.add(TEST_ROOM_COOK_TWO.getRoomKey());

        List<Key> recommended = RecommenderAlgorithm.recommendRooms(TEST_USER_SMITH, 10);
        Assert.assertEquals(recommended, rooms);
    }
}
