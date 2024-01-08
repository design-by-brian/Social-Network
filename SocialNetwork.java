/*******************************************
 * AUTHOR: Brian Smith
 * FILENAME: DSASocialNetwork.java
 * DATE_CREATED: 10/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for managing a basic social-network. Stores probabilities to follow and like, a network (Graph) for
 * connections and storing people. Along with a Queue storing people currently viewing a post to track its progress
 * through the network. Posts are also stored for displaying in order of likes.
 ********************************************/
import java.io.Serializable;
import java.util.Iterator;

public class SocialNetwork implements Serializable {
   private DSAGraph network;
   private DSALinkedList posts; //posts made in the network
   private DSAQueue currentViewing;
   private Person poster; /*if null no current post*/
   private double prob_like;
   private double prob_foll;
   private int timeStep; /* how many events have occured*/
   private int totalFollowers;
   private int totalLikes;

   public SocialNetwork(double prob_like, double prob_foll) {
      this.network = new DSAGraph(20,30);
      this.posts = new DSALinkedList();
      this.currentViewing = new DSAQueue();
      this.prob_like = prob_like;
      this.prob_foll = prob_foll;
      this.timeStep = 0;
      this.totalFollowers = 0;
      this.totalLikes = 0;
   }

   //GETTERS AND SETTERS
   public DSAGraph getGraph() {
      return this.network;
   }

   public void timeStep() {
      timeStep++;
   }

   public int getTimeStep() {
      return this.timeStep;
   }

   public double getProb_like() {
      return this.prob_like;
   }

   public double getProb_foll() {
      return prob_foll;
   }

   public int getTotalLikes() {
      return totalLikes;
   }

   public int getTotalFollowers() { return this.totalFollowers; }

   public Person getPoster() {
      return this.poster;
   }

   public void setProb_like(double prob_like) {
      this.prob_like = prob_like;
   }

   public void setProb_foll(double prob_foll) {
      this.prob_foll = prob_foll;
   }

   /* METHOD: addPerson
    * IMPORT: String
    * EXPORT: void
    * PURPOSE: Adds a person given a name to the network
    */
   public void addPerson(String name) throws KeyAlreadyExistsException {
      network.addVertex(name, new Person(name));
   }

   /* METHOD: removePerson
    * IMPORT: String
    * EXPORT: void
    * PURPOSE: Removes a person from the network. First removing any follows referencing them.
    */
   public void removePerson(String name) throws KeyNotFoundException, EmptyGraphException {
      DSALinkedList adjacentTo = network.getAdjacentTo(name);
      DSALinkedList adjacentFrom = network.getAdjacentFrom(name);
      Iterator iterTo = adjacentTo.iterator();
      Iterator iterFrom = adjacentFrom.iterator();

      while (iterTo.hasNext()) {
         String followee = ((Person) iterTo.next()).getName();
         if (network.hasEdge(name, followee)) {
            network.removeEdge(name, followee);
            totalFollowers--;
         }
      }

      while (iterFrom.hasNext()) {
         String follower = ((Person) iterFrom.next()).getName();
         if (network.hasEdge(follower, name)) {
            network.removeEdge(follower, name);
            totalFollowers--;
         }
      }
      network.removeVertex(name);
   }

   /* METHOD: addFollow
    * IMPORT: String, String
    * EXPORT: void
    * PURPOSE: Adds a follow to the network
    */
   public void addFollow(String follower, String followee) throws EmptyGraphException, KeyAlreadyExistsException, KeyNotFoundException {
      this.getPerson(follower).addFollowing();
      this.getPerson(followee).addFollower();
      network.addEdge(follower, followee);
      totalFollowers++;
   }

   /* METHOD: removeFollow
    * IMPORT: String, String
    * EXPORT: void
    * PURPOSE: Removes a follow from the network
    */
   public void removeFollow(String follower, String followee) throws KeyNotFoundException, EmptyGraphException {
      network.removeEdge(follower, followee);
      totalFollowers--;
   }

