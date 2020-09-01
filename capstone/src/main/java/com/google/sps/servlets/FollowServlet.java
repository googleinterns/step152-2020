package com.google.sps.servlets;

import java.io.IOException;
import com.google.sps.data.Lesson;
import com.google.sps.data.Room;
import com.google.sps.data.User;
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
import com.google.sps.data.CachedInterestVector;
import com.google.sps.service.FilterService;
import com.google.sps.service.DatabaseService;
import com.google.sps.data.RequestParser;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {

    private static final String ACTION_QUERY_PARAMETER = "action";
    private static final String ROOM_ID_PROPERTY_KEY = "room_id";

    private enum Action {
        FOLLOW{
            @Override
            public String asLowerCase() {
                return FOLLOW.toString().toLowerCase();
            }
        },
        UNFOLLOW{
            @Override
            public String asLowerCase() {
                return UNFOLLOW.toString().toLowerCase();
            }
        };

        public abstract String asLowerCase();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getAttribute(User.USER_ENTITY_NAME);
        Long roomId = Long.parseLong(request.getParameter(FollowServlet.ROOM_ID_PROPERTY_KEY));
        Room room = new Room(FilterService.getEntity(Room.ROOM_ENTITY_NAME, roomId));

        if (request.getParameter(FollowServlet.ACTION_QUERY_PARAMETER).equals(Action.FOLLOW.asLowerCase()) && !(room.getAllFollowers().contains(user.getUserKey()))) {
            room.addFollower(user);
        } 
        
        if (request.getParameter(FollowServlet.ACTION_QUERY_PARAMETER).equals(Action.UNFOLLOW.asLowerCase())) {
            room.removeFollower(user);
        }

        DatabaseService.save(user.getUserEntity());
        DatabaseService.save(room.getRoomEntity());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}