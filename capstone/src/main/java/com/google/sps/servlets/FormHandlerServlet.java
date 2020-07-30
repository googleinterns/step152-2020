package com.google.sps.servlets;
import java.io.IOException;
import java.io.BufferedReader;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.data.Form;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/form-handler")
public class FormHandlerServlet extends HttpServlet {

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
        Gson gson = new Gson();
        Form form = gson.fromJson(sb.toString(), Form.class);
        
        Entity formEntity = form.toDatastoreEntity();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(formEntity);

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
