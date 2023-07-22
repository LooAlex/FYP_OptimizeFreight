/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FRM;

import Algorithms.GA_Main;
import BLL.RoutingService;
import DLL.*;
import BLL.*;
import Core.*;
import Core.CoreData;
import Core.CoreFunctions;
import Entity.DataMap_Entity.RoutingData;
import Entity.GUI_Entity.DataMap.JXMapViewerCustom;
import Entity.GUI_Entity.GUI_Port.IEventPortWaypoint;
import Entity.GUI_Entity.GUI_Port.PortRenderer;
import testArena.tuto_map.GUI_Waypoint.WaypointRenderer;
import Entity.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
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
    private List<ShipCategoryDTO> myShipCats;
    private List<PortDTO> lstPorts;
       
    public Set<PortDTO> SelectedPorts = new HashSet<>();
    public ShipCategoryDTO SelectedShipCategory;
    public ContainerTypeDTO SelectedContainerType;
    
    //private List<PortDTO> SelectedPorts = new ArrayList<>();
    private List<RoutingData> myRoutingData = new ArrayList<>();
    private IEventPortWaypoint IPEvent;
    private Point mousePosition;
    
    public String classDirectory = CoreData.classDirectory;
    public String Data_ResourceFilePath = CoreData.Data_ResourceFilePath;
    
    public DLL_InitAll dll_Init;
    
    //GUI
    JCheckBox[] arrCheckBoxPort;
    /**
     * Creates new form frm_GASimulation
     */
    public frm_GASimulation() {
        dll_Init = new DLL_InitAll();
        initComponents();
        initAll();
        
    }
    public void initAll(){
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        //init EventPort
        IPEvent = getEvent();
        
        getContainerTypes();
        getShipCategorys();
        getPorts();
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
        

        //OSM = 0, VEMap = 1, VEHybrid = 2, VESatelite = 3
        cboMapType.setSelectedIndex(1);
    }
  
    public void initPort(){
    //Init Portset
        //Init PortDisplayButton
        WaypointPainter<PortDTO> wp = new PortRenderer();
        wp.setWaypoints(SelectedPorts);
        jxMapViewer_Simulation.setOverlayPainter(wp);
        //Add PortButton to map
        for(PortDTO prt : SelectedPorts){
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
        for(var c: arrCheckBoxPort){
            c.setSelected(false);
        }
        myRoutingData.clear();
        clearUIPort();
        initPort();
    }
    
    public void clearUIPort(){
        textArea.setText("");
        lblTotalPortSelected.setText("0");
         for(PortDTO p : SelectedPorts){
            jxMapViewer_Simulation.remove(p.getButton());
        }
         SelectedPorts.clear();
        
    }
    public void addPort (PortDTO port){
        for(PortDTO p : SelectedPorts){
            jxMapViewer_Simulation.remove(p.getButton());
        }
        
        Iterator<PortDTO> iter = SelectedPorts.iterator();
        while(iter.hasNext()){
            if(iter.next().getPointType() == port.getPointType()){
                iter.remove();
            }
        }
        
        SelectedPorts.add(port);
        //getContainerType
        //get
        //only choose selected ports in checkbox areas.
        //getPorts(); //db
        initPort();
        
        
    }
    
    public IEventPortWaypoint getEvent(){
        return new IEventPortWaypoint(){
            @Override
            public void selected(PortDTO port) {
                int width = 225; //max number of char per line.
               
                String html = 
                "<html><body width='%10s'>"
                + "<h1>Port</h1>"
                + "<h3>"
                + "UnLocode: "+port.UnLoCode+"<br><br>"
                + "Port Name: "+port.PortName+"<br><br>"
                + "Coords: ("+port.PortLat+","+port.PortLon+")<br><br>" 
                + "Demand Amt: "+port.demands.DemandAmt+" Ton<br><br>" 
                + "Supply Amt: "+port.demands.SupplyAmt+" Ton<br><br>" 
                + "Port Call  $"+port.PortCall_Cost+"<br><br>"
                + "Fuel Price  $"+port.Port_FuelPrice+"<br><br>"
                + "<h3>";
               
                JOptionPane.showMessageDialog(frm_GASimulation.this.tabMap,String.format(html, width,width));
                
            }
        };
    }
    
    public void getShipCategorys(){
        DLL_ShipCategory ddlShipCategory  = new DLL_ShipCategory();
        var result  = ddlShipCategory.getShipCategorys();
        if(result.Data.size()>0){
            myShipCats = result.Data;
            String[] arrShipCatCode = new String[result.Data.size()];
            
            for(int i = 0; i<result.Data.size(); i++){
                arrShipCatCode[i] = myShipCats.get(i).getCode();
            }  
            
            cboShipCode.setModel(new javax.swing.DefaultComboBoxModel<>(arrShipCatCode));
            setShipCategoryToGUIDisplay(0);
        }
        
        
        
    }
    public void setShipCategoryToGUIDisplay(int SelectedIndex){
        
        cboShipCode.setSelectedIndex(SelectedIndex);
        
        SelectedShipCategory = myShipCats.get(SelectedIndex);
        txtShipBunkerCapacity.setText(String.valueOf(SelectedShipCategory.BunkerCapacity)) ;
        txtShipCapacity.setText(String.valueOf(SelectedShipCategory.CapacityTEU));
        txtShipDescription.setText(String.valueOf(SelectedShipCategory.getDescription()));
        txtDesignSpeed.setText(String.valueOf(SelectedShipCategory.DesignSpeed));
        txtMinSpeed.setText(String.valueOf(SelectedShipCategory.MinSpeed));
        txtMaxSpeed.setText(String.valueOf(SelectedShipCategory.MaxSpeed));
        txtShipLoadUnloadTimePerContainer.setText(String.valueOf((SelectedShipCategory.TimeLoadUnLoadPerFullContainerTEU)));
        
        
    }
    
    public void getContainerTypes(){
        DLL_ContainerType dllContainerType = new DLL_ContainerType();
        var result = dllContainerType.getContainerType_ListByRow(1);
        if(result.Data != null && result.Data.size() > 0){
            SelectedContainerType = result.Data.get(0);
        }
        
    }
    public void getPorts(){

        
        DLL_Port dllport = new DLL_Port();
        var result = dllport.getPorts(IPEvent);
        
       // var result = dllport.getPort_ListByRow(1, IPEvent);
       
         if (result.errors.hasErrors){
            System.out.println("ErrorsMessage:" + result.errors.errorMessages.toString());
        }else{
             lstPorts = result.Data; //expecting List<String[]>
             
             int length = lstPorts.size();
             var PortNames = new ArrayList<String>();
            
            int displayRow =0;
           int displayCol = 2;
           int divide=(PortNames.size()%displayCol);
	    //incase "divide" is not a factor of col, 3, it means we will have 1 extra row to display
	    if(divide>0)
	    	divide+=1;
	    
	    //increment the number of rows
	    for(int i=0;i<divide;i++)
	    	displayRow++;
            
            arrCheckBoxPort = new JCheckBox[length];
            

           
           jpCheckBoxContainer.setLayout(new GridLayout(displayRow,displayCol,6,6));
           
            for(int i = 0; i<length; i++){
               
               //SelectedPorts.add(lstPorts.get(i));
               PortNames.add(lstPorts.get(i).getPortName());
               arrCheckBoxPort[i] = new JCheckBox(lstPorts.get(i).getPortName());
               
               jpCheckBoxContainer.add(new JPanel().add(arrCheckBoxPort[i]));
            }
           
         
           
           
           
        }
         
    }
    
   
    public void getPortsCSV(){
                //csv file name
        //all worlds ports      ->  portsLinerlib.csv
        //Indian Opcean ports   ->  portsLinerlibFiltered.csv
           
        String currentFilePath = classDirectory + Data_ResourceFilePath + "/Port_20-07-23.csv";
        var result = CoreFunctions.readCSV(currentFilePath, ",",true);
        if (result.errors.hasErrors){
            System.out.println("ErrorsMessage:" + result.errors.errorMessages.toString());
        }else{
            var lst = result.Data; //expecting List<String[]>
            System.out.println("Result: ");
            
            for( var arr : lst){
                SelectedPorts.add(new PortDTO(arr,IPEvent));
            }
            
        }
    }
    public frm_GASimulation(IEventPortWaypoint IPEvent, Point mousePosition, DLL_InitAll dll_Init, JPanel OutputPanel, JLabel TitlePortSelection, JButton btnAddPort, JButton btnClearPort, JComboBox<String> cboMapType, JComboBox<String> cboPortFromSelected, JComboBox<String> cboShipCode, JLabel jLabel10, JTabbedPane jTabbedPane1, JPanel jpDisplayStatus, JPanel jpPortComboArea, JPanel jpPortSelection, JPanel jpRouteDisplay, JPanel jpShipSelection, JXMapViewerCustom jxMapViewer_Simulation, JLabel lblChosenSpeed, JLabel lblDisplayStatus, JLabel lblDistance, JLabel lblFuelBunkerCost, JLabel lblFuelBunkered, JLabel lblIdealSpeed, JLabel lblLoadedCapacity, JLabel lblMaxSpeed, JLabel lblMinSpeed, JLabel lblOperationalCost, JLabel lblPortFromSelected, JLabel lblPortTo, JLabel lblShipBunkerCapacity, JLabel lblShipBunkerLevel, JLabel lblShipCapacity, JLabel lblShipCategory, JLabel lblShipCode, JLabel lblShipDescription, JLabel lblTimeTaken, JLabel lblTravelFuelCost, JMenuItem mnEnd, JMenuItem mnStart, JPopupMenu pmnChoosePointType, JPanel tabMap, JTabbedPane tabOutputDetail, JPanel tabPortOutputDetail, JPanel tabShipRouteDetail, JTextField txtChosenSpeed, JTextField txtDistance, JTextField txtFuelBunkerCost, JTextField txtFuelBunkered, JTextField txtIdealSpeed, JTextField txtLoadedCapacity, JTextField txtMaxSpeed, JTextField txtMinSpeed, JTextField txtOperationalCost, JTextField txtPortTo, JTextField txtShipBunkerCapacity, JTextField txtShipBunkerLevel, JTextField txtShipCapacity, JTextField txtShipCategory, JTextField txtShipDescription, JTextField txtTimeTaken3, JTextField txtTravelFuelCost) throws HeadlessException {
        this.IPEvent = IPEvent;
        this.mousePosition = mousePosition;
        this.dll_Init = dll_Init;
        this.OutputPanel = OutputPanel;
        this.TitlePortSelection = TitlePortSelection;
        this.btnAddPort = btnAddPort;
        this.btnClearPort = btnClearPort;
        this.cboMapType = cboMapType;
        this.cboPortFromSelected = cboPortFromSelected;
        this.cboShipCode = cboShipCode;
        this.jLabel10 = jLabel10;
        this.jTabbedPane1 = jTabbedPane1;
        this.jpDisplayStatus = jpDisplayStatus;
        this.jpPortSelection = jpPortSelection;
        this.jpRouteDisplay = jpRouteDisplay;
        this.jpShipSelection = jpShipSelection;
        this.jxMapViewer_Simulation = jxMapViewer_Simulation;
        this.lblChosenSpeed = lblChosenSpeed;
        this.lblDisplayStatus = lblDisplayStatus;
        this.lblDistance = lblDistance;
        this.lblFuelBunkerCost = lblFuelBunkerCost;
        this.lblFuelBunkered = lblFuelBunkered;
        this.lblIdealSpeed = lblIdealSpeed;
        this.lblLoadedCapacity = lblLoadedCapacity;
        this.lblMaxSpeed = lblMaxSpeed;
        this.lblMinSpeed = lblMinSpeed;
        this.lblOperationalCost = lblOperationalCost;
        this.lblPortFromSelected = lblPortFromSelected;
        this.lblPortTo = lblPortTo;
        this.lblShipBunkerCapacity = lblShipBunkerCapacity;
        this.lblShipBunkerLevel = lblShipBunkerLevel;
        this.lblShipCapacity = lblShipCapacity;
        this.lblShipLoadUnloadTimePerContainer = lblShipCategory;
        this.lblShipCode = lblShipCode;
        this.lblShipDescription = lblShipDescription;
        this.lblTimeTaken = lblTimeTaken;
        this.lblTravelFuelCost = lblTravelFuelCost;
        this.mnEnd = mnEnd;
        this.mnStart = mnStart;
        this.pmnChoosePointType = pmnChoosePointType;
        this.tabMap = tabMap;
        this.tabOutputDetail = tabOutputDetail;
        this.tabPortOutputDetail = tabPortOutputDetail;
        this.tabShipRouteDetail = tabShipRouteDetail;
        this.txtChosenSpeed = txtChosenSpeed;
        this.txtDistance = txtDistance;
        this.txtFuelBunkerCost = txtFuelBunkerCost;
        this.txtFuelBunkered = txtFuelBunkered;
        this.txtDesignSpeed = txtIdealSpeed;
        this.txtLoadedCapacity = txtLoadedCapacity;
        this.txtMaxSpeed = txtMaxSpeed;
        this.txtMinSpeed = txtMinSpeed;
        this.txtOperationalCost = txtOperationalCost;
        this.txtPortTo = txtPortTo;
        this.txtShipBunkerCapacity = txtShipBunkerCapacity;
        this.txtShipBunkerLevel = txtShipBunkerLevel;
        this.txtShipCapacity = txtShipCapacity;
        this.txtShipLoadUnloadTimePerContainer = txtShipCategory;
        this.txtShipDescription = txtShipDescription;
        this.txtTimeTaken3 = txtTimeTaken3;
        this.txtTravelFuelCost = txtTravelFuelCost;
        
        
        
        
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
        lblShipLoadUnloadTimePerContainer = new javax.swing.JLabel();
        lblShipBunkerCapacity = new javax.swing.JLabel();
        lblShipCapacity = new javax.swing.JLabel();
        lblIdealSpeed = new javax.swing.JLabel();
        lblMinSpeed = new javax.swing.JLabel();
        lblMaxSpeed = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cboShipCode = new javax.swing.JComboBox<>();
        txtShipCapacity = new javax.swing.JTextField();
        txtDesignSpeed = new javax.swing.JTextField();
        txtMinSpeed = new javax.swing.JTextField();
        txtMaxSpeed = new javax.swing.JTextField();
        txtShipDescription = new javax.swing.JTextField();
        txtShipLoadUnloadTimePerContainer = new javax.swing.JTextField();
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
        jpRouteDisplay = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        lblTotalPortSelected = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jpCheckBoxContainer = new javax.swing.JPanel();
        btnStartAlgo = new javax.swing.JButton();

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
                .addContainerGap(422, Short.MAX_VALUE))
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
            .addGap(0, 191, Short.MAX_VALUE)
        );

        jpShipSelection.setPreferredSize(new java.awt.Dimension(530, 700));

        lblShipCode.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblShipCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipCode.setText("Ship Code");

        lblShipDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipDescription.setText("Ship Description");

        lblShipLoadUnloadTimePerContainer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipLoadUnloadTimePerContainer.setText("Load Unload Per Container (hr)");

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
        cboShipCode.setPreferredSize(new java.awt.Dimension(75, 26));
        cboShipCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShipCodeActionPerformed(evt);
            }
        });

        jpDisplayStatus.setPreferredSize(new java.awt.Dimension(510, 450));

        lblDisplayStatus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDisplayStatus.setText("Display Status");

        lblChosenSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChosenSpeed.setText("Chosen Speed (kn) ");
        lblChosenSpeed.setFocusable(false);

        lblPortTo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPortTo.setText("Port To");

        lblTravelFuelCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTravelFuelCost.setText("Travel Fuel Cost ($) ");

        lblShipBunkerLevel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipBunkerLevel.setText("Ship Bunker Level (Ton) ");

        lblPortFromSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPortFromSelected.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPortFromSelected.setText("Port From");

        lblFuelBunkered.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFuelBunkered.setText("Fuel Bunkered (Ton)");

        lblFuelBunkerCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFuelBunkerCost.setText("Fuel Bunker Cost  ($)");

        cboPortFromSelected.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboPortFromSelected.setToolTipText("");
        cboPortFromSelected.setPreferredSize(new java.awt.Dimension(75, 26));
        cboPortFromSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPortFromSelectedActionPerformed(evt);
            }
        });

        lblLoadedCapacity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoadedCapacity.setText("Loaded Capacity (TEU) ");

        txtFuelBunkerCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFuelBunkerCostActionPerformed(evt);
            }
        });

        lblOperationalCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOperationalCost.setText("Operational Cost ($) ");

        lblTimeTaken.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTimeTaken.setText("Time Taken (Hr) ");

        lblDistance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDistance.setText("Distance (nmi) ");

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
                .addContainerGap(178, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpShipSelectionLayout = new javax.swing.GroupLayout(jpShipSelection);
        jpShipSelection.setLayout(jpShipSelectionLayout);
        jpShipSelectionLayout.setHorizontalGroup(
            jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpShipSelectionLayout.createSequentialGroup()
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpShipSelectionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpDisplayStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                            .addGroup(jpShipSelectionLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jpShipSelectionLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblShipLoadUnloadTimePerContainer)
                            .addGroup(jpShipSelectionLayout.createSequentialGroup()
                                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblShipCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblIdealSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblMinSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblShipCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblShipDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(lblShipBunkerCapacity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboShipCode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtShipLoadUnloadTimePerContainer)
                                    .addComponent(txtShipDescription)
                                    .addComponent(txtShipBunkerCapacity)
                                    .addComponent(txtShipCapacity)
                                    .addComponent(txtDesignSpeed)
                                    .addComponent(txtMinSpeed)
                                    .addComponent(txtMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                    .addComponent(lblShipLoadUnloadTimePerContainer)
                    .addComponent(txtShipLoadUnloadTimePerContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txtDesignSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMinSpeed)
                    .addComponent(txtMinSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaxSpeed)
                    .addComponent(txtMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jpDisplayStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMaxSpeed.getAccessibleContext().setAccessibleDescription("");

        jTabbedPane1.addTab("Ship Selection", jpShipSelection);

        TitlePortSelection.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TitlePortSelection.setText("Port Selection");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Selected Initial Route Sequence:");

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setBorder(null);
        textArea.setOpaque(false);
        jScrollPane3.setViewportView(textArea);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Total Ports:");

        lblTotalPortSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalPortSelected.setText("0");

        javax.swing.GroupLayout jpRouteDisplayLayout = new javax.swing.GroupLayout(jpRouteDisplay);
        jpRouteDisplay.setLayout(jpRouteDisplayLayout);
        jpRouteDisplayLayout.setHorizontalGroup(
            jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRouteDisplayLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jpRouteDisplayLayout.createSequentialGroup()
                        .addGroup(jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jpRouteDisplayLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTotalPortSelected)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpRouteDisplayLayout.setVerticalGroup(
            jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRouteDisplayLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblTotalPortSelected))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        lblTotalPortSelected.getAccessibleContext().setAccessibleName("lblTotalPortSelected");

        javax.swing.GroupLayout jpCheckBoxContainerLayout = new javax.swing.GroupLayout(jpCheckBoxContainer);
        jpCheckBoxContainer.setLayout(jpCheckBoxContainerLayout);
        jpCheckBoxContainerLayout.setHorizontalGroup(
            jpCheckBoxContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
        );
        jpCheckBoxContainerLayout.setVerticalGroup(
            jpCheckBoxContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jpCheckBoxContainer);

        javax.swing.GroupLayout jpPortSelectionLayout = new javax.swing.GroupLayout(jpPortSelection);
        jpPortSelection.setLayout(jpPortSelectionLayout);
        jpPortSelectionLayout.setHorizontalGroup(
            jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TitlePortSelection)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(jpRouteDisplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpPortSelectionLayout.setVerticalGroup(
            jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TitlePortSelection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jpRouteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jTabbedPane1.addTab("Port Selection", jpPortSelection);

        btnStartAlgo.setText("Start Algorithm");
        btnStartAlgo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartAlgoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OutputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabOutputDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(735, 735, 735)
                        .addComponent(btnStartAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabOutputDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(OutputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnStartAlgo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(17, 17, 17))
            .addComponent(jTabbedPane1)
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
       // addPort(new PortDTO("P001","Test before Lorete",PortDTO.PointType.START,IPEvent,new GeoPosition(-20.16366002209838, 57.50698320195978)));
       // addPort(new PortDTO("P002","Test before Citadel",PortDTO.PointType.END,IPEvent,new GeoPosition(-20.162778782150987, 57.507959525974705)));//change this from on top f citadele to the road before citadelle
        
        clearUIPort();
        for(int i = 0 ; i<lstPorts.size();i++){
            if(arrCheckBoxPort[i].isSelected()){
                SelectedPorts.add(lstPorts.get(i));
                
            }
        }
        initPort();
        List<String> text  = new ArrayList<>();
        for(var p : SelectedPorts){ 
            text.add(p.PortName);
        }
        
        textArea.setText(String.join("->",text));
        lblTotalPortSelected.setText(String.valueOf(SelectedPorts.size()));
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

    private void cboShipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShipCodeActionPerformed
        int selectedIndex = cboShipCode.getSelectedIndex();
        
        setShipCategoryToGUIDisplay(selectedIndex);
    }//GEN-LAST:event_cboShipCodeActionPerformed

    private void cboPortFromSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPortFromSelectedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPortFromSelectedActionPerformed

    private void txtFuelBunkerCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFuelBunkerCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFuelBunkerCostActionPerformed

    private void btnStartAlgoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartAlgoActionPerformed
        // TODO add your handling code here:
        //only choosing those 4 ports for testing :
        //Abu Dhabi - Bandar Abbas - Sitra - Sharjah
        long startTime = System.nanoTime();
        if( (SelectedPorts !=null && SelectedPorts.size()>2) &&(SelectedContainerType != null )&&(SelectedShipCategory != null )){
            SelectedShipCategory.AvgWeightUtilizeContainer = SelectedContainerType.AvgUtilizeWeight;
            //1 convert selectedPort from hashset to list to maintain order
            ArrayList<PortDTO> lstSelectedPorts = new ArrayList<>(SelectedPorts);
            //2 Create IndexOrder:PortIDMatrix
            HashMap<Integer,Integer> IndexToPortIDMatrix = new HashMap<>();
            
            DLL_PortPair dllPortPair = new DLL_PortPair();
            
            int numberOfPorts =lstSelectedPorts.size();
            
            double[][] TravelDistances = new double[numberOfPorts][numberOfPorts];
            
            for(int i = 0; i<numberOfPorts;i++){
                IndexToPortIDMatrix.put(i,lstSelectedPorts.get(i).PortID);
            }
            String Text= "";
            for(int i = 0; i<numberOfPorts;i++){
                for(int j =0; j<numberOfPorts; j++){
                    if(i == j){
                        TravelDistances[i][j] = 0;
                    }else{
                        var data = dllPortPair.getPairPortDistance_ListByRow(IndexToPortIDMatrix.get(i), IndexToPortIDMatrix.get(j)).Data;
                        if(data != null && data.size() > 0){
                            //we got the distance
                            TravelDistances[i][j] = data.get(0).Distance;
                            TravelDistances[j][i] = TravelDistances[i][j];
                        }
                    }
                }
            }
            long endTime = System.nanoTime();
            
            //selectedPort,SelectedShip,IndexToPortIDMatrix,TravelDistances
            //roulette or tournament, genomeSize,populationSize,TournamentSize,reproductionSize,startingCity,targetFitness.
            GA_Main main = new GA_Main();
            System.out.println(startTime+" - "+endTime);
        }
        
        
        
    }//GEN-LAST:event_btnStartAlgoActionPerformed

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
    private javax.swing.JButton btnStartAlgo;
    private javax.swing.JComboBox<String> cboMapType;
    private javax.swing.JComboBox<String> cboPortFromSelected;
    private javax.swing.JComboBox<String> cboShipCode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jpCheckBoxContainer;
    private javax.swing.JPanel jpDisplayStatus;
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
    private javax.swing.JLabel lblShipCode;
    private javax.swing.JLabel lblShipDescription;
    private javax.swing.JLabel lblShipLoadUnloadTimePerContainer;
    private javax.swing.JLabel lblTimeTaken;
    private javax.swing.JLabel lblTotalPortSelected;
    private javax.swing.JLabel lblTravelFuelCost;
    private javax.swing.JMenuItem mnEnd;
    private javax.swing.JMenuItem mnStart;
    private javax.swing.JPopupMenu pmnChoosePointType;
    private javax.swing.JPanel tabMap;
    private javax.swing.JTabbedPane tabOutputDetail;
    private javax.swing.JPanel tabPortOutputDetail;
    private javax.swing.JPanel tabShipRouteDetail;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField txtChosenSpeed;
    private javax.swing.JTextField txtDesignSpeed;
    private javax.swing.JTextField txtDistance;
    private javax.swing.JTextField txtFuelBunkerCost;
    private javax.swing.JTextField txtFuelBunkered;
    private javax.swing.JTextField txtLoadedCapacity;
    private javax.swing.JTextField txtMaxSpeed;
    private javax.swing.JTextField txtMinSpeed;
    private javax.swing.JTextField txtOperationalCost;
    private javax.swing.JTextField txtPortTo;
    private javax.swing.JTextField txtShipBunkerCapacity;
    private javax.swing.JTextField txtShipBunkerLevel;
    private javax.swing.JTextField txtShipCapacity;
    private javax.swing.JTextField txtShipDescription;
    private javax.swing.JTextField txtShipLoadUnloadTimePerContainer;
    private javax.swing.JTextField txtTimeTaken3;
    private javax.swing.JTextField txtTravelFuelCost;
    // End of variables declaration//GEN-END:variables
}
