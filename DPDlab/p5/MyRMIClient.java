import java.rmi.*;
import java.net.MalformedURLException;
import java.util.*;
import java.sql.*;

public class MyRMIClient {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        try {
            MyRMIInterface obj = (MyRMIInterface) Naming.lookup("dpd5");
            boolean exitOptionClicked;
            int choice = -1;

            int rollno;
            String name = "", message;
            float cgpa = 10;
            while (choice != 0) {
                System.out.println("\nOptions Menu:");
                System.out.println("1) Insert");
                System.out.println("2) Update");
                System.out.println("3) Delete");
                System.out.println("4) Select");
                System.out.println("5) Select with where condition");
                System.out.print("\nEnter option number (0 for exit): ");
                choice = sc.nextInt();
                System.out.println();
                switch (choice) {
                    case 0:
                        exitOptionClicked = true;
                        break;
                    case 1:
                        System.out.print("\nEnter roll no of the student: ");
                        rollno = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\nEnter name of the student: ");
                        name = sc.nextLine();
                        System.out.print("\nEnter cgpa of the student: ");
                        cgpa = sc.nextFloat();
                        message = obj.insert(rollno, name, cgpa);
                        System.out.print(message);
                        break;
                    case 2:
                        boolean updateName = false, updateCgpa = false;
                        System.out.print("\nEnter roll no of the student: ");
                        rollno = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\nDo you want to update name of the student? (Y or N): ");
                        String response = sc.nextLine();
                        if (response.equals("Y")) {
                            System.out.print("\nEnter updated name of the student: ");
                            name = sc.nextLine();
                            updateName = true;
                        }
                        System.out.print("\nDo you want to update CGPA of the student? (Y or N): ");
                        response = sc.nextLine();
                        if (response.equals("Y")) {
                            System.out.print("\nEnter updated CGPA of the student: ");
                            cgpa = sc.nextFloat();
                            updateCgpa = true;
                        }
                        message = obj.update(rollno, name, cgpa, updateName, updateCgpa);
                        System.out.print(message);
                        break;
                    case 3:
                        System.out.print("\nEnter roll no of the student whose record is to be deleted: ");
                        rollno = sc.nextInt();
                        message = obj.delete(rollno);
                        System.out.print(message);
                        break;
                    case 4:
                        message = obj.select();
                        System.out.println(message);
                        break;
                    case 5:
                        System.out.print("\nEnter the where clause to select: WHERE ");
                        sc.nextLine();
                        String whereClause = sc.nextLine();
                        message = obj.selectWhere(whereClause);
                        System.out.println(message);
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("\nError " + ex);
        }
    }
}