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
      //TODO 
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
         System.out.println("Request: " + request);
    //   System.out.println(request.getParameter("editUrl"));
    //   System.out.println(request.getParameter("publishedUrl"));
    //   System.out.println(request.getParameter("formID"));
      
    //   String editURL = request.getParameter("editUrl");
    //   String publishedURL = request.getParameter("publishedUrl");
    //   int formID = Integer.parseInt(request.getParameter("formID"));

        
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

    // StringBuffer jb = new StringBuffer();
    // String line = null;
    // try {
    //     BufferedReader reader = request.getReader();
    //     while ((line = reader.readLine()) != null)
    //     jb.append(line);
    // } catch (Exception e) { /*report an error*/ }

    //     JSONObject jsonObject;
    // try {
    //     jsonObject =  HTTP.toJSONObject(jb.toString());
    // } catch (JSONException e) {
    //     // crash and burn
    //     throw new IOException("Error parsing JSON request string");
    // }
    //   System.out.println(jsonObject.toString());


    //   System.out.println("Reader: " + reader);
    //   System.out.println("Reader.toString : " + reader.toString());

      Gson gson = new Gson();
      Form form = gson.fromJson(sb.toString(), Form.class);

    //   Form form = new Form(editURL, publishedURL, 0);
      
      Entity formEntity = form.toDatastoreEntity();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(formEntity);

      response.setContentType("text/html");
      response.sendRedirect("/form.html");
  }
}
