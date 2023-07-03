package Entity.GUI_Entity.DataMap;

import Entity.DataMap_Entity.RoutingData;
import com.graphhopper.util.shapes.GHPoint3D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JFrame;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Loo Alex
 */
public class JXMapViewerCustom extends JXMapViewer{
    private boolean first = true ;
    private List<RoutingData> routingData;

    public List<RoutingData> getRoutingData() {
        return routingData;
    }

    public void setRoutingData(List<RoutingData> routingData) {
        this.routingData = routingData;
        repaint();
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        //errors i did, it was protected void paintComponent(graphics g) now public voic paintCompents
        //2. second error is routingData.isEmpty() it should have been !routingData.isEmpty
        
        if(routingData != null && !routingData.isEmpty()) {
        //if list is initialize but empty
            Graphics2D g2d = (Graphics2D)g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Path2D p2D = new Path2D.Double();
            first = true;
            for(RoutingData d : routingData){
                draw(p2D, d);
            }
            g2d.setColor(new Color(28,23,255));
            g2d.setStroke(new BasicStroke (5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            g2d.draw(p2D);
            
            g2d.dispose();
        
        }
    }
    
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        if (routingData != null && !routingData.isEmpty()) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            Path2D p2 = new Path2D.Double();
//            first = true;
//            for (RoutingData d : routingData) {
//                draw(p2, d);
//            }
//            g2.setColor(new Color(28, 23, 255));
//            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//            g2.draw(p2);
//            g2.dispose();
//        }
//    }
    
    private void draw (Path2D p2D,RoutingData d){
        //this functin will take RoutingData that have all the PointList for map,
        //then for each Point in that PointList, they will have a GHPoint3D, a GraphHopper3D, that we use to get the 
        //lat and lon of that point, we then convert those lat and lon into x and y coordinates and create this as a Point2D
        //if it is the first point in the PointList then we make this the starting position by moving the p2d(path2d) we parsed as arg
        //to that x y coordinate of var "point"( lat and lon coverted)
        //else it is a second or third point that  we will create a line from the start point of p2D path2D cuz remember we move 
        //its starting point to first point in list. so any point after that we just "draw" line to that new coordinates
        d.getPointList().forEach(new Consumer<GHPoint3D>(){
            @Override
            public void accept(GHPoint3D t) {
                Point2D point = convertGeoPositionToPoint(new GeoPosition(t.getLat(), t.getLon()));
                if(first){
                    first = false;
                    p2D.moveTo(point.getX(), point.getY()); 
                    //if first point(start) then mov the p2D the marking there
 
                }else{
                    p2D.lineTo(point.getX(), point.getY());
                    //if it is not first point(start), then create a line to that second point
                    
                }
            }
           
        });
        
    }
    
    
}
