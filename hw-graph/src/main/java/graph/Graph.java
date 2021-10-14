package graph;
import shortestPath.GraphNeighbors;

import java.util.*;

/**
 * <b>Graph</b> is a collection of nodes and directed edges, where each edge connects two nodes. A directed edge
 * will point from one node to another node. Each edge and node can store data within the objects and represents
 * relationships between nodes via edges.
 */

public class Graph<N, E> implements GraphNeighbors<N, E> {

    // AF(this) = For Graph g, every node n within graphMap.keySet() and represents a key, and each node
    // points to a list of edges that point outward from it and towards another node and holds some
    // edge data. If there are no nodes, then the graph is considered empty.


    // Representation Invariant:
    // For any two Node n1 and n2 within graphMap.keySet, n1.getNodeData != n2.getNodeData.
    // Let Edge e1 and e2 exist. If e1.from() == e2.from() == Node n1 and e1.to() == e2.to() == Node n2,
    // then e1.getEdgeData != e2.getEdgeData. No two edges can have equal fields.
    // For every node "n" in graphMap.keySet(), they must point to a List<Edge> "lst" s.t. all Edges "e" in lst
    // must satisfy e.from() == n and e.to() != n.



    // n1 --- e > n2
    // n1 --- e > n2

    private final Map<N, List<Edge>> graphMap;


    /**
     * @spec.effects Constructs a new empty Graph
     */
    public Graph() {
        graphMap = new HashMap<>();
    }

    /**
     * @param nodes represents a list of node names to add to the Graph
     * @spec.requires nodes != null
     * @spec.effects Constructs a new Graph containing nodes made from data within the provided collection.
     */
    public Graph(Collection<N> nodes) {
        this();
        for (N nodeData : nodes) {
            if (nodeData != null) {
                graphMap.put(nodeData, new ArrayList<>());
            }
        }
    }

    /**
     * @param nodeName The data stored in the node
     * @return the Set of all edges pointing towards the parameter node
     * @spec.requires nodeName != null
     */
    public List<Edge> incomingEdgesTo(N nodeName) {
        if (nodeName == null) { throw new IllegalArgumentException("No null arguments"); }

        List<Edge> edges = new ArrayList<>();
        for (N keyNode : graphMap.keySet()) {
            if (keyNode.equals(nodeName)) { continue; }

            for (Edge edge: graphMap.get(keyNode)) {
                if (edge.to().equals(nodeName)) {
                    edges.add(edge);
                }
            }
        }
        checkRep();
        return edges;
    }


    /**
     * @param nodeName The data stored in the node
     * @return the Set of all edges pointing outward from the parameter node
     * @spec.requires nodeName != null
     */
    public List<Edge> outgoingEdgesFrom(N nodeName) {
        if (nodeName == null) { throw new IllegalArgumentException("No null arguments"); }
        return Collections.unmodifiableList(graphMap.get(nodeName));
    }


    /**
     * @param nodeName The data stored in the node
     * @return the Set of all nodes with edges pointing from those nodes to this node containing nodeName
     * @spec.requires nodeName != null
     */
    public Set<N> getParents(N nodeName) {
        if (nodeName == null) { throw new IllegalArgumentException("No null arguments"); }

        Set<N> parents = new HashSet<>();
        for (Edge e : incomingEdgesTo(nodeName)) {
            parents.add(e.from());
        }
        checkRep();
        return parents;
    }


    /**
     * @return the Set of all edges in the graph
     */
    public Set<Edge> allEdges() {
        Set<Edge> edges = new HashSet<>();
        for (N node : graphMap.keySet()) {
            edges.addAll(graphMap.get(node));
        }
        return edges;
    }

    /**
     * @return the Set of all nodes in the graph
     */
    public Set<N> allNodes() {
        return Collections.unmodifiableSet(graphMap.keySet());
    }

    /**
     * @param nodeData The data that will be stored in a node that gets added to the graph
     * @spec.requires nodeData != null and !graph.containsNode(node.data())
     * @spec.effects Inserts an additional node to the graph
     */
    public void addNode(N nodeData) {
        if (nodeData == null) { throw new IllegalArgumentException("Non-null parameters only"); }
        if (!this.containsNode(nodeData)) {
            graphMap.put(nodeData, new ArrayList<>());
        }
        checkRep();
    }


    /**
     * @param fromNode the node where the edge starts from
     * @param toNode   the node where the edge points to
     * @param edgeData the data held within the edge
     * @spec.requires fromNode, toNode, edgeData != null and graph contains both fromNode and toNode and there cannot
     * already exist an edge from 'fromNode' to 'toNode' that has the same edgeData.
     * @spec.effects Creates a new edge containing edgeData that connects fromNode to toNode.
     */

