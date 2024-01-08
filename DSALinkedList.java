/**
 * THE FOLLOWING CLASS HAS BEEN REUSED AND EXTENDED FROM DSALinkedList.java
 * SUBMITTED IN PRACTICAL 3 BY ME (BRIAN SMITH - 19463540)
 */

/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "DASLinkedList.java"
 * DATE_CREATED: 14/08/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for DSALinkedList (Data Structure) alternative to the array. Contains an inner private class DSALinkedNode.
 ***********************************************/

import java.util.*;
import java.io.*;

public class DSALinkedList implements Iterable, Serializable {
   private class DSAListNode implements Serializable {
      //class fields
      private Object value; //stores node data
      private DSAListNode next; //reference to next node - null if last node
      private DSAListNode prev; //reference to the prev node
 
      /*ALTERNATE_CONSTRUCTOR
         * IMPORT: Object value
         * EXPORT: none
         * ASSERTION: Creates memory address of a new DSAListNode with value
         * set by imported value
      */
      public DSAListNode(Object value) {
         this.value = value;
         next = null;
         prev = null;
      }
      
      //accessors
      public Object getValue() {
         return value;
      }

      public DSAListNode getNext() {
         return next;
      }

      public DSAListNode getPrev() {
         return prev;
      }
      
      //mutators
      public void setValue(Object value) {
         this.value = value;
      }
      
      public void setNext(DSAListNode next) {
         this.next = next;
      }

      public void setPrev(DSAListNode prev) {
         this.prev = prev;
      }  
   }

   //class fields
   private DSAListNode head; //first node in the linked list
   private DSAListNode tail; //last node in the linked list
   private int numElements;
   
   /*DEFAULT_CONSTRUCTOR
      * IMPORT: none
      * EXPORT: none
      * ASSERTION: Creates memory address of a new DSALinkedList with head set
      * to null
   */
   public DSALinkedList() {
      head = null;
      tail = null; 
   }

   //ACCESORS


   public int getNumElements() {
      return numElements;
   }

   public boolean isEmpty() {
      return (head == null); //if head(first) is empty list = empty
   }
   
   /* METHOD: peekLast
    * IMPORT: none
    * EXPORT: Object nodeValue
    * PURPOSE: returns the first value of the list if not empty
    */
   public Object peekFirst() throws EmptyListException {
      Object nodeValue;
      if(!isEmpty()) //if list is not empty
      {
         nodeValue = head.getValue(); //nodeValue = first value   
      }
      else //list empty
      {
         throw new EmptyListException("Empty list");
      }
      return nodeValue;
   }
  
   /* METHOD: peekLast
    * IMPORT: none
    * EXPORT: Object nodeValue
    * PURPOSE: returns the last value of the list if not empty
    */
   public Object peekLast() throws EmptyListException {
      Object nodeValue;
      if(!isEmpty()) { //if not empty
         nodeValue = tail.getValue(); //reached last so set nodeValue
      } else { //empty list
         throw new EmptyListException("Empty list");
      }
      return nodeValue;
   }

   //MUTATORS

   /* METHOD: insertFirst
    * IMPORT: Object newValue
    * EXPORT: none
    * PURPOSE: inserts a node before head. Creates a new node and sets its
    * reference to the 'old' head. Then set head to the new node. 
    */
   public void insertFirst(Object newValue)
   {
      DSAListNode newNd = new DSAListNode(newValue); //new node to be inserted
      if(isEmpty()) { //no nodes
         head = newNd; //list is empty so new node is first node
         tail = newNd; //new node is also last so tail
         numElements++;
      } else {
         newNd.setNext(head);
         head.setPrev(newNd); //new node references head
         head = newNd; //head is now the new node
         numElements++;
      }
   }

   /* METHOD: insertLast
    * IMPORT: Object newValue
    * EXPORT: none
    * PURPOSE: inserts a node at the end of the list. 
    */
   public void insertLast(Object newValue)
   {
      DSAListNode newNd = new DSAListNode(newValue); //new node to be inserted
      if(isEmpty()) {
         head = newNd;
         tail = newNd;
         numElements++;
      } else if(head.getNext() == null) { //if one element
         tail = newNd;
         tail.setPrev(head);
         head.setNext(tail);
         numElements++;
      } else { //multiple elements
         DSAListNode prevNd = tail;
         tail = newNd; //last is first
         tail.setPrev(prevNd);
         prevNd.setNext(tail);
         numElements++;
      }
   }

   /* METHOD: removeFirst
    * IMPORT: none
    * EXPORT: Object nodeValue
    * PURPOSE: removes last node and exports the value of that node 
    */
   public Object removeLast() throws EmptyListException {
      Object nodeValue;
      if(this.isEmpty()) {
         throw new EmptyListException("Empty list");
      } else if(head.getNext() == null) { //only one node in list
         nodeValue = head.getValue(); //set value to first
         head = null; //remove head
         tail = null;
         numElements--;
      } else { //more than one node
         DSAListNode newTail = tail.getPrev();
         nodeValue = tail.getValue();
         tail = newTail;
         tail.setNext(null);
         numElements--;
      }
      return nodeValue;
   }

   /* METHOD: removeFirst
    * IMPORT: none
    * EXPORT: Object nodeValue
    * PURPOSE: removes first node and exports the value of that node 
    */
   public Object removeFirst() throws EmptyListException {
      Object nodeValue;
      if(isEmpty()) { //list empty
         throw new EmptyListException("Empty list");
      } else if(head.getNext() == null) { //one element
         nodeValue = head.getValue();
         head = null;
         tail = null;
         numElements--;
      } else {
         nodeValue = head.getValue();
         DSAListNode newNd = head.getNext(); 
         head.setNext(null);
         newNd.setPrev(null);
         head = newNd;
         numElements--;
      }
      return nodeValue;
   }

   //Iterator class
   public Iterator iterator() {
      return new DSALinkedListIterator(this);
   }

   private class DSALinkedListIterator implements Iterator {
      private DSAListNode iterNext;
      
      public DSALinkedListIterator(DSALinkedList theList) {
         iterNext = theList.head;
      }

      public boolean hasNext() { //checks if list is empty
         return (iterNext != null);
      }

      public Object next() { //returns the next object in the list
         Object value;
         if(iterNext == null) { //if no more values return null
            value = null;
         } else {
            value = iterNext.getValue();
            iterNext = iterNext.getNext(); //move cursor along
         }
         return value;
      }
      public void remove() { //not supported
         throw new UnsupportedOperationException("Not suppoerted.");
      }
   }
}   
