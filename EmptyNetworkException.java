/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "EmptyGraphException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for DSAGraph.java. Thrown if network graph is empty
 * or distribute/update is called when no post has been made.
 ***********************************************/
public class EmptyNetworkException extends Exception {
   public EmptyNetworkException(String error) {
      super(error);
   }
}
