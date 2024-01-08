/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "KeyAlreadyExistsException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for DSAHashTable.java. Thrown if a key already
 * in the table is given as a new key to put.
 ***********************************************/
public class KeyAlreadyExistsException extends Exception {
   public KeyAlreadyExistsException(String error) {
      super(error);
   }
}
