/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Graph;
import marvel.MarvelParser;
import marvel.MarvelPaths;
import shortestPath.GraphNeighbors;
import shortestPath.Paths;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;


    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }
                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }


    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }
        Graph<String, Double> g = graphs.get(arguments.get(0));
        String source = arguments.get(1);
        String dest = arguments.get(2);
        boolean contains = true;
        if (!g.containsNode(source)) {
            output.println("unknown: " + source);
            contains = false;
        }
        if (!g.containsNode(dest)) {
            output.println("unknown: " + dest);
            contains = false;
        }

        if (contains) {
            double totalCost = 0.0;
            List<GraphNeighbors.Edge<String, Double>> lst = Paths.shortestWeightedPath(g, source, dest);
            output.println("path from " + source + " to " + dest + ":");
            if (lst == null) {
                output.println("no path found");
            } else {
                for (GraphNeighbors.Edge<String, Double> edge : lst) {
                    output.println(edge.getSource() + " to " + edge.getDest() + " with weight " +
                            String.format("%.3f", edge.getLabel()));
                    totalCost += edge.getLabel();
                }
                output.println("total cost: " + String.format("%.3f", totalCost));
            }

        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);

        double edgeLabel = Double.parseDouble(arguments.get(3));
        if (parentName.equals(childName)) {
            edgeLabel = 0.0;
        }
        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName, double edgeLabel) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addEdge(parentName, childName, edgeLabel);
        output.println("added edge " + String.format("%.3f", edgeLabel) + " from " + parentName + " to " +
                childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> g = graphs.get(graphName);
        String result = graphName + " contains:";
        List<String> nodeList = new ArrayList<>(g.allNodes());

        if (nodeList == null || nodeList.isEmpty()) {
            output.println(result);
            return;
        }

        Collections.sort(nodeList);

        for (String node : nodeList) {
            result += " " + node;
        }
        output.println(result);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> g = graphs.get(graphName);
        String result = "the children of " + parentName + " in " + graphName + " are:";

        List<GraphNeighbors.Edge<String, Double>> edgeList = new ArrayList<>(g.outgoingEdges(parentName));

        if (edgeList == null) {
            output.println(result);
            return;
        }

        Collections.sort(edgeList, (o1, o2) -> {
            if (o1.getLabel() > o2.getLabel()) {
                return 1;
            } else if (o1.getLabel() < o2.getLabel()) {
                return -1;
            }
            return 0;
        });

        for (GraphNeighbors.Edge<String, Double> edge : edgeList) {
            result += " " + edge.getDest() + "(" + String.format("%.3f", edge.getLabel()) + ")";
        }
        output.println(result);
    }



    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
