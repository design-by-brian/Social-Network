/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "EmptyListException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for DSALinkedList.java. Thrown if list is empty e.g.
 * numElements = 0
 ***********************************************/
public class EmptyListException extends Exception {
   EmptyListException(String error) {
      super(error);
   }
}
