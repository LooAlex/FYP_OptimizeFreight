
package testArena;

import Entity.GUI_Entity.Waypoint.IEventWaypoint;
import Entity.GUI_Entity.Waypoint.WaypointRenderer; //for waypoints entities
import Entity.GUI_Entity.Waypoint.MyWaypoint;

import Entity.DataMap_Entity.RoutingData;
import BLL.RoutingService;
        
import java.awt.Point;
import java.util.HashSet; //for waypoint set
import java.util.Set;//for waypoint set
import org.jxmapviewer.OSMTileFactoryInfo; //for map type and layground to create maptiles to be visible
import org.jxmapviewer.input.PanMouseInputListener; //for moving mouse event
import org.jxmapviewer.viewer.DefaultTileFactory; //the printer tip that instantiate and renders tilemaps on the map
import org.jxmapviewer.viewer.GeoPosition; //to use the google map coords to generate map
import org.jxmapviewer.viewer.TileFactoryInfo; //to get different properties for a tilemap to be useful
//import org.jxmapviewer.viewer.*; //for DefaultTileFactory,GeoPosition,TileFactoryInfo
import javax.swing.event.*; //for mouse
import org.jxmapviewer.VirtualEarthTileFactoryInfo;//for different map type
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter; //for zoom in out using mouse wheel
import org.jxmapviewer.viewer.WaypointPainter;// for init waypoints set and allowing rendering etc when moving or zooming.

