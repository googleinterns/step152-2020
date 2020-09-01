package com.google.sps.servlets;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Key;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.sps.data.User;
import com.google.sps.data.Room;
import com.google.sps.filter.AuthenticateFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key userKey = ((User) request.getAttribute(AuthenticateFilter.AUTHENTICATED_USER_ATTRIBUTE_KEY)).getUserKey();
        String searchRequest = request.getParameter("search");
        Filter searchFilter = null;

        if (!("".equals(searchRequest)) && searchRequest != null) {
            searchFilter = new FilterPredicate(Room.TITLE_PROPERTY_KEY, FilterOperator.EQUAL, searchRequest);
        }

        Query query = new Query(Room.ROOM_ENTITY_NAME).setFilter(searchFilter);
        PreparedQuery results = datastore.prepare(query);
    
        ArrayList<Room> rooms = new ArrayList<>();
        for(Entity entity : results.asIterable()){
            rooms.add(new Room(entity));
        }

        response.setContentType("application/json");
        response.getWriter().println(new Gson().toJson(rooms));
    }
}