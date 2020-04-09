import java.util.*;
import java.io.*;
import java.text.*;
import java.sql.Timestamp;

class patientCredentials
{
	String userID,password;
	void storeCredentials() throws IOException, InterruptedException
	{
		FileWriter fw = new FileWriter("patientcredentials.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write(userID);
		bw.write("\t"+password);
		bw.close();
		fw.close();
	}
	boolean getCredentials() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("patientcredentials.txt");
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

class info extends patientCredentials
{
	String first,last,sex,city,state,blood_group,disease;
	int age,contact;
	boolean log;
	void storeInfo() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("patientinfo.txt");
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
			userID = "P1";
		else
			userID = "P"+ Integer.toString(1+Integer.parseInt(lastId.substring(1,lastId.length())));

		FileWriter fw = new FileWriter("patientinfo.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write(userID);
		bw.write("\t"+first);
		bw.write("\t"+last);
		bw.write("\t"+sex);
		bw.write("\t"+city);
		bw.write("\t"+state);
		bw.write("\t"+blood_group);
		bw.write("\t"+disease);
		bw.write("\t"+age);
		bw.write("\t"+contact);
		bw.close();
		fw.close();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
	}
	void getInfo() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("patientinfo.txt");
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
				disease=data[7];
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
		System.out.println("\t3. Get appointment");
		System.out.println("\t4. Show appointment");
		System.out.println("\t5. Cancel appointment");
		System.out.println("\t6. Logout");
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
				{getAppointment();	break;}
			case 4:
				{showAppointment();	break;}
			case 5:
				{cancelAppointment(); break;}
			case 6:
				{log=false;	
				new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
				break;}
		}
	}
	void getAppointment()
	{
		appointment a = new appointment();
		a.pID=userID;
		char ch;
		try
		{	
			a.appoint();
			if(a.check())
			{
				a.status="YES";
				a.store();
				System.out.println("You have successfully get the appointment.");	
			}
			else
			{
				System.out.println("You did not get the appointment.");
			}
			System.out.print("Enter C to continue... ");
			ch = (char)System.in.read();
		}
		catch(Exception e)
		{System.out.println(e);}
		a=null;
		System.gc();		
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
			if(userID.equals(record[0]) & record[2].equals("YES"))
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
	void cancelAppointment() throws IOException,InterruptedException
	{
		File oldFile = new File("appointment.txt");
		File newFile = new File("temp.txt");
		FileWriter fw = new FileWriter(newFile,true);
		BufferedWriter bw = new BufferedWriter(fw);

		FileReader fr = new FileReader(oldFile);
		BufferedReader br = new BufferedReader(fr);
		int c=1,i=0;
		String s;
		br.mark(1000);
		while(c>i | c==0)
		{
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
			System.out.println("Which appointment you want to cancel?\n");
			i=0;
			s=br.readLine();
			while(s!=null)
			{
				String[] record = s.split("\t");
				if(userID.equals(record[0]) & record[2].equals("YES"))
				{
					i++;
					System.out.println(i+"-> "+s);	
				}
				s=br.readLine(); 
			}
			System.out.print("\nEnter the serial number of appointment you want to cancel?.. ");
			Scanner scan = new Scanner(System.in);
			c = scan.nextInt();
			br.reset();
		}
		s=br.readLine();
		i=0;
		while(s!=null)
		{
			String[] record = s.split("\t");
			if(userID.equals(record[0]) & record[2].equals("YES"))
			{
				i++;
				if(i==c)
				{
					record[2]="NO";
					bw.write(record[0]);
					bw.write("\t"+record[1]);
					bw.write("\t"+record[2]);
					bw.write("\t"+record[3]);
					bw.write("\t"+record[4]);
				}
				else
					bw.write(s);		
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
		System.out.println("You have cancelled the appointment");
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
	}

	void showInfo() throws IOException,InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		System.out.println("\t\tYour details");
		System.out.println("\t"+first+" "+last);
		System.out.println("\t"+age+" "+sex+" "+contact);
		System.out.println("\t"+city+" "+state);
		System.out.println("\t"+blood_group+" "+disease);
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
	}
	void updateInfo() throws IOException,InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		File oldFile = new File("patientinfo.txt");
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
				System.out.print("\tEnter Disease: ");	disease = scan.nextLine();
				System.out.print("\tEnter Contact: ");	contact = scan.nextInt();
				bw.write(userID);
				bw.write("\t"+first);
				bw.write("\t"+last);
				bw.write("\t"+sex);
				bw.write("\t"+city);
				bw.write("\t"+state);
				bw.write("\t"+blood_group);
				bw.write("\t"+disease);
				bw.write("\t"+age);
				bw.write("\t"+contact);				
			}
			else
			{
				bw.write(s);
			}
			bw.newLine();
			s=br.readLine(); 
		}
		bw.flush();
		br.close();
		fr.close();
		bw.close();
		fw.close();
		oldFile.delete();
		File dump = new File("patientinfo.txt");
		newFile.renameTo(dump);
		System.out.println("Your information has been updated.");
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
	}
} 

