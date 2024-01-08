/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "Interactive.java"
 * DATE_CREATED: 15/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for running the user interactive network testing area. Allows user to load in network, add/remove
 * people/follows, display the graph and statistics.
 ***********************************************/
import java.util.*;

public class Interactive {
   public static void mainMenu() {
      SocialNetwork interactiveNetwork = new SocialNetwork(1, 1);

      int selection = 0;
      System.out.println("    Interactive Mode - Main Menu   ");
      System.out.println("-----------------------------------");
      System.out.println("(0) Display main menu");
      System.out.println("(1) Load network");
      System.out.println("(2) Set probabilities");
      System.out.println("(3) Add/Remove person");
      System.out.println("(4) Add/Remove follow");
      System.out.println("(5) New post");
      System.out.println("(6) Display network");
      System.out.println("(7) Display statistics");
      System.out.println("(8) Update");
      System.out.println("(9) Save network");
      System.out.println("(10) Exit");

      while (selection != 10) {

         selection = inputInt("Selection: ", "ERROR: Not a valid integer");

         switch (selection) {
            case 0:
               System.out.println("    Interactive Mode - Main Menu   ");
               System.out.println("-----------------------------------");
               System.out.println("(1) Load network");
               System.out.println("(2) Set probabilities");
               System.out.println("(3) Add/Remove person");
               System.out.println("(4) Add/Remove follow");
               System.out.println("(5) New post");
               System.out.println("(6) Display network");
               System.out.println("(7) Display statistics");
               System.out.println("(8) Update");
               System.out.println("(9) Serialize network (save/load)");
               System.out.println("(10) Exit");
               break;
            case 1:
               String fileName = inputString("Filename: ");
               FileIO.processSimFile("./InputFiles/NetworkFiles/" + fileName, interactiveNetwork);
               break;
            case 2:
               changeProbabilities(interactiveNetwork);
               break;
            case 3:
               nodeOperations(interactiveNetwork);
               break;
            case 4:
               edgeOperations(interactiveNetwork);
               break;
            case 5:
               addPost(interactiveNetwork);
               break;
            case 6:
               displayGraph(interactiveNetwork);
               break;
            case 7:
               viewStatistics(interactiveNetwork);
               break;
            case 8:
               updateNetwork(interactiveNetwork);
               break;
            case 9:
               serializeNetwork(interactiveNetwork);
               break;
            case 10:
               System.out.println("Good-bye");
               break;
            default:
               System.out.println("Invalid entry " + "'" + selection + "'");
         }
      }
   }

   /* METHOD: displayGraph
    * IMPORT: SocialNetwork
    * EXPORT: void
    * PURPOSE: Allows the user to display the network either as an adjacency list or a visual representation.
    */
   private static void displayGraph(SocialNetwork interactiveNetwork) {
      int choiceI;
      do {
         System.out.println("    Interactive Mode - Display Network   ");
         System.out.println("-----------------------------------------");
         System.out.println(" //-- Enter 0 to return to main menu --//");
         System.out.println("(1) Display as an adjacency list");
         System.out.println("(2) Display as a visual graph");
         choiceI = Interactive.inputInt("Selection: ", "ERROR: not a valid integer");
         switch (choiceI) {
            case 1:
               try {
                  interactiveNetwork.displayList();
               } catch (EmptyGraphException e) {
                  System.out.println("Empty graph - cannot display list");
               }
               break;
            case 2:
               try {
                  GraphStream.visualiseGraph(interactiveNetwork);
               } catch (EmptyGraphException e) {
                  System.out.println("Empty graph - cannot display visual");
               }
               break;
            default:
               System.out.println("Returning to main menu");
         }
      } while (choiceI != 0);
   }

   /* METHOD: nodeOperations
    * IMPORT: SocialNetwork
    * EXPORT: void
    * PURPOSE: Allows the user to Add/Remove a person from the network, gracefully handles incorrect entries.
    */
   private static void nodeOperations(SocialNetwork interactiveNetwork) {
      System.out.println("Add/Remove person...");
      System.out.println("Add (A) or Remove (R)?");
      char choiceC = inputChar("Selection: ");
      switch (choiceC) {
         case 'a':
            System.out.println("Adding person to the network...");
            String name = inputString("Enter name: ");
            try {
               interactiveNetwork.addPerson(name);
            } catch (KeyAlreadyExistsException e) {
               System.out.println(name + " is already in the network");
            }
            break;
         case'r':
            System.out.println("Removing person from the network...");
            name = inputString("Enter name: ");
            try {
               interactiveNetwork.removePerson(name);
            } catch (EmptyGraphException | KeyNotFoundException e) {
               System.out.println(e.getMessage());
            }
            break;
      }
   }

