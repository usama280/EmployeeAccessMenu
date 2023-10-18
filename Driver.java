import io.github.pixee.security.BoundedLineReader;
import java.io.*;
import java.util.*;

//	\ComponentCodes.txt			\Employees.dat			\JobCodes.txt			\WorkOrders.txt
// Valid ID (Low to High): 67890, 34567, 12345 
public class Driver {
	private static int X = 1; 
	private static LinkedList<WorkOrder> link = new LinkedList<WorkOrder>();
	private static HashMap<String, String> compMap = new HashMap<String, String>();
	private static HashMap<String, String> jobMap = new HashMap<String, String>();
	private static Scanner sc = new Scanner(System.in);
	//for your ease, just input your paths
	private static String compPath = ""; //ADD YOU PATH
	private static String jobPath = ""; //ADD YOU PATH
	private static String workPath = ""; //ADD YOU PATH
	private static String employPath = ""; //ADD YOU PATH
	
	public static void main(String args[]) {
		//declaring and/or initializing objs
		ArrayList<Employee> employList = new ArrayList<Employee>();
		ObjectInputStream ois;
		Employee emp;
		Employee user = null;
		int i = 1;
		String id; 
		String secLvl = "";
		
		try {
			ois = new ObjectInputStream(new FileInputStream(employPath));
			emp = (Employee)ois.readObject();
			
			//reads employee file into list
			while(emp!=null) {
				employList.add(emp);
				emp = (Employee)ois.readObject();
			}
			ois.close();
			
			
			while(i<=3) {
				boolean valid = false;
				System.out.print("Enter your ID: "); //ask user for existing id
				id = sc.nextLine();
				
				//checks if ID exists
				for(int j=0; j<employList.size(); j++) {
					if(employList.get(j).getID().equals(id)) { //if ID exists breaks out of current loop
						valid = true; 
						user = employList.get(j);
						break;
					}
				}
				
				if(valid) { //if ID exists ends while loop
					break;
				}
				
				if(i == 3) { //if incorrect input 3 times, ends program
					System.out.println("Security Warning... Program will now end\n");
					throw new Exception();
				}
				
				i++;
			}
			
			BufferedReader br1 = new BufferedReader(new FileReader(compPath));
			String line;
			//adds current comp codes and disc onto hashmap
			while ((line = BoundedLineReader.readLine(br1, 5_000_000)) != null)
		    {
		        String[] parts = line.split(",", 2);//splits
		        if (parts.length >= 2) 
		        {
		        	compMap.put(parts[0], parts[1]);//adds values respectfully
		        } 
		    }
			br1.close();

			BufferedReader br2 = new BufferedReader(new FileReader(jobPath));
			String line2;
			//adds current job codes and disc onto hashmap
			while ((line2 = BoundedLineReader.readLine(br2, 5_000_000)) != null)
		    {
		        String[] parts = line2.split(",", 2);//splits
		        if (parts.length >= 2) 
		        {
		        	jobMap.put(parts[0], parts[1]);//adds values respectfully
		        } 
		    }
			br2.close();
			
			BufferedReader br3 = new BufferedReader(new FileReader(workPath));
			String line3;
			//adds current workorders onto list
			while ((line3 = BoundedLineReader.readLine(br3, 5_000_000)) != null)
		    {
		        String[] parts = line3.split(",", 3); //splits vals
		        if (parts.length >= 3) 
		        {
		        	link.add(new WorkOrder(parts[0], parts[1], parts[2])); //adds to list
		        } 
		    }
			br3.close();
			
			secLvl = user.getSecurityLevel(); //users security lvl
			int secNum = secLvlMenu(secLvl); //highest menu user can access based on security lvl
			
			while(true) {
				System.out.print("Option: ");
				int input = Integer.parseInt(sc.nextLine());
	
				if(input >= 1 && input <= secNum) {
					methodCaller(input, user); //this method calls the correct method respectfully
					continue; //begins loop again
				}else if(input == 9) {
					methodCaller(input, user); //updates files accordingly
					break; //ends loop
				}
				
				System.out.println("Please try again");
			}
			
			System.out.println("Program has ended"); //helpful indication
		}catch(Exception e) {
			System.out.println("Error");
		}
	}
	
	
	//prints out the menu available to user based on security level, and returns highest value, with the exception of 9
	private static int secLvlMenu(String secLvl) {
		
		if(secLvl.equals("Edit")) {
			System.out.println("1.Create work order report\r\n"
					+ "2.View specific component code\r\n"
					+ "3.View specific job code\r\n"
					+ "4.Create new work order\r\n"
					+ "5.Add component code\r\n"
					+ "6.Delete component code\r\n"
					+ "7.Update component code\r\n"
					+ "9.Exit");
			return 7;
			
		}else if(secLvl.equals("Partial edit")) {
			System.out.println("1.Create work order report\r\n"
					+ "2.View specific component code\r\n"
					+ "3.View specific job code\r\n"
					+ "4.Create new work order\r\n"
					+ "9.Exit");
			return 4;
			
		}else if(secLvl.equals("View only")) {
			System.out.println("1.Create work order report\r\n"
					+ "2.View specific component code\r\n"
					+ "3.View specific job code\r\n"
					+ "9.Exit");
			return 3;
		}
		return 0;
	}
	
