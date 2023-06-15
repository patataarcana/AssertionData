import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Assignment1_2019228 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner read = new Scanner(System.in);
		System.out.printf("Welcome!\nEnter number of patients to be onboarded to Camp:\n");
		int n = read.nextInt();
		read.nextLine();
		System.out.println("Enter Patient details line-by-line:");
		String patients[] = new String[n];
		for(int i = 0; i<n; i++) {
			patients[i] = read.nextLine();
		}
		Camp campOne = new Camp(n);
		campOne.getHealthCoord().onboardPatients(patients);
		int q;
		ArrayList<HealthCareInstitute> instituteList = new ArrayList<HealthCareInstitute>();
		int numInstitutes = 0;
		while(!campOne.isEmpty()) {
			System.out.println("Enter Query Number:");
			q = read.nextInt();
			if(q == 1) {
				System.out.println("Enter Name of Healthcare Institute");
				read.nextLine();
				String nam = read.nextLine(); 
				System.out.println("Enter Temperature Criteria");
				Float temp = read.nextFloat();
				System.out.println("Enter Oxygen Level Criteria");
				int o2lvl = read.nextInt();
				System.out.println("Enter Number of Available Beds");
				int beds =  read.nextInt();
				instituteList.add(new HealthCareInstitute(nam, o2lvl, temp, beds));
				campOne.getHealthCoord().addInstitute(instituteList.get(numInstitutes));
				ArrayList<Integer> prompts = campOne.getHealthCoord().admitPatients(instituteList.get(numInstitutes));
				if(prompts.size() != 0) {
					HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); 
					for(int i : prompts) {
						System.out.printf("Enter recommended Recovery Days for admitted Patient ID %d:\n", i);
						map.put(i,read.nextInt());
					}
					campOne.getHealthCoord().updater(map, instituteList.get(numInstitutes));
				}
				numInstitutes++;
			}
			else if(q == 2) {
				campOne.getHealthCoord().removeAdmitted();
			}
			else if(q == 3) {
				campOne.getHealthCoord().removeInstitute();
			}
			else if(q == 4) {
				System.out.printf("%d Patients are waiting in the Camp currently\n", campOne.getHealthCoord().waitingPatients());
			}
			else if(q == 5) {				
				System.out.printf("%d Healthcare Institutes are admitting Patients currently\n", campOne.getHealthCoord().countOpen());
			}
			else if(q == 6) {
				read.nextLine();
				System.out.println("Enter Name of Healthcare Institute:");
				campOne.getHealthCoord().displayInstituteDeets(read.nextLine());
			}
			else if(q == 7) {
				System.out.println("Enter Patient ID:");
				campOne.getHealthCoord().displayPatientDeets(read.nextInt());
			}
			else if(q == 8)	{
				campOne.getHealthCoord().displayPatientsInCamp();
			}
			else if(q == 9) {
				read.nextLine();
				System.out.println("Enter Name of Health Care Institute:");
				campOne.getHealthCoord().displayPofI(read.nextLine());
			}
			else {
				System.out.println("Enter Valid Query");
			}
		}
		System.out.println("All Patients Admitted! Yay!");
	}

}

class HealthCoordinator {
	 
	private Camp campAssigned;
	
	HealthCoordinator(Camp c) {
		campAssigned = c;
	}
	
	public void onboardPatients(String patientDeets[]) {
		campAssigned.setPatientsInCamp(patientDeets);
	}
	
	public void addInstitute(HealthCareInstitute h) {
		System.out.println(campAssigned.setHealthInstitutes(h));
	}
	
	public void displayPatientsInCamp() {
		System.out.print(campAssigned.getPIDAndName());
	}
	
	public void displayAllPatients() {
		System.out.println(campAssigned.printPatientsInCamp());
	}
	
	public ArrayList<Integer> admitPatients(HealthCareInstitute h) {
		return h.admission(campAssigned.getPatientsInCamp());
	}
	
	public void updater(HashMap<Integer, Integer> map, HealthCareInstitute h) {
		campAssigned.putRecoverydates(map);
		h.putRecoverydates(map);
	}
	
	public void removeAdmitted() {
		campAssigned.removeAdmittedPatients();
	}
	
	public void removeInstitute() {
		campAssigned.removeClosedInstitutes();
	}
	
	public int countOpen() {
		return campAssigned.countOpenInstitutes();
	}
	
	public int waitingPatients() {
		return campAssigned.countWaitingPatients();
	}
	
	public void displayInstituteDeets(String s) {
		System.out.println(campAssigned.displayInstitute(s));
	}
	
	public void displayPatientDeets(int pid) {
		System.out.println(campAssigned.displayPatient(pid));
	}
	
