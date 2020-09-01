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
import com.google.sps.data.CachedInterestVector;
import com.google.sps.service.DatabaseService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EmbeddedEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;

@RunWith(JUnit4.class)
public final class CachedInterestVectorTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    private static final double epsilon = .00001;

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void embeddedEntityToHashMapTest(){
        Double magnitude = 3.0;
        EmbeddedEntity embeddedVectorMap = new EmbeddedEntity();
        embeddedVectorMap.setProperty(Tag.EDUCATION.getTag(), (2.0/magnitude));
        embeddedVectorMap.setProperty(Tag.COOKING.getTag(), (2.0/magnitude));
        embeddedVectorMap.setProperty(Tag.FITNESS.getTag(), (1.0/magnitude));

        HashMap<String, Double> vectorHashMap = CachedInterestVector.embeddedEntityToHashMap(embeddedVectorMap);
        Assert.assertEquals(vectorHashMap.get(Tag.EDUCATION.getTag()), (2.0/magnitude), epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.COOKING.getTag()), (2.0/magnitude), epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.FITNESS.getTag()), (1.0/magnitude), epsilon);
    }

    @Test
    public void hashMapToEmbeddedEntityTest(){
        Double magnitude = 3.0;
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        vectorHashMap.put(Tag.EDUCATION.getTag(), (2.0/magnitude));
        vectorHashMap.put(Tag.COOKING.getTag(), (2.0/magnitude));
        vectorHashMap.put(Tag.FITNESS.getTag(), (1.0/magnitude));

        EmbeddedEntity embeddedVectorMap = CachedInterestVector.hashMapToEmbeddedEntity(vectorHashMap);

        Assert.assertEquals((double) embeddedVectorMap.getProperty(Tag.EDUCATION.getTag()), (2.0/magnitude), epsilon);
        Assert.assertEquals((double) embeddedVectorMap.getProperty(Tag.COOKING.getTag()), (2.0/magnitude), epsilon);
        Assert.assertEquals((double) embeddedVectorMap.getProperty(Tag.FITNESS.getTag()), (1.0/magnitude), epsilon);
    }

    @Test
    public void denormalizeVectorHashMapTest() {
        Double magnitude = 3.0;
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        vectorHashMap.put(Tag.EDUCATION.getTag(), (2.0/magnitude));
        vectorHashMap.put(Tag.COOKING.getTag(), (2.0/magnitude));
        vectorHashMap.put(Tag.FITNESS.getTag(), (1.0/magnitude));

        CachedInterestVector.denormalizeVectorHashMap(vectorHashMap, magnitude);

        Assert.assertEquals(vectorHashMap.get(Tag.EDUCATION.getTag()), 2.0, epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.COOKING.getTag()), 2.0, epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.FITNESS.getTag()), 1.0, epsilon);
    }

    @Test
    public void addTagToDenormalizedVectorHashMapTest() {
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        vectorHashMap.put(Tag.EDUCATION.getTag(), 1.0);
        vectorHashMap.put(Tag.COOKING.getTag(), 1.0);

        List<Tag> tagsToAdd = new ArrayList<>(); 
        tagsToAdd.add(Tag.EDUCATION);
        tagsToAdd.add(Tag.COOKING);
        tagsToAdd.add(Tag.FITNESS);

        CachedInterestVector.addTagToDenormalizedVectorHashMap(vectorHashMap, tagsToAdd);

        Assert.assertEquals(vectorHashMap.get(Tag.EDUCATION.getTag()), 2.0, epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.COOKING.getTag()), 2.0, epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.FITNESS.getTag()), 1.0, epsilon);
    }

    @Test
    public void removeTagFromDenormalizedVectorHashMapTest() {
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        vectorHashMap.put(Tag.EDUCATION.getTag(), 2.0);
        vectorHashMap.put(Tag.COOKING.getTag(), 2.0);
        vectorHashMap.put(Tag.FITNESS.getTag(), 1.0);

        List<Tag> tagsToRemove = new ArrayList<>(); 
        tagsToRemove.add(Tag.EDUCATION);
        tagsToRemove.add(Tag.COOKING);
        tagsToRemove.add(Tag.FITNESS);

        CachedInterestVector.removeTagFromDenormalizedVectorHashMap(vectorHashMap, tagsToRemove);

        Assert.assertEquals(vectorHashMap.get(Tag.EDUCATION.getTag()), 1.0, epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.COOKING.getTag()), 1.0, epsilon);
        Assert.assertNull(vectorHashMap.get(Tag.FITNESS.getTag()));
    }

    @Test
    public void magnitudeTest() {
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        vectorHashMap.put(Tag.EDUCATION.getTag(), 2.0);
        vectorHashMap.put(Tag.COOKING.getTag(), 2.0);
        vectorHashMap.put(Tag.FITNESS.getTag(), 1.0);

        Assert.assertEquals(CachedInterestVector.magnitude(vectorHashMap), 3.0, epsilon);
    }

    @Test
    public void renormalizeVectorHashMapTest() {
        Double magnitude = 3.0;
        HashMap<String, Double> vectorHashMap = new HashMap<>();
        vectorHashMap.put(Tag.EDUCATION.getTag(), 2.0);
        vectorHashMap.put(Tag.COOKING.getTag(), 2.0);
        vectorHashMap.put(Tag.FITNESS.getTag(), 1.0);

        CachedInterestVector.renormalizeVectorHashMap(vectorHashMap, magnitude);

        Assert.assertEquals(vectorHashMap.get(Tag.EDUCATION.getTag()), (2.0/magnitude), epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.COOKING.getTag()), (2.0/magnitude), epsilon);
        Assert.assertEquals(vectorHashMap.get(Tag.FITNESS.getTag()), (1.0/magnitude), epsilon);
    }
}
