/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "UnitTestDSAGraph.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Test harness for DSAGraph
 ***********************************************/

public class UnitTestDSAGraph {
   public static void main(String[] args) {
        DSAGraph graph = new DSAGraph(10, 10);
        int numTest = 9;
        int numPassed = 0;

        Person A = new Person("A");
        Person B = new Person("B");
        Person C = new Person("C");
        Person D = new Person("D");
        Person E = new Person("E");

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST addVertex
        System.out.println("=================================================================");
        System.out.println("Creating new DSAGraph - 10 vertices, 10 edges");
        System.out.println("=================================================================");
        System.out.println("<>--- TEST ONE: addVertex() ---<>");
        try {
             graph.addVertex("A", A);
             graph.addVertex("B", B);
             graph.addVertex("C", C);
             graph.addVertex("D", D);
             graph.addVertex("E", E);
             System.out.println("TEST PASSED");
             numPassed++;
        } catch (Exception e) {
             System.out.println("TEST FAILED");
        }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST getVertexValue
        System.out.println("<>--- TEST TWO: getVertex() ---<>");
        try {
             if (!(A.equals(graph.getVertexValue("A")))) {
                  throw new Exception();
             }
             if (!(D.equals(graph.getVertexValue("D")))) {
                  throw new Exception();
             }
             System.out.println("TEST PASSED");
             numPassed++;
        } catch (Exception e) {
             System.out.println("TEST FAILED");
        }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST addEdge
        System.out.println("<>--- TEST THREE: addEdge() ---<>");
        try {
             graph.addEdge("A", "B");
             graph.addEdge("C", "B");
             graph.addEdge("D", "B");
             graph.addEdge("B", "D");
             graph.addEdge("B", "A");
             numPassed++;
             System.out.println("TEST PASSED");
        } catch (Exception e) {
             e.printStackTrace();
             System.out.println("TEST FAILED");
        }

        System.out.println("<>--- TEST FOUR (a): addEdge() - self referencing - ---<>");
        try {
             graph.addEdge("B", "B");
             System.out.println("TEST FAILED");
        } catch (Exception e) {
             numPassed++;
             System.out.println(e.getMessage());
             System.out.println("TEST PASSED");
        }

        System.out.println("<>--- TEST FOUR (b): addEdge() - already existing- ---<>");
        try {
             graph.addEdge("A", "B");
             System.out.println("TEST FAILED");
        } catch (Exception e) {
             numPassed++;
             System.out.println(e.getMessage());
             System.out.println("TEST PASSED");
        }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST remove
        System.out.println("<>--- TEST FIVE: removeVertex() - existing - ---<>");
        try {
             graph.removeVertex("A");
             numPassed++;
             System.out.println("TEST PASSED");
        } catch (Exception e) {
             System.out.println("TEST FAILED");
        }

        System.out.println("<>--- TEST SIX: removeVertex() - not existing - ---<>");
        try {
             graph.removeVertex("F");
             System.out.println("TEST FAILED");
        } catch (Exception e) {
             numPassed++;
             System.out.println("TEST PASSED");
        }

        System.out.println("<>--- TEST SEVEN: removeEdge() - existing - ---<>");
        try {
             graph.removeEdge("A", "B");
             numPassed++;
             System.out.println("TEST PASSED");
        } catch (Exception e) {
             System.out.println("TEST FAILED");
        }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST displayList()
        try {
             System.out.println("<>--- TEST EIGHT: displayList() ---<>");
             graph.displayList();
             System.out.println("<>------------------------------<>");
             System.out.println("TEST PASSED");
             numPassed++;
        } catch (Exception e) {
             System.out.println("TEST FAILED");
        }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        System.out.print("\nTests Passed: " + numPassed + "/" + numTest);
   }
}