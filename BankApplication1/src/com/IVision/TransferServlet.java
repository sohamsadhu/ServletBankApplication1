package com.IVision;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransferServlet extends HttpServlet {
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		Connection connect = null;
		ServletContext context = getServletContext();
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connect = DriverManager.getConnection( context.getInitParameter("DB_URL"),
					context.getInitParameter("DB_UNAME"), context.getInitParameter("DB_PSWD"));
			Statement stmt = connect.createStatement();
			String amount = request.getParameter("amount");
			String from_account = request.getParameter("from_account");
			String to_account = request.getParameter("to_account");
			stmt.executeQuery("UPDATE customer SET balance=balance - "+ amount +
					"WHERE account_number="+ from_account);
			stmt.executeQuery("UPDATE customer SET balance=balance + "+ amount +
					"WHERE account_number="+ to_account);
			connect.commit();
			connect.close();
			response.getWriter().write(getResponseBody(from_account,  amount, to_account));
		} catch (SQLException | IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponseBody( String from_account, String amount, String to_account ) {
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
				"Amount $ " + amount + " transferred from account "+ from_account +", to account " + to_account +"."+
				"</body>" +
				"</html>"));
	}
}