   /* METHOD: addPost
    * IMPORT: String, String
    * EXPORT: void
    * PURPOSE: Adds a post to the network, which can be distributed through the network. Only one post can be active
    * at a time.
    */
   public void addPost(String poster, String post) throws KeyNotFoundException, EmptyGraphException {
      if (this.poster == null) {
         this.poster = (Person) network.getVertexValue(poster);
         Post newPost = new Post(poster, post);
         this.poster.setCurrentPost(newPost);
         currentViewing.enqueue(this.poster);
      } else {
         System.out.println("Current post still being distributed - update first");
      }
   }

   public Person getPerson(String name) throws KeyNotFoundException, EmptyGraphException {
      return (Person) network.getVertexValue(name);
   }

   /* METHOD: sortedPosts
    * IMPORT: void
    * EXPORT: DSAHeapEntry[]
    * PURPOSE: Returns a sorted array of DSAHeapEntries posts.
    */


   public Post[] sortedPosts() {
      Post[] postSort = null;
      if (posts.getNumElements() > 0) {
         postSort = new Post[posts.getNumElements()];
         Iterator iter = posts.iterator();
         for (int ii = 0;ii < posts.getNumElements() ; ii++) {
            Post post = (Post) iter.next();
            postSort[ii] = post;
         }
         quickSort(postSort);
      }
      return postSort;
   }

   /* METHOD: sortedPeople
    * IMPORT: void
    * EXPORT: DSAHeapEntry[]
    * PURPOSE: Returns a sorted array of DSAHeapEntries people.
    */
   public Person[] sortedPeople() throws EmptyGraphException {
      Person[] peopleSort = null;
      Object[] people = network.getVertexValues();
      if (people.length > 0) {
         peopleSort = new Person[people.length];
         for (int ii = 0; ii < people.length; ii++) {
            peopleSort[ii] = (Person) people[ii];
         }
         quickSort(peopleSort);
      }
      return peopleSort;
   }

   public void displayList() throws EmptyGraphException {
      network.displayList();
   }

