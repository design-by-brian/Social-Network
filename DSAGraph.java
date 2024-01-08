/**
 * THE FOLLOWING CLASS HAS BEEN REUSED AND EXTENDED FROM DSAGraph.java
 * SUBMITTED IN PRACTICAL 5 BY ME (BRIAN SMITH - 19463540)
 */

/*******************************************
 * AUTHOR: Brian Smith
 * FILENAME: DSAGraph.java
 * DATE_CREATED: 11/09/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: class for managing a generic graph. The following graph has two private inner-classes DSAGraphVertex and
 * DSAGraphEdge. Vertex stores a label, value . Edge stores vertexFrom and vertexTo. Overall class DSAGraph
 * REQUIRES: none
 ********************************************/

import java.io.Serializable;
import java.util.*;

public class DSAGraph implements Serializable {
    /*----------------------------------------------------------*/
    /*--------------------  GRAPH VERTEX -----------------------*/
    /*----------------------------------------------------------*/
    private class DSAGraphVertex implements Serializable  {
        private String label;
        private Object value;
        private DSALinkedList edgeList;

        public DSAGraphVertex(String label, Object value) {
            this.label = label; /*key to find vertex*/
            this.value = value; /*some object stored in the vertex*/
            edgeList = new DSALinkedList(); /*list of adjacent edges*/
        }

        public String getLabel() {
            return label;
        }

        public Object getValue() { return value; }

        /* METHOD: getAdjacentE
         * IMPORT: void
         * EXPORT: DSALinkedList
         * PURPOSE: Returns a copy of the edgeList (adjacent Edges of a vertex)
         */
        public DSALinkedList getAdjacentE() {
            return edgeList;
        }

        public void addEdge(DSAGraphEdge edge) {
            edgeList.insertLast(edge);
        }
    }

    /*----------------------------------------------------------*/
    /*--------------------  GRAPH EDGE -------------------------*/
    /*----------------------------------------------------------*/
    private class DSAGraphEdge implements Serializable {
        private String label; /*key to find edge*/
        private DSAGraphVertex vertexFrom;
        private DSAGraphVertex vertexTo;

        public DSAGraphEdge(DSAGraphVertex vertexFrom, DSAGraphVertex vertexTo, String label) {
            this.vertexFrom = vertexFrom;
            this.vertexTo = vertexTo;
            this.label = label;
        }

        public DSAGraphVertex getVertexTo() {
            return vertexTo;
        }

        public DSAGraphVertex getVertexFrom() {
            return vertexFrom;
        }

        public String getLabel() {
            return label;
        }
    }

    /*----------------------------------------------------------*/
    /*--------------------  OVERALL GRAPH  ---------------------*/
    /*----------------------------------------------------------*/
    private DSAHashTable vertices; /*people - using hashtable because specific people need to be accessed a lot*/
    private DSAHashTable edges; /*follows*/
    private int vertexCount;
    private int edgeCount;

