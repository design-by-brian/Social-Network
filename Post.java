/*******************************************
 * AUTHOR: Brian Smith
 * FILENAME: Post.java
 * DATE_CREATED: 10/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for managing a Post object. Stores, name of post, name of poster and numLikes.
 ********************************************/
import java.io.Serializable;

public class Post implements Serializable {
   private String name;
   private String poster;
   private int numLikes;

   public Post(String poster, String name) {
      this.poster = poster;
      this.name = name;
      numLikes = 0;
   }

   //GETTERS AND SETTERS
   public String getName() {
      return name;
   }

   public String getPoster() {
      return poster;
   }

   public int getNumLikes() {
      return numLikes;
   }

   public void addLike() {
      numLikes++;
   }

   //If all class fields are equal then the objects must be equal.
   public boolean equals(Post post) {
      boolean equals = false;
      if (this.name.equals(post.getName())) {
         if (this.poster.equals(post.getPoster())) {
            if (this.numLikes == post.getNumLikes()) {
               equals = true;
            }
         }
      }
      return equals;
   }
}
