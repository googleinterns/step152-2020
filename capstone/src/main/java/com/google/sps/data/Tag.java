package com.google.sps.data;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public enum Tag {

    EDUCATION("Education"),
    COOKING("Cooking"),
    FITNESS("Fitness"),
    LITERATURE("Literature"),
    MUSIC("Music"),
    TECHNOLOGY("Technology"),
    RANDOM("Random"),
    MISCELLANEOUS("Miscellaneous"),
    RELIGIOUS("Religious"),
    ANCIENT("Ancient"),
    SPACE("Space"),
    ROMANTIC("Romantic"),
    COMEDY("Comedy"),
    ART("Art"),
    BAKING("Baking"),
    DANCING("Dancing"),
    CHALLENGE("Challenge"),
    BODY_BUILDING("Body Building"),
    DIETING("Dieting"),
    DESSERTS("Desserts"),
    ADVENTURE("Adventure"),
    MEDITATION("Meditation"),
    NEWS("News"),

    EASY_DIFFICULTY("Easy"),
    MEDIUM_DIFFICULTY("Medium"),
    HARD_DIFFICULTY("Hard"),

    ONE_MINUTE("1:00"),
    TWO_MINUTES("2:00"),
    THREE_MINUTES("3:00"),
    FOUR_MINUTES("4:00"),
    FIVE_MINUTES("5:00"),
    TEN_MINUTES("10:00"),
    TWENTY_MINUTES("20:00");

    private final String tag;

    private Tag(String tag) {
        this.tag = tag;
    }
    
    public static final List<Tag> CATEGORY_TAGS = Arrays.asList(
        Tag.EDUCATION,
        Tag.COOKING,
        Tag.FITNESS,
        Tag.LITERATURE,
        Tag.MUSIC,
        Tag.TECHNOLOGY,
        Tag.RANDOM,
        Tag.MISCELLANEOUS,
        Tag.RELIGIOUS,
        Tag.ANCIENT,
        Tag.SPACE,
        Tag.ROMANTIC,
        Tag.COMEDY,
        Tag.ART,
        Tag.BAKING,
        Tag.DANCING,
        Tag.CHALLENGE,
        Tag.BODY_BUILDING,
        Tag.DIETING,
        Tag.DESSERTS,
        Tag.ADVENTURE,
        Tag.MEDITATION,
        Tag.NEWS
    );

    public static final List<Tag> DIFFICULTY_TAGS = Arrays.asList(
        Tag.EASY_DIFFICULTY,
        Tag.MEDIUM_DIFFICULTY,
        Tag.HARD_DIFFICULTY
    );

    public static final List<Tag> TIME_TAGS = Arrays.asList(
        Tag.ONE_MINUTE,
        Tag.TWO_MINUTES,
        Tag.THREE_MINUTES,
        Tag.FOUR_MINUTES,
        Tag.FIVE_MINUTES,
        Tag.TEN_MINUTES,
        Tag.TWENTY_MINUTES
    );

    public static List<Tag> all() {
        List<Tag> tags = new ArrayList<>(Tag.CATEGORY_TAGS);
        tags.addAll(Tag.DIFFICULTY_TAGS);
        tags.addAll(Tag.TIME_TAGS);
        return tags;
    }

    public String getTag() {
        return this.tag;
    }

    public static Tag getTagFromString(String tagString) {
        for(Tag tag : Tag.all()){
            if(tag.getTag().equals(tagString)){
                return tag;
            }
        }
        return null;
    }
}