   /* METHOD: edgeOperations
    * IMPORT: SocialNetwork
    * EXPORT: void
    * PURPOSE: Allows users to Add/Remove follows from the network. Gracefully handles incorrect entries.
    */
   private static void edgeOperations(SocialNetwork interactiveNetwork) {
      char choiceC;
      System.out.println("Add/Remove follow...");
      System.out.println("Add (A) or Remove (R)?");
      choiceC = inputChar("Selection: ");
      switch (choiceC) {
         case 'a':
            System.out.println("Adding new follow to the network...");
            String follower = inputString("Enter follower: ");
            String followee = inputString("Enter followee: ");
            try {
               interactiveNetwork.addFollow(follower, followee);
               System.out.println(follower + " is now following " + followee);
            } catch (KeyNotFoundException e) {
               System.out.println("One or more of the people are not in the network");
            } catch (EmptyGraphException e) {
               System.out.println("Graph empty - cannot add follow");
            } catch (KeyAlreadyExistsException e) {
               System.out.println(follower + " is already follwing " + followee);
            }
            break;
         case'r':
            System.out.println("Removing follow in the network...");
            follower = inputString("Enter follower: ");
            followee = inputString("Enter followee: ");
            try {
               interactiveNetwork.removeFollow(follower, followee);
               System.out.println(follower + " is not following " + followee);
            } catch (KeyNotFoundException e) {
               System.out.println("Follow does not exist in network");
            } catch (EmptyGraphException e) {
               System.out.println("Graph empty - cannot add follow");
            }
            break;
      }
   }

   /* METHOD: addPost
    * IMPORT: SocialNetwork
    * EXPORT: void
    * PURPOSE: Allows the user to Add a post to the network to be distibuted. Will not allow another if the current
    * has not propagated.
    */
   private static void addPost(SocialNetwork interactiveNetwork) {
      System.out.println("Adding new post to the network...");
      String name = inputString("Enter name of poster: ");
      String post = inputString("Enter post content: ");
      Person poster = null;
      try {
         interactiveNetwork.addPost(name, post);
      } catch (EmptyGraphException e) {
         System.out.println(e.getMessage());
      } catch (KeyNotFoundException e) {
         System.out.println("Person " + name + " is not in the network");
      }
   }

   /* METHOD: viewStatistics
    * IMPORT: SocialNetwork
    * EXPORT: void
    * PURPOSE: Allows the user to view major statistics. Users by followers, posts by likes and an individuals top
    * statistics.
    */
   private static void viewStatistics(SocialNetwork interactiveNetwork) {
      int choiceI;
      do {
         System.out.println("    Interactive Mode - View Statistics   ");
         System.out.println("-----------------------------------------");
         System.out.println(" //-- Enter 0 to return to main menu --//");
         System.out.println("(1) Posts by popularity");
         System.out.println("(2) People by popularity");
         System.out.println("(3) User records");
         choiceI = inputInt("Selection: ", "ERROR: not a valid integer");
         switch (choiceI) {
            case 1:
               try {
                  Post[] sortedPosts = interactiveNetwork.sortedPosts();
                  for (int ii = sortedPosts.length - 1; ii >= 0; ii--) {
                     Post post = sortedPosts[ii];
                     System.out.println("Post: " + post.getName());
                     System.out.println("    Likes: " + post.getNumLikes());
                  }
               } catch (EmptyStackException | NullPointerException e) {
                  System.out.println("No posts have been made");
               }
               break;
            case 2:
               try {
                  Person[] peopleSort = interactiveNetwork.sortedPeople();

                  for (int ii = peopleSort.length - 1; ii >= 0; ii--) {
                     Person person = peopleSort[ii];
                     System.out.println("Name: " + person.getName() + "\n" + "   Followers: " + person.getNumFollowers());
                  }
               } catch (EmptyGraphException | NullPointerException e) {
                  System.out.println("No people have been added");
               }
               break;
            case 3:
               String name = inputString("Enter name: ");
               try {
                  Person person = interactiveNetwork.getPerson(name);
                  System.out.println("Number of posts: " + person.getNumPosts());
                  System.out.println("Number of followers: " + person.getNumFollowers());
                  System.out.println("Number of following: " + person.getNumFollowing());
               } catch (KeyNotFoundException e) {
                  System.out.println(name + " is not in the network");
               } catch (EmptyGraphException e) {
                  System.out.println("No people have been added");
               }
               break;
            default:
               System.out.println("Returning to main menu");
         }
      } while (choiceI != 0);
   }

