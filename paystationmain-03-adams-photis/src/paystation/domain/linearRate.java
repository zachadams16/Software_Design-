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
public class linearRate implements RateStrategy{

    @Override
    public int calculateTime(int moneyInserted){
        return (moneyInserted / 5) * 2;
    }      

}


