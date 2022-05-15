package com;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Item {
	
	public Connection connect() 
	{ 
	 Connection con = null; 
	 
	 try 
	 { 
	 Class.forName("com.mysql.jdbc.Driver"); 
	 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/items", "root", ""); 
	 //For testing
	 System.out.print("Successfully connected"); 
	 } 
	 catch(Exception e) 
	 { 
	 e.printStackTrace(); 
	 } 
	 
	 return con; 
	}
	
	//Insert Data
	public String insertItem(String code, String name, String price, String desc) {
    	Connection conn = connect();
    	
    	String Output = "";
    	
    	try {
        	if (conn == null) {
        		return "Database connection error";
        	}
        	
        	//SQL query
        	String query = "INSERT INTO items (itemCode,itemName,itemPrice,itemDesc) VALUES (?,?,?,?)";
        	
        	//binding data to SQL query
        	PreparedStatement preparedStatement = conn.prepareStatement(query);
        	preparedStatement.setString(1, code);
        	preparedStatement.setString(2, name);
        	preparedStatement.setDouble(3, Double.parseDouble(price));
        	preparedStatement.setString(4, desc);
        	
        	//execute the SQL statement
        	preparedStatement.execute();
        	conn.close();

			String newItems = readItems(); 
			Output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
        	
    	} catch(Exception e) {
			Output = "{\"status\":\"error\", \"data\": \"Failed to insert the item\"}";
    		System.err.println(e.getMessage());
    	}
    	
    	return Output;
    }
	
	//read Data
	public String readItems() {
    	Connection conn = connect();
    	
    	String Output = "";
    	
    	try {
        	if (conn == null) {
        		return "Database connection error";
        	}
        	
        	//SQL query
        	String query = "SELECT * FROM items";
        	
        	//executing the SQL query
        	Statement statement = conn.createStatement();
        	ResultSet resultSet = statement.executeQuery(query);
        	
        	// Prepare the HTML table to be displayed
    		Output = "<table border='1'><tr><th>Item Code</th>" +"<th>Item Name</th><th>Item Price</th>"
    		+ "<th>Item Description</th>"
    		+ "<th>Update</th><th>Remove</th></tr>";
        	
        	while(resultSet.next()) {
        		String itemID = Integer.toString(resultSet.getInt("itemID"));
        		String itemCode = resultSet.getString("itemCode");
        		String itemName = resultSet.getString("itemName");
        		String itemPrice = Double.toString(resultSet.getDouble("itemPrice"));
        		String itemDesc = resultSet.getString("itemDesc");
        		
        		// Add a row into the HTML table
        		Output += "<tr><td>" + itemCode + "</td>"; 
        		Output += "<td>" + itemName + "</td>"; 
        		Output += "<td>" + itemPrice + "</td>"; 
        		Output += "<td>" + itemDesc + "</td>";
        		
        		// buttons
        		Output += "<td>"
						+ "<input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-sm btn-secondary' data-itemid='" + itemID + "'>"
						+ "</td>" 
        				+ "<td>"
						+ "<input name='btnRemove' type='button' value='Remove' class='btn btn-sm btn-danger btnRemove' data-itemid='" + itemID + "'>"
						+ "</td></tr>";
        	}

        	conn.close();
        	
        	// Complete the HTML table
        	Output += "</table>";
        	
    	} catch(Exception e) {
    		Output = "Failed to read the items";
    		System.err.println(e.getMessage());
    	}
    	
    	return Output;
    }
		
	//Update Data
	public String updateItem(String itemID, String code, String name, String price, String desc) {
    	Connection conn = connect();
    	
    	String Output = "";
    	
    	try {
        	if (conn == null) {
        		return "Database connection error";
        	}
        	
        	//SQL query
        	String query = "UPDATE items SET itemCode = ?,itemName = ?,itemPrice = ?,itemDesc = ? WHERE itemID = ?";
        	
        	//binding data to SQL query
        	PreparedStatement preparedStatement = conn.prepareStatement(query);
        	preparedStatement.setString(1, code);
        	preparedStatement.setString(2, name);
        	preparedStatement.setDouble(3, Double.parseDouble(price));
        	preparedStatement.setString(4, desc);
        	preparedStatement.setInt(5, Integer.parseInt(itemID));
        	
        	//execute the SQL statement
        	preparedStatement.executeUpdate();
        	conn.close();
        	
        	String newItems = readItems(); 
      		Output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
        	
    	} catch(Exception e) {
    		Output = "{\"status\":\"error\", \"data\":\"Failed to update the item.\"}"; 
    		System.err.println(e.getMessage());
    	}
    	
    	return Output;
    }
		
	//Delete Data
	public String deleteItem(String itemID) {
    	String Output = "";
    	Connection conn = connect();
    	
    	try {
        	if (conn == null) {
        		return "Database connection error";
        	}
        	
        	//SQL query
        	String query = "DELETE FROM items WHERE itemID = ?";
        	
        	//binding data to the SQL query
        	PreparedStatement preparedStatement = conn.prepareStatement(query);
        	preparedStatement.setInt(1, Integer.parseInt(itemID));
        	
        	//executing the SQL statement
        	preparedStatement.execute();
        	conn.close();
        	
        	String newItems = readItems(); 
      		Output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}"; 
        	
    	} catch(Exception e) {
			Output = "{\"status\":\"error\", \"data\":\"Failed to delete the item.\"}";
    		System.err.println(e.getMessage());
    	}
    	return Output;
    }

		
		
}



