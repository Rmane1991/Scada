package API;

import java.util.Scanner;

public class ReverseString 
{
  public static void main (String args[])
  {
	  Scanner sc=new Scanner(System.in);
	  System.out.println("Enter String");
	  String ch=sc.nextLine();
	  
	  String rev="";
	  
	  for (int i=0;i<ch.length();i++)
	  {
		 char c= ch.charAt(i);
		 rev=c+rev ;		  
				  
		  
	  }
	  System.out.println(rev);
	  sc.close();
  }
}
