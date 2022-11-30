import java.rmi.*;
import java.net.MalformedURLException;
import java.util.*;

public class MyRMIClient 
{
public static void main(String args[]) 
{
int n=0;		
String id="";
Scanner scanner=new Scanner(System.in);

try
{
MyRMIInterface obj =(MyRMIInterface)Naming.lookup("rashmi");
System.out.print("Enter amount in pounds ");
n=scanner.nextInt();

//call remote methods
int rs=obj.convertPoundsToRupees(n);
System.out.println("Value of "+n+" pounds is "+rs+" rupees");

// System.out.print("Enter userid ");
// id=scanner.nextLine();
// System.out.println("Name : "+obj.getUserName(id));
}
catch(Exception ex) 
{
System.out.println("Error " +ex);
}	
}
}