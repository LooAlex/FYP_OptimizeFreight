
package Entity.DataMap_Entity;
import com.graphhopper.util.PointList; //import the graphhopper lib in part 3, use for PointList
/**
 *
 * @author Loo Alex
 * RoutingData will hold the properties between 2 specific points START and END that will allow the system to draw a path,
 * Between 2 points there can be several points, such has turns etc, those are stored as PointList in a RoutingData.
 * a PointList is an Iterable of class GHPoint3D + other properties.
 * Hence a routingData holds various points between 2 specific points that will then allow to draw path in a specific way.
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
