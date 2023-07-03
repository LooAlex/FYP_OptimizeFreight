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

    //each time this function is called, it draw paths
    public void setRoutingData(List<RoutingData> routingData) {
        this.routingData = routingData;
        repaint(); // refresh, and apply the correct painting on this JComponent(the one we are in currently)
       
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        if(routingData != null && !routingData.isEmpty()) {
        //if routingData list has routingDatas -> Draw that route base on the points in pointList available
            Graphics2D g2d = (Graphics2D)g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Path2D p2D = new Path2D.Double();
            first = true;
            for(RoutingData d : routingData){
                draw(p2D, d); //Drawing of Path
            }
            g2d.setColor(new Color(28,23,255));
            g2d.setStroke(new BasicStroke (5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            g2d.draw(p2D);
            
            g2d.dispose();
        
        }
    }
    
    
    private void draw (Path2D p2D,RoutingData d){
        d.getPointList().forEach(new Consumer<GHPoint3D>(){
            @Override
            public void accept(GHPoint3D t) {
                Point2D point = convertGeoPositionToPoint(new GeoPosition(t.getLat(), t.getLon()));
                if(first){
                    //if first point(start) then move the path2D starting position there
                    first = false;
                    p2D.moveTo(point.getX(), point.getY()); 
                    
                }else{
                    //if it is not first point(start), then create a line to that next point in the RoutingData List
                    p2D.lineTo(point.getX(), point.getY());
                    
                }
            }
           
        });
        
    }
    
    
}
