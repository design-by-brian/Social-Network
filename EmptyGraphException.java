/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "EmptyGraphException.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class extends exception and is used as a custom exception for DSAGraph.java. Thrown if graph is empty e.g.
 * vertices or edges = 0
 ***********************************************/
public class EmptyGraphException extends Exception {
   public EmptyGraphException(String error) {
      super(error);
   }
}
