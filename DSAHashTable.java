/**
 * THE FOLLOWING CLASS HAS BEEN REUSED AND EXTENDED FROM DSAHashTable.java
 * SUBMITTED IN PRACTICAL 6 BY ME (BRIAN SMITH - 19463540)
 */

/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "DSAHashTable.java"
 * DATE_CREATED: 16/09/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for managing the DSAHashTable object. Main methods are put, get, remove and resize.
 ***********************************************/
import java.io.Serializable;
import java.lang.Math;

public class DSAHashTable implements Serializable {
   private static final double UPPER_THRESHOLD = 0.7;
   private static final double LOWER_THRESHOLD = 0.4;

   private class DSAHashEntry implements Serializable {
      private String key;
      private Object value;
      private int state; //never used = 0, used = 1, formerly used = -1

      public DSAHashEntry() {
         this.key = "";
         this.value = null;
         this.state = 0;
      }

      /*ALTERNATE_CONSTRUCTOR
       * IMPORT: String key, Object value
       * EXPORT: none
       * ASSERTION: Creates memory address of a new DSAHashTable with values
       * set by imported value
       */
      public DSAHashEntry(String key, Object value) {
         this.key = key;
         this.value = value;
         this.state = 1;
      }
   }

   private DSAHashEntry[] hashArray;
   private int count;

   public DSAHashTable(int tableSize) {
      int actualSize = nextPrime(tableSize);
      hashArray = new DSAHashEntry[actualSize];
      for(int ii = 0; ii < actualSize;ii++) {
         hashArray[ii] = new DSAHashEntry();
      }
      count = 0;
   }

   public int getCount() {
      return count;
   }

   /* METHOD: get
    * IMPORT: String inKey
    * EXPORT: Object value
    * PURPOSE: returns the value found at hashEntry index, if null throw exception
    */
   public Object get(String inKey) throws KeyNotFoundException {
      int hashIdx = find(inKey);
      Object value;
      if(hashArray[hashIdx].value != null) { //valid index returned and value not null
         value = hashArray[hashIdx].value;
      }
      else {
         throw new KeyNotFoundException("Key not found: " + inKey);
      }
      return value;
   }

   /* METHOD: put
    * IMPORT: String inKey, Object inValue
    * EXPORT: none
    * PURPOSE: sets the value of the hashEntry at the hashIndex provided by string.
    */
   public void put(String inKey, Object inValue) throws KeyAlreadyExistsException {
      int hashIdx = find(inKey);
      if (hashArray[hashIdx].state == 0) { //never been used
         hashArray[hashIdx].value = inValue;
         hashArray[hashIdx].state = 1; //set used
         hashArray[hashIdx].key = inKey;
         count++;
         if (getLoadFactor() > UPPER_THRESHOLD) {
            resize(2);
         }
      } else if (hashArray[hashIdx].state == 1) {
         throw new KeyAlreadyExistsException("Key " + inKey + " already exists");
      }
   }

   /* METHOD: remove
    * IMPORT: String inKey
    * EXPORT: none
    * PURPOSE: removes the hashEntry at the hashIdx
    */
   public void remove(String inKey) throws KeyNotFoundException {
      int hashIdx = find(inKey);
      if(hashArray[hashIdx].state == 1) {
         hashIdx = hash(inKey);
         hashArray[hashIdx].value = null; //removes value
         hashArray[hashIdx].state = -1; //sets previously used
         hashArray[hashIdx].key = "";
         count--;
         if(getLoadFactor() < LOWER_THRESHOLD) {
            resize(1/2);
         }
      }
      else {
         throw new KeyNotFoundException("Key not found: " + inKey);
      }
   }

   /* METHOD: getLoadFactor
    * IMPORT: none
    * EXPORT: Float loadFactor
    * PURPOSE: Returns the load factor of the table, count/length of table.
    */
   public float getLoadFactor() {
      return ((float)count)/((float)hashArray.length);
   }

   /* METHOD: display
    * IMPORT: none
    * EXPORT: none
    * PURPOSE: Loops through table and prints.
    */
   public void display() {
      System.out.println("--------------------------------------------");
      System.out.println("Display Table");
      System.out.println("--------------------------------------------");
      for(int ii = 0;ii < hashArray.length;ii++) {
         if(hashArray[ii].value != null) {
            System.out.print(hashArray[ii].key + " ");
            System.out.print(hashArray[ii].value);
            System.out.println();
         }
      }
   }

