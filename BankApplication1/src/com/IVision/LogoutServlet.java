package com.IVision;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//Credit Pankaj Kumar
//Referred page: http://www.journaldev.com/1907/java-servlet-session-management-tutorial-with-examples-of-cookies-httpsession-and-url-rewriting

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {
  
  public void service( HttpServletRequest request, HttpServletResponse response ) 
      throws IOException {
    response.setContentType("text/html");
    Cookie[] cookies = request.getCookies();
    if(cookies != null){
      for(Cookie cookie : cookies){
        if( cookie.getName().equals("JSESSIONID") ) {
          System.out.println("JSESSIONID = "+cookie.getValue());
          break;
        }
      }
    }
    //invalidate the session if exists
    HttpSession session = request.getSession(false);
    System.out.println("User = "+session.getAttribute("user"));
    if(session != null) {
        session.invalidate();
    }
    response.sendRedirect("login.html");
  }
}