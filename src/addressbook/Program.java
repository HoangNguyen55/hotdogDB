/*
 A simple example for an addressbook with Java DB
 This example shows 
 1. How to connect with an existing Java DB
 2. How to delete all the records in a table (flushing table)
 3. How to insert record
 4. How to update an existing record
 5. How to delete an existing record
 6. How to print a table in tabular format to the command line
 */
package addressbook;
import java.sql.*;
/**
 *
 * @author sgo7
 */
public class Program {

    //Make sure you have the correct path for your derby embedded database in below string
    private static final String DBURL = "jdbc:derby:hotdogDB1;create=true;user=root;password=root";
    //jdbc connection
    private static Connection conn = null;
    
    public static void main(String[] args) {
        createConnection();//Creating a connection to Derby Embedded database
        deleteCustomers();//Flushing customer table before changing anything
        insertCustomer(1, "Alice", "Doe", "Hot Dog");//Inserting a new record
        insertCustomer(2, "Yi", "Duan", "Combo");//Inserting a new record
        insertCustomer(3, "Michael", "Faraday", "Drink");//Inserting a new record
        updateCustomer(1, "Alice", "Doe", "Combo");//Updating an existing record
        deleteCustomer(3);//Delete an existing record
        selectCustomer();//See the final table
    }
    
    /**
      This method creates a connection to Derby Embedded database
      It can throw an exception if the db URL is not correct
      or DB is already accessed by another program (or derby server)
    */
    public static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(DBURL);
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method prints the whole table to the command line in a tabular format
    */
    private static void selectCustomer()
    {
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from HD_CUSTOMER");
            ResultSetMetaData rsmd = results.getMetaData();
            //Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            //Print column names as header
            for (int i = 1; i <= numberCols; i++)
            {
                //print Column Names
                System.out.printf("%12s", rsmd.getColumnLabel(i));
            }
            System.out.println();
            //Print the results
            while(results.next())
            {
                for (int i = 1; i <= numberCols; i++)
                {
                    System.out.printf("%12s", results.getString(i));
                }
                System.out.println();
            }
            results.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method updates an existing contact if customer ID matches
    */
    private static void updateCustomer(int customerID, String customerFirstName, String customerLastName, String customerFavoriteMeal)
    {
        try
        {
            String sql = "UPDATE HD_CUSTOMER SET CUSTOMER_FIRST_NAME=?, CUSTOMER_LAST_NAME=?, CUSTOMER_FAVORITE_MEAL=? WHERE CUSTOMER_ID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerFirstName);
            stmt.setString(2, customerLastName);
            stmt.setString(3, customerFavoriteMeal);
            stmt.setInt(4, customerID);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing customer was updated successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method inserts a new customer
    */
    private static void insertCustomer(int customerID, String customerFirstName, String customerLastName, String customerFavoriteMeal)
    {
        try
        {
            String sql = "INSERT INTO HD_CUSTOMER(CUSOMTER_ID, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_FAVORITE_MEAL) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerID);
            stmt.setString(2, customerFirstName);
            stmt.setString(3, customerLastName);
            stmt.setString(4, customerFavoriteMeal);
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new customer was inserted successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method deletes a single customer if the customer id matches
    */
    private static void deleteCustomer(int customerID)
    {
        try
        {
            String sql = "DELETE FROM HD_CUSTOMER WHERE CUSTOMER_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerID);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A customer was deleted successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method deletes all the Customers
    */
    private static void deleteCustomers()
    {
        try
        {
            String sql = "DELETE FROM HD_CUSTOMER";
            PreparedStatement stmt = conn.prepareStatement(sql);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("All customers are deleted!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    private static void selectOrder()
    {
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from HD_ORDER");
            ResultSetMetaData rsmd = results.getMetaData();
            //Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            //Print column names as header
            for (int i = 1; i <= numberCols; i++)
            {
                //print Column Names
                System.out.printf("%12s", rsmd.getColumnLabel(i));
            }
            System.out.println();
            //Print the results
            while(results.next())
            {
                for (int i = 1; i <= numberCols; i++)
                {
                    System.out.printf("%12s", results.getString(i));
                }
                System.out.println();
            }
            results.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method updates an existing contact if order ID matches
    */
    private static void updateOrder(int orderID, int orderPrice, String orderDateTime, String itemName, int customerID)
    {
        try
        {
            String sql = "UPDATE HD_ORDER SET ORDER_PRICE=?, ORDER_DATE_TIME=?, ITEM_NAME=?, CUSTOMER_ID=? WHERE ORDER_ID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderPrice);
            stmt.setString(2, orderDateTime);
            stmt.setString(3, itemName);
            stmt.setInt(4, customerID);
            stmt.setInt(5, orderID);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing customer was updated successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method inserts a new order
    */
    private static void insertOrder(int orderID, int orderPrice, String orderDateTime, String itemName, int customerID)
    {
        try
        {
            String sql = "INSERT INTO HD_ORDER(ORDER_ID, ORDER_PRICE, ORDER_DATE_TIME, ITEM_NAME, CUSTOMER_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderID);
            stmt.setInt(2, orderPrice);
            stmt.setString(3, orderDateTime);
            stmt.setString(4, itemName);
            stmt.setInt(5, customerID);
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new order was inserted successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method deletes a single customer if the customer id matches
    */
    private static void deleteOrder(int orderID)
    {
        try
        {
            String sql = "DELETE FROM HD_ORDER WHERE CUSTOMER_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderID);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("An order was deleted successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method deletes all the Customers
    */
    private static void deleteOrders()
    {
        try
        {
            String sql = "DELETE FROM HD_ORDER";
            PreparedStatement stmt = conn.prepareStatement(sql);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("All orders are deleted!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
}

    