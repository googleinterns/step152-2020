package com.google.sps.data;
import java.io.*;

public interface User {
    public void userInfo(String email, String nickname, String id);
    public void setNickname(String newNickname);
    public void setType(String type);
    public String getEmail();
    public String getClassroom();
    public String getNickname();
    public String getType();
    public int getId();
}
