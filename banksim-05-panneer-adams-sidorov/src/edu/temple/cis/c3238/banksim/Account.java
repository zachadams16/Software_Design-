package edu.temple.cis.c3238.banksim;


//using draft pull request
/*
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */

public class Account {

    private volatile int balance;
    private final int id;
    private final Bank myBank;

    
    public synchronized void waitForEnoughfunds(int amount){
        while(!myBank.closed && amount >= balance){
            try{
                wait();
            }catch(InterruptedException ex){}
        }
    }
    
    public Account(Bank myBank, int id, int initialBalance) {
        this.myBank = myBank;
        this.id = id;
        balance = initialBalance;
    }

    //Sync -- takes care of mutual exclusion
    public synchronized int getBalance() {
        return balance;
    }


    public synchronized boolean withdraw(int amount) {
        if (amount <= balance) {
            int currentBalance = balance;
            //Thread.yield(); // Try to force collision
            int newBalance = currentBalance - amount;
            balance = newBalance;
            return true;
        } else {
            return false;
        }
    }

    //Sync
    public synchronized void deposit(int amount) {
        int currentBalance = balance;
        //Thread.yield();
        int newBalance = currentBalance + amount;
        balance = newBalance;
        notifyAll();
    }
    
    @Override
    public String toString() {
        return String.format("Account[%d] balance %d", id, balance);
    }
}