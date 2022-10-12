import java.rmi.*;
import java.net.MalformedURLException;
import java.util.*;

public class MyRMIClient {
    public static void main(String args[]) {
        int n = 0;
        int sr = 0;
        Scanner scanner = new Scanner(System.in);

        try {
            MyRMIInterface obj = (MyRMIInterface) Naming.lookup("Shivam");
            System.out.print("Enter number to convert to square :");
            n = scanner.nextInt();

            System.out.print("\nEnter amount for to get discount ");
            sr = scanner.nextInt();

            // call remote methods
            int ss = obj.makeSquare(n);
            System.out.println("\nThe Square of " + n + " is " + ss);

            int rs = obj.discounter(n);
            System.out.println("\nThe Discount on " + sr + " is " + rs);
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }
}