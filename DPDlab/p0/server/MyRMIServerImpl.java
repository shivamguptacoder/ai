import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.sql.*;

public class MyRMIServerImpl extends UnicastRemoteObject implements MyRMIInterface {
    public MyRMIServerImpl() throws RemoteException {
        System.out.println("Creating server Object ...");
    }

    // define remote methods for clients
    public int convertPoundsToRupees(int pounds) throws RemoteException {
        int rupees;
        rupees = pounds * 95;
        return (rupees);
    }

    // public String getUserName(String uid) throws RemoteException {
    //     String unm = "";
    //     Connection con;
    //     PreparedStatement pst;
    //     ResultSet rs;

    //     try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");

    //         con = DriverManager.getConnection("jdbc:mysql://localhost:3306/visadb?user=root&password=volkswagen");

    //         pst = con.prepareStatement("select usernm from users where userid=?;");
    //         pst.setString(1, uid);
    //         rs = pst.executeQuery();
    //         if (rs.next())
    //             unm = rs.getString(1);
    //         else
    //             unm = "userid does not exist";

    //         con.close();
    //     } catch (Exception e) {
    //         unm = e.getMessage();
    //     }
    //     return (unm);
    // }

    public static void main(String args[]) {
        try {
            // create a server object
            MyRMIServerImpl myserver = new MyRMIServerImpl();

            // bind/register the server object (RMI registry)
            // Naming.rebind("rmi://localhost:5001/rashmi",myserver);
            Naming.rebind("rashmi", myserver);
            System.out.println("Server Ready...");
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }
}