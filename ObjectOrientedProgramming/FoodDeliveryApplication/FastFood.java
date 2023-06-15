package assignment2;

public final class FastFood extends Restaurant {

	public FastFood(String name, String owner, String address) {
		super(name, owner, address);
	}
	
	@Override
	public String getCategory() {
		return "Fast Food Restaurant";
	}
	
	@Override
	public float getDiscount(float billVal) {
		if(byPercent == 100) {
			return billVal;
		}
		return billVal * (1 - (float)byPercent/100);
	}
	
	@Override
	public int getRewardPts(float billVal) {
		return (int)billVal/150 * 10;
	}
}