// for list routing
import java.util.ArrayList; 
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class frm_tst_Map extends javax.swing.JFrame {
    //Var
    private final Set<MyWaypoint> waypoints = new HashSet<>(); //hashset does not allow any dupps
    private List<RoutingData> routingData = new ArrayList<>();//added in Pt3// to have routes displayed
    private IEventWaypoint Ievent;
    private Point mousePosition;  // get the current mouse position on gui
    //main
    public frm_tst_Map() {

        initComponents(); // InitGUI
        initMap();
       
    }
    
    //-----Functions START
    //NOTE as from part 3 the jxMapViewer is not the same from org.jxmapviewer.JXMapViewer,
    //we are using the JXMapViewerCustom that we created.
    
    public void initMap(){
        //<< OSMTileFactoryInfo has all the respective data, and attribute from API for the map
        // TileFactoryInfo contains info of 1 tilemap, the how much it zoom , how it change each tiles etc.>>
        TileFactoryInfo info = new OSMTileFactoryInfo(); 
        
        //<< DefaultTileFactory will then take the "info" after all initialization of class of 1 tilemap and allow to invoke it>>
        DefaultTileFactory tileFactory  = new DefaultTileFactory(info); 
        //<<jxMapViewer.setTileFactory will thent take the tileFactory and create each tiles in the Map for the size the MapView is>>
        jXMapViewer.setTileFactory(tileFactory);
        
        //<<Next we will be taking the geoposition using google map to trial it out
        //<<Use GeoPosition to get  convert the lat and long of a  specific point then use the setAddressLocation to that point>>
        GeoPosition geo = new GeoPosition(12.943926, 105.036671);
        jXMapViewer.setAddressLocation(geo);
        
        //<<use set zoom to the appropriate size for my frame size, this shows how close we are to the geo location from satelite>>
        jXMapViewer.setZoom(12); // [6:can view street] [8:part of MRU] [10:shows the island] [12:shows reunion + MRU]
        
        //--Creating event mouse move.
        
        //<<PanMouseInputListener has properties that allows the swing.MouseInputListener to zoom, click and pan on the jxmapviewer>>
        MouseInputListener mn = new PanMouseInputListener(jXMapViewer); 
        jXMapViewer.addMouseListener(mn); 
        //addMouseMotionListener code allow the JxMapViewer to register the movement of the mouse and reflect the appropriate respond on the map
        jXMapViewer.addMouseMotionListener(mn);
        //addMouseWheelListener code allow the JxMapViewer to register the mousewheel movement and reflect the appropriate respond on the map
        jXMapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jXMapViewer));
        
        //
        Ievent = getEvent();
    }

    public void initWaypoint(){
        //Init WaypointSets 
        WaypointPainter<MyWaypoint> wp = new WaypointRenderer();
        wp.setWaypoints(waypoints);
        jXMapViewer.setOverlayPainter(wp); 
        
        for(MyWaypoint point: waypoints){
            jXMapViewer.add(point.getButton()); 
        }
        
        //Routing Data.
        if(waypoints.size() == 2){ //only has a START and an END
            GeoPosition start = null;
            GeoPosition end = null;
            
            for(MyWaypoint w : waypoints){
                if(w.getPointType() == MyWaypoint.PointType.START){
                    start = w.getPosition();
                }else if (w.getPointType() == MyWaypoint.PointType.END){
                    end = w.getPosition();
                }
            }
            
            if(start != null && end != null){
                routingData = RoutingService.getInstance().routing(
                        start.getLatitude(), start.getLongitude(), 
                        end.getLatitude(), end.getLongitude(
                        )); //its 1 LOC i broke it down to make it easier to read
            }else{
                //if start and end null, then clear the current RoutingData list
                routingData.clear();
            }
            // by sending the routing data to setRoutingData, we draw it.
            jXMapViewer.setRoutingData(routingData); 
            
        }
                   
    }
    
    private void clearWaypoint(){
        
        for(MyWaypoint point: waypoints){
            jXMapViewer.remove(point.getButton()); 
        }
        routingData.clear();
        waypoints.clear();
        initWaypoint();
    }
    
    public void addWaypoint(MyWaypoint waypoint){
        
        //each time we add, we remove all waypoint's waypointButton, we do not remove the waypoint, they remain, only there
        //buttons are removed so that we dont see and click on them but their coordinates and etc remain on the map, invisible
        for(MyWaypoint point: waypoints){ //remove all button (no visible waypoint)
            jXMapViewer.remove(point.getButton()); 
        }
        Iterator<MyWaypoint> iter = waypoints.iterator();
        
        while(iter.hasNext()){ //iterate through all the "invisible waypoints"
           
            //check if the to be added waypoint's pointType, matches any of the existing waypoints'pointType
            //in the hashset. 
            if(iter.next().getPointType() == waypoint.getPointType()){ 
                //if it is then remove that waypoint, because if the current waypoint's pointType is START 
                //and we have another different waypoint in the hashset with pointType "START" then it will create 
                //problem, as there can be only 1 START and 1 END per route
                iter.remove();
            }
        }
        waypoints.add(waypoint);
        initWaypoint();
        
    }
    //this function is use upon init map
    public IEventWaypoint getEvent (){
        //overidden abstract functions selected from IEventWaypoint
        //functions selected was defined here, and in short will initialize the "IEventWaypoint" abstract function with a defined function
        //that when used later when creating MyWaypoints objects will show up that waypoint name.
        return new IEventWaypoint() {
            @Override
            public void selected(MyWaypoint waypoint) {
                JOptionPane.showMessageDialog(frm_tst_Map.this, waypoint.getName());
            }
        };
            
    }
            
    
    //-----Functions END
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmnChoosePointType = new javax.swing.JPopupMenu();
        menuStart = new javax.swing.JMenuItem();
        menuEnd = new javax.swing.JMenuItem();
        jXMapViewer = new Entity.GUI_Entity.DataMap.JXMapViewerCustom();
        btnAddWaypoints = new javax.swing.JButton();
        btnClearWaypoints = new javax.swing.JButton();
        cboMapType = new javax.swing.JComboBox<>();

        menuStart.setText("Start");
        menuStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStartActionPerformed(evt);
            }
        });
        pmnChoosePointType.add(menuStart);

        menuEnd.setText("End");
        menuEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEndActionPerformed(evt);
            }
        });
        pmnChoosePointType.add(menuEnd);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jXMapViewer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jXMapViewerMouseReleased(evt);
            }
        });

        btnAddWaypoints.setText("Add Waypoint");
        btnAddWaypoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddWaypointsActionPerformed(evt);
            }
        });

        btnClearWaypoints.setText("Clear Waypoint");
        btnClearWaypoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearWaypointsActionPerformed(evt);
            }
        });

        cboMapType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Open Street", "Virtual Earth", "Hybrid", "Satelite" }));
        cboMapType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMapTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jXMapViewerLayout = new javax.swing.GroupLayout(jXMapViewer);
        jXMapViewer.setLayout(jXMapViewerLayout);
        jXMapViewerLayout.setHorizontalGroup(
            jXMapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXMapViewerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddWaypoints)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearWaypoints)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 645, Short.MAX_VALUE)
                .addComponent(cboMapType, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jXMapViewerLayout.setVerticalGroup(
            jXMapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXMapViewerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXMapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMapType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddWaypoints)
                    .addComponent(btnClearWaypoints))
                .addContainerGap(518, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXMapViewer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXMapViewer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cboMapTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMapTypeActionPerformed
        //<<TileFactoryInfo is incharge of getting properties of different Map type and their colors etc like the brain of a printer>>
        TileFactoryInfo info; 
        
        int selectedIndex = cboMapType.getSelectedIndex();
        
        if(selectedIndex == 0){
            info = new OSMTileFactoryInfo();
        }else if(selectedIndex == 1){
            info = new  VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        }else if(selectedIndex == 2){
            info = new  VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
        }else{//else its 3
            info = new  VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
        }
        
        //<<DefaultTileFactory will then set the map to be invokable aka instantiated, the print tip of printer>>
        DefaultTileFactory tileFactory = new DefaultTileFactory(info); 
        //<<by setting the tileFactory its like giving the printer its printer tips to print on the JMapFrame>>
        jXMapViewer.setTileFactory(tileFactory); 
    }//GEN-LAST:event_cboMapTypeActionPerformed

    private void btnAddWaypointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddWaypointsActionPerformed
        
    }//GEN-LAST:event_btnAddWaypointsActionPerformed

    private void btnClearWaypointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearWaypointsActionPerformed
        clearWaypoint();
        
    }//GEN-LAST:event_btnClearWaypointsActionPerformed

    private void menuEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEndActionPerformed
        // MENUE: END Waypoint:
        GeoPosition geop = jXMapViewer.convertPointToGeoPosition(mousePosition);
        MyWaypoint waypoint  = new MyWaypoint("End Location", MyWaypoint.PointType.END, Ievent, new GeoPosition(geop.getLatitude(),geop.getLongitude()));
        addWaypoint(waypoint);
    }//GEN-LAST:event_menuEndActionPerformed

    private void menuStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStartActionPerformed
        // MENUE: START Waypoint:
        GeoPosition geop = jXMapViewer.convertPointToGeoPosition(mousePosition);
        MyWaypoint waypoint  = new MyWaypoint("Start Location", MyWaypoint.PointType.START, Ievent, new GeoPosition(geop.getLatitude(),geop.getLongitude()));
        addWaypoint(waypoint);
    }//GEN-LAST:event_menuStartActionPerformed

    //we did this by going design, click on the map, choose Event, mouse, MouseRelease
    private void jXMapViewerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jXMapViewerMouseReleased
        if(SwingUtilities.isRightMouseButton(evt)){
            mousePosition = evt.getPoint();
            //jpopupMenuVar.show(whatPanelToInstantiateFrom,X-CoordinateToPopUp,Y-CoordinateToPopUp)
            pmnChoosePointType.show(jXMapViewer, evt.getX(), evt.getY()); 
            
        }
    }//GEN-LAST:event_jXMapViewerMouseReleased

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm_tst_Map.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_tst_Map.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_tst_Map.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_tst_Map.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_tst_Map().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddWaypoints;
    private javax.swing.JButton btnClearWaypoints;
    private javax.swing.JComboBox<String> cboMapType;
    private Entity.GUI_Entity.DataMap.JXMapViewerCustom jXMapViewer;
    private javax.swing.JMenuItem menuEnd;
    private javax.swing.JMenuItem menuStart;
    private javax.swing.JPopupMenu pmnChoosePointType;
    // End of variables declaration//GEN-END:variables
}
