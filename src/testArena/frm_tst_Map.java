
package testArena;
import Entities.GUI_Entities.Waypoint.IEventWaypoint;
import Entities.GUI_Entities.Waypoint.WaypointRenderer; //for waypoints entities
import Entities.GUI_Entities.Waypoint.MyWaypoint;
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

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class frm_tst_Map extends javax.swing.JFrame {
    //Var
    private final Set<MyWaypoint> waypoints = new HashSet<>();
    private IEventWaypoint Ievent;
    //main
    public frm_tst_Map() {

        initComponents(); // InitGUI
        initMap();
       
    }
    
    //-----Functions START
    
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
        GeoPosition geo = new GeoPosition(-20.2715747,57.4740515);
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
        WaypointPainter<MyWaypoint> wp = new WaypointRenderer(); //cuz WaypointRenderer extends to WaypointPainter.
        wp.setWaypoints(waypoints); //the final static Hashset of Waypoint above
        //<<means the WaypointPainter will act as if a panel component , hence the waypoints 
        //in the HashSet are created on top of the jxMapViewer >>
        jXMapViewer.setOverlayPainter(wp); 
        
        for(MyWaypoint point: waypoints){
            jXMapViewer.add(point.getButton()); 
        }
                   
    }
    
    private void clearWaypoint(){
        
        for(MyWaypoint point: waypoints){
            jXMapViewer.remove(point.getButton()); 
        }
        waypoints.clear();
        initWaypoint();
    }
    
    public void addWaypoint(MyWaypoint waypoint){
        
        //first it remove to then add, meaning it will reset the list each time we add a waypoint, change that later
        for(MyWaypoint point: waypoints){
            jXMapViewer.remove(point.getButton()); 
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

        jXMapViewer = new org.jxmapviewer.JXMapViewer();
        cboMapType = new javax.swing.JComboBox<>();
        btnAddWaypoints = new javax.swing.JButton();
        btnClearWaypoints = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cboMapType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Open Street", "Virtual Earth", "Hybrid", "Satelite" }));
        cboMapType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMapTypeActionPerformed(evt);
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

        javax.swing.GroupLayout jXMapViewerLayout = new javax.swing.GroupLayout(jXMapViewer);
        jXMapViewer.setLayout(jXMapViewerLayout);
        jXMapViewerLayout.setHorizontalGroup(
            jXMapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXMapViewerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddWaypoints)
                .addGap(18, 18, 18)
                .addComponent(btnClearWaypoints)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 521, Short.MAX_VALUE)
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
                .addContainerGap(498, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXMapViewer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jXMapViewer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        addWaypoint(new MyWaypoint("Test 001",Ievent,new GeoPosition(-20.240089, 57.514709)));
        addWaypoint(new MyWaypoint("Test 002",Ievent,new GeoPosition(-20.22892482190348, 57.46517382802314)));
    }//GEN-LAST:event_btnAddWaypointsActionPerformed

    private void btnClearWaypointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearWaypointsActionPerformed
        clearWaypoint();
        
    }//GEN-LAST:event_btnClearWaypointsActionPerformed

    
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
    private org.jxmapviewer.JXMapViewer jXMapViewer;
    // End of variables declaration//GEN-END:variables
}
