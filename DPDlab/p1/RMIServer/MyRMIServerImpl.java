
import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.sql.*;

public class MyRMIServerImpl extends UnicastRemoteObject implements MyRMIInterface {
    public MyRMIServerImpl() throws RemoteException {
        System.out.println("Creating server Object ...");
    }

    // define remote methods for clients
    @Override
    public void sendMessage(String s) throws RemoteException {
        System.out.println(s);
    }

    @Override
    public String getMessage(String text) throws RemoteException {
        return "Your message is: " + text;
    }

    public static void main(String args[]) {
        try {
            // create a server object
            MyRMIServerImpl myserver = new MyRMIServerImpl();

            // bind/register the server object (RMI registry)
            // Naming.rebind("rmi://localhost:5001/rashmi",myserver);
            Naming.rebind("Shivam", myserver);
            System.out.println("Server Ready...");
            
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }
}