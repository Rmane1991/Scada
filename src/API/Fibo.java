package API;

import java.util.Scanner;

public class Fibo 
{
 public static void main (String args[])
 
 {
	 int first=0, sec=1, no , num;
	Scanner sc =new Scanner(System.in);
	System.out.println("Enter a no");
	no=sc.nextInt();
	
	for(int i=0;i<no;i++)
	{
		System.out.println(first);
		num=first+sec;
		first=sec;
		sec=num;
	}

	
	
	
	sc.close();
			 
 }
}