   private static void changeProbabilities(SocialNetwork interactiveNetwork) {
      int choiceI;
      boolean quit = false;
      do {
         System.out.println("    Interactive Mode - Change Probabilities   ");
         System.out.println("----------------------------------------------");
         System.out.println("   //-- Enter 0 to return to main menu --//");
         System.out.println("(1) Change probability of liking");
         System.out.println("(2) Change probability of following");
         choiceI = Interactive.inputInt("Selection: ", "ERROR: not a valid integer");
         switch (choiceI) {
            case 1:
               interactiveNetwork.setProb_like(inputDouble("Probability to like: ", "ERROR: Not a valid real number (0-1)"));
               break;
            case 2:
               interactiveNetwork.setProb_foll(inputDouble("Probability to follow: ", "ERROR: Not a valid real number (0-1)"));
               break;
            case 0:
               System.out.println("Returning to main menu");
               quit = true;
               break;
            default:
               System.out.println("Invalid entry");
         }
      } while (!quit);
   }

   private static void updateNetwork(SocialNetwork interactiveNetwork) {
      try {
         interactiveNetwork.updateNetwork(); //steps the post one degree of separation
         System.out.println("Updating network");
      } catch (KeyAlreadyExistsException | KeyNotFoundException e) {
         e.printStackTrace();
      } catch (EmptyNetworkException | EmptyListException | EmptyGraphException e) {
         System.out.println(e.getMessage());
      }
   }

   private static void serializeNetwork(SocialNetwork interactiveNetwork) {
      boolean quit = false;
      do {
         System.out.println("    Interactive Mode - Save/Load Network   ");
         System.out.println("----------------------------------------------");
         System.out.println("   //-- Enter 0 to return to main menu --//");
         System.out.println("(1) Save network (serialized file)");
         System.out.println("(2) Load network (serialized file)");
         int choiceI = inputInt("Selection: ", "ERROR: not a valid integer");
         switch (choiceI) {
            case 1:
               String fileName = inputString("Filename: ");
               FileIO.save(fileName, interactiveNetwork);
               break;
            case 2:
               fileName = inputString("Filename: ");
               interactiveNetwork = FileIO.load(fileName);
               break;
            case 0:
               System.out.println("Returning to main menu");
               quit = true;
               break;
            default:
               System.out.println("Invalid entry");
         }
      } while (!quit);
   }

   private static int inputInt(String prompt, String error) {
      Scanner sc = new Scanner(System.in);
      int selection = 0; boolean success = true;

      do {
         System.out.print(prompt);
         try {
            selection = sc.nextInt();
            success = true;
         } catch (InputMismatchException e) {
            sc.next();
            success = false;
         }
         prompt = error + "\n" + prompt;
      } while (!success);
      return selection;
   }

   private static double inputDouble(String prompt, String error) {
      Scanner sc = new Scanner(System.in);
      double selection = 0; boolean success = true;

      do {
         System.out.print(prompt);
         try {
            selection = sc.nextDouble();
            success = true;
         } catch (InputMismatchException e) {
            sc.next();
            success = false;
         }
         prompt = error + "\n" + prompt;
      } while (!success || selection < 0 || selection > 1);
      return selection;
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

   private static char inputChar(String prompt) {
      Scanner sc = new Scanner(System.in);
      char input = ' '; boolean success = true;

      do {
         System.out.print(prompt);
         try {
            input = sc.next().charAt(0);
            input = Character.toLowerCase(input);
            if (input == 'a' || input == 'r') {
               success = true;
            }
         } catch (InputMismatchException e) {
            sc.next();
            success = false;
         }
         prompt = "\nERROR: Please enter a file name\nFilename: ";
      } while (!success);
      return input;
   }
}