import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyRMIServerImpl extends UnicastRemoteObject implements MyRMIInterface {
    int currentSize = 0, prepareRepliesReceived = 0;
    ArrayList<String> participantIds = new ArrayList<String>();
    ArrayList<String> notReadyparticipantsIDs = new ArrayList<String>();
    ArrayList<String> acknowledgementIDs = new ArrayList<String>();
    String decision = "NONE";
    boolean sendPrepare, allReady = true, sendDecision, timedOut;

    public MyRMIServerImpl() throws RemoteException {
        System.out.println("Server Created ...");
    }

    // define remote methods for clients

    public void addParticipant(String id) throws RemoteException {
        System.out.println("\nClient " + id + " has joined");
        this.participantIds.add(id);
    }

    public void respondToPrepareMsg(String reply, String id) throws RemoteException {
        if (reply.equals("READY")) {
            System.out.println("\nClient " + id + " has sent READY : ");
        } else {
            System.out.println("\nClient " + id + " has sent ABORT reply : ");
            this.notReadyparticipantsIDs.add(id);
            this.allReady = this.allReady && false;
        }
        this.prepareRepliesReceived++;

    }

    public String receivePrepareMsg() throws RemoteException {
        if (this.sendPrepare)
            return "PREPARE";
        else
            return "OTHER";
    }

    public String receiveDecisionMsg() throws RemoteException {
        if (this.sendDecision)
            return this.decision;
        else
            return "OTHER";
    }

    public void sendAcknowledgement(String id) throws RemoteException {
        System.out.println("\nClient " + id + " has sent ACK :");
        this.acknowledgementIDs.add(id);
    }

    public static void main(String args[]) {

        try {
            MyRMIServerImpl myserver = new MyRMIServerImpl();
            // bind/register the server object (RMI registry)
            // Naming.rebind("rmi://localhost:5001/dpd",myserver);

            Naming.rebind("dpd", myserver);
            System.out.print("Server is Ready");

            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter number of participants : ");
            int maxParticipants = sc.nextInt();
            System.out.println("\nWaiting for " + maxParticipants + " participants to join...");
            while (myserver.participantIds.size() != maxParticipants) {
                TimeUnit.SECONDS.sleep(2);
                int remaining = maxParticipants - myserver.participantIds.size();
                System.out.println(
                        "Waiting for " + remaining + (remaining > 1 ? " more participants" : " more participant")
                                + " to join... ");
            }

            // TimeUnit.SECONDS.sleep(10);
            // if(myserver.participantIds.size() == maxParticipants)
            // {System.out.println("\nAll " + maxParticipants + " participants have joined...begining transaction...\n");
            // }
            
            System.out.println("\n--PHASE-I--\n");
            System.out.println("Sending PREPARE message to all participants...");
            

            try (
                    FileWriter fw = new FileWriter("log.txt", false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter p_out = new PrintWriter(bw)) {
                p_out.println("PREPARE");
                System.out.println("\nWrote PREPARE to the log file.");
                myserver.sendPrepare = true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            long limit = 100000, timeoutAt = System.currentTimeMillis() + limit, now, timeLeft;
            System.out.println("\nWaiting for replies from participants for " + limit + " ms...");

            while (myserver.prepareRepliesReceived != maxParticipants) {
                now = System.currentTimeMillis();
                TimeUnit.SECONDS.sleep(2);
                timeLeft = timeoutAt - now;
                int remaining = maxParticipants - myserver.prepareRepliesReceived;
                System.out.println(
                        "Waiting for " + remaining + (remaining > 1 ? " more participants" : " more participant")
                                + " to reply... ");
                if (timeLeft < 0) {
                    System.out.print("Server timed-out..\n");
                    myserver.timedOut = true;
                    break;
                }
            }

            System.out.println("All " + maxParticipants + " participants have replied...\n");

            System.out.println("\n--PHASE-II--\n");

            if (myserver.allReady && !myserver.timedOut) {
                System.out.println("All participants have sent READY message...");
                System.out.println("Sending COMMIT message to all participants...");
                myserver.decision = "GLOBAL_COMMIT";
            } else if (myserver.timedOut) {
                System.out.println("Timeoout occurred and all replies have not been received...");
                System.out.println("Sending ABORT message to all participants...");
                myserver.decision = "GLOBAL_ABORT";
            } else {
                System.out.println("Participants " + String.join(", ", myserver.notReadyparticipantsIDs)
                        + " have sent ABORT ...");
                System.out.println("Sending ABORT message to all participants...");
                myserver.decision = "GLOBAL_ABORT";
            }

            try (
                    FileWriter fw = new FileWriter("log.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter p_out = new PrintWriter(bw)) {
                p_out.println(myserver.decision);
                System.out.println("\nWrote " + myserver.decision + " to the log file.");
                myserver.sendDecision = true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            System.out.println("\nWaiting for acknowledgements from participants...");
            // while (myserver.acknowledgementIDs.size() != myserver.prepareRepliesReceived) {
            //     TimeUnit.SECONDS.sleep(2);
            //     int remaining = maxParticipants - myserver.acknowledgementIDs.size();
            //     System.out.println(
            //             "Waiting for " + remaining + (remaining > 1 ? " more participants" : " more participant")
            //                     + " to acknowledge... ");
            // }

            if (myserver.timedOut)
                System.out.println("\nAll participants who have not timed-out have acknowledged...\n");
            else
                System.out.println("\nAll participants have acknowledged...\n");

            try (
                    FileWriter fw = new FileWriter("log.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter p_out = new PrintWriter(bw)) {
                p_out.println("COMPLETE");
                System.out.println("\nWrote COMPLETE to the log file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            if (myserver.decision.equals("GLOBAL_COMMIT")) {
                System.out.println("\nTransaction successfully COMITTED !!\n");
            } else {
                System.out.println("\nTransaction successfully ABORTED !!\n");
            }
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }
}