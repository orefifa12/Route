import java.security.InvalidAlgorithmParameterException;
import java.io.*;
import java.util.*;


/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain
 * @author Owen Astrachan modified in Fall 2023
 *
 */
public class GraphProcessor {
    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */

    // include instance variables here
    private HashMap<Point, List<Point>> myMap;
    private int numVertices;
    private int numEdges;
    //private HashMap<String, Point> myVertices; //name associated with vertices
    private Point[] myPoints; //to preserve indices

    public GraphProcessor(){
        // TODO initialize instance variables
        myMap = new HashMap<>();
        numVertices = 0;
        numEdges = 0;
        //myVertices = new HashMap<>();
        myPoints = new Point[1];
    }

    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws IOException if file not found or error reading
     */

    public void initialize(FileInputStream file) throws IOException {
        // TODO implement by reading info and creating graph
        Scanner reader = new Scanner(file);
        String lineOne = reader.nextLine();
        String[] nums = lineOne.split(" ");
        numVertices = Integer.parseInt(nums[0]);
        numEdges = Integer.parseInt(nums[1]);
        //myVertices = new HashMap<>();
        myPoints = new Point[numVertices];
        for(int i = 0; i < numVertices; i++)
        {
            String line = reader.nextLine();
            String[] data = line.split(" ");
            //String name = data[0];
            double x = Double.parseDouble(data[1]);
            double y = Double.parseDouble(data[2]);
            Point me = new Point(x, y);
            //myVertices.put(name, me);
            myPoints[i] = me;
        }
        for(int i = 0; i < numEdges; i++)
        {
            String line = reader.nextLine();
            String[] data = line.split(" ");
            int start = Integer.parseInt(data[0]);
            int end = Integer.parseInt(data[1]);
            //String name = data[2];
            if(myMap.keySet().contains(myPoints[start]))
            {
                List<Point> list = myMap.get(myPoints[start]);
                list.add(myPoints[end]);
                myMap.put(myPoints[start], list);
            }
            else
            {
                List<Point> list = new ArrayList<>();
                list.add(myPoints[end]);
                myMap.put(myPoints[start], list);
            }

            if(myMap.keySet().contains(myPoints[end]))
            {
                List<Point> list = myMap.get(myPoints[end]);
                list.add(myPoints[start]);
                myMap.put(myPoints[end], list);
            }
            else
            {
                List<Point> list = new ArrayList<>();
                list.add(myPoints[start]);
                myMap.put(myPoints[end], list);
            }
        }
        reader.close();
    }

    /**
     * NOT USED IN FALL 2023, no need to implement
     * @return list of all vertices in graph
     */

    public List<Point> getVertices(){
        return null;
    }

    /**
     * NOT USED IN FALL 2023, no need to implement
     * @return all edges in graph
     */
    public List<Point[]> getEdges(){
        return null;
    }

    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * @param p is a point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public Point nearestPoint(Point p) {
        Double min = Double.POSITIVE_INFINITY;
        Point closestPoint = p;
        for (Point eachpoint : myMap.keySet()) {
            Double newdistance = p.distance(eachpoint);
            if (newdistance < min) {
                min = newdistance;
                closestPoint = eachpoint;
            }
        }
        return closestPoint;
    }


    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points, 
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * @param start Beginning point. May or may not be in the graph.
     * @param end Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public double routeDistance(List<Point> route) {
        double d = 0.0;
        // TODO implement routeDistance
        int len = route.size();
        for(int i = 0; i < len - 1; i++)
        {
            Point start = route.get(i);
            Point end = route.get(i + 1);
            double dis = start.distance(end);
            d += dis;
        }
        return d;
    }
    

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * @param p1 one point
     * @param p2 another point
     * @return true if and onlyu if p2 is reachable from p1 (and vice versa)
     */
    public boolean connected(Point p1, Point p2) {
        // TODO implement connected
        return false;
    }

    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * @param start Beginning point.
     * @param end Destination point.
     * @return The shortest path [start, ..., end].
     * @throws IllegalArgumentException if there is no such route, 
     * either because start is not connected to end or because start equals end.
     */
    public List<Point> route(Point start, Point end) throws IllegalArgumentException {
        // TODO implement route
        return null;
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String name = "data/usa.graph";
        GraphProcessor gp = new GraphProcessor();
        gp.initialize(new FileInputStream(name));
        System.out.println("running GraphProcessor");
    }


    
}
