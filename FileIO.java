import scala.xml.Null;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EmptyStackException;
import java.util.Iterator;

public class FileIO {
   public static void save(String filename, SocialNetwork objToSave) {
      FileOutputStream fileStrm; //Opens file output stream
      ObjectOutputStream objStrm; //Opends object file output stream

      try {
         fileStrm = new FileOutputStream(filename);
         objStrm = new ObjectOutputStream(fileStrm);
         objStrm.writeObject(objToSave); //serializes object
         objStrm.close(); //closes object stream
      } catch (IOException e) {
         e.printStackTrace();
      } catch (Exception e) {
         throw new IllegalArgumentException("Unable to save network to file.");
      }
   }

   //load object
   public static SocialNetwork load(String filename) throws IllegalArgumentException {
      FileInputStream fileStrm;
      ObjectInputStream objStrm;
      SocialNetwork inObj = null;

      try {
         fileStrm = new FileInputStream(filename);
         objStrm = new ObjectInputStream(fileStrm);
         inObj = (SocialNetwork) objStrm.readObject(); //read object in from file
         objStrm.close(); //closes object stream
      } catch (ClassNotFoundException e) {
         System.out.println("Class DSAGraph not found" + e.getMessage());
      } catch (Exception e) {
         throw new IllegalArgumentException("Unable to load object from file.");
      }
      return inObj;
   }

