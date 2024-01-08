/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "SocialSim.java"
 * DATE_CREATED: 15/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class contains the main for this Social Network project. Takes in a set of string arguments form the
 * command line. If either -i or -s <with files and probabilities> the main calls the respective main function.
 * Interactive or Simulation. Displays usage information if no arguments is provided, along with the available input
 * files (Network and Events).
 ***********************************************/
import java.io.File;

public class SocialSim {
   public static void main(String[] args) {
      int argc = args.length;
      if (argc == 0) { /*no arguments - usage information*/
         File[] networkFiles = new File("./InputFiles/NetworkFiles").listFiles(); /*directory for network files*/
         File[] eventFiles = new File("./InputFiles/EventFiles").listFiles(); /*directory for event files*/

         System.out.println();
         System.out.println("SocialSim Usage Information (Command Line Arguments)");
         System.out.println("'-i' for interactive testing environment");
         System.out.println("'-s' for simulation mode: -s <netfile> <eventfile> <prob_like> <prob_foll>");
         System.out.println("    netfile - text file that describes network connections.");
         System.out.println("    eventfile - text file that describes network events over time.");
         System.out.println("    prob_like - real number (decimal) " +
               "that defines the probability of liking a post.");
         System.out.println("    prob_foll - real number (decimal) " +
               "that defines the probability of following a person.");

         System.out.println();
         System.out.println("<<< Network Files >>>");
         if (networkFiles != null) {
            for (int ii = 0; ii < networkFiles.length; ii++) {
               if (networkFiles[ii].isFile()) {
                  System.out.println(networkFiles[ii].getName());
               }
            }
            System.out.println();
         } else {
            System.out.println("No network files...");
            System.out.println();
         }

         System.out.println("<<< Event Files >>>");
         if(eventFiles != null) {
            for (int ii = 0; ii < eventFiles.length; ii++) {
               if (eventFiles[ii].isFile()) {
                  System.out.println(eventFiles[ii].getName());
               }
            }
            System.out.println();
         } else {
            System.out.println("No event files...");
            System.out.println();
         }
      }
      else { /*one or more arguments - interactive, simulation or incorrect arguments*/
         if (args[0].equals("-i")) { /*argument is "-i" - interactive mode*/
            Interactive.mainMenu(); /*interactive menu*/
         }
         else if (args[0].equals("-s")) { /*argument is "-s" - simulation mode*/
            if (argc == 5) { /* 5 arguments "-s", netfile, eventile, prob_like, prob_follow*/
               try {
                  Double.parseDouble(args[3]);
                  Double.parseDouble(args[4]);
                  Simulation.run(args); /*calls simulation and passes in probabilities*/
               } catch (NumberFormatException e) {
                  System.out.println("Error in arguments: " + e.getMessage());
               }
            }
            else {
               System.out.println("Incorrect number of arguments");
            }
         }
         else {
            System.out.println("Incorrect argument: " + args[0]);
         }
      }
   }
}
