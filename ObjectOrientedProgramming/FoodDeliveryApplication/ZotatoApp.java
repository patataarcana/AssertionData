package assignment2;

import java.util.Scanner;

public class ZotatoApp {

	static User restaurants[];
	static User customers[];
	
	public static void makeRestaurantList() {
		restaurants = new Restaurant[5];
		restaurants[0] = new Authentic("Shah's", "Shah", "Pune");
		restaurants[1] = new Regular("Ravi's", "Ravi", "Delhi");
		restaurants[2] = new Authentic("The Chinese", "Chi", "Mumbai");
		restaurants[3] = new FastFood("Wang's","Wang", "Delhi");
		restaurants[4] = new Regular("Paradise", "Mohan", "Chennai");
	}
	
	public static void makeCustomerList() {
		customers = new Customer[5];
		customers[0] = new Elite("Ram", "Delhi");
		customers[1] = new Elite("Sam", "Pune");
		customers[2] = new Special("Tim", "Mumbai");
		customers[3] = new Normie("Kim", "Chennai");
		customers[4] = new Normie("Jim", "Delhi");
	}
	
	public static void printRestroNames() {
		for(int i = 0; i<restaurants.length; i++) 
			System.out.printf("\t%d) %s (%s)\n", i+1, ((Restaurant) restaurants[i]).getName(), restaurants[i].getCategory());		
	}
	
