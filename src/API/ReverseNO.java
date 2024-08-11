package API;

import java.util.Scanner;

public class ReverseNO 
{
	public static void main(String args[])
	{
		Scanner sc= new Scanner(System.in);
		System.out.println("Enter A no");
		int num=sc.nextInt();
		int rev=0;
		
		while(num>0)
		{
			int reminder=num%10;
			rev=(rev*10)+reminder;
			num=num/10;
		}
		System.out.println(rev);
		sc.close();
	}

}
