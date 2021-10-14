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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;

public class SparkServer {

    private final static CampusMap universityMap = new CampusMap();

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        // testing method
        Spark.get("/hello-world", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                // As a first example, let's just return a static string.
                return "Hello, Spark!";
            }
        });

        // This is the findPath endpoint for the Spark server. This endpoint will apply CampusMap to find the
        // path from one building to another building and return a Json format of the resulting Path<Point>
        // object of the path.
        Spark.get("/findPath", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                // set up the start and end strings
                String startString = request.queryParams("start");
                String endString = request.queryParams("end");
                // possible errors that server may face due to input
                if (startString == null || endString == null) {
                    Spark.halt(400, "Must provide a start and end");
                }
                if (!universityMap.shortNameExists(startString) || !universityMap.shortNameExists(endString)) {
                    Spark.halt(400, "The start and/or end does not exist on campus map!");
                }
                // get the shortest path and return it
                Path<Point> path = universityMap.findShortestPath(startString, endString);
                Gson gson = new Gson();
                return gson.toJson(path);
            }
        });

        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                // get the building names map and return it via GSON
                Map<String, String> nameMap = universityMap.buildingNames();
                Gson gson = new Gson();
                return gson.toJson(nameMap);
            }
        });
    }
}
