package API;

import java.util.Scanner;

public class First 
{
  public static void main (String args[])
  {
	  int a=0 , b=1 , d ,e;
	  Scanner sc = new Scanner(System.in);
	  System.out.println("Series no");
	  d=sc.nextInt();
	  
	  //System.out.println("Enter First no");
	  //b=sc.nextInt();
	  
	  //c=a+b;
	  
	//  System.out.println(a +" ");
	 // System.out.print(b +" ");
	  //System.out.print(c +" ");
	  
	  for(int i=0;i<d;i++)
	  {
		  System.out.println(a);
		  e=a+b;
		  a=b;
		  b=e;
	  }
	  
	  sc.close();
  }
}