    public DSAGraph(int numVertices, int numEdges) {
        vertices = new DSAHashTable(numVertices);
        edges = new DSAHashTable(numEdges);
        vertexCount = 0;
        edgeCount = 0;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    /* METHOD: addVertex
     * IMPORT: String label
     * EXPORT: void
     * PURPOSE: creates the address of a new DSAGraphVertex object
     */
    public void addVertex(String label, Object value) throws KeyAlreadyExistsException {
        DSAGraphVertex vertex = new DSAGraphVertex(label, value);
        vertices.put(label, vertex);
        vertexCount++;
    }

    /* METHOD: addEdge
     * IMPORT: String label1, String label2
     * EXPORT: void
     * PURPOSE: Adds and edge to the graph object
     */
    public void addEdge(String label1, String label2) throws EmptyGraphException, KeyNotFoundException, KeyAlreadyExistsException {
        if (!(label1.equals(label2))) { //if label1 is label2
            if (hasVertex(label1) && hasVertex(label2)) { //if both labels exist
                DSAGraphVertex v1 = getVertex(label1);
                DSAGraphVertex v2 = getVertex(label2);
                DSAGraphEdge newEdge = new DSAGraphEdge(v1, v2, label1 + "->" + label2);
                edges.put(newEdge.getLabel(), newEdge);
                v1.addEdge(newEdge); //add v1 as a link for v2 directed
                v2.addEdge(newEdge); //add v1 as a link for v2 directed
                edgeCount++;
            }
        } else {
            throw new KeyAlreadyExistsException("Cannot add self referencing edge");
        }
    }

    /* METHOD: hasVertex
     * IMPORT: String label
     * EXPORT: boolean
     * PURPOSE: Checks if graph object has a specific vertex given a string and returns a boolean
     */
    private boolean hasVertex(String label) throws EmptyGraphException, KeyNotFoundException {
        boolean hasVertex = false;
        if (vertexCount > 0) {
                vertices.get(label);
                hasVertex = true;
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
        return hasVertex;
    }

    /* METHOD: hasEdge
     * IMPORT: String label1, String label2
     * EXPORT: boolean
     * PURPOSE: Finds if a specific edge exists - from label1 to label2
     */
    public boolean hasEdge(String label1, String label2) {
        boolean hasEdge;
        try {
            getEdge(label1 + "->" + label2);
            hasEdge = true;
        } catch (KeyNotFoundException | EmptyGraphException e) {
            hasEdge = false;
        }
        return hasEdge;
    }

    /* METHOD: getVertex
     * IMPORT: String label
     * EXPORT: DSAGraphVertex
     * PURPOSE: Finds a vertex given a label and returns the vertex
     */
    private DSAGraphVertex getVertex(String label) throws EmptyGraphException, KeyNotFoundException {
        DSAGraphVertex vertex = null;
        if (vertexCount > 0) {
            vertex = (DSAGraphVertex) vertices.get(label);
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
        return vertex;
    }

    /* METHOD: getEdge
     * IMPORT: String label
     * EXPORT: DSAGraphEdge
     * PURPOSE: Finds an edge based on the label. If edgeCount = 0 throws EmptyGraphException. If key does
     * not exist throws KeyNotFoundException.
     */
    private DSAGraphEdge getEdge(String label) throws EmptyGraphException, KeyNotFoundException {
        DSAGraphEdge edge = null;
        if (edgeCount > 0) {
            edge = (DSAGraphEdge) edges.get(label);
        }
        else {
            throw new EmptyGraphException("Graph Empty - no edges have benn added");
        }
        return edge;
    }

    /* METHOD: getVertexValue
     * IMPORT: String label
     * EXPORT: Object
     * PURPOSE: Gets a vertex and returns the value stored in it. throws EmptyGraphException if vertexCount = 0.
     * If label not found throws KeyNotFoundException
     */
    public Object getVertexValue(String label) throws EmptyGraphException, KeyNotFoundException {
        DSAGraphVertex vertex;
        if (vertexCount > 0) {
            vertex = (DSAGraphVertex) vertices.get(label);
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
        return vertex.getValue();
    }

    /* METHOD: getVertexValues
     * IMPORT: void
     * EXPORT: Object[]
     * PURPOSE: gets all vertex values in the graph and returns them in an Object array. Throws EmptyGraphException
     * if vertexCount = 0;
     */
    public Object[] getVertexValues() throws EmptyGraphException {
        Object[] outArray = new Object[vertexCount];
        if (vertexCount > 0) {
            try {
                Object[] vertexArray = vertices.exportValues();
                for (int ii = 0; ii < vertexCount; ii++) { /*DSAHashTable doesnt know what DSAGraphVertex is so must be cast back one by one*/
                    outArray[ii] = ((DSAGraphVertex) vertexArray[ii]).getValue();
                }
            } catch (EmptyHashTableException e) {
                e.getMessage();
            }
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
        return outArray;
    }

    /* METHOD: getVertexKeys
     * IMPORT: void
     * EXPORT: String[]
     * PURPOSE: Gets vertex keys in the graph and returns them in a String[]. Throws EmptyGraphException if edgeCount = 0;
     */
    public String[] getVertexKeys() throws EmptyGraphException {
        String[] outArray = new String[edgeCount];
        if (edgeCount > 0) {
            try {
                outArray = vertices.exportKeys();
            } catch (EmptyHashTableException e) {
                e.getMessage();
            }
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
        return outArray;
    }

    /* METHOD: getEdgeKeys
     * IMPORT: void
     * EXPORT: String[]
     * PURPOSE: Gets all edge keys and exports them as String[]. Throws EmptyGraphException if edgeCount = 0;
     */
    public String[] getEdgeKeys() throws EmptyGraphException {
        String[] outArray = new String[edgeCount];
        if (edgeCount > 0) {
            try {
                outArray = edges.exportKeys();
            } catch (EmptyHashTableException e) {
                e.getMessage();
            }
        }
        else {
            throw new EmptyGraphException("Graph Empty - no edges have been added");
        }
        return outArray;
    }

    /* METHOD: getAdjacentFrom
     * IMPORT: String label
     * EXPORT: DSALinkedList
     * PURPOSE: get all vertices pointing to this vertex. Returns as a linked list. if label does not exist
     * throws KeyNotFoundException, if edgeCount = 0 throws EmptyGraphException.
     */
    public DSALinkedList getAdjacentFrom(String label) throws EmptyGraphException, KeyNotFoundException {
        DSALinkedList adjacentValues = new DSALinkedList();
        if (edgeCount > 0) {
            DSALinkedList adjacentEdges = this.getAdjacentE(label);
            Iterator iter = adjacentEdges.iterator();

            while (iter.hasNext()) {
                DSAGraphEdge edge = (DSAGraphEdge) iter.next();
                if (label.equals(edge.getVertexTo().getLabel())) {
                    adjacentValues.insertLast(edge.getVertexFrom().getValue());
                }
            }
        }
        else {
            throw new EmptyGraphException("Graph Empty - no edges have been added");
        }
        return adjacentValues;
    }

    /* METHOD: getAdjacentTo
     * IMPORT: String label
     * EXPORT: DSALinkedList
     * PURPOSE: get all vertices this vertex is pointing to. Returns as a linked list. if label does not exist
     * throws KeyNotFoundException, if edgeCount = 0 throws EmptyGraphException.
     */
    public DSALinkedList getAdjacentTo(String label) throws EmptyGraphException, KeyNotFoundException {
        DSALinkedList adjacentValues = new DSALinkedList();
        if (edgeCount > 0) {
            DSALinkedList adjacentEdges = this.getAdjacentE(label);
            Iterator iter = adjacentEdges.iterator();

            while (iter.hasNext()) {
                DSAGraphEdge edge = (DSAGraphEdge) iter.next();
                if (label.equals(edge.getVertexFrom().getLabel())) {
                    adjacentValues.insertLast(edge.getVertexTo().getValue());
                }
            }
        }
        else {
            throw new EmptyGraphException("Graph Empty - no edges have been added");
        }
        return adjacentValues;
    }

    /* METHOD: removeVertex
     * IMPORT: String label
     * EXPORT: void
     * PURPOSE: removes a vertex from the graph. Throws EmptyGraphException if not vertices added yet. Throws
     * KeyNotFoundException if label is not a vertex label.
     */
    public void removeVertex(String label) throws EmptyGraphException, KeyNotFoundException {
        if (vertexCount > 0) {
            vertices.remove(label);
            vertexCount--;
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
    }

    /* METHOD: removeEdge
     * IMPORT: String label
     * EXPORT: void
     * PURPOSE: removes an edge from the graph. Throws EmptyGraphException if not edges added yet. Throws
     * KeyNotFoundException if edge is not a edge label.
     */
    public void removeEdge(String label1, String label2) throws EmptyGraphException, KeyNotFoundException {
        if (edgeCount > 0) {
            edges.remove(label1 + "->" + label2);
            edgeCount--;
        }
        else {
            throw new EmptyGraphException("Graph Empty - no edges have been added");
        }
    }

    /* METHOD: getAdjacentE
     * IMPORT: String label
     * EXPORT: DSALinkedList
     * PURPOSE: Returns the linked list of adjacent Edges
     */
    private DSALinkedList getAdjacentE(String label) throws EmptyGraphException, KeyNotFoundException {
        DSAGraphVertex vertex = null;
        if (vertexCount > 0) {
            vertex = getVertex(label);
        }
        else {
            throw new EmptyGraphException("Graph Empty - no vertices have been added");
        }
        return vertex.getAdjacentE();
    }

    /* METHOD: displayList
     * IMPORT: none
     * EXPORT: none
     * PURPOSE: Displays the graph as an adjacency list
     */
    public void displayList() throws EmptyGraphException {
        if(edgeCount > 0 || vertexCount > 0) {
            try {
                String[] vertexArray = vertices.exportKeys();
                for (int ii = 0; ii < vertexCount; ii++) {
                    System.out.print(vertexArray[ii] + " is followed by ");
                    DSAGraphVertex vertex = getVertex(vertexArray[ii]);
                    DSALinkedList adjList = vertex.getAdjacentE();
                    Iterator iter = adjList.iterator();
                    boolean following = false;
                    while (iter.hasNext()) {
                        DSAGraphEdge edge = (DSAGraphEdge) iter.next();
                        if (edge.getVertexTo().getLabel().equals(vertex.getLabel())) {
                            System.out.print(edge.getVertexFrom().getLabel() + " ");
                            following = true;
                        }
                    }
                    if (!following) {
                        System.out.println("no one");
                    } else {
                        System.out.println();
                    }
                }
            } catch (EmptyHashTableException | KeyNotFoundException e) {
                e.getMessage();
            }
        }
        else {
            throw new EmptyGraphException("Graph Empty - cannot display list");
        }
    }
}