/*******************************************
 * AUTHOR: Brian Smith
 * FILENAME: Person.java
 * DATE_CREATED: 10/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for managing a person object. Stores name, current post (viewing), num folls, num follwing, num posts.
 ********************************************/
import java.io.Serializable;

public class Person implements Serializable {
   private String name;
   private Post currentPost;
   private int numFollowers;
   private int numFollowing;
   private int numPosts;

   public Person(String name) {
      this.name = name;
      currentPost = null;
      numFollowers = 0;
      numFollowing = 0;
      numPosts = 0;
   }

   public boolean hasCurrentPost(Post post) {
      boolean has = false;
      if (currentPost != null) {
         has = this.currentPost.equals(post);
      }
      return has;
   }

   //GETTERS SETTERS
   public void addPost() {
      numPosts++;
   }

   public String getName() {
      return name;
   }

   public Post getCurrentPost() {
      return currentPost;
   }

   public void setCurrentPost(Post currentPost) {
      this.currentPost = currentPost;
   }

   public int getNumFollowers() {
      return numFollowers;
   }

   public int getNumFollowing() {
      return numFollowing;
   }

   public int getNumPosts() {
      return numPosts;
   }

   public void addFollower() {
      numFollowers++;
   }

   public void addFollowing() {
      numFollowing++;
   }
}
