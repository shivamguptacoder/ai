import java.rmi.*;

public interface MyRMIInterface extends Remote 
{
//declare functions here
public int convertPoundsToRupees(int pounds) throws RemoteException;

// public String getUserName(String uid) throws RemoteException;
} 