    public void addEdge(N fromNode, N toNode, E edgeData) {
        checkRep();
        if (fromNode == null || toNode == null || edgeData == null) {
            throw new NullPointerException();
        }
        if (!containsEdge(fromNode, toNode, edgeData)) {
            Edge e = new Edge(fromNode, toNode, edgeData);
            graphMap.get(fromNode).add(e);
            checkRep();
        }
    }

    /**
     * @param nodeData the data held within the edge
     * @return true if the graph contains the node with nodeData in it. Else, false.
     * @spec.requires nodeData != null
     */
    public boolean containsNode(N nodeData) {
        if (nodeData == null) { throw new IllegalArgumentException(); }
        if (graphMap.containsKey(nodeData)) {
            return true;
        }
        for (N node : graphMap.keySet()) {
            if (node.equals(nodeData)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param fromNode the node where the edge starts from
     * @param toNode   the node where the edge points to
     * @param edgeData the data held within the edge
     * @return true if the graph contains both nodes AND the graph contains the edge storing edgeData AND
     * the edge points from 'fromNode' and  'toNode'. Else, false.
     * @spec.requires fromNode, toNode, edgeData != null
     */
    public boolean containsEdge(N fromNode, N toNode, E edgeData) {
        checkRep();
        if (edgeData == null) {
            throw new IllegalArgumentException("Non-null parameters only");
        }

        if (fromNode == null || toNode == null) {
            return false;
        }

        List<Edge> lst = graphMap.get(fromNode);
        if (lst == null) { return false; }
        for (Edge e : lst) {
            if (e.getEdgeData().equals(edgeData) && e.from().equals(fromNode) && e.to().equals(toNode)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return true if Graph contains no nodes. Else, false.
     */
    public boolean isEmpty() {
        return graphMap.isEmpty();
    }

    // checks whether the Graph is maintaining its representation invariant
    private void checkRep() {
        // all nodes are unique AND edges in a node list point FROM it, not TO it.
        boolean DEBUG = true;
        if (!DEBUG) {
            return;
        }

        Set<N> nodeNames = new HashSet<>();
        for (N n : allNodes()){
            assert !nodeNames.contains(n);
            assert graphMap.get(n) != null;
            for (Edge e : graphMap.get(n)) {
                assert e.from().equals(n);
            }
            nodeNames.add(n);
        }
        // duplicate edge checks
        Set<Edge> edges = new HashSet<>();
        for (Edge e : allEdges()) {
            assert !edges.contains(e);
            edges.add(e);
        }
    }


    /**
     * @param nodeName The data stored in the node
     * @return the Set of all edges pointing outward from the parameter node
     * @spec.requires nodeName != null
     */
    @Override
    public Set<GraphNeighbors.Edge<N, E>> outgoingEdges(N nodeName) {
        if (nodeName == null) { throw new IllegalArgumentException("No null arguments"); }

        if (nodeName != null) {
            Set<GraphNeighbors.Edge<N, E>> edgeSet = new HashSet<>();
            for (Edge e : graphMap.get(nodeName)) {
                GraphNeighbors.Edge<N, E> edge = new GraphNeighbors.Edge<>(e.from(), e.to(), e.getEdgeData());
                edgeSet.add(edge);
            }
            return edgeSet;
        }
        return null;
    }



    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * <b>Edge</b> will represent a connection between two Nodes and holds data in a String
     */
    public class Edge {

        private final N from;
        private final N to;
        private E data;

        // Abstraction Function:
        // Edge e represents both ends of the connection s.t. e.from is the node where the edge points out from
        // and e.to is the node that the edge would point toward, and the edge data is stored in e.data.

        // Representation Invariant:
        // For some Edge e, e.from, e.to, e.data != null, and edge.to != edge.from

        /**
         * @param from the Node that the edge is pointed outward from
         * @param to the Node that the edge is directed toward
         * @param data the data that the edge will store
         *
         * @spec.effects creates a new Edge connecting the 'from' and 'to' Node and stores 'data' in it
         */
        public Edge(N from, N to, E data) {
            this.from = from;
            this.to = to;
            this.data = data;
            checkRep();
        }

        /**
         * @return the Node that the edge is pointing outward from
         */
        public N from() {
            return from;
        }

        /**
         * @return the Node that the edge is directly pointed towards
         */
        public N to() {
            return to;
        }

        /**
         * @return the edge data stored in the Edge
         */
        public E getEdgeData() {
            return data;
        }


        /**
         * @param otherEdge, representing the edge being checked with if equal
         * @spec.requires otherEdge != null
         * @return true if the edgeData, to Node node-data and from Node node-data between both edges are
         * equals. Else, false.
         */
        public boolean equals(Edge otherEdge) {
            return this.data == otherEdge.data && this.to == otherEdge.to &&
                    this.from == otherEdge.from;
        }

        // checks representation invariant by ensuring that from and to != null and that from and to aren't pointing at
        // the same node.
        private void checkRep() {
            assert from != null;
            assert to != null;
            assert data != null;
        }
    }

}
