package com.google.sps.data;
import java.io.*;
import com.google.sps.data.Teacher;
import com.google.sps.data.Classroom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random; 

public class Key {

    private int id;
    private Classroom classroom;
    private Teacher teacher;

    public Key (Classroom classroom, Teacher teacher) {
        this.classroom = classroom;
        this.teacher = teacher;
        this.id = -1;
    }

    // Setters
    public void setId(List<Integer> idList) {
        Random random = new Random();

        this.id = random.nextInt(100);

        while (idList.contains(this.id)) {
            this.id = random.nextInt(100);
        }

        idList.add(this.id);
    }

    // Getters
    public int getId() {
        assert this.id != -1: "ID needs to be created";
        return this.id;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }
}