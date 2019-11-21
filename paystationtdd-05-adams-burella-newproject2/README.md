# Lab 3 - PayStation TDD
### Requirements:
* Create an empty method that returns the total amount collected so far and resets the amount
* Change the cancel method so that it returns each coin the user put in back to the user if the transaction is canceled
* Implement tests for the following conditions:
  * Calling empty returns total amount collected so far
  * Canceling an entry does not add to this amount returned by calling empty
  * Calling empty resets the amount collected to zero
  * Calling cancel returns a map containing a single coin entered
  * Calling cancel returns a map containing multiple different coins entered
  * Calling cancel returns a map that does not contain a coin that was not entered
  * Calling cancel clears the map
  * Calling buy clears the map
 
We were able to implement everything that was asked, including the map that neither of us had any experience programming with. We had trouble making a deep copy of the map (could only figure out a shallow copy which was cleared when map was cleared) so we implemented a separate method that clears the map.
We split the coding and testing evenly.

##### Zachary Adams:
I first added a empty method which returned the amount entered. Then to that empty method I added a function that also emptied the total coins entered and then returned the amount emptied. I added a test for both of these functions. One to test if it returned the total amount and another to empty the value and check that it was correctly emptied. Also, I wrote a function that makes sure a call to cancel does not add to the value returned by empty. The last part I worked on was that a call to cancel will reset the map. 

##### Danielle Burella:
I first added an empty cancel method that returned NULL instead of a map. Then I tested that calling cancel returned a map. I added a test to see if cancel will return a single coin entered in the paystation and added a single coin to the map. I then added a test to see if cancel will return multiple coins entered in the paystation and finished implementing how the addPayment method will add coins to the map. Then I added a test that checked if a coin that was not entered was returned.  The last test I did was testing if calling buy emptied the map and added a line of code in the buy method to clear the map. Finally, I refactored the code and removed a duplicate line of code in buy that cleared the map. Now buy calls the new method we made that clears the map.