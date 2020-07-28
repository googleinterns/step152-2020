package com.google.sps.data;
import java.io.*;
import java.util.List;

public interface User {
    public void userInfo(String email, String nickname, String id);
    public void setNickname(String newNickname);
    public String getEmail();
    public List<Classroom> getClassrooms();
    public String getNickname();
    public int getId();
}
