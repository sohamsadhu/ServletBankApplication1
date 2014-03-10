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

public class DepositServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) {
		Connection connect = null;
		ServletContext context = getServletContext();
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connect = DriverManager.getConnection( context.getInitParameter("DB_URL"),
					context.getInitParameter("DB_UNAME"), context.getInitParameter("DB_PSWD"));
			Statement stmt = connect.createStatement();
			String amount = request.getParameter("amount");
			String account = request.getParameter("account");
			ResultSet rs = stmt.executeQuery("UPDATE customer SET balance=balance + "+ amount +
					"WHERE account_number="+ account);
			int balance = 0;
			rs = stmt.executeQuery("SELECT balance FROM customer WHERE account_number="+ account);
			while(rs.next()) {
				balance = rs.getInt("balance");
			}
			connect.commit();
			connect.close();
			response.getWriter().write(getResponseBody(balance,  amount, account));
		} catch (SQLException | IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponseBody( int balance, String amount, String account ) {
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
				"Account number " + account + " with deposit $"+ amount +" updated balance is $" + balance +"."+
				"</body>" +
				"</html>"));
	}
}