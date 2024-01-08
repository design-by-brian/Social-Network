/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "EmptyGraphException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for DSAHashTable.java. Thrown if get call uses
 * a key not in the table.
 ***********************************************/
public class KeyNotFoundException extends Exception {
   public KeyNotFoundException(String error) {
      super(error);
   }
}
