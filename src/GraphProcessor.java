// import java.security.InvalidAlgorithmParameterException;
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
    private Point destination;

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

        
        try{Scanner reader = new Scanner(file);
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
        reader.close();}
        catch(Exception e)
        {
            throw new IOException("Could not read .graph file");
        }
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
        HashSet<Point> visited = new HashSet<>();
        Stack<Point> toVisit = new Stack<>();
        toVisit.push(p1);
        visited.add(p1);
        while(!toVisit.isEmpty()){
            Point current = toVisit.pop();
            if(current.equals(p2))
            {
                return true;
            }
            List<Point> neighbors = myMap.get(current);//get neighbors
            for(Point each : neighbors){//for each neighbor 
                if(!visited.contains(each)) // if it hasn't been visited
                {
                    toVisit.push(each); 
                    visited.add(each);
                }
                if(visited.contains(p2)){
                    return true;
                }
            }
        }
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
        if (start.equals(end) || !connected(start, end)) {
            throw new IllegalArgumentException("not possible");
        }
        Map<Point, Double> distanceMap = new HashMap<>();
        Map<Point,Point> predMap = new HashMap<>();
        predMap.put (start, null);
        final Comparator<Point> comp = new Comparator<Point>(){ //this will compare the distances of two points
            @Override
            public int compare(Point p1, Point p2){
                return p1.compareTo(p2);//compare the distances
            }
        };
        PriorityQueue<Point> pq = new PriorityQueue<Point>(comp);//
        Point current = start;//start at the start
        // Remove the duplicate declaration of distanceMap
        // Map<Point, Double> distanceMap = new HashMap<>();
        // Map<Point, Double> distanceMap = new HashMap<>();
        for (Point point : myMap.keySet()) {
            if (point.equals(start)) {
                distanceMap.put(point, 0.0); // Set distance of start point to 0
            } else {
                distanceMap.put(point, Double.MAX_VALUE); // Set distance of other points to a large value
            }
        }
        pq.add(current);//

        while (pq.size() > 0){
            current = pq.remove();//get the next point to visit
            if (current.equals(end)){
                break;//if the current point is the end
            }
            for(Point p: myMap.get(current)){//for each neighbor of the current point
                double weight = current.distance(p);//get the weight of the edge
                double newDistance = distanceMap.get(p) + weight;//get the distance from the start to the neighbor
                if(newDistance < distanceMap.get(p)){//if the new distance is less than the current distance
                    distanceMap.put(p, newDistance);//update the distance map
                    predMap.put(p, current);//update the recon path
                    pq.add(p);//add the neighbor to the priority queue
                }
            }
            
        }
        List<Point> shortestPath = new ArrayList<>();
        Point point = end;
        while (point != null) {
            shortestPath.add(0, point);
            point = predMap.get(point);
        }

        return shortestPath;
        }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String name = "data/usa.graph";
        GraphProcessor gp = new GraphProcessor();
        gp.initialize(new FileInputStream(name));
        System.out.println("running GraphProcessor");
    }


    
}