   /* METHOD: distributePost
    * IMPORT: void
    * EXPORT: void
    * PURPOSE: Propagates post through the network until it can go anywhere else.
    */
   public void distributePost() throws EmptyGraphException, KeyAlreadyExistsException, KeyNotFoundException, EmptyNetworkException {
      if (currentViewing.isEmpty()) { //No current post
         throw new EmptyNetworkException("Network up to date - Post distributed");
      }
      while (!(currentViewing.isEmpty()) && poster != null) { /*post in network and poster is not null*/
         try {
            updateNetwork();
         } catch (EmptyNetworkException | EmptyListException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   /* METHOD: updateNetwork
    * IMPORT: void
    * EXPORT: Post
    * PURPOSE: Propagates post through the network one step/degree of serperation at a time.
    */
   public Post updateNetwork() throws EmptyGraphException, KeyAlreadyExistsException, KeyNotFoundException, EmptyNetworkException, EmptyListException {
      Post currentPost = null;
      if (currentViewing.getNumElements() != 0) { //if there is a post available.
         int numViewing = currentViewing.getNumElements();
         for (int ii = 0; ii < numViewing; ii++) { //loop though number of people on this 'level' of viewing
            Person person = (Person) currentViewing.dequeue(); //remove person at front of the queue.
            currentPost = poster.getCurrentPost();
            DSALinkedList followers = network.getAdjacentFrom(person.getName()); /*get persons followers*/
            if (!followers.isEmpty()) { /*if person has followers*/
               Iterator iterMain = followers.iterator();
               while (iterMain.hasNext()) { /*loops through all followers*/
                  Person follower = (Person) iterMain.next();
                  if (!follower.equals(poster) && prob(prob_like) && !(follower.hasCurrentPost(currentPost))) { /*if its not the poster*/
                     currentPost.addLike();
                     totalLikes++;
                     follower.setCurrentPost(currentPost);
                     currentViewing.enqueue(follower); //add follower to the back of the queue
                     if (!network.hasEdge(follower.getName(), poster.getName()) && prob(prob_foll)) { /*if they arent already following*/
                        addFollow(follower.getName(), poster.getName());
                        poster.addFollower();
                        follower.addFollowing();
                     }
                  }
               }
            }
         }

      }
      else if (poster != null){ //if none left to view, add the post to the the posts. set poster to null.
         Post post = poster.getCurrentPost();
         poster.addPost(); /*add distributed post to poster*/
         posts.insertLast(post); /*add post to network posts*/
         this.poster = null;
         throw new EmptyNetworkException("Network up to date - Post distributed");
      }
      else {
         throw new EmptyNetworkException("Network up to date - Post distributed");
      }
      return currentPost;
   }

   private static boolean prob(double probability) {
      return (Math.random() <= probability);
   }

   /* METHOD: quickSort
    * IMPORT: Object[]
    * EXPORT: void
    * PURPOSE: Sorts an array of objects. Used here specfically fro sorting people by follwers and posts by likes.
    */

   /*
    * This code was submitted by me Brian Smith for practical 8. The following three methods have been reused from this
    * practical submission. They are a quick sort implementation based on the pseudo code provided in lecture 8.
    */
   private static void quickSort(Object[] A) {
      quickSortRecurse(A, 0, A.length - 1);
   }//quickSort()

   private static void quickSortRecurse(Object[] A, int leftIdx, int rightIdx)
   {
      int pivotIdx, newPivotIdx = 0;

      if(rightIdx > leftIdx) {
         pivotIdx = (leftIdx + rightIdx) / 2;
         if (A instanceof Person[]) {
            newPivotIdx = doPartitioning((Person[]) A, leftIdx, rightIdx, pivotIdx);
         }
         if (A instanceof Post[]) {
            newPivotIdx = doPartitioning((Post[]) A, leftIdx, rightIdx, pivotIdx);
         }

         quickSortRecurse(A, leftIdx, newPivotIdx - 1);
         quickSortRecurse(A, newPivotIdx + 1, rightIdx);
      }
   }//quickSortRecurse()

   private static int doPartitioning(Person[] A, int leftIdx, int rightIdx, int pivotIdx)
   {
      Person pivotVal, temp;
      int currIdx, newPivIdx;

      pivotVal = A[pivotIdx];
      A[pivotIdx] = A[rightIdx];
      A[rightIdx] = pivotVal;

      currIdx = leftIdx;

      for(int ii = leftIdx; ii < rightIdx; ii++) {
         if(A[ii].getNumFollowers() < pivotVal.getNumFollowers()) {
            temp = A[ii];
            A[ii] = A[currIdx];
            A[currIdx] = temp;
            currIdx++;
         }
      }

      newPivIdx = currIdx;
      A[rightIdx] = A[newPivIdx];
      A[newPivIdx] = pivotVal;
      return newPivIdx;	// TEMP - Replace this when you implement QuickSort
   }//doPartitioning

   private static int doPartitioning(Post[] A, int leftIdx, int rightIdx, int pivotIdx)
   {
      Post pivotVal,temp;
      int currIdx, newPivIdx;

      pivotVal = A[pivotIdx];
      A[pivotIdx] = A[rightIdx];
      A[rightIdx] = pivotVal;

      currIdx = leftIdx;

      for(int ii = leftIdx; ii < rightIdx; ii++) {
         if(A[ii].getNumLikes() < pivotVal.getNumLikes()) {
            temp = A[ii];
            A[ii] = A[currIdx];
            A[currIdx] = temp;
            currIdx++;
         }
      }

      newPivIdx = currIdx;
      A[rightIdx] = A[newPivIdx];
      A[newPivIdx] = pivotVal;
      return newPivIdx;	// TEMP - Replace this when you implement QuickSort
   }
}