	public static void printCustomerNames() {
		for(int i = 0; i<customers.length; i++) 
			System.out.printf("\t%d) %s (%s)\n", i+1, ((Customer) customers[i]).getName(), customers[i].getCategory());
		}
		
	
	public static void makeDemo() {
		makeRestaurantList();
		makeCustomerList();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub 
		makeDemo();
		Scanner read = new Scanner(System.in);
		int Q, q, qq;
		while(true) {
			System.out.println("Welcome To ZOTATO!");
			System.out.printf("\t1) Enter as Restaurant Owner\n\t2) Enter as Customer\n\t3) Check User Details\n\t4) Company Account Details\n\t5) Exit\n");
			Q = read.nextInt();
			if(Q == 1) {
				System.out.println("Choose Restaurant");
				printRestroNames();
				q = read.nextInt() - 1;
				while(true) {
					System.out.print(restaurants[q].entryMenu());
					qq = read.nextInt();
					if(qq == 1) {
						//Add Item
						System.out.println("Enter food item details");
						System.out.println("Food Name:");
						read.nextLine();
						String fname = read.nextLine();
						System.out.println("Item Price:");
						float fprice = read.nextFloat();
						System.out.println("Item Quantity:");
						int fqty = read.nextInt();
						System.out.println("Item Category");
						String fcat = read.next();
						System.out.println("Offer:");
						float foff = read.nextFloat();
						System.out.print(((Restaurant) restaurants[q]).addFood(fname, fprice, fqty, fcat, foff));
					}
					else if(qq == 2) {
						//Edit Item
						System.out.println("Choose Item by code (FID)");
						((Restaurant) restaurants[q]).printFoodItems();
						int fid = read.nextInt();
						System.out.println("Choose an Attribute to Edit:");
						System.out.printf("\t1) Name\n\t2) Price\n\t3) Quantity\n\t4) Category\n\t5) Offer\n");
						int fquery = read.nextInt();
						Food freqd = ((Restaurant) restaurants[q]).availFood(fid);
						if(freqd != null) {
							if(fquery == 1) {
								System.out.print("Enter the new Name - ");
								read.nextLine();
								freqd.setName(read.nextLine());
							}
							else if(fquery == 2) {
								System.out.print("Enter the new Price - ");
								freqd.setPrice(read.nextFloat());								
							}
							else if(fquery == 3) {
								System.out.print("Enter the new Quantity - ");
								freqd.setQuantity(read.nextInt());
							}
							else if(fquery == 4) {
								System.out.print("Enter the new Category - ");
								freqd.setCategory(read.next());								
							}
							else if(fquery == 5) {
								System.out.print("Enter the new Offer - ");
								freqd.setDiscount(read.nextFloat());
							}
							System.out.printf("%d %s - %s", fid, ((Restaurant) restaurants[q]).getOwner(), freqd.toString());							
						}
						else {
							System.out.println("Enter valid FID next time");
						}
					}
					else if(qq == 3) {
						System.out.printf("Total Rewards Points: %d\n",((Restaurant) restaurants[q]).getRewardPts());
					}
					else if(qq == 4) {
						System.out.println("Offer on bill value - ");
						((Restaurant) restaurants[q]).setByPercent(read.nextInt());
					}
					else if(qq == 5) {
						break;
					}
					else {
						System.out.println("Enter Valid Query Please :(");
					}
				}
			}
			else if(Q == 2) {
				System.out.println("Pick a customer");
				printCustomerNames();
				q = read.nextInt() - 1;
				while(true) {
					System.out.println(customers[q].entryMenu());
					qq = read.nextInt();
					if(qq == 1) {
						printRestroNames();
						int r = read.nextInt() - 1;
						((Customer) customers[q]).setCart(new Cart(customers[q]));
						int error = 0;
						while(true && error<5) {
							System.out.println("Choose Item by code (FID)");
							((Restaurant) restaurants[r]).printFoodItems();
							int fid = read.nextInt();
							if(((Restaurant) restaurants[r]).availFood(fid) == null) {
								System.out.println("Enter valid food item code");
								error += 1;
								continue;
							}
							System.out.println("Enter Item Quantity -");
							int fqty = read.nextInt();
							if(((Restaurant) restaurants[r]).availFood(fid).getQuantity() < fqty) {
								System.out.println("Enter valid food item quantity");
								error += 1;
								continue;
							}
							System.out.printf("\t1) Add this Food Item to Cart\n\t2) Exit\n");
							int yn = read.nextInt();
							if(yn == 1) {
								Food ff = new Food(((Restaurant) restaurants[r]).availFood(fid));
								((Customer) customers[q]).getCart().addIt(ff, fqty);
								System.out.println("Item added to Cart");
							}
							else if(yn == 2) {
								System.out.println("Item NOT added to Cart");
								break;
							}						
						}
					}
					else if(qq == 2) {
						if(((Customer) customers[q]).getCart() == null) {
							System.out.println("Cart is Empty. Shop first");
						}
						else {
						System.out.println("Items in Cart -");
						((Customer) customers[q]).getCart().printCart();
						System.out.printf("Delivery Charge - %d /-\n", ((Customer) customers[q]).getDeliveryCharge());
						float billAmt = ((Customer) customers[q]).getCart().calcBalance();
						System.out.printf("Total Order Value - %.2f /-\n", billAmt);
						while(billAmt > ((Customer) customers[q]).getWalletBal() + ((Customer) customers[q]).fetchRewardAC()) {
							System.out.println("Oops! Insufficient Balance");
							System.out.printf("\t1) Delete Items\n\t2) Exit\n");
							int ans = read.nextInt();
							if(ans == 1) {
								System.out.println("Pick An Item ID to delete");
								((Customer) customers[q]).getCart().printCart();
								int del = read.nextInt();
								((Customer) customers[q]).getCart().delIt(del);
								billAmt = ((Customer) customers[q]).getCart().calcBalance();
								System.out.printf("Total Order Value - %.2f\n", billAmt);
							}
							else if(ans == 2){
								break;	//handle
							}
						}
						System.out.printf("\t1) Proceed to Checkout\n\t2) Exit\n");
						int rply = read.nextInt();
						if(rply == 1) {
							((Customer) customers[q]).getCart().orderIt();
						}
						else if(rply == 2) {
							System.out.println("No Items Bought. Discarding Cart");
						}
						((Customer) customers[q]).setCart(null);
						}
					}
					else if(qq == 3) {
						System.out.printf("Total Rewards - %d\n", ((Customer) customers[q]).fetchRewardAC());	
					}
					else if(qq == 4) {
						System.out.print(((Customer) customers[q]).getPrevOrders());
					}
					else if(qq == 5) {
						break;
					}
				}
			}
			else if(Q == 3) {
				System.out.printf("\t1) Restaurant List\n\t2) Customer List\n");
				q = read.nextInt();
				if(q == 1) {
					printRestroNames();
					qq = read.nextInt() - 1;
					restaurants[qq].displayDeets();
				}
				else if(q == 2) {
					printCustomerNames();
					qq = read.nextInt() - 1;
					customers[qq].displayDeets();
				}
			}
			else if(Q == 4) {
				restaurants[0].chargeCollected();
				customers[0].chargeCollected();
			}
			else if(Q == 5) {
				System.out.println("Thankyou for choosing ZOTATO :)");
				break;
			}
			else {
				System.out.println("Enter Valid Query Please :(");
			}
		}
	}

}