	public void displayPofI(String h) {
		System.out.println(campAssigned.allPatientsOfI(h));
	}
}

class Camp {
	
	private int numPatients;
	private ArrayList<Patient> patientsInCamp;
	private ArrayList<HealthCareInstitute> healthInstitutes;
	private HealthCoordinator healthCoord;
	int cntr;
	
	Camp(int n) {
		numPatients = n;
		patientsInCamp = new ArrayList<Patient>();
		healthInstitutes = new ArrayList<HealthCareInstitute>();
		cntr = 0;
		healthCoord = new HealthCoordinator(this);
	}
		
	public HealthCoordinator getHealthCoord() {
		return healthCoord;
	}
	
	public ArrayList<Patient> getPatientsInCamp() {
		return patientsInCamp; 
	}
	
	public void setPatientsInCamp(String patientsInfo[]) {
		for(int i = 0; i<numPatients; i++) {
			patientsInCamp.add(new Patient(patientsInfo[i].split(" ")));
		}
	}
	
	public String setHealthInstitutes(HealthCareInstitute h) {
		healthInstitutes.add(h);
		cntr++;
		return h.initialInstituteStringer();
	}
		
	public String printPatientsInCamp() {
		String toRet = "";
		for(Patient p : patientsInCamp) {
			toRet += p.toString();
		}
		return toRet;
	}
	
	public String getPIDAndName() {
		String toRet = "";
		for(Patient p : patientsInCamp) {
			toRet += String.format("%d %s\n", p.getPID(), p.getName());
		}
		return toRet;
	}
	
	public boolean isEmpty() {
		for(Patient p : patientsInCamp) {
			if(p.getAdmissionStatus().contentEquals("WAITING"))
				return false;
		}
		return true;
	}
	
	public void putRecoverydates(HashMap<Integer, Integer> map) {
		for(Map.Entry m : map.entrySet()) {
			for(Patient p : patientsInCamp) {
				if(p.getPID() == (int)(m.getKey())) {
					p.setRecoveryDays((int)(m.getValue()));
				}
			}
		}
	}
	
	public void removeAdmittedPatients() {
		ArrayList<Patient> y = new ArrayList<Patient>();
		for(Patient p : patientsInCamp) {
			if(p.getAdmissionStatus().contentEquals("ADMITTED")) {
				y.add(p);
				System.out.printf("Removing PID %d from Camp\n", p.getPID());
			}
		}
		for(int i = 0; i<y.size(); i++) {
			patientsInCamp.remove(y.get(i));
		}
		numPatients -= y.size();
	}
	
	public void removeClosedInstitutes() {
		ArrayList<HealthCareInstitute> y = new ArrayList<HealthCareInstitute>();
		for(HealthCareInstitute h : healthInstitutes) {
			if(h.getStatus().contentEquals("CLOSED")) {
				y.add(h);
				System.out.printf("Removing %s since it is CLOSED\n", h.getName());
			}
		}
		for(int i = 0; i<y.size(); i++) {
			healthInstitutes.remove(y.get(i));
		}
	}
	
	public int countOpenInstitutes() {
		int count = 0;
		for(HealthCareInstitute h : healthInstitutes) {
			if(h.getStatus().contentEquals("OPEN")) {
				count += 1;
			}
		}
		return count;
	}
	
	public int countWaitingPatients() {
		int count = 0;
		for(Patient p : patientsInCamp) {
			if(!p.getAdmissionStatus().contentEquals("ADMITTED")) {
				count += 1;
			}
		}
		return count;
	}
	
	public String displayInstitute(String str) {
		for(HealthCareInstitute h : healthInstitutes) {
			if(h.getName().contentEquals(str)) {
				return h.initialInstituteStringer();
			}
		}
		return "ERROR: Above Healthcare Institute was not found";
	}
	
	public String displayPatient(int id) {
		for(Patient p : patientsInCamp) {
			if(p.getPID() == id) {
				return p.toString();
			}
		}
		return "Patient not found";
	}
	
	public String allPatientsOfI(String str) {
		String res = "";
		for(HealthCareInstitute h : healthInstitutes) {
			if(h.getName().contentEquals(str)) {
				res = h.displayAllPatients();
				break;
			}
		}
		return res;
	}
}

class HealthCareInstitute {
	
	private String name;
	private int minOxy;
	private float maxBodyTemp;
	private int availableBeds;
	private String status;
	private ArrayList<Patient> patientsAdmitted;
	int cntr;
	
