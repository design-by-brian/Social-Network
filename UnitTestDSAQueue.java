/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "UnitTestDSAQueue.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Test harness for DSAQueue
 ***********************************************/
public class UnitTestDSAQueue {
   public static void main(String[] args)
   {
      DSAQueue queue = new DSAQueue();
      int data;
      int numTest = 5;
      int numPassed = 0;

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
      System.out.println("<>-- TEST ONE: enqueue --<>");
      try {
         System.out.println("--> Adding 'Four'");
         queue.enqueue(4);
         System.out.println("--> Adding 'Ten'");
         queue.enqueue(10);
         System.out.println("--> Adding 'Three'");
         queue.enqueue(3);
         System.out.println("--> Adding 'One'");
         queue.enqueue(1);

         System.out.println("TEST PASSED: All elements successfully added");
         System.out.println("--------------------------------------------");
         numPassed++;
      }
      catch (Exception e) {
         System.out.println("TEST FAILED: Could not add elements");
         System.out.println("-----------------------------------");
      }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
      try {
         System.out.println("<>-- TEST TWO: Removing Elements --<>");
         data = (int) queue.dequeue();
         System.out.println("Expected Four --> " + data);

         data = (int) queue.dequeue();
         System.out.println("Expected Ten --> " + data);

         data = (int) queue.dequeue();
         System.out.println("Expected Three --> " + data);

         data = (int) queue.dequeue();
         System.out.println("Expected One --> " + data);

         System.out.println("TEST PASSED: All elements successfully removed");
         System.out.println("----------------------------------------------");
         numPassed++;
      }
      catch (Exception e) {
         System.out.println("TEST FAILED: Elements could not be removed");
         System.out.println("------------------------------------------");
         System.out.println(e.getMessage());
         System.out.println(e.getStackTrace());
      }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
      try {
         System.out.println("<>-- TEST THREE: Removing from queue heap --<>");
         queue.dequeue();
         System.out.println("TEST FAILED: No exception thrown");
         System.out.println("--------------------------------");
      }
      catch (Exception e) {
         System.out.println("TEST PASSED: Element could not be removoed;");
         System.out.println("ERROR MESSAGE: " + e.getMessage());
         System.out.println("------------------------------------------");
         numPassed++;
      }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
      System.out.println("<>-- TEST FOUR: peek() --<>");
      try {
         System.out.println("<>-- peek() empty --<>");
         queue.peek();
         System.out.println("TEST FAILED");
      }
      catch (Exception e) {
         numPassed++;
         System.out.println("TEST PASSED");
         System.out.println(e.getMessage());
      }

      System.out.println("--> Adding 'Four'");
      queue.enqueue(4);
      System.out.println("--> Adding 'Ten'");
      queue.enqueue(10);

      try {
         System.out.println("<>-- peek() --<>");
         int result = (int) queue.peek();
         if (result == 4) {
            System.out.println("4 expected -> " + result);
            numPassed++;
            System.out.println("TEST PASSED");
         }
      } catch (Exception e) {
         System.out.println("TEST FAILED");
      }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
      System.out.print("\nTests Passed: " + numPassed + "/" + numTest);
   }
}
   

   
   
