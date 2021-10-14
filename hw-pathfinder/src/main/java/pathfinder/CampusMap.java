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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;
import shortestPath.GraphNeighbors;
import shortestPath.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CampusMap implements ModelAPI {

    private List<CampusBuilding> campusBuildings;
    private List<CampusPath> campusPaths;
    private Graph<Point, Double> campusGraph;
    private Map<String, String> buildingMap;

    public CampusMap() {
        // construct all the private fields in the constructor
        campusBuildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        campusPaths = CampusPathsParser.parseCampusPaths("campus_paths.csv");
        campusGraph = new Graph<>();
        // construct campus graph
        for (CampusPath path : campusPaths) {
            Point p1 = new Point(path.getX1(), path.getY1());
            Point p2 = new Point(path.getX2(), path.getY2());
            campusGraph.addNode(p1);
            campusGraph.addNode(p2);
            campusGraph.addEdge(p1, p2, path.getDistance());
        }
        // set up building names map
        buildingMap = new HashMap<>();
        for (CampusBuilding building : campusBuildings) {
            buildingMap.put(building.getShortName(), building.getLongName());
        }
    }


    @Override
    public boolean shortNameExists(String shortName) {
        return buildingMap.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        return buildingMap.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        return buildingMap;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        // null check
        if (startShortName == null || endShortName == null ||
                !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }
        // search for the start and end names  in the campus building maps and construct Points from it
        Point start = null;
        Point end = null;
        for (CampusBuilding building : campusBuildings) {
            if (building.getShortName().equals(startShortName)) {
                start = new Point(building.getX(), building.getY());
            } else if (building.getShortName().equals(endShortName)) {
                end = new Point(building.getX(), building.getY());
            }
        }
        // find shortest weighted path with Djikstra's
        List<GraphNeighbors.Edge<Point, Double>> edgeList = Paths.shortestWeightedPath(campusGraph, start, end);
        Path<Point> path = new Path<>(edgeList.get(0).getSource());
        // construct the Path object
        for (GraphNeighbors.Edge<Point, Double> edge : edgeList) {
            path = path.extend(edge.getDest(), edge.getLabel());
        }

        return path;
    }

}
