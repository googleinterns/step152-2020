package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.sps.data.Lesson;
import com.google.sps.service.DatabaseService;
import com.google.sps.data.RequestParser;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lesson")
public class LessonHandlerServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // JsonObject jobject = JsonParser.parseString(RequestParser.parseStringFromRequest(request)).getAsJsonObject();

        // System.out.println(jobject);

        // Lesson lesson = new Lesson(jobject.get(Lesson.TITLE_PROPERTY_KEY).getAsString(), jobject.get(Lesson.DESCRIPTION_PROPERTY_KEY).getAsString());
        // DatabaseService.save(room.getRoomEntity());

        // response.setStatus(HttpServletResponse.SC_OK);
    }
}