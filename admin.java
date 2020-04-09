import java.util.*;
import java.io.*;
import java.util.Date;

class adminCredentials
{
	String userID,password;
	void storedoctorCredentials() throws IOException, InterruptedException
	{
		FileWriter fw = new FileWriter("doctorcredentials.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write(userID);
		bw.write("\t"+password);
		bw.close();
		fw.close();
	}
	boolean getadminCredentials() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("admincredentials.txt");
		BufferedReader br = new BufferedReader(fr);
		String s=br.readLine();
		while(s!=null)
		{
			String[] data = s.split("\t");
			if(data[0].equals(userID))
			{
				if(data[1].equals(password))
				{
					br.close();
					fr.close();
					return true;
				}
				else
				{
					System.out.println("UserID and password does not match..");
					br.close();
					fr.close();
					return false;
				}
			}
			s=br.readLine();
		}
		System.out.println("userID does not exist..");
		br.close();
		fr.close();
		return false;
	}
}

class doctorinfo extends adminCredentials
{
	String first,last,sex,city,state,blood_group,doc_regis_no;
	int age,contact;
	void storadminInfo() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("doctorinfo.txt");
		BufferedReader br = new BufferedReader(fr);
		String lastId = "";
		String s = br.readLine();
		while(s != null)
		{
			String[] data = s.split("\t");
			lastId = data[0];
			s = br.readLine();
		}
		br.close();
		fr.close();
		if(lastId == "")
			userID = "D1";
		else
			userID = "D"+ Integer.toString(1+Integer.parseInt(lastId.substring(1,lastId.length())));

		FileWriter fw = new FileWriter("doctorinfo.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write(userID);
		bw.write("\t"+first);
		bw.write("\t"+last);
		bw.write("\t"+sex);
		bw.write("\t"+city);
		bw.write("\t"+state);
		bw.write("\t"+blood_group);
		bw.write("\t"+doc_regis_no);
		bw.write("\t"+age);
		bw.write("\t"+contact);
		bw.close();
		fw.close();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
	}
} 

class adminlogin
{
	adminlogin() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		doctorinfo I = new doctorinfo();
		Scanner s = new Scanner(System.in);
		System.out.print("UserID: ");	I.userID = s.nextLine();
		System.out.print("Password: "); I.password = s.nextLine();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		if(I.getadminCredentials())
		{
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\tYou are logged in as "+I.userID);	
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n1 for doctor registrations"+"\n"+"2 for exit ");	
			System.out.print("\nEnter your choice:");
			Scanner scan = new Scanner(System.in);
			int in = scan.nextInt();
			if(in==1)
				new doctorsignup();
			else if(in==2)	
				System.out.println("Terminated");   		
		}
		else
		{
			I=null;
			s=null;
			System.gc();
			System.out.print("Enter C to continue... ");
			char ch = (char)System.in.read();
			try{new runadminprogram().run();}
			catch(Exception e)
			{System.out.println(e);}
		}
	}
} 

class doctorsignup
{
	doctorsignup() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		doctorinfo I = new doctorinfo();
		Scanner s = new Scanner(System.in);
		System.out.print("First Name: ");	I.first = s.nextLine();
		System.out.print("Last Name: ");	I.last = s.nextLine();
		System.out.print("Sex(M/F): ");	I.sex = s.nextLine();	
		System.out.print("City: ");	I.city = s.nextLine();
		System.out.print("State: ");	I.state = s.nextLine();
		System.out.print("Blood Group: ");	I.blood_group = s.nextLine();
		System.out.print("Doctor Registration number: ");	I.doc_regis_no = s.nextLine();
		System.out.print("Contact: ");	I.contact = s.nextInt();
		System.out.print("Age: ");	I.age = s.nextInt();
		Scanner p = new Scanner(System.in);
		System.out.print("Password: ");	I.password = p.nextLine();
		I.storadminInfo();
		I.storedoctorCredentials();
		System.out.println(I.first+" "+I.last+",your user ID is "+I.userID);
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
		I = null;
		s = null;
		p = null;
		System.gc();
		try{new runadminprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
}

class runadminprogram
{
	void run() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		System.out.print("1 for login"+"\n"+"2 for exit \n");
		System.out.print("\nEnter your choice:");
		Scanner scan = new Scanner(System.in);
		int in = scan.nextInt();
		if(in==1)
			new adminlogin();
		else if(in==2)	
			System.out.println("Terminated");
	    else
			this.run();
	}
}

class admin
{
	public static void main(String[] args)
	{
		try{new runadminprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
}