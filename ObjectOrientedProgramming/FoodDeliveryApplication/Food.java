package assignment2;

public final class Food {
	
	private final int FID;
	private String name;
	private float price;
	private int qty;
	private float discount;		
	private String category;
	private Restaurant restro;
	private static int curID = 0;
	
	public Food(String itemName, float itemPrice, int quantity, String catgry, float offer) {
		FID = ++curID;
		name = itemName;
		price = itemPrice;
		qty = quantity;
		category = catgry;
		discount = offer;
	}
	
	public Food(Food f) {
		this.FID = f.FID;
		this.name = f.name;
		this.price = f.price;
		this.qty = f.qty;
		this.category = f.category;
		this.discount = f.discount;
		this.restro = f.restro;
	}
	
	public int getFID() {
		return FID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String fname) {
		name = fname;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float fprice) {
		price = fprice;
	}
	
	public int getQuantity() {
		return qty;
	}
	
	public void setQuantity(int newQty) {
		qty = newQty;
	}
	
	public float getDiscount() {
		return discount;
	}
	
	public void setDiscount(float foff) {
		discount = foff;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String fcat) {
		category = fcat;
	}
	
	public Restaurant getRestro() {
		return restro;
	}
	
	public void setRestro(Restaurant eatery) {
		restro = eatery;
	}
	
	
	public String toString() {
			return String.format("%d %s %.2f %d %.0f%% off %s\n", FID, name, price, qty, discount, category);
	}
	
}
