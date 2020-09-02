package com.google.sps.servlets;

import java.io.IOException;
import com.google.sps.data.Lesson;
import com.google.sps.data.Room;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.sps.data.Lesson;
import com.google.sps.data.Room;
import com.google.sps.data.RequestParser;
import com.google.sps.service.DatabaseService;
import com.google.sps.data.RequestParser;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lesson")
public class LessonHandlerServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery roomResults = datastore.prepare(new Query(Room.ROOM_ENTITY_NAME));
        Room room = null;

        for(Entity entity : roomResults.asIterable()) {
            if (new Room(entity).getRoomKey().getId() == Long.parseLong(request.getParameter("room_id"))) {
                room = new Room(entity);
            }
        }
        
        response.setContentType("application/json");
        response.getWriter().println(new Gson().toJson(getLessons(room)));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lesson lesson = Lesson.deserializeJson(RequestParser.parseStringFromRequest(request));
        DatabaseService.save(lesson.getLessonEntity());

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery roomResults = datastore.prepare(new Query(Room.ROOM_ENTITY_NAME));
        Room room = null;

        for(Entity entity : roomResults.asIterable()) {
            if (new Room(entity).getRoomKey().getId() == Long.parseLong(request.getParameter("room_id"))) {
                room = new Room(entity);
                room.addLesson(lesson);
            }
        }
        
        DatabaseService.save(room.getRoomEntity()); 
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public List<Lesson> getLessons(Room room) {
        List<Lesson> lessons = new ArrayList<>();
    
        try {
            for(Key key : room.getAllLessons()) {
                lessons.add(DatabaseService.getLesson(key));
            }
        }
        catch (EntityNotFoundException e) {
            System.err.println("Lesson entities don't exist.");
        }
        return lessons;
    }
}