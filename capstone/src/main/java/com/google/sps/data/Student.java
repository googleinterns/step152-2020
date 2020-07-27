package com.google.sps.data;
import java.io.*;
import com.google.sps.data.Form;
import com.google.sps.data.Classroom;
import com.google.sps.data.User;

public class Student implements User {

    private String email;
    private String classroom;
    private String nickname;
    private String type;
    private int id;

    public Student(String email, String classroom, String nickname, String type, int id) {
        this.email = email;
        this.classroom = classroom;
        this.nickname = nickname;
        this.type = type;
        this.id = id;
    }

    // Setters
    public void setNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    // Getters
    public String getEmail() {
        return this.email;
    }

    public String getClassroom() {
        return this.classroom;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public void userInfo(String email, String nickname, String id) {
        System.out.println("Email: " + email);
        System.out.println("Nickname: " + nickname);
        System.out.println("ID: " + id);
    }
}