   public static void processSimFile(String fileName, SocialNetwork socialNetwork, String[]... args) {
      FileInputStream fileStrm = null;
      InputStreamReader rdr;
      BufferedReader bufrdr;
      int lineNum = 0; int errors = 0;
      String line;

      LocalTime time = LocalTime.now();
      LocalDate date = LocalDate.now();
      String[] times = time.toString().split(":");
      String logFile = "EventLog_" + date + "_" + times[0] + "-" + times[1] + "-" + times[2] + ".txt";

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
            try {
               if (fileName.contains("NetworkFiles")) {
                  processNetLine(line, socialNetwork);
               } else if (fileName.contains("EventFiles")) {
                  processEventLine(line, args[0], socialNetwork, logFile);
               }
            } catch (FileLineException | KeyNotFoundException | KeyAlreadyExistsException | EmptyGraphException e) {
               System.out.println("Line: " + lineNum + " - " + e.getMessage());
               errors++;
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
      if (errors > 0) {
         System.out.println("Reading " + fileName + " had errors - " + errors + " errors");
      }
      else if (errors == 0) {
         System.out.println("Reading " + fileName + " was successful");
      }
   }

   private static void processNetLine(String csvRow, SocialNetwork socialNetwork)
         throws FileLineException, KeyAlreadyExistsException, KeyNotFoundException, EmptyGraphException {
      String[] tokens;
      tokens = csvRow.split(":");
      if (tokens.length == 1) {
         socialNetwork.addPerson(tokens[0]);
      } else if (tokens.length == 2) {
         socialNetwork.addFollow(tokens[1], tokens[0]);
      } else {
         throw new FileLineException("Error in line format");
      }
   }

   private static void processEventLine(String line, String[] args, SocialNetwork socialNetwork, String logFile)
         throws FileLineException, EmptyGraphException, KeyAlreadyExistsException, KeyNotFoundException {
      String[] tokens;
      tokens = line.split(":");
      int step = 0;


      if (tokens[0].charAt(0) == 'A') {
         if (tokens.length == 2) {
            socialNetwork.addPerson(tokens[1]);
            String message = "NEW PERSON - " + tokens[1];
            fileWrite("./SimLogs/" + logFile, socialNetwork, message);
            socialNetwork.timeStep();
         } else {
            throw new FileLineException("Error in line format");
         }
      } else if (tokens[0].charAt(0) == 'P') {
         if (tokens.length == 3 || tokens.length == 4) {
            double oldLike = socialNetwork.getProb_like();
            if (tokens.length == 4) {
               double newLike = oldLike * Double.parseDouble(tokens[3]);
               socialNetwork.setProb_like(newLike);
            }
            socialNetwork.addPost(tokens[1], tokens[2]);
            boolean quit = false;
            do {
               try {
                  socialNetwork.updateNetwork();
                  Person poster = socialNetwork.getPoster();
                  String message = "POST DISTRIBUTING" + "\n" + "       POST TIME-STEP: " + step
                        + "\n" + "         - Poster: " + poster.getName() + "\n" + "         - Post: " + poster.getCurrentPost().getName();
                  fileWrite("./SimLogs/" + logFile, socialNetwork, message);
                  step++;
                  socialNetwork.timeStep();
               } catch (EmptyNetworkException e) {
                  quit = true;
               } catch (Exception e) {
                  System.out.print(e.getMessage());
               }
            } while (!quit);
            socialNetwork.setProb_like(oldLike);
         }
         else {
            throw new FileLineException("Error in line format");
         }
      } else if (tokens[0].charAt(0) == 'F') {
         if (tokens.length == 3) {
            socialNetwork.addFollow(tokens[2], tokens[1]);
            String message = "NEW FOLLOW - " + tokens[2] + "->" + tokens[1];
            fileWrite("./SimLogs/" + logFile, socialNetwork, message);
            socialNetwork.timeStep();
         } else {
            throw new FileLineException("Error in line format");
         }
      } else if (tokens[0].charAt(0) == 'U')  {
         if (tokens.length == 3) {
            socialNetwork.removeFollow(tokens[2], tokens[1]);
            String message = "UNFOLLOW - " + tokens[2] + "->" + tokens[1];
            fileWrite("./SimLogs/" + logFile, socialNetwork, message);
            socialNetwork.timeStep();
         } else {
            throw new FileLineException("Error in line format");
         }
      } else if (tokens[0].charAt(0) == 'R')  {
         if (tokens.length == 2) {
            socialNetwork.removePerson(tokens[1]);
            String message = "REMOVED PERSON - " + tokens[1];
            fileWrite("./SimLogs/" + logFile, socialNetwork, message);
            socialNetwork.timeStep();
         } else {
            throw new FileLineException("Error in line format");
         }
      } else {
         throw new FileLineException("Error in line format");
      }
   }

   private static void fileWrite(String fileName, SocialNetwork socialNetwork, String message) {
      FileOutputStream fileStrm = null;
      PrintWriter pw;



      try {
         fileStrm = new FileOutputStream(fileName, true);
         pw = new PrintWriter(fileStrm);
         if (socialNetwork.getTimeStep() == 0) {
            pw.println("<>------------------------------------------------------------------------------<>");
            pw.println("                              SIMULATION LOG FILE                                  ");
            pw.println("                         -- PROBABILITY TO LIKE: " + socialNetwork.getProb_like() + "--");
            pw.println("                       -- PROBABILITY TO FOLLOW: " + socialNetwork.getProb_foll() + "--");
         }
         pw.println();
         pw.println("<>-------------- TIME-STEP: " + socialNetwork.getTimeStep() + " -----------------<>");
         pw.println();
         pw.println("-> TOTAL LIKES: " + socialNetwork.getTotalLikes());
         pw.println("-> TOTAL FOLLOWERS: " + socialNetwork.getTotalFollowers());
         pw.println();
         pw.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         pw.println("EVENT: " + message);
         pw.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


         Post[] sortedPosts = socialNetwork.sortedPosts();
         if (sortedPosts != null) {
            pw.println();
            pw.println("<>----- |POSTS| -----<>");
            for (int ii = sortedPosts.length - 1;ii >=0 ; ii--) {
               Post post = (Post) sortedPosts[ii];
               pw.println();
               pw.println(" -> Poster: " + post.getPoster() + "\n     " + " Post: " + post.getName() + "\n      Likes: " + post.getNumLikes());
            }
         } else {
            pw.println();
            pw.println("<>----- |POSTS| -----<>");
            pw.println();
            pw.println("No posts yet...");
         }

         Person[] peopleSort = socialNetwork.sortedPeople();

         pw.println();
         pw.println("<>----- |PEOPLE| -----<>");
         for (int ii = peopleSort.length -1;ii >=0 ; ii--) {
            Person person = peopleSort[ii];
            pw.println();
            pw.println(" -> Name: " + person.getName() + "\n       Followers: " + person.getNumFollowers());
         }

         pw.close();
      } catch (IOException | EmptyGraphException e) {
         if (fileStrm != null) {
            try {
               fileStrm.close();
            } catch (IOException ex2) {
            }
         }
         System.out.println("Error in writing to file: " + e.getMessage());
      }
   }
}