   /* METHOD: exportKeys
    * IMPORT: void
    * EXPORT: String[]
    * PURPOSE: Iterates through the hashArray retrieving all hashEntries set to 1 (used) and adds their keys to an array of
    * Strings.
    */
   public String[] exportKeys() throws EmptyHashTableException {
      String[] outArray;
      if (count > 0) {
         outArray = new String[count];
         int element = 0;
         for (int jj = 0; jj < getTableSize(); jj++) {
            if (hashArray[jj].key != null && hashArray[jj].value != null) {
               if (element < count) {
                  outArray[element] = hashArray[jj].key;
                  element++;
               }
            }
         }
      }
      else {
         throw new EmptyHashTableException("Empty hash table");
      }
      return outArray;
   }

   /* METHOD: exportKeys
    * IMPORT: void
    * EXPORT: String[]
    * PURPOSE: Iterates through the hashArray retrieving all hashEntries set to 1 (used) and adds their values to an array of
    * Objects.
    */
   public Object[] exportValues() throws EmptyHashTableException {
      Object[] outArray;
      if (count > 0) {
         outArray = new Object[count];
         int element = 0;
         for (int jj = 0; jj < getTableSize(); jj++) {
            if (hashArray[jj].state == 1) {
               if (element < count) {
                  outArray[element] = hashArray[jj].value;
                  element++;
               }
            }
         }
      }
      else {
         throw new EmptyHashTableException("Empty hash table");
      }
      return outArray;
   }

   public int getTableSize() {
      return hashArray.length;
   }

   /* METHOD: find
    * IMPORT: String inKey
    * EXPORT: Integer hashIdx
    * PURPOSE: Finds the viable hashIdx, that is either empty, used or returns -1 for full table
    */
   private int find(String inKey) {
      int hashIdx = hash(inKey);
      int origIdx = hashIdx;
      boolean found = false;
      boolean giveUp = false;

      while(!found && !giveUp) {
         if(hashArray[hashIdx].state == 0) { //never used entry
            giveUp = true;
         }
         else if(hashArray[hashIdx].key.equals(inKey)) { //key found
            found = true;
         }
         else { //probing
            hashIdx = ((hashIdx + hashStep(inKey)) % hashArray.length);
            if (hashIdx == origIdx) { //table full
               giveUp = true;
               hashIdx = -1;
            }
         }
      }

      return Math.abs(hashIdx);
   }

   /* METHOD: resize
    * IMPORT: int factor
    * EXPORT: none
    * PURPOSE: resizes the table if its too small or too big
    */
   private void resize(int factor) {
      DSAHashEntry[] oldArray = hashArray;
      int newSize = hashArray.length;
      newSize = (newSize) * (factor);
      newSize = nextPrime(newSize); //make sure new size is still a prime
      hashArray = new DSAHashEntry[newSize];
      count = 0;
      for(int ii = 0; ii < newSize;ii++) {
         hashArray[ii] = new DSAHashEntry(); //initialise new hashArray
      }
      for (int ii = 0; ii < oldArray.length; ii++) {
         DSAHashEntry entry = oldArray[ii];
         if (entry.state == 1) { //If old array entry used then insert.
            try {
               put(entry.key, entry.value);
            } catch (KeyAlreadyExistsException e) {} /*shouldnt happen but still need to catch*/
         }
      }
   }

   /* METHOD: hashStep
    * IMPORT: String inKey
    * EXPORT: Integer hashStep
    * PURPOSE: Determines a unique step size for a key to use in probing.
    */
   private int hashStep(String inKey) {
      int key = 0;
      int MAX_STEP = hashArray.length/2;
      MAX_STEP = nextPrime(MAX_STEP);
      for(int ii = 0;ii < inKey.length();ii++) {
         key = key + inKey.charAt(ii);
      }
      return Math.abs((MAX_STEP - (key % MAX_STEP)));
   }

   /* METHOD: hash
    * IMPORT: String inKey
    * EXPORT: Integer hash
    * PURPOSE: Hash function that returns a hashIdx
    */
   private int hash(String inKey) {
      int hashIdx = 0;
      for (int ii = 0; ii < inKey.length(); ii++) {
         hashIdx = (33 * hashIdx) + inKey.charAt(ii);
      }
      return Math.abs(hashIdx % hashArray.length);
   }

   /* METHOD: nextPrime
    * IMPORT: Integer startVal
    * EXPORT: Integer primeVal
    * PURPOSE: Finds the next prime number
    */
   private int nextPrime(int startVal) {
      int primeVal;
      if(startVal % 2 == 0) {
         primeVal = startVal - 1;
      }
      else {
         primeVal = startVal;
      }

      boolean isPrime = false;
      do {
         primeVal = primeVal + 2;
         int ii = 3;
         isPrime = true;
         int rootVal = (int) Math.sqrt(primeVal);
         do {
            if(primeVal % ii == 0) {
               isPrime = false;
            }
            else {
               ii = ii + 2;
            }
         } while(ii <= rootVal && isPrime);
      } while (!isPrime);
      return primeVal;
   }
}
