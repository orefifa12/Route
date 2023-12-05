# Project 7: Route

This is the directions document for Project 7 Route in CompSci 201 at Duke University, Fall23 2023. 

See the [details document](docs/details.md) for information on using Git, starting the project, and more details about the project including information about the classes and concepts that are outlined briefly below. You'll absolutely need to read the information in the details document to understand how the classes in this project work independently and together. The details document also contains project-specific details. This current document provides a high-level overview of the assignment.

You are STRONGLY encouraged to work with a partner on this final project! See the [details document](docs/details.md) for information on using Git with a partner and how the workflow can proceed. 

### Background

This project was initially developed by Brandon Fain and the UTA/TA team in 201, with some ephemeral
connection to the Bear Maps project from UC Berkeley's 61B course.

## Outline 

- [Project Introduction](#project-introduction)
    - [The `Point` Class](#the-point-class)
    - [The `Visualize` Class](#the-visualize-class)
    - [Graph Data](#graph-data)
- [Part 1: Implementing `GraphProcessor`](#part-1-implementing-graphprocessor)
- [Part 2: Creating `GraphDemo`](#part-2-creating-graphdemo)
- [Submitting and Grading](#submitting-and-grading)

## Project Introduction

In this project you are asked to implement a routing service that represents the United States highway network as a graph and calculates routes and distances on this network. At a high level, in part 1 you will implement `GraphProcessor` which stores a graph representation and provides public methods to answer connectivity, distance, and pathfinding queries. This part of the project will be autograded as per usual. 

In part 2 you will implement a `main` method in `GraphDemo` that produces a minimal viable product (sometimes known in industry as an _MVP_) demonstrating the functionality of `GraphProcessor` and visualizing the results. You could, for example, include a video of your demo as part of submitting something for [P8 Create](https://docs.google.com/document/d/1VEJzUNFPdutbD-zIscUmEf8y0Ie4dLU_e8FNm0c7nWA/edit?usp=sharing).

To complete Part 1, you'll need to understand the classes you're given and the code you're asked to write. The classes you're given include `Point` and `Visualize` (which uses the `StdDraw` class you used in *P1 NBody*), and some JUnit testing classes. Details can be found in [details document](docs/details.md) with a high-level overview here.

## Classes

### The `Point` Class

You are provided in the starter code with `Point.java` that represents an immutable (meaning it cannot be changed after creation) point on the Earth's surface. You call methods in this class, details can be found in the [details document](docs/details.md). You'll use this class extensively to represent vertices in a graph. You will not change `Point`.

### The `Visualize` Class

`Visualize.java` (which, in turn, uses `StdDraw.java`, though you won't need to directly call anything from this class). You do not need to edit this class, methods and details can be found in the [details document](docs/details.md).

## Part 1: Implementing `GraphProcessor`

In this part you will implement `GraphProcessor`, which stores a graph representation and provides public methods to answer connectivity, distance, and pathfinding queries. *This part of the project will be autograded*. To pass autograder compilation, you must write your `GraphProcessor` implemention entirely within the provided `GraphProcessor.java` file. If you use helper classes, they should be included in the file as nested classes.

JUnit tests are also supplied to test your code locally, and we suggest starting by testing with the straightforward `TestSimpleGraphProcessor` for ease of debugging. Once you pass `TestSimpleGraphProcessor`, you can also check compliance with `TestUSGraphProcessor`, which runs on the same data as the autograder. However, **you may need to make some changes before you can run JUnit tests** locally for this project -- the changes can be found in the [details document](docs/details.md).

The starter code for `GraphProcessor.java` includes five public methods you must implement (there are two methods with empty bodies you will *not* implement, they're for future use). Each is described below and also in javadocs inside of the starter code. While these are the only methods you must implement, you are very much *encouraged to create additional helper methods* where convenient for keeping your code organized and to avoid repetitive code. As a rough rule of thumb, if you find yourself writing a method that is longer than fits on your text editor at once (maybe 30 lines), or if you find yourself copy/pasting many lines of code, you might consider abstracting some of that away into a helper method. 

### Instance variables

You will need to add instance variables to your `GraphProcessor` class to represent a graph, but exactly how to do this is left up to you. Remember that vertices/nodes in the graph will be `Point` objects, see [the `Point` class](#the-point-class). As a reminder/hint, your graph representation should allow you to efficiently do things like:
- Check if two vertices are adjacent (meaning there is an edge between them), or
- For a given vertex, lookup/loop over all of its adjacent vertices.  

In class examples we typically used an adjacency list representation, e.g., `Map<Point, List<Point>>` or `Map<Point, Set<Point>>` for the `myGraph` instance variable. You are *strongly encouraged* to use such a map in your code. Initialize your instance variables in the `GraphProcessor` constructor.

### Implement `initialize`

This method takes as input a `FileInputStream`. This input stream should be for a file in the `.graph` format which is described in detail in the [details document](docs/details.md) as is this method `initialize`. You'll need to read that information to see what `initialize` does, how to test it, and perhaps more about how to create instance variables. The code you're given in `GraphDemo.main` calls `initialize` as an example.

### Implement `nearestPoint`

In general you may be interested in routing between points that are not themselves vertices of the graph, in which case you need to be able to find the closest points actually on the graph. This method takes a `Point p` as input and returns the vertex in the graph that is closest to `p`, in terms of the straight-line distance calculated by the `distance` method of [the Point class](#the-point-class), NOT shortest path distance, e.g., you do *NOT* use Dijkstra's algorithm, you simply call the `Point.distance` method for all vertices/points. Note that the input `p` may not be in the graph. If there are ties, you can break them arbitrarily. You may test correctness with `testNearestPoint()` in JUnit. *Summary: loop over every `Point` that's a vertex in the graph calling `p.distance(v)` for these vertex points `v` and return the minimal/closest `Point`.*

A simple implementation of the `nearestPoint` method should have $`O(N)`$ runtime complexity where $`N`$ is the number of vertices in the graph. Your implementation should be at least this efficient. It is possible to use more advanced data structures to substantial improve the runtime, but that is outside the scope of this project.

### Implement `routeDistance`

This method takes a `List<Point> route` representing a path in the graph as input and should calculate the total distance along that path, starting at the first point and adding the distances from the first to the second point, the second to the third point, and so on. Use the `distance` method of [the `Point` class](#the-point-class). You may test correctness using `testRouteDistance()` in JUnit. *Simply iterate through the points accumulating a sum of distances between points in the path.* Note that this method does *NOT* reference the graph, it simply uses `Point.distance`.

The runtime complexity of the method should be linear in `route.size()`, that is, the number of points on the path. 

### Implement `connected`

This method takes two points `p1` and `p2` and should return `true` if the points are connected, meaning there exists a path in the graph (a sequence of edges) from `p1` to `p2`. Otherwise, the method should return `false`, including if `p1` or `p2` are not themselves points in the graph. You may test correctness using `testConnected()` in JUnit.

You will get full credit for correctness if you implement `connected` by searching in the graph, for example, using a depth-first search (DFS) with linear runtime complexity $`O(N+M)`$ where $`N`$ is the number of vertices in the graph and $`M`$ is the number of edges in the graph.


### Implement `route`

This method takes two points, `start` and `end`, as input and should return a `List<Point>` representing the **shortest path** from `start` to `end` as a sequence of points. The total distance along a path is the sum of the edge weights, equal to the sum of the straight-line distance between consecutive points (see [implement `routeDistance`](#implement-routedistance)). Note that you must return the path itself, not just the distance along the path. The first point in your returned list should be `start`, and the last point should be `end`. 

If there is no path between `start` and `end`, either because the two points are not in the graph, or because they are the same point, or because they are not connected in the graph, then you should throw an exception, for example: 
```java
throw new IllegalArgumentException("No path between start and end");
```

See the [details document](docs/details.md) for information on implementing Dijkstra's algorithm.


## Part 2: Creating `GraphDemo`

The starter code for `GraphDemo.java` includes a `main` method. Feel free to organize `GraphDemo` however you see fit - it will not be autograded. *Running your `GraphDemo` `main` method should produce a demonstration of the functionality of your project on the USA highway network* -- it *already does that minimally*, you're welcome to modify the code. See the  [details document](docs/details.md) for information. You do *NOT* need to make changes for the purposes of grades.


## Submitting and Grading

Commit and push your code often as you develop. To submit:

1. Submit your code on gradescope to the autograder. If you worked with a partner, you and your partner will make a **single submission together on gradescope**. Refer to [this document](https://docs.google.com/document/d/e/2PACX-1vREK5ajnfEAk3FKjkoKR1wFtVAAEN3hGYwNipZbcbBCnWodkY2UI1lp856fz0ZFbxQ3yLPkotZ0U1U1/pub) for submitting to Gradescope with a partner. 

The first part, `GraphProcessor`, will be autograded for the correctness and efficiency of the code. Most of the points are for correctness, so focus on that first rather than on efficiency, which you'll likely achieve simply by following directions. If you modify `GraphDemo` as suggested in the [details document](docs/details.md) you can be proud of your accomplishment.

