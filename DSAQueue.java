/**
 * THE FOLLOWING CLASS HAS BEEN REUSED AND EXTENDED FROM DSAQueue.java
 * SUBMITTED IN PRACTICAL 2 BY ME (BRIAN SMITH - 19463540)
 */

/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "DSAQueue.java"
 * DATE_CREATED: 08/08/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class file for DSAQueue object. Updated with DSALinkedList
 ***********************************************/
import java.io.Serializable;
import java.util.*;

public class DSAQueue implements Iterable, Serializable {
   private DSALinkedList list;

   /*DEFAULT_CONSTRUCTOR
      * IMPORT: none
      * EXPORT: none
      * ASSERTION: Creates memory address of a new DSAQueue object with default 
           classfields
   */
   public DSAQueue() {
      list = new DSALinkedList();  
   }
 
   //accesors
   public Object peek() throws EmptyListException {
      return list.peekFirst();
   }

   public int getNumElements() {
      return this.list.getNumElements();
   }
   
   //mutators 
   public void enqueue(Object value) {
      list.insertLast(value); //inserts value at front of queue
   }

   public Object dequeue() throws EmptyListException {
      Object value = list.peekFirst();
      list.removeFirst(); //removes the first value
      return value;
   }

   public boolean isEmpty() {
      return list.isEmpty();
   }

   public Iterator iterator() {
      return list.iterator();
   }
}   
