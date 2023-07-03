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
 * class to render the waypoints when zooming in or out of map
 */ 
public class WaypointRenderer extends WaypointPainter<MyWaypoint>
{

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {  
        
       for (MyWaypoint wp : getWaypoints()){   
           Point2D p = map.getTileFactory().geoToPixel(wp.getPosition(),map.getZoom());
           Rectangle rec = map.getViewportBounds();
         
           int x = (int)(p.getX() - rec.getX()); 
           int y = (int)(p.getY() - rec.getY());
           //x and y var represent the "GUI_Coordinate" of a Waypoint on the Screen,not on map
           
           JButton cmd = wp.getButton();
           cmd.setLocation(x - cmd.getWidth()/2, y - cmd.getHeight());
           
           //this function takes each waypoint in the set in main, 
           //add a button with a waypoint icon, on top of those waypoint position.
           
       }
    }
    
}
