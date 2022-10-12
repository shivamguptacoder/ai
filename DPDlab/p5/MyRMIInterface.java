import java.rmi.*;
import java.sql.ResultSet;

public interface MyRMIInterface extends Remote {
    // declare functions here
    public String insert(int rollno, String name, float cgpa) throws RemoteException;

    public String update(int rollno, String name, float cgpa, boolean updateName, boolean updateCgpa)
            throws RemoteException;

    public String delete(int rollno) throws RemoteException;

    public String select() throws RemoteException;

    public String selectWhere(String whereClause) throws RemoteException;
}