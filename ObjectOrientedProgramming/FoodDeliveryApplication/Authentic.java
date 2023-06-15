package assignment2;

public final class Authentic extends Restaurant {
	
	public Authentic(String name, String owner, String address) {
		super(name, owner, address);
	}
	
	@Override
	public String getCategory() {
		return "Authentic Restaurant";
	}
	
	@Override
	public float getDiscount(float billVal) {
		if(byPercent == 100) {
			if(billVal > 100) 
				return billVal - 50;
			else 
				return billVal;
		}
		billVal -= billVal*((float)byPercent/100);
		if(billVal > 100) 
			return billVal - 50;
		else 
			return billVal;
	}
	
	@Override
	public int getRewardPts(float billVal) {
		return (int)billVal/200 * 25;
	}
	
}
