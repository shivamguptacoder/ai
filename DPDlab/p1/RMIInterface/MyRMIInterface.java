import java.rmi.*;

public interface MyRMIInterface extends Remote 
{
//declare functions here
public int makeSquare(int sqr) throws RemoteException;

public int discounter(int dis) throws RemoteException;
}