import java.util.*;
import java.io.*;
import java.util.Date;
import java.text.*;
import java.sql.Timestamp;

class doctorCredentials
{
	String userID,password;
	boolean getdoctorCredentials() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("doctorcredentials.txt");
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

class docinfo extends doctorCredentials
{
	String first,last,sex,city,state,blood_group,doc_regis_no;
	int age,contact;
	boolean log;
	void getInfo() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("doctorinfo.txt");
		BufferedReader br = new BufferedReader(fr);
		String s=br.readLine();
		while(s!=null)
		{
			String[] data = s.split("\t");
			if(data[0].equals(userID))
			{
				first=data[1];
				last=data[2];
				sex=data[3];
				city=data[4];
				state=data[5];
				blood_group=data[6];
				doc_regis_no=data[7];
				age=Integer.valueOf(data[8]);
				contact=Integer.valueOf(data[9]);
				break;
			}
			s=br.readLine();	
		}
		br.close();
		fr.close();	
	}
	void options() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		System.out.println("\t1. Show your details");
		System.out.println("\t2. Update your details");
		System.out.println("\t3. Show all appointment");
		System.out.println("\t4. Show Today's appointment");
		System.out.println("\t5. Logout");
		System.out.print("Enter your choice: ");
		Scanner scan = new Scanner(System.in);
		int choice =scan.nextInt();
		switch(choice)
		{
			case 1:
				{showInfo();	break;}
			case 2:
				{updateInfo();	break;}
			case 3:
				{showAppointment();	break;}
			case 4:
				{todayAppointment();	break;}
			case 5:
				{log=false;	
				new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
				break;}
		}
	}
	void showInfo() throws IOException,InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		System.out.println("\t\tYour details");
		System.out.println("\t"+first+" "+last);
		System.out.println("\t"+age+" "+sex+" "+contact);
		System.out.println("\t"+city+" "+state);
		System.out.println("\t"+blood_group+" "+doc_regis_no);
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
	}
	void updateInfo() throws IOException,InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		File oldFile = new File("doctorinfo.txt");
		File newFile = new File("temp.txt");
		FileWriter fw = new FileWriter(newFile,true);
		BufferedWriter bw = new BufferedWriter(fw);

		FileReader fr = new FileReader(oldFile);
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		while(s!=null)
		{
			String[] record = s.split("\t");
			if(userID.equals(record[0]))
			{
				Scanner scan = new Scanner(System.in);
				System.out.print("\n\tEnter City: ");	city = scan.nextLine();
				System.out.print("\tEnter State: ");	state = scan.nextLine();
				System.out.print("\tEnter Contact: ");	contact = scan.nextInt();
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
			}
			else
			{
				bw.write(s);
			}
			s=br.readLine(); 
			if(s != null)
				bw.newLine();
		}
		bw.flush();
		br.close();
		fr.close();
		bw.close();
		fw.close();
		oldFile.delete();
		File dump = new File("doctorinfo.txt");
		newFile.renameTo(dump);
		System.out.println("Your information has been updated.");
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
	}
	void todayAppointment() throws IOException,InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		appointment a = new appointment();
		FileReader fr = new FileReader("appointment.txt");
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		if(s==null)
			System.out.println("No appointments");
		while(s!=null)
		{
			String[] record = s.split("\t");
			Date date = new Date();
			if(userID.equals(record[1]) & record[2].equals("YES"))
			{
				a.pID = record[0];
				a.dID = record[1];
				a.status = record[2];
				try
				{
					a.appointDate = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy").parse(record[3]); 
					a.bookDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(record[4]).getTime());
				}catch(Exception e)
				{System.out.println(e);}
				if(compareDate(date,a.appointDate)==0)
					System.out.println("\t"+a.pID+"\t"+a.dID+"\t"+a.status+"\t"+a.appointDate+"\t"+a.bookDate);	
			}
			s=br.readLine();
		}
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
		a=null;
		System.gc();
		br.close();
		fr.close();
	}
	void showAppointment() throws IOException,InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		appointment a = new appointment();
		FileReader fr = new FileReader("appointment.txt");
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		if(s==null)
			System.out.println("No appointments");
		while(s!=null)
		{
			String[] record = s.split("\t");
			if(userID.equals(record[1]) & record[2].equals("YES"))
			{
				a.pID = record[0];
				a.dID = record[1];
				a.status = record[2];
				try
				{
					a.appointDate = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy").parse(record[3]); 
					a.bookDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(record[4]).getTime());
				}catch(Exception e)
				{System.out.println(e);}
				System.out.println("\t"+a.pID+"\t"+a.dID+"\t"+a.status+"\t"+a.appointDate+"\t"+a.bookDate);	
			}
			s=br.readLine();
		}
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
		a=null;
		System.gc();
		br.close();
		fr.close();
	}
	private static int compareDate(Date d1,Date d2)
	{
		if(d1.getYear()!=d2.getYear())
			return d1.getYear()-d2.getYear();
		else if(d1.getMonth()!=d2.getMonth())
			return d1.getMonth()-d2.getMonth();
		else
			return d1.getDate()-d2.getDate();
	}
} 

class doctorlogin
{
	doctorlogin() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		docinfo I = new docinfo();
		I.log=true;
		Scanner s = new Scanner(System.in);
		System.out.print("UserID: ");	I.userID = s.nextLine();
		System.out.print("Password: "); I.password = s.nextLine();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		if(I.getdoctorCredentials())
		{
			I.getInfo();
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tYou are logged in as "+I.first+" "+I.last);	
			System.out.print("Enter C to continue... ");
			char ch = (char)System.in.read();
			while(I.log)
			{
				I.options();
			}
			I=null;
			s=null;
			System.gc();
			System.out.println("You logged out");	
		}
		else
		{
			I=null;
			s=null;
			System.gc();
		}
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
		try{new rundoctorprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
} 
class docappointment
{
	String pID,dID,status;
	Date appointDate;
	Timestamp bookDate;
	void clean() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		File oldFile = new File("appointment.txt");
		File newFile = new File("temp.txt");
		FileWriter fw = new FileWriter(newFile,true);
		BufferedWriter bw = new BufferedWriter(fw);

		FileReader fr = new FileReader(oldFile);
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		while(s!=null)
		{
			String[] record = s.split("\t");
			try
			{appointDate = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy").parse(record[3]);}
			catch(Exception e)
			{System.out.println(e);}
			Date currentDate = new Date();
			long diff=(currentDate.getTime()-appointDate.getTime())/(60*1000)%60;
			if(diff>=10)
			{
				record[2]="NO";
				bw.write(record[0]);
				bw.write("\t"+record[1]);
				bw.write("\t"+record[2]);
				bw.write("\t"+record[3]);
				bw.write("\t"+record[4]);		
			}
			else
			{
				bw.write(s);
			}
			s=br.readLine(); 
			if(s != null)
				bw.newLine();
		}
		bw.flush();
		br.close();
		fr.close();
		bw.close();
		fw.close();
		oldFile.delete();
		File dump = new File("appointment.txt");
		newFile.renameTo(dump);
	}
}

class rundoctorprogram
{
	void run() throws IOException, InterruptedException
	{
		new docappointment().clean();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		System.out.print(" 1 for doctor login \n 2 for exit: ");
		System.out.print("\nEnter your choice:");
		Scanner scan = new Scanner(System.in);
		int in = scan.nextInt();
		if(in==1)
			new doctorlogin();
		else if(in==2)	
			System.out.println("Terminated");
		else
			this.run();
	}
}

class doctor
{
	public static void main(String[] args)
	{
		try{new rundoctorprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
}