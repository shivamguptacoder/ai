import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.sql.*;

public class MyRMIServerImpl extends UnicastRemoteObject implements MyRMIInterface {
    Connection con;

    public MyRMIServerImpl() throws RemoteException {
        System.out.println("Creating server Object ...");
    }

    public String insert(int rollno, String name, float cgpa) throws RemoteException {
        Statement stmt;

        String sql = "insert into student values (" + Integer.toString(rollno) + ", '" + name + "',"
                + Float.toString(cgpa) + ")";
        String message = "";
        try {
            stmt = this.con.createStatement();
            stmt.executeUpdate(sql);
            message = "\nRecord successfully inserted..\n";
        } catch (SQLException e) {
            message = "\nException...The record could not be inserted...\n";
        }
        return message;
    }

    public String update(int rollno, String name, float cgpa, boolean updateName, boolean updateCgpa)
            throws RemoteException {
        Statement stmt;
        String message = "Nothing to update";
        String sql = "update student set ";
        if (updateName && updateCgpa) {
            sql = sql + "name='" + name + "', cgpa=" + Float.toString(cgpa);
        } else if (!updateName && updateCgpa) {
            sql = sql + "cgpa=" + Float.toString(cgpa);
        } else if (updateName && !updateCgpa) {
            sql = sql + "name='" + name + "'";
        } else
            return message;
        sql = sql + " where rollno=" + Integer.toString(rollno);
        try {
            stmt = this.con.createStatement();
            stmt.executeUpdate(sql);
            message = "\nRecord successfully updated..\n";
        } catch (SQLException e) {
            e.printStackTrace();
            message = "\nError...The record could not be updated...\n";
        }
        return message;
    }

    public String delete(int rollno) throws RemoteException {
        Statement stmt;
        String sql = "delete from student where rollno=" + Integer.toString(rollno);
        String message = "";
        try {
            stmt = this.con.createStatement();
            stmt.executeUpdate(sql);
            message = "\nRecord successfully deleted..\n";
        } catch (SQLException e) {
            e.printStackTrace();
            message = "\nError...The record could not be deleted...\n";
        }
        return message;
    }

    public String select() throws RemoteException {
        ResultSet rs;
        String s = "ROLLNO\t     NAME\tCGPA\n------------------------------\n";
        Statement stmt;
        int len = 0;
        boolean empty = true;
        try {
            stmt = this.con.createStatement();
            rs = stmt.executeQuery("select * from student");
            while (rs.next()) {
                len++;
                empty = false;
                s = s + Integer.toString(rs.getInt(1)) + "\t" + rs.getString(2) + "\t"
                        + Float.toString(rs.getFloat(3))
                        + "\n";
            }
            s = s + "\n" + Integer.toString(len) + " rows selected.\n";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (empty)
            return "No rows selected.\n";
        return s;
    }

    public String selectWhere(String whereClause) throws RemoteException {
        ResultSet rs;
        String s = "ROLLNO\t     NAME\tCGPA\n------------------------------\n";
        Statement stmt;
        int len = 0;
        boolean empty = true;
        try {
            stmt = this.con.createStatement();
            String sql = "select * from student where " + whereClause;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                len++;
                empty = false;
                s = s + Integer.toString(rs.getInt(1)) + "\t" + rs.getString(2) + "\t"
                        + Float.toString(rs.getFloat(3))
                        + "\n";
            }
            s = s + "\n" + Integer.toString(len) + " rows selected.\n";
        } catch (SQLException e) {
            System.out.println("Error...");
            e.printStackTrace();
        }
        if (empty)
            return "No rows selected.\n";
        return s;
    }

    public static void main(String[] args) throws Exception {
        try {
            // create a server object
            MyRMIServerImpl myserver = new MyRMIServerImpl();
            // open cmd ONLY in
            // C:\Users\ABHISHEK YELNE\Desktop\mydb\RMISever\bin
            // to start rmiregistry

            // bind/register the server object (RMI registry)
            // Naming.rebind("rmi://localhost:5001/rashmi",myserver);
            Naming.rebind("dpd5", myserver);
            System.out.println("Server Ready...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // myserver.con = DriverManager.getConnection(
                // "jdbc:mysql://localhost:3306/college", "root", "system");
                // biews2yu6ssoafcgjobl-mysql.services.clever-cloud.com
                myserver.con = DriverManager.getConnection(
                        "jdbc:mysql://biews2yu6ssoafcgjobl-mysql.services.clever-cloud.com", "uwheyaqrnjqdfa90",
                        "kQe5Lji0vPaN2tNRk6Ts");

                System.out.println("Server Ready2...");
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }
}
