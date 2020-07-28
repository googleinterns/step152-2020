package com.google.sps.servlets;

import java.io.IOException;
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
      //TO DO??
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      System.out.println(request);
      System.out.println(request.getParameter("editUrl"));
      System.out.println(request.getParameter("publishedUrl"));
      System.out.println(request.getParameter("formID"));
      
      String editURL = request.getParameter("editUrl");
      String publishedURL = request.getParameter("publishedUrl");
      int formID = Integer.parseInt(request.getParameter("formID"));

      Form form = new Form(editURL, publishedURL, formID);
      Entity formEntity = form.toDatastoreEntity();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(formEntity);

      response.setContentType("text/html");
      response.sendRedirect("/form.html");


    /*String email = request.getParameter("mail-input");

    Entity taskEntity = newEntity("Task");
    taskEntity.setProperty("user-email", email);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    response.sendRedirect("/form.html");*/
  }
}