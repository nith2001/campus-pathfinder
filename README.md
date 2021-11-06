# UW Campus Pathfinder
Contains the folders of code that I used to make my path finder on the UW campus

# What is It?
- Implemented a visual shortest path finder for the UW campus.
- Used React.js, Java, and Spark Java framework while following model-view-controller design pattern.
- Built custom graph implementation and utilized Dijkstra’s algorithm to find the shortest path.

My Java graph implementation will be loaded with a csv file full of coordinate points for building locations and sidewalk points from the UW map, and 
using those points and paths loaded into a graph. Once that's done, the user is given the option to pick two buildings in UW. Once chosen, Djikstra's algorithm will search for the best path from one building location to another location. The building start and end input is set up through React, and once the request for the best path is made, it will call an endpoint set up through Java Spark Server, which will call my back-end implementation and return the path information via JSON to the front-end for the React code to use and 
display on the screen. The path is displayed as a red line on top of a picture of a UW campus map.
