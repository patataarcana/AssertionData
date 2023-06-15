package assignment2;

public final class Regular extends Restaurant {
	
	public Regular(String name, String owner, String address) {
		super(name, owner, address);
	}	
	
	@Override
	public float getDiscount(float billVal) {
		System.out.println("Overall Bill Value Discount not applicable");
		return billVal;
	}
}
