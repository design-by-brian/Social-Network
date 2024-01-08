/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "GraphStream.java"
 * DATE_CREATED: 15/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Class for displaying a network/graph. Using an external library GraphStream from: http://graphstream-project.org/
 * Code has been written entirely by me (Brian Smith -19463540) using the help of tutorials and discussion provided by
 * the GraphStream documentation. No verbatim code used.
 *
 * Visualisation stabilisation rendering quality: http://graphstream-project.org/doc/FAQ/Attributes/Is-there-a-list-of-attributes-with-a-predefined-meaning-for-the-layout-algorithms/
 * Visualisation styling and layout information: http://graphstream-project.org/doc/Advanced-Concepts/GraphStream-CSS-Reference/
 * http://graphstream-project.org/doc/FAQ/The-graph-viewer/How-do-I-dynamically-change-color-and-size-in-the-viewer/
 * Visualisation fundamentals (creating and using graph provided by GraphStream, using viewer): http://graphstream-project.org/doc/Tutorials/Getting-Started/
 ***********************************************/

import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class GraphStream {
   public static void visualiseGraph(SocialNetwork socialNetwork) throws EmptyGraphException {
      Graph visualise_network = new MultiGraph("Social_Network"); //Creates a new multigraph (edges) called Social_Network
      //allows the use of J2Renderer
      System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
      //Enables the use of my custom stylesheet
      visualise_network.addAttribute("ui.stylesheet", "url(./GraphStreamLib/stylesheet.css)"); //Enables the use of my custom stylesheet
      //Determines how well in equilibrium the graph should be (0-4), 4 is highest
      visualise_network.addAttribute("layout.quality", 3);
      visualise_network.addAttribute("ui.antialias");
      //Determines how long to try and find equilbiruim. Max 1.1. Anything over 1 seems to take too long.
      visualise_network.addAttribute("layout.stabilization-limit", 0.999);

      DSAGraph simGraph = socialNetwork.getGraph(); //retrieves graph input to visualise

      //gets all people from the graph and order them by followers.
      Person[] peopleSort = socialNetwork.sortedPeople();

      Person top = (Person) peopleSort[peopleSort.length - 1]; //most followers

      double maxFoll = top.getNumFollowers();
      double maxSize = 10; //max size of node.

      Object[] people = simGraph.getVertexValues();

      for (int ii = 0; ii < people.length; ii++) {
         Person person = (Person) people[ii];
         int weight = person.getNumFollowers(); //weight is their followers

         visualise_network.addNode(person.getName());
         Node node = visualise_network.getNode(person.getName());
         node.addAttribute("ui.label", person.getName()); //sets the label for the node
         //determines how far to push away neighbour nodes. The  more followers the less they push away. This forces
         //most popular to the inside of the graph and lower follower nodes to the outside (easier to read).
         node.addAttribute("layout.weight", (maxFoll / ((double) weight + 1.0) + 40.0));
         //color ranges from green to red. The more followers the more red.
         node.addAttribute("ui.color", ((double) weight / maxFoll));
         //Size of node scales with num followers as well. More followers, bigger the node. Min size is 6px
         int size = (int) (((double) weight / maxFoll) * maxSize);
         node.addAttribute("ui.size", size + 6);
      }

      String[] follows = simGraph.getEdgeKeys(); // get all the edges from the input graph.

      for (int ii = 0; ii < follows.length; ii++) {
         String[] node = follows[ii].split("->");
         try {
            //adds edges to the network. Boolean true enables directed edges.
            visualise_network.addEdge(node[0] + "->" + node[1], node[0], node[1], true); //adds node to the GraphStream graph
            //adds weight to the edge, multiplying teh default length.
            visualise_network.getEdge(node[0] + "->" + node[1]).addAttribute("layout.weight", 6);
         } catch (ElementNotFoundException e) {
            System.out.println();
         }
      }

      Viewer viewer = visualise_network.display();
      viewer.enableAutoLayout();
   }
}