	//This takes user input and calls methods respectfully
	private static void methodCaller(int input, Employee u) throws Exception {
		switch(input) {
		case 1:
			createWorkReport(u);
			break;
		case 2:
			viewComp();
			break;
		case 3:
			viewJob();
			break;
		case 4:
			createNewWork();
			break;
		case 5:
			addComp();
			break;
		case 6:
			deleteComp();
			break;
		case 7:
			updateComp();
			break;
		case 9:
			exit(u);
			break;
		}
	}
	
	//Creates a report of current workorders and associated codes. Along with all comp and job codes with disc.
	private static void createWorkReport(Employee u)throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "WorkOrder" + X + ".txt"));
		X++; //Next time report created, its a new one
		
		Calendar cal = Calendar.getInstance(Locale.US);
	    int month = cal.get(Calendar.MONTH);
	    int quarter = (month / 3) + 1; //Current quarter
	    
	    //Adds following user info to report
		bw.write("User Name: " + u.getName());
		bw.newLine();
		bw.write("User Department: " + u.getDepartment());
		bw.newLine();
		bw.write("User Security Level: " + u.getSecurityLevel());
		bw.newLine();
		bw.write("Report Created in Quarter: " + quarter);
		bw.newLine();
		bw.newLine();
		
		
		//Holds temp job and comp code values within WorkOrders
		ArrayList<String> tempJob = new ArrayList<String>();
		ArrayList<String> tempComp = new ArrayList<String>();
		
		//loops through and writes current work orders
		bw.write("Current Work Orders:");
		bw.newLine();
		for(int i=0; i<link.size(); i++) {
			if(!tempJob.contains(link.get(i).getJobCode())){ //must not already contain in list
				tempJob.add(link.get(i).getJobCode());
			}
			if(!tempComp.contains(link.get(i).getCompCode())){ //must not already contain in list
				tempComp.add(link.get(i).getCompCode());
			}
			bw.write(link.get(i).toString());
			bw.newLine();
		}
		bw.newLine();
		
		//writes out all unique comp codes and descriptions accordingly
		bw.write("All associated component codes and descriptions: ");
		bw.newLine();
		for (String name: tempComp){
            String key = name;
            String value = (String)compMap.get(key);  
            bw.write(key + "\t" + value);  
            bw.newLine();
		} 
		bw.newLine();
		
		//writes out all unique job codes and descriptions accordingly
		bw.write("All associated job codes and descriptions: ");
		bw.newLine();
		for (String name: tempJob){
            String key = name;
            String value = (String)jobMap.get(name);  
            bw.write(key + "\t" + value);  
            bw.newLine();
		}
		System.out.println("Created successfully\n");
		bw.close();
	}
	
	//Displays existing component code's disc
	private static void viewComp() {
		System.out.print("Enter existing component code: ");
		String cc = sc.nextLine();
		
		if(compMap.containsKey(cc)) {//if it exists
			System.out.println("Component code: " + cc + "\t Description: " + compMap.get(cc) + "\n");
		}else {
			System.out.println("This component code does not exist"+ "\n");
		}
	}
	
	//Displays existing job code's disc
	private static void viewJob() {
		System.out.print("Enter existing job code: ");
		String jc = sc.nextLine();
		
		if(jobMap.containsKey(jc)) {//if it exists
			System.out.println("Job code: " + jc + "\t Description: " + jobMap.get(jc)+ "\n");
		}else {
			System.out.println("This job code does not exist\n");
		}
	}
	
	//creates a new work order with existing component and job code
	private static void createNewWork() {
		System.out.print("Enter a new work order number: ");
		String won = sc.nextLine();
		boolean valid = false;			//Existing work order: 31341
		
		for(int i=0; i<link.size(); i++) {//if workorder already within list
			if(link.get(i).getWorkNum().equals(won)) {
				valid = true;
			}
		}
		
		if(!valid) { 
			System.out.print("Enter an existing component code: ");		//Existing comp code: 8761
			String cc = sc.nextLine();
			System.out.print("Enter an existing job code: ");		//Existing job code: 599
			String jc = sc.nextLine();
			
			if(jobMap.containsKey(jc) && compMap.containsKey(cc)) {//if job/comp codes exist
				link.add(new WorkOrder(won, jc, cc));
				System.out.println("Created successfully\n");
			}else {
				System.out.println("Job code or Component code does not exist\n");
			}
		}else {
			System.out.println("Work order already exists\n");
		}
	}
	
	//Creates a new component code along with a disc
	private static void addComp() {
		System.out.print("Enter a new component code: ");
		String cc = sc.nextLine();
		System.out.print("Enter a discription: ");
		String disc = sc.nextLine();
		
		if(!compMap.containsKey(cc)) {//comp code must not exist
			compMap.put(cc, disc);
			System.out.println("Added successfully\n");
		}else {
			System.out.println("Component code already exists\n");
		}
	}
	
	//Deletes an existing component code
	private static void deleteComp() {
		System.out.print("Enter existing component code: ");
		String cc = sc.nextLine();
		
		if(compMap.containsKey(cc)) {//if comp code exists
			compMap.remove(cc);
			System.out.println("Removed successfully\n");
		}else {
			System.out.println("Component code does not exist\n");
		}
	}
	
	//Updates the component code disc
	private static void updateComp() {
		System.out.print("Enter existing component code: ");
		String cc = sc.nextLine();
		System.out.print("Enter new discription: ");
		String disc = sc.nextLine();
		
		if(compMap.containsKey(cc)) {//if comp code exists
			compMap.replace(cc, disc);
			System.out.println("Updated successfully\n");
		}else {
			System.out.println("Component code does not exist\n");
		}
	}
	
	//Updates input files depending on user security lvl
	private static void exit(Employee u) throws IOException {
		
		if(u.getSecurityLevel().equals("Edit")) {
			//overwrite the comp input file
			BufferedWriter br1 = new BufferedWriter(new FileWriter(compPath));
			for (String name: compMap.keySet()){
	            String key = name.toString();
	            String value = compMap.get(name).toString();  
	            br1.write(key + "," + value);  
	            br1.newLine();
			}
			br1.close();
			
			//overwrite the job input file
			BufferedWriter br2 = new BufferedWriter(new FileWriter(jobPath));
			for (String name: jobMap.keySet()){
	            String key = name.toString();
	            String value = jobMap.get(name).toString();  
	            br2.write(key + "," + value);  
	            br2.newLine();
			}
			br2.close();
			
			//overwrite the work order input file
			BufferedWriter br3 = new BufferedWriter(new FileWriter(workPath));
			for (WorkOrder val: link){
				String work = val.getWorkNum();
	            String job = val.getJobCode();
	            String comp = val.getCompCode();
	            br3.write(work + "," + job + "," + comp);  
	            br3.newLine();
			}
			br3.close();
			
		}else if(u.getSecurityLevel().equals("Partial edit")) {
			//overwrite the work order input file
			BufferedWriter br3 = new BufferedWriter(new FileWriter(workPath));
			for (WorkOrder val: link){
				String work = val.getWorkNum();
	            String job = val.getJobCode();
	            String comp = val.getCompCode();
	            br3.write(work + "," + job + "," + comp);  
	            br3.newLine();
			}
			br3.close();
		}
		
	}
}
