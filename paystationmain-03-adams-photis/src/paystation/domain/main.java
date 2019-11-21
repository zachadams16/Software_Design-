/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paystation.domain;

import java.util.Scanner;
import static java.lang.System.exit;
import java.util.Map;

/**
 *
 * @author alecphotis and zachadams
 */
public class main {
         public static void main(String[] args) throws IllegalCoinException {
             
         
         PayStationImpl ps = new PayStationImpl();    
         boolean menu = true;
         int choice;
        ps.switchRate(new linearRate());
       
        while(menu) {
            
            System.out.println(
                "Main Menu: Enter a digit for requested action.\n" +
                "1 = Deposit Coins\n" +
                "2 = Display Amount of Time\n" +
                "3 = Buy Ticket\n" +
                "4 = Cancel Transaction\n" +
                "5 = Change Rate Strategy\n" +
                "6 = Leave Paystation"
           
            );
            
            Scanner scan = new Scanner(System.in);
            choice = scan.nextInt();
            System.out.println(" ");
           
            switch(choice) {
                case 1:
                    System.out.println("Enter requested amount:");
                    int amount = scan.nextInt();
                    try{
                        ps.addPayment(amount);
                        System.out.println(" ");
                    }catch(IllegalCoinException e){
                        System.out.println("Invalid coin, Please choose another action");
                    }
                    break;
                case 2:       
                    System.out.println("Total time bought is " + ps.readDisplay()+ " minutes");
                    break;
                case 3:
                    Receipt receipt = ps.buy();
                    System.out.println("Time purchase complete");
                    System.out.println("Priting parking ticket receipt..." + " " + receipt.value() + " minutes.\n");
                    //int next = scan.nextInt();
                    break;
                case 4:
                    
                    System.out.println("This transaction will now be cancelled");
                    Map<Integer, Integer> coins = ps.cancel();
                    if(coins.isEmpty()){
                        System.out.println("You have not entered any coins");
                    }else{
                        coins.keySet().forEach((coin) -> {
                            int coinAmount = 0;
                            if(null!=coin)switch (coin) {
                                case 1:
                                    coinAmount = 5;
                                    break;
                                case 2:
                                    coinAmount = 10;
                                    break;
                                case 3:
                                    coinAmount = 25;
                                    break;
                                default:
                                    break;
                            }
                            System.out.println("Coin: " + coinAmount  + " Amount of Coins: " + coins.get(coin));
                            });
                                }
                    break;
                case 5:
                    System.out.println("Enter what town you want");
                    System.out.println("1 for Alphatown");
                    System.out.println("2 for betatown");
                    System.out.println("3 for gammatown");
                    int num;
                    num = scan.nextInt();
                    switch(num){
                       case 1:
                            ps.switchRate(new linearRate());
                            break;
                      case 2:
                            ps.switchRate(new ProgressiveRate());
                            break;
                       case 3:
                            ps.switchRate(new alternatingRate());
                            break;
                        default:
                            break;
                    }
                    break;
                    
                case 6:
                    return;
            }
            }
         }
            
    //test comment1
}
