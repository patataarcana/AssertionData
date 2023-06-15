package assignment2;

import java.util.HashMap;
import java.util.Map;

public class Cart {

	private HashMap<Integer, Food> foodMap;
	private float billVal;
	private User fromRestro;
	private User who;
	private int deliveryCharge;
	
	Cart(User c){
		foodMap = new HashMap<Integer, Food>();
		billVal = 0;
		who = c;
	}
	
	public float getBillVal() {
		return billVal;
	}
	
	public HashMap<Integer, Food> getFoodMap(){
		return foodMap;
	}
	
	public void setBillVal(float billAmt) {
		billVal = billAmt;
	}
		
	public int getDelivery() {
		return deliveryCharge;
	}
	
	public void addIt(Food f, int qty) {
		fromRestro = f.getRestro();
		f.setQuantity(qty);		
		foodMap.put(f.getFID(), f);
	}

	public void delIt(int fid) {
		//handle null case
		foodMap.remove(fid);
	}
	
	public void printCart() {
		for(Map.Entry m : foodMap.entrySet()){    
		    System.out.print(m.getKey()+" "+((Food) m.getValue()).getRestro().getOwner()+" - "+m.getValue());    
		}  
	}
	
	public float calcBalance() {
		billVal = 0;
		for(Map.Entry m : foodMap.entrySet()){    
		    billVal += ((Food) m.getValue()).getPrice()*((Food) m.getValue()).getQuantity()*(1 - (float)((Food) m.getValue()).getDiscount()/100);   
		}  
		billVal = fromRestro.getDiscount(billVal);
		billVal = who.getDiscount(billVal);
		billVal += ((Customer) who).getDeliveryCharge();
		return billVal;	
	}
	
	public void orderIt() {
		if(foodMap.isEmpty()) {
			System.out.println("Order unsuccessful");
			return;
		}
		int numItems = 0;
		for(Map.Entry m : foodMap.entrySet()){    
		    numItems += ((Food) m.getValue()).getQuantity(); 
		    Food fEdit = ((Restaurant) fromRestro).availFood(((Food) m.getValue()).getFID());
		    fEdit.setQuantity(fEdit.getQuantity() - ((Food) m.getValue()).getQuantity());
		}  
		System.out.printf("%d items successfully bought for INR %.2f /-\n", numItems, billVal);
		((Restaurant) fromRestro).setNumOrders(((Restaurant) fromRestro).getNumOrders() + 1);
		float bill = billVal;
		int thisReward = 0;
		if(((Customer) who).fetchRewardAC()>0) {
			if(bill <= ((Customer) who).fetchRewardAC()) {
				((Customer) who).setRewardAC(((int)-bill));
				bill = 0;
			}
			else {
				bill -= ((Customer) who).fetchRewardAC();
				((Customer) who).setRewardAC(-((Customer) who).fetchRewardAC());
				((Customer) who).setWalletBal(((Customer) who).getWalletBal() - bill);
			}
		}
		else {
			((Customer) who).setWalletBal(((Customer) who).getWalletBal() - bill);
		}
		float excluding = billVal - ((Customer) who).getDeliveryCharge();
		((Restaurant) fromRestro).setTotalBills(excluding);
		fromRestro.rewarder((Restaurant)fromRestro, excluding);
		who.rewarder((Restaurant)fromRestro, excluding);
		this.deliveryCharge = ((Customer) who).getDeliveryCharge();
		((Customer) who).setAllDelivery(deliveryCharge);
		((Customer) who).setPrevOrders(this);
		for(Map.Entry m : foodMap.entrySet()){    
		    Food fEdit = ((Restaurant) fromRestro).availFood(((Food) m.getValue()).getFID());
		    if(fEdit.getQuantity() <= 0) {
		    	((Restaurant) fromRestro).getFoodItems().remove(fEdit.getFID());
		    }
		}  
	}
	
}

