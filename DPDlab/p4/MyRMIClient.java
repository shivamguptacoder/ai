import java.rmi.*;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MyRMIClient{
       public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        
        try {
            MyRMIInterface obj = (MyRMIInterface) Naming.lookup("dpd");
            
            String alphabet = "1234567890";
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            int length = 7;
            for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
            }
            String timeStampID =sb.toString();
          
           
            obj.addParticipant(timeStampID);
            System.out.println("ID for this client: " + timeStampID + "\n--PHASE-I--\n");

            String serverMsg, clientReply, action;
            System.out.print("Waiting for server message");
            while (true) {
                serverMsg = obj.receivePrepareMsg();
                TimeUnit.SECONDS.sleep(1);
                System.out.print(".");
                if (serverMsg.equals("PREPARE")) {
                    System.out.print("\nServer has sent PREPARE message. (READY or ABORT) ?: ");
                    clientReply = sc.nextLine();
                    obj.respondToPrepareMsg(clientReply, timeStampID);

                    try (
                            FileWriter fw = new FileWriter("log" + timeStampID + ".txt", true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter p_out = new PrintWriter(bw)) {
                        p_out.println(clientReply);
                        System.out.println("\nWrote " + clientReply + " to the log file.");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    break;
                }
            }

            System.out.println("\n--PHASE-II--\n");
            System.out.print("Waiting for server message");
            while (true) {
                serverMsg = obj.receiveDecisionMsg();
                TimeUnit.SECONDS.sleep(1);
                System.out.print(".");
                if (serverMsg.equals("GLOBAL_COMMIT") || serverMsg.equals("GLOBAL_ABORT")) {
                    if (serverMsg.equals("GLOBAL_COMMIT")) {
                        action = "COMMIT";
                    } else {
                        action = "ABORT";
                    }
                    System.out.print("\nServer has sent command: " + action);

                    try (
                            FileWriter fw = new FileWriter("log" + timeStampID + ".txt", true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter p_out = new PrintWriter(bw)) {
                        p_out.println(action);
                        System.out.println("\nWrote " + action + " to the log file.");
                        obj.sendAcknowledgement(timeStampID);
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    break;
                }
            }

            System.out.println("\n\nSub-transaction successfully " + action + "ED !!");
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }
}