class login
{
	login() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		info I = new info();
		I.log=true;
		Scanner s = new Scanner(System.in);
		System.out.print("\n\tUserID: ");	I.userID = s.nextLine();
		System.out.print("\tPassword: "); I.password = s.nextLine();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		if(I.getCredentials())
		{
			I.getInfo();
			System.out.println("You are logged in as "+I.first+" "+I.last);
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
		try{new runprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
} 

class signup
{
	signup() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		info I = new info();
		Scanner s = new Scanner(System.in);
		System.out.print("First Name: ");	I.first = s.nextLine();
		System.out.print("Last Name: ");	I.last = s.nextLine();
		System.out.print("Sex(M/F): ");	I.sex = s.nextLine();	
		System.out.print("City: ");	I.city = s.nextLine();
		System.out.print("State: ");	I.state = s.nextLine();
		System.out.print("Blood Group: ");	I.blood_group = s.nextLine();
		System.out.print("Disease: ");	I.disease = s.nextLine();
		System.out.print("Contact: ");	I.contact = s.nextInt();
		System.out.print("Age: ");	I.age = s.nextInt();
		Scanner p = new Scanner(System.in);
		System.out.print("Password: ");	I.password = p.nextLine();
		I.storeInfo();
		I.storeCredentials();
		System.out.println(I.first+" "+I.last+",your user ID is "+I.userID);
		System.out.print("Enter C to continue... ");
		char ch = (char)System.in.read();
		I = null;
		s = null;
		p = null;
		System.gc();
		try{new runprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
}

class appointment
{
	String pID,dID,status;
	Date appointDate;
	Timestamp bookDate;
	private appointment at;

	void appoint() throws IOException, InterruptedException
	{
		FileReader fr = new FileReader("doctorinfo.txt");
		BufferedReader br = new BufferedReader(fr);
		int c=1,i=0;
		Scanner scan = new Scanner(System.in);
		String s;
		br.mark(1000);
		while(c>i | c==0)
		{
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
			System.out.println("From which doctor you want to get appointment?\n");
			s=br.readLine();
			i=0;
			while(s!=null)
			{
				i++;
				System.out.print(i+"->\t");
				System.out.println(s);
				s=br.readLine();	
			}
			System.out.print("\nEnter Serial no. of doctor from whom you want appointment? ");
			c = scan.nextInt();
			br.reset();
		}
		s=br.readLine();
		i=0;
		while(s!=null)
		{
			i++;
			if(i==c)
			{
				String[] record = s.split("\t");
				System.out.println("You want appointment from "+record[1]+" "+record[2]+"?");
				dID=record[0];
				break;
			}
			s=br.readLine();
		}
		br.close();
		fr.close();
		System.out.print("\nEnter the date and time (dd/mm/yyyy-hh:mm-(AMorPM)): ");
		String date = scan.next();
		try
		{
			appointDate = new SimpleDateFormat("dd/MM/yyyy-hh:mm-a").parse(date);
			bookDate = new Timestamp(new Date().getTime());
		}catch(ParseException e)
		{e.printStackTrace();}
		
	}
	void store() throws IOException, InterruptedException
	{
		FileWriter fw = new FileWriter("appointment.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write(pID);
		bw.write("\t"+dID);
		bw.write("\t"+status);
		bw.write("\t"+appointDate);
		bw.write("\t"+bookDate);
		bw.close();
		fw.close();
	}
	boolean check() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		boolean check=true;
		at = new appointment();
		FileReader fr = new FileReader("appointment.txt");
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		while(s!=null)
		{
			String[] record = s.split("\t");
			at.pID = record[0];
			at.dID = record[1];
			at.status = record[2];
			try
			{
				at.appointDate = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy").parse(record[3]); 
				at.bookDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(record[4]).getTime());
			}catch(Exception e)
			{System.out.println(e);}
			if(compareDate(appointDate,at.appointDate)==0 & at.status.equals("YES")) //for date only, status of appointment
			{
				long diff = (appointDate.getTime()-at.appointDate.getTime())/(60*1000)%60;
				if(diff>10 | diff<-10)	//including time, 10 for time duration of appointment
				{
					if(pID.equals(at.pID) & dID.equals(at.dID))
					{
						System.out.println("You already booked with the same doctor at this time");
						check=false;
						break;
					}
					else if(pID.equals(at.pID))
					{
						System.out.println("You already booked an appointment at this time");
						check=false;
						break;
					}
					else if(dID.equals(at.dID))
					{
						System.out.println("Doctor is already booked by someone at this time");
						check=false;
						break;
					}
				}
			}
			s=br.readLine();
		}
		at=null;
		System.gc();
		br.close();
		fr.close();
		return check;
	}
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

class runprogram
{
	void run() throws IOException, InterruptedException
	{
		new appointment().clean();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		System.out.print("\t1. for login\n\t2. for signup\n\t3. for exit\nEnter choice: ");
		Scanner scan = new Scanner(System.in);
		int in = scan.nextInt();
		if(in==1)
			new login();
		else if(in==2)	
			new signup();
		else if(in==3)
			System.out.println("Terminated");
		else
			this.run();
	}
}

class patient
{
	public static void main(String[] args)
	{
		try{new runprogram().run();}
		catch(Exception e)
		{System.out.println(e);}
	}
}