	HealthCareInstitute(String n, int o2, float temp, int b) {
		name = n;
		minOxy = o2;
		maxBodyTemp = temp;
		availableBeds = b;
		if(availableBeds!=0)
			status = "OPEN";
		else
			status = "CLOSED";
		patientsAdmitted = new ArrayList<Patient>();
		cntr = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMinOxy() {
		return minOxy;
	}
	
	public float getMaxBodyTemp() {
		return maxBodyTemp;
	}
	
	public int getAvailableBeds() {
		return availableBeds;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String str) {
		status = str;
	}
	
	public String initialInstituteStringer() {
		return String.format("Details for the Healthcare Institue are:\nName: %s\nTemperature should be <= %.1f\nOxygen Levels should be >= %d\nThere are %d available beds currently\nAdmission Status %s\n", name, maxBodyTemp, minOxy, availableBeds, status);
	}
	
	public ArrayList<Integer> admission(ArrayList<Patient> waitingList) {
		ArrayList<Integer> askRDFor = new ArrayList<Integer>();
		if(status.contentEquals("CLOSED")) {
			System.out.printf("%s is CLOSED currently\n", name);
		}
		else {
			for(Patient p: waitingList) {
				if(p.getAdmissionStatus().contentEquals("WAITING") && (p.getOxyLevel() >= minOxy && availableBeds>0)) {
					p.setAdmissionStatus("ADMITTED");
					p.setInstituteAdmitted(this);
					patientsAdmitted.add(new Patient(p));
					askRDFor.add(p.getPID());
					availableBeds -= 1;
					cntr++;					
				}
			}
			if(availableBeds>0) {
				for(Patient p: waitingList) {
					if(availableBeds>0 && (p.getAdmissionStatus().contentEquals("WAITING") && p.getBodyTemp() <= maxBodyTemp)) {
						p.setAdmissionStatus("ADMITTED");
						p.setInstituteAdmitted(this);
						patientsAdmitted.add(new Patient(p));
						askRDFor.add(p.getPID());
						availableBeds -= 1;
						cntr++;	
					}
				}
			}
			if(availableBeds == 0) {
				status = "CLOSED";
			}
		}
		return askRDFor;
	}
	
	public void putRecoverydates(HashMap<Integer, Integer> map) {
		for(Map.Entry m : map.entrySet()) {
			for(Patient p : patientsAdmitted) {
				if(p.getPID() == (int) m.getKey()) {
					p.setRecoveryDays((int) m.getValue());
				}
			}
		}
	}
	
	public String displayAllPatients() {
		String res = "";
		for(Patient p : patientsAdmitted) {
			res += String.format("%s, recovery time is %d days\n", p.getName(), p.getRecoveryDays());
		}
		return res;
	}
}

class Patient {
	
	private String name;
	private int age;
	private int oxyLevel;
	private float bodyTemp;
	private final int pID;
	private int recoveryDays;
	private String admissionStatus;
	private HealthCareInstitute instituteAdmitted;
	
	private static int curID = 0;
	
	Patient(String tokens[]) {
		name = tokens[0];
		age = Integer.valueOf(tokens[3]);
		oxyLevel = Integer.valueOf(tokens[2]);
		bodyTemp = Float.valueOf(tokens[1]);
		admissionStatus = "WAITING";
		pID = ++curID;
	}
	
	Patient(Patient p) {
		name = p.name;
		age = p.age;
		oxyLevel = p.oxyLevel;
		bodyTemp = p.bodyTemp;
		admissionStatus = p.admissionStatus;
		pID = p.pID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOxyLevel() {
		return oxyLevel;
	}
	
	public float getBodyTemp() {
		return bodyTemp;
	}
	
	public int getPID() {
		return pID;
	}
	
	public void setRecoveryDays(int rd) {
		recoveryDays = rd;
	}
	
	public int getRecoveryDays() {
		return recoveryDays;
	}
	
	public String getAdmissionStatus() {
		return admissionStatus;
	}
	
	public void setAdmissionStatus(String stats) {
		admissionStatus = stats;
	}
	
	public HealthCareInstitute getInstituteAdmitted() {
		return instituteAdmitted;
	}
	
	public void setInstituteAdmitted(HealthCareInstitute h) {
		instituteAdmitted = h;
	}
	
	public String toString() {
		if(admissionStatus.contentEquals("WAITING"))
			return String.format("Name %s\nPID %d\nBody Temperature %.1f\nOxygen Level %d\nAdmission Status %s\n", name, pID, bodyTemp, oxyLevel, admissionStatus);
		else
			return String.format("Name %s\nPID %d\nBody Temperature %.1f\nOxygen Level %d\nAdmission Status %s\nAdmitted In %s\n", name, pID, bodyTemp, oxyLevel, admissionStatus, instituteAdmitted.getName());
	}
}