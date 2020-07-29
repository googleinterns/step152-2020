package com.google.sps.servlets;

import java.io.IOException;
import java.io.*;
import org.json.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.data.Form;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/form-handler")
public class FormHandlerServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<MemePost> memePosts = new ArrayList<>();

        MemePost memePost = null;
        Query query = new Query("MemePost");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            memePost = new MemePost(entity);
            memePosts.add(memePost);
        }

        Gson gson = new Gson();
        String json = gson.toJson(memePosts);
        response.setContentType("application/json");
        response.getWriter().println(json);


  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      BufferedReader reader = request.getReader();
      StringBuilder sb = new StringBuilder();
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line).append('\n');
        }
      } finally {
          reader.close();
      }
      System.out.println(sb.toString());

      Gson gson = new Gson();
      Form form = gson.fromJson(sb.toString(), Form.class);
      
      Entity formEntity = form.toDatastoreEntity();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(formEntity);

      response.setContentType("text/html");
    //   response.sendRedirect("/form.html");
  }
}
