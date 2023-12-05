/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network, showing the functionality of GraphProcessor and using
 * Visualize
 * @author Owen Astrachan (preliminary)
 * TO DO: Add your name(s) as authors
 */
import java.util.*;
import java.io.*;

public class GraphDemo {
    
    /**
     * Keys in the map are locations like "Durham NC" or "Portland OR" or "Portland ME",
     * the corresponding value is the Point for that location label
     */
    private Map<String, Point> myMap;

    public GraphDemo(){
        myMap = new HashMap<>();
    }

    /**
     * This works for "uscities.csv" as the filename, or other
     * files formatted similarly
     * @param filename is the name of a properly formatted file
     * @throws IOException
     */
    public void readData(String filename) throws IOException{
        Scanner s = new Scanner(new File(filename));
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] data = line.split(",");
            String name = data[0] + " " + data[1];
            myMap.put(name, new Point(Double.parseDouble(data[2]),Double.parseDouble(data[3])));
        }
    }

    public void segmented(GraphProcessor gp, Visualize viz){
        String start = "Miami FL";
        String inter = "San Diego CA";
        String end = "Seattle WA";
        // no code here
    }

    /**
     * Modify this code to allow the user to choose cities rather than
     * having the cities hard-wired by uncommenting code 
     * @param gp
     * @param viz
     */
    public void userInteract(GraphProcessor gp,Visualize viz) {
        
        String start = "Miami FL";
        String end = "Seattle WA";

        /** remove comment for user-interaction
         
        Scanner in = new Scanner(System.in);
        System.out.print("enter source location: ");
        start = in.nextLine();
        System.out.print("enter end location: ");
        end = in.nextLine();
        in.close();

        **/

        if (! myMap.containsKey(start)){
            System.out.printf("couldn't find %s in graph\n",start);
            return;
        }
        if (! myMap.containsKey(end)){
            System.out.printf("couldn't find %s in graph\n",end);
        }
        Point nearStart = gp.nearestPoint(myMap.get(start));
        Point nearEnd = gp.nearestPoint(myMap.get(end));
        System.out.printf("found %s and %s\n",nearStart,nearEnd);
        List<Point> path = gp.route(nearStart, nearEnd);
        double dist = gp.routeDistance(path);
        System.out.printf("start: %s, end: %s\n",
                          nearStart,nearEnd);
        System.out.printf("short path has %d points\n",path.size());
        System.out.printf("short path is %2.3f in length\n",dist);
        viz.drawRoute(path);
    }
   
    public static void main(String[] args) throws IOException {
        String usaCityFile = "data/uscities.csv";

        String[] durhamData = {"images/durham.png", 
                               "data/durham.vis",
                               "data/durham.graph"};
        String[] usaData = {"images/usa.png",
                            "data/usa.vis",
                            "data/usa.graph"};

        String[] simpleData = {"images/simple.png",
                               "data/simple.vis",
                               "data/simple.graph"};

        // useThisData can point to durham, simple, or usa, modify to test
        String[] useThisData = usaData;
        
        GraphDemo gd = new GraphDemo();
        gd.readData(usaCityFile);

        GraphProcessor gp = new GraphProcessor();
        gp.initialize(new FileInputStream(useThisData[2]));
        Visualize viz = new Visualize(useThisData[1],useThisData[0]);
        gd.userInteract(gp,viz);
    }
}