package assignment2;

public interface User {

	public String entryMenu();
	public String getCategory();
	public float getDiscount(float billVal);
	public int rewarder(Restaurant restro, float billAmt);
	public void chargeCollected();
	public void displayDeets();	
	
}
