import java.rmi.*;

public interface MyRMIInterface extends Remote {

    public void addParticipant(String id) throws RemoteException;

    public void respondToPrepareMsg(String reply, String id) throws RemoteException;

    public String receivePrepareMsg() throws RemoteException;

    public String receiveDecisionMsg() throws RemoteException;

    public void sendAcknowledgement(String id) throws RemoteException;
}