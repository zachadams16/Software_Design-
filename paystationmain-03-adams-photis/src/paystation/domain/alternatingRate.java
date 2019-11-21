/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paystation.domain;

import java.util.Scanner;

/**
 *
 * @author alecphotis
 */

public class alternatingRate implements RateStrategy{

    

    @Override
    public int calculateTime(int moneyInserted){
     
        int option;
        System.out.println("Enter 1 for weekday 2 for weekend");
        Scanner scan = new Scanner(System.in);
        option = scan.nextInt();
        switch (option) {
            case 1:
                if(moneyInserted < 150){
                    return ((moneyInserted * 2) / 5);
                }else if(moneyInserted >= 150 || moneyInserted <= 350){
                    return (int) ((moneyInserted - 150) * (.3) + 60);
                }else{
                    return (moneyInserted - 350) / 5 + 120;
                }
            case 2:
                return (moneyInserted / 5) * 2;
            default:
                System.out.println("Incorrect input");
                return -1;
        }
    }         
}
