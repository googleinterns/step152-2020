package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.sps.data.Room;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.data.RequestParser;
import com.google.sps.service.DatabaseService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class DeleteDataServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JsonObject jobject = JsonParser.parseString(RequestParser.parseStringFromRequest(request)).getAsJsonObject();
    String kind = jobject.get("kind").getAsString();
    Long id = Long.parseLong(jobject.get("id").getAsString());

    Key key = KeyFactory.createKey(kind, id);

    if (kind.equals(Room.ROOM_ENTITY_NAME)) {
        try {
            for (Key lessonKey : DatabaseService.getRoom(key).getAllLessons()) {
                DatabaseService.delete(lessonKey);
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Lesson entity does not exist: " + e);
        }
    }

    DatabaseService.delete(key);
    response.setStatus(HttpServletResponse.SC_OK);
  }
}