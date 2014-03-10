package com.IVision;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BalanceServlet extends HttpServlet{

	public void service(HttpServletRequest request, HttpServletResponse response) {
		Connection connect = null;
		ServletContext context = getServletContext();
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connect = DriverManager.getConnection( context.getInitParameter("DB_URL"),
					context.getInitParameter("DB_UNAME"), context.getInitParameter("DB_PSWD"));
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT customer_id, balance FROM customer WHERE account_number="+
					request.getParameter("account"));
			int balance = 0;
			int customer_id = 0;
			while(rs.next()) {
				customer_id = rs.getInt("customer_id");
				balance = rs.getInt("balance");
			}
			connect.commit();
			connect.close();
			response.getWriter().write(getResponseBody(customer_id, balance, request.getParameter("account")));
		} catch (SQLException | IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponseBody( int customer_id, int balance, String account ) {
		return (new String("<!DOCTYPE html>"+
				"<html>" +
				"<head>" +
				"<meta charset=\"utf-8\">" +
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">" +
				"<title>Open an account with us!</title>" +
				"</head>" +
				"<body>" +
				"  <ul class=\"top-nav-ul\">" +
				"    <li class=\"top-nav-list\"><a href=\"openacct.html\"" +
				"      class=\"top-nav-anchor\">Open Account</a></li>" +
				"    <li class=\"top-nav-list\"><a href=\"balance.html\"" +
				"      class=\"top-nav-anchor\">Balance</a></li>" +
				"    <li class=\"top-nav-list\"><a href=\"deposit.html\"" +
				"      class=\"top-nav-anchor\">Deposit</a></li>" +
				"    <li class=\"top-nav-list\"><a href=\"withdraw.html\"" +
				"      class=\"top-nav-anchor\">Withdraw</a></li>" +
				"    <li class=\"top-nav-list\"><a href=\"transfer.html\"" +
				"      class=\"top-nav-anchor\">Transfer</a></li>" +
				"  </ul>" +
				"  <br/>" +
				"  <br/>" +
				"Customer with id " + customer_id + " for account " + account +" has balance of $"+ balance +"."+
				"</body>" +
				"</html>"));
	}
}