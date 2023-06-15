package assignment2;

import java.util.Map;
import java.util.ArrayList;

public class Customer implements User {
	
	private String name;
	private String address;
	private float walletBal;
	private int rewardAC;
	private static int allDelivery = 0;
	private ArrayList<Cart> prevOrders;	
	private Cart myCart;
	
	public Customer(String name, String address){
		this.name = name;
		this.address = address;
		this.walletBal = 1000;
		this.rewardAC = 0;
		this.prevOrders = new ArrayList<Cart>();
	}
	
	public String getName() {
		return name;
	}
	
	public float getWalletBal() {
		return walletBal;
	}
	
	public void setWalletBal(float val) {
		walletBal = val;
	}
	
	public int fetchRewardAC() {
		//getter
		return rewardAC;
	}
	
	public void setRewardAC(int pts) {
		rewardAC += pts;
	}
	
	public Cart getCart() {
		return myCart;
	}
	
	public void setCart(Cart newCart) {
		myCart = newCart;
	}
	
	public void setAllDelivery(int chargeToAdd) {
		allDelivery += chargeToAdd;
	}
	
	public int getAllDelivery() {
		return allDelivery;
	}
	
	public int getDeliveryCharge() {
		return 40;
	}
	
	public String getPrevOrders(){	
		String res = "";
		int cnt = 0;
		for(Cart o: prevOrders) {
			for(Map.Entry m : o.getFoodMap().entrySet()){ 
				Food f = (Food) m.getValue();
				if(cnt<10) {
					res += String.format("Bought Item: %s, quantity: %d for Rs %.2f from Restaurant %s and DeliveryCharge %d\n", f.getName(), f.getQuantity(), f.getPrice(), f.getRestro().getName(), o.getDelivery());
					cnt++;
				}
			} 
		}
		return res;
	}
	
	public void setPrevOrders(Cart myOrder) {
		prevOrders.add(myOrder);
	}
	
	@Override
	public String entryMenu() {
		return String.format("Welcome %s\nCustomer Menu\n\t1) Select Restaurant\n\t2) Checkout Cart\n\t3) Reward Won\n\t4) Print Recent Orders\n\t5) Exit\n", name);
	}
	
	@Override
	public String getCategory() {
		return "Regular Customer";
	}
	
	@Override
	public float getDiscount(float billVal) {
		return billVal;
	}
	
	@Override
	public int rewarder(Restaurant restro, float billAmt) {
		this.setRewardAC(restro.rewarder(restro, billAmt));
		return 1;
	}
	
	@Override
	public void chargeCollected() {
		System.out.printf("Zotato collected Rs %d /- as Delivery Charges from customers\n", allDelivery);
	}
	
	
	@Override
	public void displayDeets() {
		System.out.printf("%s (%s) from %s has %.2f /- in Wallet\n", name, this.getCategory(), address, walletBal);
	}
		
}
