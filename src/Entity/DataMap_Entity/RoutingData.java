
package Entity.DataMap_Entity;
import com.graphhopper.util.PointList; //import the graphhopper lib in part 3, use for PointList
/**
 *
 * @author Loo Alex
 * RoutingData will hold the respective properties between 2 points, PointList is a class from Graphhopper that will 
 */
public class RoutingData {
        
    private double distance;
    private String turnDescription;
    private PointList pointList;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTurnDescription() {
        return turnDescription;
    }

    public void setTurnDescription(String turnDescription) {
        this.turnDescription = turnDescription;
    }

    public PointList getPointList() {
        return pointList;
    }

    public void setPointList(PointList pointList) {
        this.pointList = pointList;
    }

    public RoutingData(double distance, String turnDescription, PointList pointList) {
        this.distance = distance;
        this.turnDescription = turnDescription;
        this.pointList = pointList;
    }

    public RoutingData() {
    }
    

    
         
}
