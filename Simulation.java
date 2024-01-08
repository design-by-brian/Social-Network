/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "Simulation.java"
 * DATE_CREATED: 15/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class runs a simulation fo events from an event and netowrk file. Calls FileIO to read in these files. Also
 * Calls GRraphStream to visualise the graph to the user.
 ***********************************************/

public class Simulation {
   public static void run (String[] args) {
      SocialNetwork simulationNetwork = new SocialNetwork(Double.valueOf(args[3]), Double.valueOf(args[4]));
      FileIO.processSimFile("./InputFiles/NetworkFiles/" + args[1], simulationNetwork);

      try {
         GraphStream.visualiseGraph(simulationNetwork); /*Before eventfile*/
      } catch (EmptyGraphException e) {
         System.out.println(e.getMessage());
      }

      FileIO.processSimFile("./InputFiles/EventFiles/" + args[2], simulationNetwork, args);

      try {
         GraphStream.visualiseGraph(simulationNetwork); /*Aftereventfile*/
      } catch (EmptyGraphException e) {
         System.out.println(e.getMessage());
      }
   }
}
