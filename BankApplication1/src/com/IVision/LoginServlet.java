package com.IVision;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Credit Pankaj Kumar
// Referred page: http://www.journaldev.com/1907/java-servlet-session-management-tutorial-with-examples-of-cookies-httpsession-and-url-rewriting

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
  
  public void service( HttpServletRequest request, HttpServletResponse response ) 
      throws IOException, ServletException {
    ServletConfig config = getServletConfig();
    String username = config.getInitParameter("login_name");
    String password = config.getInitParameter("login_password");
    String uname = request.getParameter("user_name");
    String pswd = request.getParameter("usr_pswd");
    if( username.equals(uname) && password.equals(pswd) ) {
      HttpSession session = request.getSession();
      session.setAttribute("user", uname);
      session.setMaxInactiveInterval( 30 * 60 );
      Cookie userName = new Cookie("user", uname);
      userName.setMaxAge( 30 * 60 );
      response.addCookie( userName );
      response.sendRedirect("LoginSuccess.jsp");
    } else {
      RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
      response.getWriter().println("<font color=red>Either user name or password is wrong.</font>.");
      rd.include(request, response);
    }
  }
}