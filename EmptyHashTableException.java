/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "EmptyHashTableException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for DSAHashTable.java. Thrown if table is empty e.g.
 * count = 0;
 ***********************************************/
public class EmptyHashTableException extends Exception {
   public EmptyHashTableException(String error) {
      super(error);
   }
}
