package com.google.sps.filter;
 
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.User;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Entity;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.security.GeneralSecurityException;
import com.google.sps.data.RequestParser;
 
@WebFilter("/auth-filter")
public class AuthenticateFilter implements javax.servlet.Filter {

    public static final String AUTHENTICATED_USER_ATTRIBUTE_KEY = "User";
    private static final String AUTH_HEADER_NAME = "id_token";

    public void init(FilterConfig filterConfig) throws ServletException{
        
    }
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        GoogleIdToken idToken = null;
        try {
            idToken = RequestParser.verifyToken(req.getHeader(AuthenticateFilter.AUTH_HEADER_NAME));
        } catch (GeneralSecurityException e){
            System.out.println("Cannot verify token: " + e);
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e){
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String userId = idToken.getPayload().getSubject();

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter userFilter = new FilterPredicate(User.USER_ID_PROPERTY_KEY, FilterOperator.EQUAL, userId);
        Query query = new Query(User.USER_ENTITY_NAME).setFilter(userFilter);
        PreparedQuery results = datastore.prepare(query);

        request.setAttribute(User.USER_ENTITY_NAME, new User(results.asSingleEntity()));
        
        chain.doFilter(request, response);
    }
    
    public void destroy() {
        
    }
}
