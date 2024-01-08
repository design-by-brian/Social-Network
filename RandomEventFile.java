/*******************************************
 * AUTHOR: Brian Smith
 * FILENAME: randomEventFile.java
 * DATE_CREATED: 27/09/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: This class conatins a main method for reading in a network file and creating a dummy event file based on
 * the contents. This can be used well with the simulation part of the network. Names can be added to networkNames1, this
 * is what the program reads in. The event file created is save to eventsRandom.txt in EventFiles.
 ********************************************/

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RandomEventFile {
   public static void main(String[] args) {
      String[] names = new String[100]; //size of input network
      readNames("./InputFiles/NetworkFiles/networkNames1.txt", names);
      createNetworkFile("./InputFiles/NetworkFiles/networkNames1.txt", names);
      createEventFile("./InputFiles/EventFiles/eventsRandom.txt", names);
   }
   private static void createNetworkFile(String fileName, String[] names) {
      FileOutputStream fileStrm = null;
      PrintWriter pw;
      try {
         fileStrm = new FileOutputStream(fileName);
         pw = new PrintWriter(fileStrm);
         for (int ii = 0; ii < names.length; ii++) {
            pw.println(names[ii]);
         }
         for (int ii = 0; ii < names.length-1; ii++) {
            pw.println(names[ii] + ":" + names[(int) ((double) (names.length-1) * Math.random())]);
            pw.println(names[(int) ((double) (names.length-1) * Math.random())] + ":" + names[ii]);
         }
         pw.close();
      } catch (IOException e) {
         if (fileStrm != null) {
            try {
               fileStrm.close();
            } catch (IOException ex2) {
            }
         }
         System.out.println("Error in writing to file: " + e.getMessage());
      }
   }

   private static void createEventFile(String fileName, String[] names) {
      FileOutputStream fileStrm = null;
      PrintWriter pw;
      try {
         fileStrm = new FileOutputStream(fileName);
         pw = new PrintWriter(fileStrm);
         for (int ii = 0; ii < 20; ii++) {
            int event = (int) (Math.random() * 20);
            if (event < 5) {//remove
               pw.println("R:" + names[ii]);
            }  else if (event < 15){//post
               pw.println("P:" + names[ii] + ":" + "random_post_" + (int) Math.random()*300);
            } else if (event < 20) {
               pw.println("F:" + names[ii] + ":" + names[(int) ((double) (names.length-1) * Math.random())]);
            }
         }
         pw.close();
      } catch (IOException e) {
         if (fileStrm != null) {
            try {
               fileStrm.close();
            } catch (IOException ex2) {
            }
         }
         System.out.println("Error in writing to file: " + e.getMessage());
      }
   }

   private static void readNames(String fileName, String[] names) {
      FileInputStream fileStrm = null;
      InputStreamReader rdr;
      BufferedReader bufrdr;
      int lineNum = 0; int errors = 0;
      String line;

      try {
         fileStrm = new FileInputStream(fileName);
         rdr = new InputStreamReader(fileStrm);
         bufrdr = new BufferedReader(rdr);

         line = bufrdr.readLine();
         if (line == null) {
            System.out.println(fileName + " is an empty file");
            errors = -1;
         }
         while (line != null) {
            lineNum++;
	    if (lineNum < names.length) {
               names[lineNum-1] = line;
	    }
            line = bufrdr.readLine();
         }

         fileStrm.close();
      } catch (IOException e) {
         if (fileStrm != null) {
            try {
               fileStrm.close();
            } catch (IOException ex2) {}
         }
         System.out.println("Error in processing: " + e.getMessage());
         errors = -1;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static String inputString(String prompt) {
      Scanner sc = new Scanner(System.in);
      String input = ""; boolean success = true;

      do {
         System.out.print(prompt);
         try {
            input = sc.nextLine();
            success = true;
         } catch (InputMismatchException e) {
            sc.next();
            success = false;
         }
         prompt = "\nERROR: Please enter a file name\nFilename: ";
      } while (!success);
      return input;
   }
}
