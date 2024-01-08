/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "FileLineException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for FileIO.java. Thrown if a line read in does
 * not follow the correct format.
 ***********************************************/
public class FileLineException extends Exception {
   public FileLineException (String error) {
      super(error);
   }
}
