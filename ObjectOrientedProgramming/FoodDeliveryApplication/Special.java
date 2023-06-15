package assignment2;

public final class Special extends Customer {

	public Special(String name, String address) {
		super(name, address);
	}
	
	@Override
	public String getCategory() {
		return "Special Customer";
	}
	
	@Override
	public float getDiscount(float billVal) {
		if(billVal > 200.00)
			return billVal - 25;
		else
			return billVal;
	}
	
	@Override
	public int getDeliveryCharge() {
		return 20;
	}
}
