package assignment2;

public final class Elite extends Customer {

	public Elite(String name, String address) {
		super(name, address);
	}
	
	@Override
	public String getCategory() {
		return "Elite Customer";
	}
	
	@Override
	public float getDiscount(float billVal) {
		if(billVal > 200.00)
			return billVal - 50;
		else
			return billVal;
	}
	
	@Override
	public int getDeliveryCharge() {
		return 0;
	}
}
