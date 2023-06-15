package assignment2;

import java.util.HashMap;
import java.util.Map;

public class Restaurant implements User {

	private String name;
	private String owner;
	private String address;
	private int rewardPts;
	private int numOrders;
	protected int byPercent;
	private static float totalBills = 0;
	private HashMap<Integer, Food> foodItems;
	static int curID = 0;
	
	public Restaurant(String name, String owner, String address) {
		this.name = name;
		this.owner = owner;
		this.address = address;
		this.rewardPts = 0;
		this.numOrders = 0;
		this.byPercent = 100;
		this.foodItems = new HashMap<Integer, Food>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getOwner() {
		return owner;
	} 
	
	public String getAddress() {
		return address;
	}
	
	public void setRewardPts(int reward) {
		rewardPts += reward;
	}
	
	public int getRewardPts() {
		//getter
		return rewardPts;
	}
	
	public int getRewardPts(float billVal) {
		return (int)billVal/100 * 5;
	}
	
	public void setNumOrders(int ordersTaken) {
		numOrders = ordersTaken;
	}
	
	public int getNumOrders() {
		return numOrders;
	}
	
	public void setByPercent(int per) {
		byPercent = per;
	}
	
	public void setTotalBills(float valToAdd) {
		totalBills += 0.01*valToAdd;
	}
	
	public float getTotalBills() {
		return totalBills;
	}
	
	public Food availFood(int ID) {
		return foodItems.get(ID);
	}
		
	protected String addFood(String fname, float fprice, int fqty, String fcat, float foff) {
		Food f = new Food(fname, fprice, fqty, fcat, foff);
		f.setRestro(this);
		foodItems.put(f.getFID(), f);
		return f.toString(); 
	}
	
	protected HashMap<Integer, Food> getFoodItems() {
		return foodItems;
	}
	
	public void printFoodItems() {
		for(Map.Entry m : foodItems.entrySet()){    
			Food f = (Food) m.getValue();
			System.out.printf("%d %s - %s", f.getFID(), owner, f.toString());   
		}  
	}
	
	@Override
	public String entryMenu() {
		return String.format("Welcome %s\n\t1) Add Item\n\t2) Edit Item\n\t3) Print Rewards\n\t4) Discount on Bill Value\n\t5) Exit\n", owner);
	}
	
	@Override
	public String getCategory() {
		return "Regular Restaurant";
	}
		
	@Override
	public float getDiscount(float billVal) {
		return billVal;
	}
	
	@Override
	public int rewarder(Restaurant restro, float billAmt) {
		int retVal = restro.getRewardPts(billAmt);
		this.setRewardPts(retVal);
		return retVal;
	}
	
	@Override
	public void chargeCollected() {
		System.out.printf("Zotato collected Rs %.2f /- as Transaction Fee from restaurants\n", totalBills);
	}
	
	@Override
	public void displayDeets() {
		System.out.printf("%s (%s) in %s took %d orders\n", name, this.getCategory(), address, numOrders);
	}
	
}
