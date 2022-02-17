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
    private static final String DBURL = "jdbc:derby:addressbookdb;create=true;user=addressbookdb;password=addressbookdb";
    //jdbc connection
    private static Connection conn = null;
    
    public static void main(String[] args) {
        createConnection();//Creating a connection to Derby Embedded database
        deleteContacts();//Flushing contacts table before changing anything
        insertContact(1, "Alice", "Doe", "2155559893");//Inserting a new record
        insertContact(2, "Yi", "Duan", "4243323231");//Inserting a new record
        insertContact(3, "Michael", "Faraday", "3343321050");//Inserting a new record
        updateContact(1, "Alice", "Souza", "2155559893");//Updating an existing record
        deleteContact(3);//Delete an existing record
        selectContacts();//See the final table
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
    private static void selectContacts()
    {
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from contact");
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
      This method updates an existing contact if contact ID matches
    */
    private static void updateContact(int contactID, String firstName, String lastName, String phoneNumber)
    {
        try
        {
            String sql = "UPDATE Contact SET firstName=?, lastName=?, phoneNumber=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phoneNumber);
            stmt.setInt(4, contactID);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing contact was updated successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method inserts a new contact
    */
    private static void insertContact(int contactID, String firstName, String lastName, String phoneNumber)
    {
        try
        {
            String sql = "INSERT INTO Contact(ID, firstName, lastName, phoneNumber) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, contactID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, phoneNumber);
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new contact was inserted successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method deletes a single contact if the contactID matches
    */
    private static void deleteContact(int contactID)
    {
        try
        {
            String sql = "DELETE FROM Contact WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, contactID);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A contact was deleted successfully!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    /**
      This method deletes all the contacts
    */
    private static void deleteContacts()
    {
        try
        {
            String sql = "DELETE FROM Contact";
            PreparedStatement stmt = conn.prepareStatement(sql);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("All contacts are deleted!");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}
