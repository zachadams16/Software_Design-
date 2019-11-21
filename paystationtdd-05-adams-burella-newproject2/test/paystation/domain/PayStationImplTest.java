/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package paystation.domain;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PayStationImplTest {

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
    }

     /**
     * Adding a single coin and canceling returns the single coin entered
     */
    @Test
    public void shouldReturnOneCoinAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        assertEquals("Cancel should return a Map",
                 1, ps.cancel().size());
        assertEquals("Cancel should return a dime",
                 Integer.valueOf(1), ps.cancel().get(10));
    }
    
     /**
     * Adding multiple coins and canceling returns all coins entered
     */
    @Test
    public void shouldReturnAllCoinsAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(5);
        assertEquals("Cancel should return a Map",
                 2, ps.cancel().size());
        assertEquals("Cancel should return all dimes",
                 Integer.valueOf(3), ps.cancel().get(10));
        assertEquals("Cancel should return all nickels",
                 Integer.valueOf(1), ps.cancel().get(5));
    }
    
     /**
     * Adding a single coin or multiple coins and canceling does not return
     * a coin that was not entered
     */
    @Test
    public void shouldNotReturnUnusedCoinAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        assertEquals("Cancel should return a dime",
                 Integer.valueOf(1), ps.cancel().get(10));
        assertEquals("Cancel should not return a quarter",
                 null, ps.cancel().get(25));
        ps.addPayment(25);
        ps.addPayment(25);
        assertEquals("Cancel should return a dime",
                 Integer.valueOf(1), ps.cancel().get(10));
        assertEquals("Cancel should return all quarters",
                 Integer.valueOf(2), ps.cancel().get(25));
        assertEquals("Cancel should not return a nickel",
                 null, ps.cancel().get(5));
    } 
    
     /**
     * Buying should empty the map contents
     */
    @Test
    public void shouldEmptyMapAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.buy();
        assertEquals("Map should have size 0 after buying",
                0, ps.cancel().size());
        assertTrue(ps.cancel().isEmpty());
    } 
    
    /**    
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }
    
    /**
     * Verify that empty returns the amount entered
     */
    @Test
    public void shouldReturnAmountEnteredAfterEmpty()
            throws IllegalCoinException{
        ps.addPayment(25);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(5);
        assertEquals("Will display amount entered", 50, ps.empty());
    }
    /**
     * Verify that call to cancel does not add to the amount returned by empty
     */
    @Test
    public void ShouldNotAddToAmountReturnedByEmptyIfCanceled()
            throws IllegalCoinException{
        ps.addPayment(25);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(5);
        ps.cancel();
        assertEquals("Call to cancel should not add to value returned by empty",0 , ps.empty());
    }
    
     /**
     * Verify that call to empty resets the amount to 0
     */
    @Test
    public void ShouldResetToZeroAfterEmptyCall()
            throws IllegalCoinException{
        
        ps.addPayment(25);
        ps.addPayment(10);
        assertEquals("Should return amount entered by User", 35, ps.empty());
        assertEquals("Call to empty should reset the amount to 0", 0, ps.empty());
    }
    
    @Test 
    public void ShouldClearMapAfterCallToCancel()
            throws IllegalCoinException{
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.cancel2();
        assertEquals("Map should have size 0 after buying",
                0, ps.cancel().size());
    }
    
}
