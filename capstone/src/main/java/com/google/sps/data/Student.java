package com.google.sps.data;
import java.io.*;
import com.google.sps.data.Form;
import com.google.sps.data.Classroom;
import com.google.sps.data.User;
import java.util.ArrayList;
import java.util.List;

public class Student implements User {

    private String email;
    private List<Classroom> classrooms = new ArrayList<>();
    private String nickname;
    private int id;

    public Student(String email, List<Classroom> classrooms, String nickname, int id) {
        this.email = email;
        this.classrooms = classrooms;
        this.nickname = nickname;
        this.id = id;
    }

    // Setters
    public void setNickname(String newNickname) {
        this.nickname = newNickname;
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
}