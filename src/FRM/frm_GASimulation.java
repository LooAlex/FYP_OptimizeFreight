/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FRM;

import BLL.RoutingService;
import DLL.*;
import BLL.*;
import Core.CoreData;
import Core.CoreFunctions;
import Entity.DataMap_Entity.RoutingData;
import Entity.GUI_Entity.GUI_Port.IEventPortWaypoint;
import Entity.GUI_Entity.GUI_Port.PortRenderer;
import testArena.tuto_map.GUI_Waypoint.WaypointRenderer;
import Entity.PortDTO;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author Loo Alex
 * New Window, accessed from frm_SimulationPanel_Main
 */
public class frm_GASimulation extends javax.swing.JFrame {
    private Set<PortDTO> myPorts = new HashSet<>();
    private List<RoutingData> myRoutingData = new ArrayList<>();
    private IEventPortWaypoint IPEvent;
    private Point mousePosition;
    
    public String classDirectory = CoreData.classDirectory;
    public String Data_ResourceFilePath = CoreData.Data_ResourceFilePath;
    
    public DLL_InitAll dll_Init;
    /**
     * Creates new form frm_GASimulation
     */
    public frm_GASimulation() {
        dll_Init = new DLL_InitAll();
        initComponents();
        initMap();
    }
    
    public void initMap(){
        //using Virtual Earth
        TileFactoryInfo info = new  VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jxMapViewer_Simulation.setTileFactory(tileFactory);
        
        //setting open position of the map
        GeoPosition geo = new GeoPosition(-20.154097381559236, 57.498836018696394);
        jxMapViewer_Simulation.setAddressLocation(geo);
        
        //Zoom [6:street] [8:part of MRU] [10: MRU] [12:reunion + MRU] 14[Madagascar] 16[the world]
        jxMapViewer_Simulation.setZoom(15);
        
        //MouseEvent
        MouseInputListener mn = new PanMouseInputListener(jxMapViewer_Simulation);
        jxMapViewer_Simulation.addMouseListener(mn);
        jxMapViewer_Simulation.addMouseMotionListener(mn);
        jxMapViewer_Simulation.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jxMapViewer_Simulation));
        
        //init EventPort
        IPEvent = getEvent();
        //OSM = 0, VEMap = 1, VEHybrid = 2, VESatelite = 3
        cboMapType.setSelectedIndex(1);
    }
  
    public void initPort(){
    //Init Portset
        //Init PortDisplayButton
        WaypointPainter<PortDTO> wp = new PortRenderer();
        wp.setWaypoints(myPorts);
        jxMapViewer_Simulation.setOverlayPainter(wp);
        //Add PortButton to map
        for(PortDTO prt : myPorts){
            jxMapViewer_Simulation.add(prt.getButton());
        }
        
        //RoutingData
//        if(myPorts.size() == 2){//has START and END
//            GeoPosition start = null;
//            GeoPosition end = null;
//            
//            for (PortDTO prt : myPorts){
//                if(prt.getPointType() == PortDTO.PointType.START){
//                    start = prt.getPosition();
//                }else if (prt.getPointType() == PortDTO.PointType.END){
//                    end = prt.getPosition();
//                }
//            }
//            
//            //Getting RoutingData here - call BLL.
//            if(start != null && end != null){
//                
//                myRoutingData = RoutingService.getInstance().routing(
//                        start.getLatitude(), start.getLongitude(), 
//                        end.getLatitude(), end.getLongitude());
//                
//            }else{
//                myRoutingData.clear();
//            }
//            jxMapViewer_Simulation.setRoutingData(myRoutingData);
//        }
        
    }
    
    public void clearPort(){
        for(PortDTO p : myPorts){
            jxMapViewer_Simulation.remove(p.getButton());
        }
        myRoutingData.clear();
        myPorts.clear();
        initPort();
    }
    
    public void addPort (PortDTO port){
        for(PortDTO p : myPorts){
            jxMapViewer_Simulation.remove(p.getButton());
        }
        
        Iterator<PortDTO> iter = myPorts.iterator();
        while(iter.hasNext()){
            if(iter.next().getPointType() == port.getPointType()){
                iter.remove();
            }
        }
        
        myPorts.add(port);
        getPorts(); //fill the myPorts with new ports
        initPort();
        
        
    }
    
    public IEventPortWaypoint getEvent(){
        return new IEventPortWaypoint(){
            @Override
            public void selected(PortDTO port) {
                JOptionPane.showMessageDialog(frm_GASimulation.this.tabMap,port.getPortName());
            }
        };
    }
    
    public void getPorts(){
        //csv file name
        //all worlds ports      ->  portsLinerlib.csv
        //Indian Opcean ports   ->  portsLinerlibFiltered.csv
           
//        String currentFilePath = classDirectory + Data_ResourceFilePath + "/Port_20-07-23.csv";
//        var result = CoreFunctions.readCSV(currentFilePath, ",",true);
//        if (result.errors.hasErrors){
//            System.out.println("ErrorsMessage:" + result.errors.errorMessages.toString());
//        }else{
//            var lst = result.Data; //expecting List<String[]>
//            System.out.println("Result: ");
//            
//            for( var arr : lst){
//                myPorts.add(new PortDTO(arr,IPEvent));
//            }
//            
//        }
        var result = dll_Init.getPorts(IPEvent);
         if (result.errors.hasErrors){
            System.out.println("ErrorsMessage:" + result.errors.errorMessages.toString());
        }else{
             var lst = result.Data; //expecting List<String[]>
           for(var p : lst){
               myPorts.add(p);
           }
            System.out.println("Result: ");
            
        }
         
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmnChoosePointType = new javax.swing.JPopupMenu();
        mnStart = new javax.swing.JMenuItem();
        mnEnd = new javax.swing.JMenuItem();
        tabOutputDetail = new javax.swing.JTabbedPane();
        tabMap = new javax.swing.JPanel();
        jxMapViewer_Simulation = new Entity.GUI_Entity.DataMap.JXMapViewerCustom();
        btnAddPort = new javax.swing.JButton();
        btnClearPort = new javax.swing.JButton();
        cboMapType = new javax.swing.JComboBox<>();
        tabPortOutputDetail = new javax.swing.JPanel();
        tabShipRouteDetail = new javax.swing.JPanel();
        OutputPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpShipSelection = new javax.swing.JPanel();
        lblShipCode = new javax.swing.JLabel();
        lblShipDescription = new javax.swing.JLabel();
        lblShipCategory = new javax.swing.JLabel();
        lblShipBunkerCapacity = new javax.swing.JLabel();
        lblShipCapacity = new javax.swing.JLabel();
        lblIdealSpeed = new javax.swing.JLabel();
        lblMinSpeed = new javax.swing.JLabel();
        lblMaxSpeed = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cboShipCode = new javax.swing.JComboBox<>();
        txtShipCapacity = new javax.swing.JTextField();
        txtIdealSpeed = new javax.swing.JTextField();
        txtMinSpeed = new javax.swing.JTextField();
        txtMaxSpeed = new javax.swing.JTextField();
        txtShipDescription = new javax.swing.JTextField();
        txtShipCategory = new javax.swing.JTextField();
        txtShipBunkerCapacity = new javax.swing.JTextField();
        jpDisplayStatus = new javax.swing.JPanel();
        lblDisplayStatus = new javax.swing.JLabel();
        txtFuelBunkered = new javax.swing.JTextField();
        lblChosenSpeed = new javax.swing.JLabel();
        lblPortTo = new javax.swing.JLabel();
        lblTravelFuelCost = new javax.swing.JLabel();
        txtChosenSpeed = new javax.swing.JTextField();
        txtPortTo = new javax.swing.JTextField();
        lblShipBunkerLevel = new javax.swing.JLabel();
        lblPortFromSelected = new javax.swing.JLabel();
        txtLoadedCapacity = new javax.swing.JTextField();
        lblFuelBunkered = new javax.swing.JLabel();
        lblFuelBunkerCost = new javax.swing.JLabel();
        txtShipBunkerLevel = new javax.swing.JTextField();
        cboPortFromSelected = new javax.swing.JComboBox<>();
        lblLoadedCapacity = new javax.swing.JLabel();
        txtFuelBunkerCost = new javax.swing.JTextField();
        txtTravelFuelCost = new javax.swing.JTextField();
        lblOperationalCost = new javax.swing.JLabel();
        txtOperationalCost = new javax.swing.JTextField();
        lblTimeTaken = new javax.swing.JLabel();
        txtTimeTaken3 = new javax.swing.JTextField();
        lblDistance = new javax.swing.JLabel();
        txtDistance = new javax.swing.JTextField();
        jpPortSelection = new javax.swing.JPanel();
        TitlePortSelection = new javax.swing.JLabel();
        jpPortComboArea = new javax.swing.JPanel();
        jpRouteDisplay = new javax.swing.JPanel();

        mnStart.setText("Start");
        mnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnStartActionPerformed(evt);
            }
        });
        pmnChoosePointType.add(mnStart);

        mnEnd.setText("End");
        mnEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEndActionPerformed(evt);
            }
        });
        pmnChoosePointType.add(mnEnd);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("frameGASimulation"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1500, 800));

        tabMap.setName(""); // NOI18N
        tabMap.setPreferredSize(new java.awt.Dimension(800, 458));

        jxMapViewer_Simulation.setPreferredSize(new java.awt.Dimension(800, 452));
        jxMapViewer_Simulation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jxMapViewer_SimulationMouseReleased(evt);
            }
        });

        btnAddPort.setText("Add Port");
        btnAddPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPortActionPerformed(evt);
            }
        });

        btnClearPort.setText("Clear Port");
        btnClearPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPortActionPerformed(evt);
            }
        });

        cboMapType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Open Street", "Virtual Earth", "Hybrid", "Satelite" }));
        cboMapType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMapTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jxMapViewer_SimulationLayout = new javax.swing.GroupLayout(jxMapViewer_Simulation);
        jxMapViewer_Simulation.setLayout(jxMapViewer_SimulationLayout);
        jxMapViewer_SimulationLayout.setHorizontalGroup(
            jxMapViewer_SimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jxMapViewer_SimulationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddPort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearPort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 544, Short.MAX_VALUE)
                .addComponent(cboMapType, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jxMapViewer_SimulationLayout.setVerticalGroup(
            jxMapViewer_SimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jxMapViewer_SimulationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jxMapViewer_SimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddPort)
                    .addComponent(btnClearPort)
                    .addComponent(cboMapType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(425, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabMapLayout = new javax.swing.GroupLayout(tabMap);
        tabMap.setLayout(tabMapLayout);
        tabMapLayout.setHorizontalGroup(
            tabMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jxMapViewer_Simulation, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
        );
        tabMapLayout.setVerticalGroup(
            tabMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jxMapViewer_Simulation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );

        tabOutputDetail.addTab("Map", tabMap);

        tabPortOutputDetail.setPreferredSize(new java.awt.Dimension(800, 458));

        javax.swing.GroupLayout tabPortOutputDetailLayout = new javax.swing.GroupLayout(tabPortOutputDetail);
        tabPortOutputDetail.setLayout(tabPortOutputDetailLayout);
        tabPortOutputDetailLayout.setHorizontalGroup(
            tabPortOutputDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        tabPortOutputDetailLayout.setVerticalGroup(
            tabPortOutputDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );

        tabOutputDetail.addTab("Ports Detail", tabPortOutputDetail);

        tabShipRouteDetail.setPreferredSize(new java.awt.Dimension(800, 458));

        javax.swing.GroupLayout tabShipRouteDetailLayout = new javax.swing.GroupLayout(tabShipRouteDetail);
        tabShipRouteDetail.setLayout(tabShipRouteDetailLayout);
        tabShipRouteDetailLayout.setHorizontalGroup(
            tabShipRouteDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        tabShipRouteDetailLayout.setVerticalGroup(
            tabShipRouteDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );

        tabOutputDetail.addTab("Ship Route Detail", tabShipRouteDetail);

        javax.swing.GroupLayout OutputPanelLayout = new javax.swing.GroupLayout(OutputPanel);
        OutputPanel.setLayout(OutputPanelLayout);
        OutputPanelLayout.setHorizontalGroup(
            OutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 876, Short.MAX_VALUE)
        );
        OutputPanelLayout.setVerticalGroup(
            OutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );

        jpShipSelection.setPreferredSize(new java.awt.Dimension(530, 700));

        lblShipCode.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblShipCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipCode.setText("Ship Code");

        lblShipDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipDescription.setText("Ship Description");

        lblShipCategory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipCategory.setText("Ship Category");

        lblShipBunkerCapacity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipBunkerCapacity.setText("Ship Bunker Capacity (Ton)");

        lblShipCapacity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipCapacity.setText("Ship Capacity (TEU)");

        lblIdealSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIdealSpeed.setText("Ideal Speed (kn)");

        lblMinSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinSpeed.setText("Min Speed (kn)");

        lblMaxSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxSpeed.setText("Max Speed (kn)");
        lblMaxSpeed.setFocusable(false);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Ship Selection");

        cboShipCode.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cboShipCode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboShipCode.setPreferredSize(new java.awt.Dimension(75, 26));

        txtShipCapacity.setText("jTextField1");

        txtIdealSpeed.setText("jTextField2");

        txtMinSpeed.setText("jTextField3");

        txtMaxSpeed.setText("jTextField4");

        txtShipDescription.setText("jTextField5");

        txtShipCategory.setText("jTextField6");

        txtShipBunkerCapacity.setText("jTextField7");

        jpDisplayStatus.setPreferredSize(new java.awt.Dimension(510, 450));

        lblDisplayStatus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDisplayStatus.setText("Display Status");

        txtFuelBunkered.setText("jTextField6");

        lblChosenSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChosenSpeed.setText("Chosen Speed (kn) ");
        lblChosenSpeed.setFocusable(false);

        lblPortTo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPortTo.setText("Port To");

        lblTravelFuelCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTravelFuelCost.setText("Travel Fuel Cost ($) ");

        txtChosenSpeed.setText("jTextField4");

        txtPortTo.setText("jTextField5");

        lblShipBunkerLevel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipBunkerLevel.setText("Ship Bunker Level (Ton) ");

        lblPortFromSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPortFromSelected.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPortFromSelected.setText("Port From");

        txtLoadedCapacity.setText("jTextField1");

        lblFuelBunkered.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFuelBunkered.setText("Fuel Bunkered (Ton)");

        lblFuelBunkerCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFuelBunkerCost.setText("Fuel Bunker Cost  ($)");

        txtShipBunkerLevel.setText("jTextField2");

        cboPortFromSelected.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboPortFromSelected.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboPortFromSelected.setPreferredSize(new java.awt.Dimension(75, 26));

        lblLoadedCapacity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoadedCapacity.setText("Loaded Capacity (TEU) ");

        txtFuelBunkerCost.setText("jTextField7");

        txtTravelFuelCost.setText("jTextField3");

        lblOperationalCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOperationalCost.setText("Operational Cost ($) ");

        txtOperationalCost.setText("jTextField1");

        lblTimeTaken.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTimeTaken.setText("Time Taken (Hr) ");

        txtTimeTaken3.setText("jTextField2");

        lblDistance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDistance.setText("Distance (nmi) ");

        txtDistance.setText("jTextField3");

        javax.swing.GroupLayout jpDisplayStatusLayout = new javax.swing.GroupLayout(jpDisplayStatus);
        jpDisplayStatus.setLayout(jpDisplayStatusLayout);
        jpDisplayStatusLayout.setHorizontalGroup(
            jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblDisplayStatus))
                    .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblOperationalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTimeTaken, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtDistance, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTimeTaken3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtOperationalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFuelBunkerCost, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLoadedCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblShipBunkerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTravelFuelCost, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblChosenSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPortFromSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPortTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblFuelBunkered, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtFuelBunkered)
                                    .addComponent(txtPortTo)
                                    .addComponent(txtFuelBunkerCost)
                                    .addComponent(cboPortFromSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtChosenSpeed)
                                    .addComponent(txtTravelFuelCost)
                                    .addComponent(txtLoadedCapacity)
                                    .addComponent(txtShipBunkerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jpDisplayStatusLayout.setVerticalGroup(
            jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblDisplayStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPortFromSelected)
                    .addComponent(cboPortFromSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPortTo)
                    .addComponent(txtPortTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFuelBunkered)
                    .addComponent(txtFuelBunkered, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFuelBunkerCost)
                    .addComponent(txtFuelBunkerCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoadedCapacity)
                    .addComponent(txtLoadedCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShipBunkerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtShipBunkerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTravelFuelCost)
                    .addComponent(txtTravelFuelCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChosenSpeed)
                    .addComponent(txtChosenSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperationalCost)
                    .addComponent(txtOperationalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimeTaken, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimeTaken3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDistance)
                    .addComponent(txtDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpShipSelectionLayout = new javax.swing.GroupLayout(jpShipSelection);
        jpShipSelection.setLayout(jpShipSelectionLayout);
        jpShipSelectionLayout.setHorizontalGroup(
            jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpShipSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpDisplayStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpShipSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jpShipSelectionLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblShipCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblIdealSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMinSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblShipCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblShipDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblShipCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblShipBunkerCapacity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboShipCode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtShipCategory)
                    .addComponent(txtShipDescription)
                    .addComponent(txtShipBunkerCapacity)
                    .addComponent(txtShipCapacity)
                    .addComponent(txtIdealSpeed)
                    .addComponent(txtMinSpeed)
                    .addComponent(txtMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jpShipSelectionLayout.setVerticalGroup(
            jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpShipSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShipCode)
                    .addComponent(cboShipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShipDescription)
                    .addComponent(txtShipDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShipCategory)
                    .addComponent(txtShipCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShipBunkerCapacity)
                    .addComponent(txtShipBunkerCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShipCapacity)
                    .addComponent(txtShipCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdealSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdealSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMinSpeed)
                    .addComponent(txtMinSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaxSpeed)
                    .addComponent(txtMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jpDisplayStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMaxSpeed.getAccessibleContext().setAccessibleDescription("");

        jTabbedPane1.addTab("Ship Selection", jpShipSelection);

        TitlePortSelection.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TitlePortSelection.setText("Port Selection");

        javax.swing.GroupLayout jpPortComboAreaLayout = new javax.swing.GroupLayout(jpPortComboArea);
        jpPortComboArea.setLayout(jpPortComboAreaLayout);
        jpPortComboAreaLayout.setHorizontalGroup(
            jpPortComboAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );
        jpPortComboAreaLayout.setVerticalGroup(
            jpPortComboAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpRouteDisplayLayout = new javax.swing.GroupLayout(jpRouteDisplay);
        jpRouteDisplay.setLayout(jpRouteDisplayLayout);
        jpRouteDisplayLayout.setHorizontalGroup(
            jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpRouteDisplayLayout.setVerticalGroup(
            jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpPortSelectionLayout = new javax.swing.GroupLayout(jpPortSelection);
        jpPortSelection.setLayout(jpPortSelectionLayout);
        jpPortSelectionLayout.setHorizontalGroup(
            jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpPortComboArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpPortSelectionLayout.createSequentialGroup()
                        .addComponent(TitlePortSelection)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jpRouteDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpPortSelectionLayout.setVerticalGroup(
            jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TitlePortSelection)
                .addGap(18, 18, 18)
                .addComponent(jpPortComboArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpRouteDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Port Selection", jpPortSelection);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OutputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabOutputDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabOutputDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(OutputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPortActionPerformed
        // TODO add your handling code here:
        clearPort();
    }//GEN-LAST:event_btnClearPortActionPerformed

    private void btnAddPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPortActionPerformed
        // TODO add your handling code here:
        addPort(new PortDTO("P001","Test before Lorete",PortDTO.PointType.START,IPEvent,new GeoPosition(-20.16366002209838, 57.50698320195978)));
        addPort(new PortDTO("P002","Test before Citadel",PortDTO.PointType.END,IPEvent,new GeoPosition(-20.162778782150987, 57.507959525974705)));//change this from on top f citadele to the road before citadelle
        
        
    }//GEN-LAST:event_btnAddPortActionPerformed

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
        jxMapViewer_Simulation.setTileFactory(tileFactory); 
    }//GEN-LAST:event_cboMapTypeActionPerformed

    private void jxMapViewer_SimulationMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jxMapViewer_SimulationMouseReleased
        // TODO add your handling code here:
        if(SwingUtilities.isRightMouseButton(evt)){
            mousePosition = evt.getPoint();
            pmnChoosePointType.show(jxMapViewer_Simulation, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jxMapViewer_SimulationMouseReleased

    private void mnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnStartActionPerformed
            // MENUE: START Waypoint:
            GeoPosition geop = jxMapViewer_Simulation.convertPointToGeoPosition(mousePosition);
            addPort(new PortDTO("P001 tst", "Start location", PortDTO.PointType.START, IPEvent, new GeoPosition(geop.getLatitude(), geop.getLongitude())));
    }//GEN-LAST:event_mnStartActionPerformed

    private void mnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEndActionPerformed
            // MENUE: END Waypoint:
            GeoPosition geop = jxMapViewer_Simulation.convertPointToGeoPosition(mousePosition);
            addPort(new PortDTO("P002 tst", "End location", PortDTO.PointType.END, IPEvent, new GeoPosition(geop.getLatitude(), geop.getLongitude())));
    }//GEN-LAST:event_mnEndActionPerformed

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
            java.util.logging.Logger.getLogger(frm_GASimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_GASimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_GASimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_GASimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_GASimulation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel OutputPanel;
    private javax.swing.JLabel TitlePortSelection;
    private javax.swing.JButton btnAddPort;
    private javax.swing.JButton btnClearPort;
    private javax.swing.JComboBox<String> cboMapType;
    private javax.swing.JComboBox<String> cboPortFromSelected;
    private javax.swing.JComboBox<String> cboShipCode;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jpDisplayStatus;
    private javax.swing.JPanel jpPortComboArea;
    private javax.swing.JPanel jpPortSelection;
    private javax.swing.JPanel jpRouteDisplay;
    private javax.swing.JPanel jpShipSelection;
    private Entity.GUI_Entity.DataMap.JXMapViewerCustom jxMapViewer_Simulation;
    private javax.swing.JLabel lblChosenSpeed;
    private javax.swing.JLabel lblDisplayStatus;
    private javax.swing.JLabel lblDistance;
    private javax.swing.JLabel lblFuelBunkerCost;
    private javax.swing.JLabel lblFuelBunkered;
    private javax.swing.JLabel lblIdealSpeed;
    private javax.swing.JLabel lblLoadedCapacity;
    private javax.swing.JLabel lblMaxSpeed;
    private javax.swing.JLabel lblMinSpeed;
    private javax.swing.JLabel lblOperationalCost;
    private javax.swing.JLabel lblPortFromSelected;
    private javax.swing.JLabel lblPortTo;
    private javax.swing.JLabel lblShipBunkerCapacity;
    private javax.swing.JLabel lblShipBunkerLevel;
    private javax.swing.JLabel lblShipCapacity;
    private javax.swing.JLabel lblShipCategory;
    private javax.swing.JLabel lblShipCode;
    private javax.swing.JLabel lblShipDescription;
    private javax.swing.JLabel lblTimeTaken;
    private javax.swing.JLabel lblTravelFuelCost;
    private javax.swing.JMenuItem mnEnd;
    private javax.swing.JMenuItem mnStart;
    private javax.swing.JPopupMenu pmnChoosePointType;
    private javax.swing.JPanel tabMap;
    private javax.swing.JTabbedPane tabOutputDetail;
    private javax.swing.JPanel tabPortOutputDetail;
    private javax.swing.JPanel tabShipRouteDetail;
    private javax.swing.JTextField txtChosenSpeed;
    private javax.swing.JTextField txtDistance;
    private javax.swing.JTextField txtFuelBunkerCost;
    private javax.swing.JTextField txtFuelBunkered;
    private javax.swing.JTextField txtIdealSpeed;
    private javax.swing.JTextField txtLoadedCapacity;
    private javax.swing.JTextField txtMaxSpeed;
    private javax.swing.JTextField txtMinSpeed;
    private javax.swing.JTextField txtOperationalCost;
    private javax.swing.JTextField txtPortTo;
    private javax.swing.JTextField txtShipBunkerCapacity;
    private javax.swing.JTextField txtShipBunkerLevel;
    private javax.swing.JTextField txtShipCapacity;
    private javax.swing.JTextField txtShipCategory;
    private javax.swing.JTextField txtShipDescription;
    private javax.swing.JTextField txtTimeTaken3;
    private javax.swing.JTextField txtTravelFuelCost;
    // End of variables declaration//GEN-END:variables
}
