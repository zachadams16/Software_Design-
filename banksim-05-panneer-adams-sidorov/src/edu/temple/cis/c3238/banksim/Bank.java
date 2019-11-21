package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
//comment to test draft pull
public class Bank {

    public static final int NTEST = 10;
    private final Account[] accounts;
    private long ntransacts = 0;
    private final int initialBalance;
    private final int numAccounts;
    private boolean testing;
    private final ReentrantLock tLock;
    public int current;
    public boolean closed;


    public Bank(int numAccounts, int initialBalance) {
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
        closed = testing = false;
        current = 0;
        tLock = new ReentrantLock();
    }

    public void transfer(int from, int to, int amount) {
       
        accounts[from].waitForEnoughfunds(amount); //make sure there is enough funds to complete the transfer
        
        if(closed){ //if closed end program
            return ;
        }
        
        tLock.lock(); 
        try{
        if(closed){ //if closed end program even if the thread got passed the first check if its closed
            return ;
        }            
            if (accounts[from].withdraw(amount)) {
                accounts[to].deposit(amount);
                //System.out.println("Transfering " + amount +" from _ to _ "+ from +" "+ to);
            }
        }finally{
            tLock.unlock();
        }
        if (shouldTest()) {
           // test();
          TestThread tester = new TestThread();
            try{
                tester.join();
                tester.start();
           }catch(InterruptedException ex){}
        }

    }

    public void test() {
   
            int sum = 0;
            
            tLock.lock(); //So that it will print the correct balance I have to make sure nothing is getting deposited or withdrawn
            try{
                for (Account account : accounts) {
                    System.out.printf("%s %s%n",
                    Thread.currentThread().toString(), account.toString());
                    sum += account.getBalance();
                }
            }finally{
                tLock.unlock();
            }
            
            System.out.println(Thread.currentThread().toString()
                    + " Sum: " + sum);
            if (sum != numAccounts * initialBalance) {
                System.out.println(Thread.currentThread().toString()
                        + " Money was gained or lost");
                System.exit(1);
            } else {
                System.out.println(Thread.currentThread().toString()
                        + " The bank is in balance");
            }
    }

    public int size() {
        return accounts.length;
    }

    public boolean shouldTest() {
        return ++ntransacts % NTEST == 0;
    }
    
    public void closeBank(){
        synchronized(this){
            closed = true;
            //System.out.println("bank is closed");
        }
        for(Account account : accounts){
            synchronized(account){
                account.notifyAll();
            }
        }
    }

    private class TestThread extends Thread {

        @Override
        public void run() {
            test();
        }

    }
}