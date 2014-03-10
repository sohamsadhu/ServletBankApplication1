package com.IVision;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenAccountServlet extends HttpServlet {
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		Connection connect = null;
		ServletContext context = getServletContext();
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connect = DriverManager.getConnection( context.getInitParameter("DB_URL"),
					context.getInitParameter("DB_UNAME"), context.getInitParameter("DB_PSWD"));
			insertAddress( connect, request );
			insertCustomer( connect, request );
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery("select account_number from customer where customer_id="+
					request.getParameter("customer_id"));
			String account = null;
			while(rs.next()) {
				account = rs.getString("account_number");
			}
			connect.commit();
			connect.close();
			response.getWriter().write(getResponseBody(request.getParameter("customer_id"), account));
		} catch (SQLException | IOException | NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void insertAddress( Connection connect, HttpServletRequest request ) throws SQLException {
		PreparedStatement pstmt = connect.prepareStatement
				("INSERT INTO ADDRESS VALUES (?, ?, ?, ?, ?, ?)");
		pstmt.setInt(1, Integer.valueOf(request.getParameter("customer_id")));
		pstmt.setInt(2, Integer.valueOf(request.getParameter("apt")));
		pstmt.setString(3, request.getParameter("street"));
		pstmt.setString(4, request.getParameter("city"));
		pstmt.setString(5, request.getParameter("state"));
		pstmt.setInt(6, Integer.valueOf(request.getParameter("zip")));
		pstmt.executeUpdate();
	}
	
	public void insertCustomer( Connection connect, HttpServletRequest request ) 
			throws SQLException, ParseException {
		PreparedStatement pstmt = connect.prepareStatement
				("INSERT INTO CUSTOMER VALUES (ACCOUNT_NUMBER.NEXTVAL, ?, ?, ?, ?, ?)");
		pstmt.setInt(1, Integer.valueOf(request.getParameter("customer_id")));
		pstmt.setString(2, request.getParameter("firstname"));
		pstmt.setString(3, request.getParameter("lastname"));
		pstmt.setDate(4, new java.sql.Date(new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH)
			.parse(request.getParameter("dateOfBirth")).getTime()));
		pstmt.setInt(5, Integer.valueOf(request.getParameter("balance")));
		pstmt.executeUpdate();
	}
	
	public String getResponseBody( String customer_id, String account ) {
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
				"Account made with account number " + account + " for customer with id " + customer_id +"."+
				"</body>" +
				"</html>"));
	}
}