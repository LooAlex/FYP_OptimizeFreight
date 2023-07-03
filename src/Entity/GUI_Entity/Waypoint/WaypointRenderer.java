package Entity.GUI_Entity.Waypoint;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author Loo Alex
 * class to render the waypoint when zooming in or out of map
 */ 
public class WaypointRenderer extends WaypointPainter<MyWaypoint>
{

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        //<<getWaypoints is an inherited function from WaypointPainter that has a set of waypoint class ready
        //When this set is initialized the getWaypoints will returnt that set>>
       for (MyWaypoint wp : getWaypoints()){
           
           //<<Point2D is another class from Ra Ven Library, simply put its a dot on the map with a coordinate>>
           //<<geoToPixel return  coordinate of the current waypoint wp and the respective map zoom level,
           //this allow the waypoint to remain same size and coordinate on map despite zoom in and out>>
           Point2D p = map.getTileFactory().geoToPixel(wp.getPosition(),map.getZoom());
           Rectangle rec = map.getViewportBounds();
           //<<assuming p.getX() greater than rec.getX where rec.getX() means point of origin of Jframe/JPanel that contains the map
           //ex: if guiContainer that have map inside start on JFrame at point 0,0 then x,y of rec = 0,0 
           //  : if guiContainer start at 10, 30, then origin of container = 10,30
           //  : NOTE: Remember all gui point of origin is top left of itself. thats where they start.>>
           int x = (int)(p.getX() - rec.getX()); 
           int y = (int)(p.getY() - rec.getY());//<<same principle of rec.getX() applies here for rec.getY()>>
           //hence x and y var represent the "GUI_Coordinate" of a Waypoint on the Screen and not the Map Coordinate?
           JButton cmd = wp.getButton();
           cmd.setLocation(x - cmd.getWidth()/2, y - cmd.getHeight());
           //setLocation() is a function for any GUI where we set the new GUI component at that GUI_Coordinate
           //x - cmd.getWidth()/2 || means place JButton cmd's X-axis'middle on to the Point2D coords, base on the button width of current waypoint, 
           //y - cmd.getHeight() || means place JButton cmd above the point2D Y coords
           // inshort it will be horizontally centered with the x coords of the point2D but above the point
           //eg [ b u t t on ] <-- button with "myPin.png"
           //         [o]       <-- the current Point2D
           
       }
    }
    
}
