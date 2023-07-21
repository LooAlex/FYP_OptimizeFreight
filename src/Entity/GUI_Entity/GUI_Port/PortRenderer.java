/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.GUI_Entity.GUI_Port;

import Entity.PortDTO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author Loo Alex
 * Render the port where it should base on the frame of its JXMapViewer and actual GeoPosition
 */
public class PortRenderer extends WaypointPainter<PortDTO>{

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        for(PortDTO prt : getWaypoints()){
            Point2D p2d = map.getTileFactory().geoToPixel(prt.getPosition(), map.getZoom());//get size and position of the port.
            Rectangle rec = map.getViewportBounds(); //JxMapViewerCustom size of Panel
            
            int x = (int)(p2d.getX() - rec.getX());
            int y = (int)(p2d.getY() - rec.getY());
            
            JButton cmd = prt.getButton();
            cmd.setLocation(x-cmd.getWidth()/2, y - cmd.getHeight()); //x place button in middle of point, y place it above point, together = just above point n the middle 
                    
        }
            
    }
    
    
     
}
