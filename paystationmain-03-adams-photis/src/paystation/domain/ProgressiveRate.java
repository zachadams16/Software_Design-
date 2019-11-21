/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paystation.domain;

/**
 *
 * @author alecphotis
 */
public class ProgressiveRate implements RateStrategy{
   
    @Override
    public int calculateTime(int moneyInserted){
        float money = moneyInserted;
        if(moneyInserted < 150){
            return ((moneyInserted * 2) / 5); 
        }else if(moneyInserted >= 150 & moneyInserted <= 350){
            return (int) ((money - 150) * ((.3)) + 60);
        }else{
            return (moneyInserted - 350) / 5 + 120;
        }
    } 
}
