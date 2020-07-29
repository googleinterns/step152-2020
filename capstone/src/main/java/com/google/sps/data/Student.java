package com.google.sps.data;
import java.io.*;
import com.google.sps.data.Form;
import com.google.sps.data.Classroom;
import com.google.sps.data.User;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.Entity;


public class Student implements User {

    private String email;
    private List<Classroom> classrooms = new ArrayList<>();
    private String nickname;
    private int id;

    public Student(Entity studentEntity) {
        this.email = (String) studentEntity.getProperty("email");
        this.nickname = (String) studentEntity.getProperty("nickname");
        this.id = (Integer) studentEntity.getProperty("id");
        //this.classrooms = classrooms; // Check info
    }

    // Setters
    public void setNickname(String newNickname) {
        this.nickname = newNickname;
        // update datastore
    }

    // Getters
    public String getEmail() {
        return this.email;
    }

    public List<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getId() {
        return this.id;
    }

    // Database
    public void joinClassroom(Classroom classroom, Student student) {
        classroom.addStudent(student);
    }

    // User Information
    public void userInfo(String email, String nickname, String id) {
        System.out.println("Email: " + email);
        System.out.println("Nickname: " + nickname);
        System.out.println("ID: " + id);
    }

    public Entity toDatastoreEntity(){
        Entity studentEntity = new Entity("Student");
        studentEntity.setProperty("email", this.email);
       // studentEntity.setProperty("classrooms", this.classrooms);
        studentEntity.setProperty("nickname", this.nickname);
        studentEntity.setProperty("id", this.id);
        return studentEntity;
    }
}