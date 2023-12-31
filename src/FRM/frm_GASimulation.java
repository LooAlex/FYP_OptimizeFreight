/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FRM;

import Algorithms.GA_PortAlgorithm;
import BLL.RoutingService;
import DAL.*;
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
import com.mysql.cj.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;
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
    private List<PortDTO> lstPorts; //will not change
       
    public Set<PortDTO> setSelectedPorts = new HashSet<>();
    public ArrayList<PortDTO> lstSelectedPorts = new ArrayList<>();//used to bound index order as HashSet does not maintain order.
    public ShipCategoryDTO selectedShipCategory;
    public ContainerTypeDTO SelectedContainerType;
    public PortDTO SelectedStartPort;
    public int indexSelectedOriginPort;
    public int f_AfterOpt_SelectedPortID;
    public GAGenome result;
    public int ShipContainerCapacity;
    public double FrequencyCycle = 2.00; //number of weeks
    
    public String[] arrSelectedPortName;
    public double targetOrperatingCostFitness = 0;
    public int generationSize   = 5000;
    public int reproductionSize = 200;
    public int tournamentSize   = 40;
    public float mutationRate = 0.1f;
    public String[] arrSelectionTypes = new String[]{"Roulette","Tournament"};
    public CoreEnum.SelectionType type ;
    public String[] arrFuelFunctionTypes = new String[]{"Weight Variable","Speed Only"};
    public CoreEnum.FuelFunctionType Ftype;
    public CoreEnum.FuelFunctionType functionTypeUsed;
    public boolean FlipFunction;
    //private List<PortDTO> setSelectedPorts = new ArrayList<>();
    private List<RoutingData> myRoutingData = new ArrayList<>();
    private IEventPortWaypoint IPEvent;
    private Point mousePosition;
    
    public String classDirectory = CoreData.classDirectory;
    public String Data_ResourceFilePath = CoreData.Data_ResourceFilePath;
    
    
    
    //GUI
    JCheckBox[] arrCheckBoxPort;
    /**
     * Creates new form frm_GASimulation
     */
    public frm_GASimulation() {
        
        initComponents();
        initAll();
        
    }
    
    public void resetResultControl(){
        taFinalPath.setText("");
        txtGenomeTotalTimeTaken.setText("");
        txtGenomeTotalDistanceTravel.setText("");
        txtGenomeTotalOperationCost.setText("");
        
        DefaultTableModel model = (DefaultTableModel)dt_PortDetails.getModel();
        model.setRowCount(0);
        dt_PortDetails.setModel(model);
        
        cboPortToSelected.removeAllItems();
        
        
    }
    public void initAll(){
        initGUI();
        //init EventPort
        IPEvent = getEvent();
        getPorts();
        getContainerTypes();
        getShipCategorys();
        
        initMap();
    }
    public void initGUI(){
        //Port
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        
        //Algo 
        taSelectedPortSequence.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taSelectedPortSequence.setLineWrap(true);
        taSelectedPortSequence.setWrapStyleWord(true);
        taSelectedPortSequence.setEditable(false);
        
        cboSelectedFuelFunction.setModel(new javax.swing.DefaultComboBoxModel<>(arrFuelFunctionTypes));
        cboSelectedFuelFunction.setSelectedIndex(0);
        Ftype = CoreEnum.FuelFunctionType.WEIGHTVARIABLE;
        
        cboSelectionType.setModel(new javax.swing.DefaultComboBoxModel<>(arrSelectionTypes));
        cboSelectionType.setSelectedIndex(0);
        type = CoreEnum.SelectionType.ROULETTE;
        
        initCboStartingPort();
        
        sldGenerationSize.setValue(generationSize);
        lblGenerationSize.setText(generationSize+"");
        sldReproductionSize.setValue((reproductionSize));
        lblReproductionSize.setText(reproductionSize+"");
        sldTournamentSize.setValue(tournamentSize);
        lblTournamentSize.setText(tournamentSize+"");
        sldMutationRateBig.setValue((int) (mutationRate*100));
        lblMurationRate.setText(mutationRate+"");
        sldTournamentSize.setEnabled(false);
        
        txtNumberOfPorts.setText("0");
        
        PlainDocument doc  = (PlainDocument)txtTargetOperationalCostFitness.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
        
        //dt_PortDetails
        CoreFunctions.resizeColumnWidth(dt_PortDetails, 120);
        
    }
    
    public void initCboShipCategory(){
        cboShipCode.removeAllItems();
        
        for(var s : myShipCats){
            cboShipCode.addItem(s);
        }
        cboShipCode.setSelectedIndex(0);
        selectedShipCategory = cboShipCode.getItemAt(0);
        setShipCategoryToGUIDisplay(selectedShipCategory);
       
        
    }
    
    public void initCboStartingPort(){
        cboStartingPort.removeAllItems();
        
        for(var p : lstSelectedPorts){
            cboStartingPort.addItem(p);
        }
        if(cboStartingPort.getItemCount() > 2){
            cboStartingPort.setEnabled(true);
            
        }else{
            cboStartingPort.setEnabled(false);
        }
    }
    public void initMap(){
        //using Virtual Earth
        TileFactoryInfo info = new  VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jxMapViewer_Simulation.setTileFactory(tileFactory);
        
        //setting open position of the map
        GeoPosition geo = new GeoPosition(-2.326736058337824, 73.60900606371786);
        jxMapViewer_Simulation.setAddressLocation(geo);
        
        //Zoom [6:street] [8:part of MRU] [10: MRU] [12:reunion + MRU] 14[Madagascar] 16[the world]
        jxMapViewer_Simulation.setZoom(16);
        
        //MouseEvent
        MouseInputListener mn = new PanMouseInputListener(jxMapViewer_Simulation);
        jxMapViewer_Simulation.addMouseListener(mn);
        jxMapViewer_Simulation.addMouseMotionListener(mn);
        jxMapViewer_Simulation.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jxMapViewer_Simulation));
        

        //OSM = 0, VEMap = 1, VEHybrid = 2, VESatelite = 3
        cboMapType.setSelectedIndex(1);
    }
  
    public void initUIPort(){
    //Init Portset
        //Init PortDisplayButton
        WaypointPainter<PortDTO> wp = new PortRenderer();
        wp.setWaypoints(setSelectedPorts);
        jxMapViewer_Simulation.setOverlayPainter(wp);
        //Add PortButton to map
        for(PortDTO prt : setSelectedPorts){
            jxMapViewer_Simulation.add(prt.getButton());
        }
  
    }
    
    public void clearPort(){
        for(var c: arrCheckBoxPort){
            c.setSelected(false);
        }
        myRoutingData.clear();
        clearUIwithPort();
        SelectedStartPort= null;
        f_AfterOpt_SelectedPortID = Integer.MAX_VALUE;
        initUIPort();
    }
    
    public void clearUIwithPort(){
        textArea.setText("");
        taSelectedPortSequence.setText("");
        //Algo
        lblTotalPortSelected.setText("0");
        txtNumberOfPorts.setText("0");
        
         for(PortDTO p : setSelectedPorts){
            jxMapViewer_Simulation.remove(p.getButton());
        }
         setSelectedPorts.clear();
         lstSelectedPorts.clear();
         
        cboStartingPort.removeAllItems(); 
        
    }
    public void addPort (PortDTO port){
        for(PortDTO p : setSelectedPorts){
            jxMapViewer_Simulation.remove(p.getButton());
        }
        
        Iterator<PortDTO> iter = setSelectedPorts.iterator();
        while(iter.hasNext()){
            if(iter.next().getPointType() == port.getPointType()){
                iter.remove();
            }
        }
        
        setSelectedPorts.add(port);
        //getContainerType
        //get
        //only choose selected ports in checkbox areas.
        //getPorts(); //db
        initUIPort();
        
        
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
                + "UnLocode: "+port.unLoCode+"<br><br>"
                + "Port Name: "+port.portName+"<br><br>"
                + "Coords: ("+port.portLat+","+port.portLon+")<br><br>" 
                + "Demand Amt: "+port.demands.DemandAmt+" Ton<br><br>" 
                + "Port Call  $"+port.portCall_Cost+"<br><br>"
                + "Fuel Price  $"+port.port_FuelPrice+"<br><br>"
                + "Cost Per Container $"+port.port_CostPerFullContainer+"<br><br>"
                + "<h3>";
               
                JOptionPane.showMessageDialog(frm_GASimulation.this.tabMap,String.format(html, width,width));
                
            }
        };
    }
    
    public void getShipCategorys(){
        DAL_ShipCategory dalShipCategory  = new DAL_ShipCategory();
        var result  = dalShipCategory.getShipCategorys();
        if(result.Data.size()>0){
            myShipCats = result.Data;  
            initCboShipCategory();
        } 
        
    }
    public void setShipCategoryToGUIDisplay(ShipCategoryDTO s){

        ShipContainerCapacity = selectedShipCategory.capacityTEU;
        
        //refresh demands of all ports base on category
        for(int i = 0; i<lstPorts.size(); i++){
            PortDTO dto = lstPorts.get(i);
            DemandDTO newDemands = new DemandDTO(0,ShipContainerCapacity);
            dto.setDemands(newDemands);
            lstPorts.set(i, dto);
        }
        //refresh waypoint display
        setSelectedPortsToDisplay();
        
        txtShipBunkerCapacity.setText(String.valueOf(s.BunkerCapacity)) ;
        txtShipCapacity.setText(String.valueOf(s.capacityTEU));
        txtShipDescription.setText(String.valueOf(s.getDescription()));
        txtDesignSpeed.setText(String.valueOf(s.DesignSpeed));
        txtMinSpeed.setText(String.valueOf(s.MinSpeed));
        txtMaxSpeed.setText(String.valueOf(s.MaxSpeed));
        txtShipLoadUnloadTimePerContainer.setText(String.valueOf((s.timeLoadUnLoadPerFullContainerTEU)));
        
        
    }
    
    public void getContainerTypes(){
        DAL_ContainerType dllContainerType = new DAL_ContainerType();
        var result = dllContainerType.getContainerType_ListByRow(1);
        if(result.Data != null && result.Data.size() > 0){
            SelectedContainerType = result.Data.get(0);
        }
        
    }
    
    public void setFType(int selectedIndex){
        if(selectedIndex == 0){
            Ftype = CoreEnum.FuelFunctionType.WEIGHTVARIABLE;
        }else if(selectedIndex == 1){
            Ftype = CoreEnum.FuelFunctionType.SPEEDONLY;
        }
    }
    public void getPorts(){
        DAL_Port dllport = new DAL_Port();
        var result = dllport.getPorts(IPEvent);

         if (result.errors.hasErrors){
            System.out.println("ErrorsMessage:" + result.errors.errorMessages.toString());
        }else{
            lstPorts = result.Data;
             
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

            //Creating CheckBox
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
    public void setSelectedPortsToDisplay(){
        clearUIwithPort(); //clear set and lst, cbo port selected
        SelectedStartPort = null;
        
        //getNewSelected
        for(int i = 0 ; i<lstPorts.size();i++){
            if(arrCheckBoxPort[i].isSelected()){
                setSelectedPorts.add(lstPorts.get(i)); //selected waypoints  
            }
        }
        
        lstSelectedPorts.addAll(setSelectedPorts); //fix new starting port
        
        initUIPort();//render new ports selected
        
        String path  = lstSelectedPorts.stream().map(i -> i.portName).collect(Collectors.joining("-> "));

        textArea.setText(path);
        lblTotalPortSelected.setText(String.valueOf(lstSelectedPorts.size()));
        
        taSelectedPortSequence.setText(path);
        txtNumberOfPorts.setText(String.valueOf(lstSelectedPorts.size()));
        initCboStartingPort();
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
                setSelectedPorts.add(new PortDTO(arr,IPEvent));
            }
            
        }
    }
    
     public static   void printTravelDistances(double [][]TravelDistances, int numberOfPorts){
        for(int i = 0; i< numberOfPorts; i++){
            for(int j = 0; j < numberOfPorts; j++){
                System.out.print(TravelDistances[i][j]);
                if(TravelDistances[i][j]/10==0)
                    System.out.print("   ");//cuz ij = 0, so to make retain space and have like a matrix, we add 2extra space
                else
                    System.out.print(" ");
            }
            System.out.println("");
                
        }
    }
         public void setGUI_resultGenome(){
        
         if(result != null && Ftype != null && result.dataPortHistory != null){
             functionTypeUsed = Ftype;
             System.out.println(result.toString());
             String resultMessage = "";
             if(functionTypeUsed == CoreEnum.FuelFunctionType.WEIGHTVARIABLE){
                 resultMessage+="Weight Variable Function\n";
             }else if(functionTypeUsed == CoreEnum.FuelFunctionType.SPEEDONLY){
                 resultMessage+="Speed Only Variable Function\n";
             }
             
            double fitness = 0;
            result.toPath();
    //FinalResult
            fitness = result.calculateNewFitness(functionTypeUsed);
            
            initJtable_PortDetails();
            
            
            DecimalFormat df = CoreFunctions.getDecimalFormat(2);
            
            taFinalPath.setText(resultMessage+result.path);
            txtGenomeTotalDistanceTravel.setText(df.format(result.totalDistanceTravel));
            
            String fitnessDisplay  = "";
            String errorMsg ="";
            
            if(fitness < Integer.MAX_VALUE/10){
                fitnessDisplay = df.format(fitness);
            }else{
                if(result.totalDistanceTravel>= Integer.MAX_VALUE/4){
                    errorMsg = "Infeasable Distance. Some Ports cannot be used as pairs.";
                }
                else if (fitness > Integer.MAX_VALUE/10 && FlipFunction){
                    errorMsg = "Abnormal Data.\nInfeasible conversion of this path using different fuel function.";
                }
                else{
                    errorMsg="Some abnormal data was found, you may consider another ship or starting port for this trip.";
                }
                JOptionPane.showMessageDialog(null ,errorMsg );
                fitnessDisplay= "NA";
            }
           
            txtGenomeTotalOperationCost.setText(fitnessDisplay);
            
            txtGenomeTotalTimeTaken.setText(df.format(result.getTotalTimeTaken()));
  
            PortDTO tempPort = new PortDTO();
            cboPortToSelected.removeAllItems();
            
            for(int i = 0 ; i<result.getGenome_FullCyclePath().size() ; i++){
                
                if(i == 0 ){
                    tempPort = result.dataPortsAlter.get(i);
                    tempPort.portName = tempPort.portName+" ORIGIN";
                    cboPortToSelected.addItem(tempPort);
                }
                else if( i == result.getGenome_FullCyclePath().size()-1){
                    tempPort = result.dataPortsAlter.get(i);
                    tempPort.portName = tempPort.portName+" DESTINATION";
                    cboPortToSelected.addItem(tempPort);
                }else{
                    cboPortToSelected.addItem(result.dataPortsAlter.get(i));
                }
            }
            SetGUI_GenomeSelectedPortFrom(cboPortToSelected.getItemAt(0),false);
        }else{
            JOptionPane.showMessageDialog(null, "No define Optimize Path found. Try to Start the Algorithm","Warning",JOptionPane.WARNING_MESSAGE);
        }
          FlipFunction = false;
    }
    public void initJtable_PortDetails(){
        DefaultTableModel model = (DefaultTableModel)dt_PortDetails.getModel();
        model.setRowCount(0);
        if(result!= null && result.dataPortsAlter !=null){
            
            LinkedList<Object[]> lst = result.convertDataPortToStringArray(result.dataPortsAlter);
            for(var arr: lst){
                model.addRow(arr);
            }
            dt_PortDetails.setModel(model);
            
        }
        
    }
    public void SetGUI_GenomeSelectedPortFrom(PortDTO pt,boolean isFromAlgoParameter){
        DecimalFormat df = CoreFunctions.getDecimalFormat(2);
        
        if(pt != null){
            
            txtPortTo.setText(pt.currentShip.previousPortName);
            txtFuelBunkered.setText(df.format(pt.currentShip.AmountBunkered));
            txtFuelBunkerCost.setText(df.format(pt.currentShip.AmountBunkered*pt.port_FuelPrice));

            txtLoadedDemand.setText(df.format(pt.currentShip.totalLoadUnload));
            txtShipBunkerLevel.setText(df.format(pt.currentShip.BunkerLevelAfterOper));
            txtTravelFuelCost.setText(df.format(pt.totalFuelTravelCost));
            txtSelectedSpeed.setText(df.format(pt.shp_chosenSpeed));

            txtOperationalCost.setText(df.format(pt.totalOperationalCost));
            txtTimeTaken.setText(df.format(pt.shp_PortTimeTaken));//Time taken in port
            txtDistance.setText(df.format(pt.currentShip.DistanceTravel));
            
            for(var dto: setSelectedPorts){
                ImageIcon newImageIcon = new ImageIcon();
                
                if(dto.portID == f_AfterOpt_SelectedPortID){
                    newImageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/Icons/selectedOriginPin.png")).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));     
                }
                else if(dto.portID == pt.portID ){
                    if( isFromAlgoParameter==true){
                        newImageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/Icons/myPinOrigin.png")).getImage().getScaledInstance(24   , 24, Image.SCALE_SMOOTH));
                    
                    }else{
                        newImageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/Icons/myPin.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                    }
                    
                }else{
                    newImageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/Icons/myPin.png")).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
                }
                
                 dto.getButton().setIcon(newImageIcon);
            }
            initUIPort();
        }
        
    }
    
    
    public frm_GASimulation(IEventPortWaypoint IPEvent, Point mousePosition, JPanel OutputPanel, JLabel TitlePortSelection, JButton btnAddPort, JButton btnClearPort, JComboBox<String> cboMapType, JComboBox<PortDTO> cboPortFromSelected, JComboBox<ShipCategoryDTO> cboShipCode, JLabel jLabel10, JTabbedPane jTabbedPane1, JPanel jpDisplayStatus, JPanel jpPortComboArea, JPanel jpPortSelection, JPanel jpRouteDisplay, JPanel jpShipSelection, JXMapViewerCustom jxMapViewer_Simulation, JLabel lblChosenSpeed, JLabel lblDisplayStatus, JLabel lblDistance, JLabel lblFuelBunkerCost, JLabel lblFuelBunkered, JLabel lblIdealSpeed, JLabel lblLoadedCapacity, JLabel lblMaxSpeed, JLabel lblMinSpeed, JLabel lblOperationalCost, JLabel lblPortFromSelected, JLabel lblPortTo, JLabel lblShipBunkerCapacity, JLabel lblShipBunkerLevel, JLabel lblShipCapacity, JLabel lblShipCategory, JLabel lblShipCode, JLabel lblShipDescription, JLabel lblTimeTaken, JLabel lblTravelFuelCost, JMenuItem mnEnd, JMenuItem mnStart, JPopupMenu pmnChoosePointType, JPanel tabMap, JTabbedPane tabOutputDetail, JPanel tabPortOutputDetail, JPanel tabShipRouteDetail, JTextField txtChosenSpeed, JTextField txtDistance, JTextField txtFuelBunkerCost, JTextField txtFuelBunkered, JTextField txtIdealSpeed, JTextField txtLoadedCapacity, JTextField txtMaxSpeed, JTextField txtMinSpeed, JTextField txtOperationalCost, JTextField txtPortTo, JTextField txtShipBunkerCapacity, JTextField txtShipBunkerLevel, JTextField txtShipCapacity, JTextField txtShipCategory, JTextField txtShipDescription, JTextField txtTimeTaken3, JTextField txtTravelFuelCost) throws HeadlessException {
        this.IPEvent = IPEvent;
        this.mousePosition = mousePosition;
        this.ResultPanel = OutputPanel;
        this.TitlePortSelection = TitlePortSelection;
        this.btnAddPort = btnAddPort;
        this.btnClearPort = btnClearPort;
        this.cboMapType = cboMapType;
        this.cboPortToSelected = cboPortFromSelected;
        this.cboShipCode = cboShipCode;
        this.jLabel10 = jLabel10;
        this.jtInputs = jTabbedPane1;
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
        this.lblLoadedDemand = lblLoadedCapacity;
        this.lblMaxSpeed = lblMaxSpeed;
        this.lblMinSpeed = lblMinSpeed;
        this.lblOperationalCost = lblOperationalCost;
        this.lblPortToSelected = lblPortFromSelected;
        this.lblPortFrom = lblPortTo;
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
        this.tabDisplayDetail = tabOutputDetail;
        this.tabPortOutputDetail = tabPortOutputDetail;
        this.txtSelectedSpeed = txtChosenSpeed;
        this.txtDistance = txtDistance;
        this.txtFuelBunkerCost = txtFuelBunkerCost;
        this.txtFuelBunkered = txtFuelBunkered;
        this.txtDesignSpeed = txtIdealSpeed;
        this.txtLoadedDemand = txtLoadedCapacity;
        this.txtMaxSpeed = txtMaxSpeed;
        this.txtMinSpeed = txtMinSpeed;
        this.txtOperationalCost = txtOperationalCost;
        this.txtPortTo = txtPortTo;
        this.txtShipBunkerCapacity = txtShipBunkerCapacity;
        this.txtShipBunkerLevel = txtShipBunkerLevel;
        this.txtShipCapacity = txtShipCapacity;
        this.txtShipLoadUnloadTimePerContainer = txtShipCategory;
        this.txtShipDescription = txtShipDescription;
        this.txtTimeTaken = txtTimeTaken3;
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
        tabDisplayDetail = new javax.swing.JTabbedPane();
        tabMap = new javax.swing.JPanel();
        jxMapViewer_Simulation = new Entity.GUI_Entity.DataMap.JXMapViewerCustom();
        btnAddPort = new javax.swing.JButton();
        btnClearPort = new javax.swing.JButton();
        cboMapType = new javax.swing.JComboBox<>();
        tabPortOutputDetail = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        dt_PortDetails = new javax.swing.JTable();
        jtInputs = new javax.swing.JTabbedPane();
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
        lblPortFrom = new javax.swing.JLabel();
        lblTravelFuelCost = new javax.swing.JLabel();
        txtSelectedSpeed = new javax.swing.JTextField();
        txtPortTo = new javax.swing.JTextField();
        lblShipBunkerLevel = new javax.swing.JLabel();
        lblPortToSelected = new javax.swing.JLabel();
        txtLoadedDemand = new javax.swing.JTextField();
        lblFuelBunkered = new javax.swing.JLabel();
        lblFuelBunkerCost = new javax.swing.JLabel();
        txtShipBunkerLevel = new javax.swing.JTextField();
        cboPortToSelected = new javax.swing.JComboBox<>();
        lblLoadedDemand = new javax.swing.JLabel();
        txtFuelBunkerCost = new javax.swing.JTextField();
        txtTravelFuelCost = new javax.swing.JTextField();
        lblOperationalCost = new javax.swing.JLabel();
        txtOperationalCost = new javax.swing.JTextField();
        lblTimeTaken = new javax.swing.JLabel();
        txtTimeTaken = new javax.swing.JTextField();
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
        jpAlgorithm = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cboSelectedFuelFunction = new javax.swing.JComboBox<>();
        txtNumberOfPorts = new javax.swing.JTextField();
        cboStartingPort = new javax.swing.JComboBox<>();
        txtTargetOperationalCostFitness = new javax.swing.JTextField();
        sldGenerationSize = new javax.swing.JSlider();
        sldReproductionSize = new javax.swing.JSlider();
        cboSelectionType = new javax.swing.JComboBox<>();
        sldMutationRateBig = new javax.swing.JSlider();
        lblTournamentSize = new javax.swing.JLabel();
        lblGenerationSize = new javax.swing.JLabel();
        lblReproductionSize = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taSelectedPortSequence = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        sldTournamentSize = new javax.swing.JSlider();
        lblMurationRate = new javax.swing.JLabel();
        ResultPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtGenomeTotalTimeTaken = new javax.swing.JTextField();
        txtGenomeTotalOperationCost = new javax.swing.JTextField();
        txtGenomeTotalDistanceTravel = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        taFinalPath = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        btnStartAlgo = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        btnAlterFuelFunction = new javax.swing.JButton();

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 566, Short.MAX_VALUE)
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
                .addContainerGap(386, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabMapLayout = new javax.swing.GroupLayout(tabMap);
        tabMap.setLayout(tabMapLayout);
        tabMapLayout.setHorizontalGroup(
            tabMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMapLayout.createSequentialGroup()
                .addComponent(jxMapViewer_Simulation, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabMapLayout.setVerticalGroup(
            tabMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jxMapViewer_Simulation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );

        tabDisplayDetail.addTab("Map", tabMap);

        tabPortOutputDetail.setPreferredSize(new java.awt.Dimension(800, 458));

        jScrollPane6.setColumnHeaderView(null);

        dt_PortDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SequenceNo", "Port From", "Port To", "Distance", "Speed", "Time", "Fuel Consumed Travel", "Port Time Taken", "Time Arrival", "Time Left", "Fuel Arrival", "Fuel At Leave", "Port Supply", "Port Demand", "Cost Per Container", "Oper Time", "Oper Fuel Consumed", "Has Bunker", "Amount Bunkered", "Port Fuel Price", "Is Late", "Time Late", "Has NoFuel", "Dept Bunker", "Port Call", "Total Travel Cost", "Total Idle Fuel Cost", "Total Handling Cost", "Total Penalty Cost", "Total Value Fuel Left", "Total Operating Cost"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dt_PortDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        dt_PortDetails.setColumnSelectionAllowed(true);
        jScrollPane6.setViewportView(dt_PortDetails);
        dt_PortDetails.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout tabPortOutputDetailLayout = new javax.swing.GroupLayout(tabPortOutputDetail);
        tabPortOutputDetail.setLayout(tabPortOutputDetailLayout);
        tabPortOutputDetailLayout.setHorizontalGroup(
            tabPortOutputDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
        );
        tabPortOutputDetailLayout.setVerticalGroup(
            tabPortOutputDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPortOutputDetailLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabDisplayDetail.addTab("Ports Detail", tabPortOutputDetail);

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

        txtShipCapacity.setEditable(false);

        txtDesignSpeed.setEditable(false);

        txtMinSpeed.setEditable(false);

        txtMaxSpeed.setEditable(false);

        txtShipDescription.setEditable(false);

        txtShipLoadUnloadTimePerContainer.setEditable(false);

        txtShipBunkerCapacity.setEditable(false);

        jpDisplayStatus.setPreferredSize(new java.awt.Dimension(510, 450));

        lblDisplayStatus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDisplayStatus.setText("Display Status");

        txtFuelBunkered.setEditable(false);

        lblChosenSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChosenSpeed.setText("Chosen Speed (kn) ");
        lblChosenSpeed.setFocusable(false);

        lblPortFrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPortFrom.setText("Port From");

        lblTravelFuelCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTravelFuelCost.setText("Travel Fuel Cost ($) ");

        txtSelectedSpeed.setEditable(false);

        txtPortTo.setEditable(false);

        lblShipBunkerLevel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShipBunkerLevel.setText("Ship Bunker Level (Ton) ");

        lblPortToSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPortToSelected.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPortToSelected.setText("Port To");

        txtLoadedDemand.setEditable(false);

        lblFuelBunkered.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFuelBunkered.setText("Fuel Bunkered (Ton)");

        lblFuelBunkerCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFuelBunkerCost.setText("Fuel Bunker Cost  ($)");

        txtShipBunkerLevel.setEditable(false);

        cboPortToSelected.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboPortToSelected.setToolTipText("");
        cboPortToSelected.setPreferredSize(new java.awt.Dimension(75, 26));
        cboPortToSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPortToSelectedActionPerformed(evt);
            }
        });

        lblLoadedDemand.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoadedDemand.setText("Load Unload Demands (TEU) ");

        txtFuelBunkerCost.setEditable(false);
        txtFuelBunkerCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFuelBunkerCostActionPerformed(evt);
            }
        });

        txtTravelFuelCost.setEditable(false);

        lblOperationalCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOperationalCost.setText("Operational Cost ($) ");

        txtOperationalCost.setEditable(false);

        lblTimeTaken.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTimeTaken.setText("Time Taken At this Port (Hr) ");

        txtTimeTaken.setEditable(false);

        lblDistance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDistance.setText("Distance (nmi) ");

        txtDistance.setEditable(false);

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
                        .addGap(33, 33, 33)
                        .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblFuelBunkerCost, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblShipBunkerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblTravelFuelCost, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblChosenSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblFuelBunkered, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lblLoadedDemand, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(lblPortFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPortToSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtFuelBunkered)
                                    .addComponent(txtPortTo)
                                    .addComponent(txtFuelBunkerCost)
                                    .addComponent(cboPortToSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSelectedSpeed)
                                    .addComponent(txtTravelFuelCost)
                                    .addComponent(txtLoadedDemand)
                                    .addComponent(txtShipBunkerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblOperationalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTimeTaken, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtDistance, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTimeTaken, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtOperationalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(166, 166, 166))
        );
        jpDisplayStatusLayout.setVerticalGroup(
            jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDisplayStatusLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblDisplayStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPortToSelected)
                    .addComponent(cboPortToSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPortFrom)
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
                    .addComponent(lblLoadedDemand)
                    .addComponent(txtLoadedDemand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txtSelectedSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperationalCost)
                    .addComponent(txtOperationalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimeTaken, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimeTaken, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDisplayStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDistance)
                    .addComponent(txtDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
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
                            .addComponent(jpDisplayStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
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
                .addGap(27, 27, 27)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpShipSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaxSpeed)
                    .addComponent(txtMaxSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpDisplayStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        lblMaxSpeed.getAccessibleContext().setAccessibleDescription("");

        jtInputs.addTab("Ship Selection", jpShipSelection);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpRouteDisplayLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpRouteDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(jpRouteDisplayLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotalPortSelected)))
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
            .addGap(0, 620, Short.MAX_VALUE)
        );
        jpCheckBoxContainerLayout.setVerticalGroup(
            jpCheckBoxContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jpCheckBoxContainer);

        javax.swing.GroupLayout jpPortSelectionLayout = new javax.swing.GroupLayout(jpPortSelection);
        jpPortSelection.setLayout(jpPortSelectionLayout);
        jpPortSelectionLayout.setHorizontalGroup(
            jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                .addGroup(jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpPortSelectionLayout.createSequentialGroup()
                        .addGroup(jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(TitlePortSelection))
                            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jpRouteDisplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpPortSelectionLayout.setVerticalGroup(
            jpPortSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPortSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TitlePortSelection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jpRouteDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtInputs.addTab("Port Selection", jpPortSelection);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Algorithm Selected:");

        jLabel4.setText("Fuel Function");

        jLabel5.setText("Number of Ports Selected");

        jLabel6.setText("Sequence Ports");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel7.setText("Starting Port");

        jLabel8.setText("Target Operating Cost");

        jLabel9.setText("Generation Size");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel11.setText("Selection Type");

        jLabel12.setText("Reproduction Size");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel13.setText("Tournament Size");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        cboSelectedFuelFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSelectedFuelFunctionActionPerformed(evt);
            }
        });

        txtNumberOfPorts.setEditable(false);
        txtNumberOfPorts.setText("0");

        cboStartingPort.setEnabled(false);
        cboStartingPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStartingPortActionPerformed(evt);
            }
        });

        txtTargetOperationalCostFitness.setText("0");
        txtTargetOperationalCostFitness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTargetOperationalCostFitnessActionPerformed(evt);
            }
        });

        sldGenerationSize.setMajorTickSpacing(1000);
        sldGenerationSize.setMaximum(10000);
        sldGenerationSize.setMinimum(2000);
        sldGenerationSize.setMinorTickSpacing(100);
        sldGenerationSize.setPaintLabels(true);
        sldGenerationSize.setPaintTicks(true);
        sldGenerationSize.setSnapToTicks(true);
        sldGenerationSize.setValue(5000);
        sldGenerationSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldGenerationSizeStateChanged(evt);
            }
        });

        sldReproductionSize.setMajorTickSpacing(100);
        sldReproductionSize.setMaximum(1000);
        sldReproductionSize.setMinimum(200);
        sldReproductionSize.setMinorTickSpacing(10);
        sldReproductionSize.setPaintLabels(true);
        sldReproductionSize.setPaintTicks(true);
        sldReproductionSize.setSnapToTicks(true);
        sldReproductionSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldReproductionSizeStateChanged(evt);
            }
        });

        cboSelectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSelectionTypeActionPerformed(evt);
            }
        });

        sldMutationRateBig.setMinimum(1);
        sldMutationRateBig.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldMutationRateBigStateChanged(evt);
            }
        });

        lblTournamentSize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTournamentSize.setText("40");

        lblGenerationSize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGenerationSize.setText("5000");

        lblReproductionSize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblReproductionSize.setText("200");

        taSelectedPortSequence.setEditable(false);
        taSelectedPortSequence.setColumns(20);
        taSelectedPortSequence.setRows(5);
        taSelectedPortSequence.setBorder(null);
        taSelectedPortSequence.setOpaque(false);
        jScrollPane2.setViewportView(taSelectedPortSequence);

        jLabel14.setText("Mutation Rate");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        sldTournamentSize.setMajorTickSpacing(20);
        sldTournamentSize.setMaximum(150);
        sldTournamentSize.setMinimum(10);
        sldTournamentSize.setMinorTickSpacing(5);
        sldTournamentSize.setPaintLabels(true);
        sldTournamentSize.setPaintTicks(true);
        sldTournamentSize.setSnapToTicks(true);
        sldTournamentSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldTournamentSizeStateChanged(evt);
            }
        });

        lblMurationRate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMurationRate.setText("0.4");

        javax.swing.GroupLayout jpAlgorithmLayout = new javax.swing.GroupLayout(jpAlgorithm);
        jpAlgorithm.setLayout(jpAlgorithmLayout);
        jpAlgorithmLayout.setHorizontalGroup(
            jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAlgorithmLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jpAlgorithmLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel8)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sldTournamentSize, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNumberOfPorts)
                                .addComponent(jScrollPane2)
                                .addComponent(cboStartingPort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboSelectionType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sldMutationRateBig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblMurationRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sldReproductionSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblReproductionSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sldGenerationSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblGenerationSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTargetOperationalCostFitness)
                                .addComponent(lblTournamentSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboSelectedFuelFunction, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jpAlgorithmLayout.setVerticalGroup(
            jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAlgorithmLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cboSelectedFuelFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNumberOfPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cboStartingPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTargetOperationalCostFitness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblGenerationSize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldGenerationSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(lblReproductionSize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sldReproductionSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(lblMurationRate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldMutationRateBig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cboSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(lblTournamentSize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAlgorithmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldTournamentSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jtInputs.addTab("Algorithm Parameter", jpAlgorithm);

        jLabel16.setText("Total Operation Cost ($)");

        jLabel17.setText("Total Distance Travel (kn)");

        jLabel18.setText("Total Time Taken (day)");

        txtGenomeTotalTimeTaken.setEditable(false);

        txtGenomeTotalOperationCost.setEditable(false);

        txtGenomeTotalDistanceTravel.setEditable(false);
        txtGenomeTotalDistanceTravel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGenomeTotalDistanceTravelActionPerformed(evt);
            }
        });

        taFinalPath.setEditable(false);
        taFinalPath.setColumns(20);
        taFinalPath.setRows(5);
        jScrollPane4.setViewportView(taFinalPath);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Path:");

        btnStartAlgo.setText("Start Algorithm");
        btnStartAlgo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartAlgoActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Result");

        btnAlterFuelFunction.setText("Alter Function Fuel");
        btnAlterFuelFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterFuelFunctionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ResultPanelLayout = new javax.swing.GroupLayout(ResultPanel);
        ResultPanel.setLayout(ResultPanelLayout);
        ResultPanelLayout.setHorizontalGroup(
            ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultPanelLayout.createSequentialGroup()
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(ResultPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
            .addGroup(ResultPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16))
                .addGap(30, 30, 30)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtGenomeTotalOperationCost, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(txtGenomeTotalDistanceTravel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGenomeTotalTimeTaken, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(289, 289, 289)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAlterFuelFunction, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(btnStartAlgo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        ResultPanelLayout.setVerticalGroup(
            ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultPanelLayout.createSequentialGroup()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ResultPanelLayout.createSequentialGroup()
                        .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txtGenomeTotalTimeTaken, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtGenomeTotalDistanceTravel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtGenomeTotalOperationCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ResultPanelLayout.createSequentialGroup()
                        .addComponent(btnAlterFuelFunction, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnStartAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtInputs, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ResultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 50, Short.MAX_VALUE))
                    .addComponent(tabDisplayDetail))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabDisplayDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jtInputs, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnStartActionPerformed
            // MENUE: START Waypoint:
            GeoPosition geop = jxMapViewer_Simulation.convertPointToGeoPosition(mousePosition);
            
    }//GEN-LAST:event_mnStartActionPerformed

    private void mnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEndActionPerformed
            // MENUE: END Waypoint:
            GeoPosition geop = jxMapViewer_Simulation.convertPointToGeoPosition(mousePosition);
           
    }//GEN-LAST:event_mnEndActionPerformed

    private void cboShipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShipCodeActionPerformed
        selectedShipCategory = (ShipCategoryDTO)cboShipCode.getSelectedItem();
        setShipCategoryToGUIDisplay(selectedShipCategory);
    }//GEN-LAST:event_cboShipCodeActionPerformed

    private void cboPortToSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPortToSelectedActionPerformed
        // TODO add your handling code here:
        SetGUI_GenomeSelectedPortFrom((PortDTO)cboPortToSelected.getSelectedItem(),false);
    }//GEN-LAST:event_cboPortToSelectedActionPerformed

    private void txtFuelBunkerCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFuelBunkerCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFuelBunkerCostActionPerformed

    private void cboSelectedFuelFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSelectedFuelFunctionActionPerformed
        // TODO add your handling code here:
        int selectedIndex = cboSelectedFuelFunction.getSelectedIndex();
        setFType(selectedIndex);
        
    }//GEN-LAST:event_cboSelectedFuelFunctionActionPerformed

    private void sldGenerationSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldGenerationSizeStateChanged
        // TODO add your handling code here:
        generationSize = sldGenerationSize.getValue();
        lblGenerationSize.setText(generationSize+"");
    }//GEN-LAST:event_sldGenerationSizeStateChanged

    private void sldReproductionSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldReproductionSizeStateChanged
        // TODO add your handling code here:
        reproductionSize = sldReproductionSize.getValue();
        lblReproductionSize.setText(reproductionSize+"");
    }//GEN-LAST:event_sldReproductionSizeStateChanged

    private void cboSelectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSelectionTypeActionPerformed
        // TODO add your handling code here:
        int selectedIndex = cboSelectionType.getSelectedIndex();
        if(selectedIndex == 1){
            sldTournamentSize.setEnabled(true);
            type = CoreEnum.SelectionType.TOURNAMENT;
        }else{
            sldTournamentSize.setEnabled(false);
            type = CoreEnum.SelectionType.ROULETTE;
        }
    }//GEN-LAST:event_cboSelectionTypeActionPerformed

    private void sldMutationRateBigStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldMutationRateBigStateChanged
        // TODO add your handling code here:
        mutationRate = sldMutationRateBig.getValue()/100.0f;
        lblMurationRate.setText(mutationRate+"");
    }//GEN-LAST:event_sldMutationRateBigStateChanged

    private void cboStartingPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStartingPortActionPerformed
        // TODO add your handling code here:
        SelectedStartPort = (PortDTO)cboStartingPort.getSelectedItem();
        SetGUI_GenomeSelectedPortFrom(SelectedStartPort,true);
    }//GEN-LAST:event_cboStartingPortActionPerformed

    private void txtTargetOperationalCostFitnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTargetOperationalCostFitnessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTargetOperationalCostFitnessActionPerformed

    private void sldTournamentSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldTournamentSizeStateChanged
        // TODO add your handling code here:
        tournamentSize = sldTournamentSize.getValue();
        lblTournamentSize.setText(tournamentSize+"");
    }//GEN-LAST:event_sldTournamentSizeStateChanged

    private void jxMapViewer_SimulationMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jxMapViewer_SimulationMouseReleased
        // TODO add your handling code here:
        if(SwingUtilities.isRightMouseButton(evt)){
            mousePosition = evt.getPoint();
            pmnChoosePointType.show(jxMapViewer_Simulation, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jxMapViewer_SimulationMouseReleased

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

    private void btnClearPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPortActionPerformed
        // TODO add your handling code here:
        clearPort();
    }//GEN-LAST:event_btnClearPortActionPerformed

    private void btnAddPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPortActionPerformed
        // TODO add your handling code here:
        setSelectedPortsToDisplay();
    }//GEN-LAST:event_btnAddPortActionPerformed

    private void btnAlterFuelFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterFuelFunctionActionPerformed
        // TODO add your handling code here:

        if(Ftype == functionTypeUsed){
            FlipFunction = true;
            //reverse
            int index = cboSelectedFuelFunction.getSelectedIndex();
            if(index > 0){
                //if 1 make it 0: Weight
                Ftype = CoreEnum.FuelFunctionType.WEIGHTVARIABLE;
                index = 0;
            }else{
                // if 0 make it 1: Speedonly
                Ftype = CoreEnum.FuelFunctionType.SPEEDONLY;
                index= 1;
            }
            cboSelectedFuelFunction.setSelectedIndex(index);
        }
        setGUI_resultGenome();
    }//GEN-LAST:event_btnAlterFuelFunctionActionPerformed

    private void btnStartAlgoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartAlgoActionPerformed
        // TODO add your handling code here:
        String errorMsg = "";
        long startTime = System.nanoTime();
        if  (      (lstSelectedPorts !=null && lstSelectedPorts.size()>2)
            && (SelectedContainerType != null) && (selectedShipCategory != null)
            && (Ftype != null)
            && (type !=null) && (SelectedStartPort !=null)
            && (generationSize >=2000 && reproductionSize >=200 && tournamentSize >= 10)
            && (!txtTargetOperationalCostFitness.getText().isEmpty() && Integer.valueOf(txtTargetOperationalCostFitness.getText())>=0)
        )
        {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dailogResult = JOptionPane.showConfirmDialog(null, "Do you want to start algorithm?","Warning",dialogButton,JOptionPane.WARNING_MESSAGE);
            if(dailogResult == 0){
                //1. prepare Fueltype function use
                functionTypeUsed = Ftype;
                FlipFunction = false;
                targetOrperatingCostFitness = Double.valueOf(txtTargetOperationalCostFitness.getText());
                //Init Ship AverageWeightPerContainerUtilized and weeklyFrequency
                selectedShipCategory.avgWeightUtilizeContainer = SelectedContainerType.getAvgUtilizeWeight();
                selectedShipCategory.weeklyFrequencyHour = FrequencyCycle * (24.00*7.00); //in hours 2 week = 336hr
                
                //2.Create IndexToPortDTOMatrix
                HashMap<Integer,PortDTO> indexToPortMatrix = new HashMap<>();
                int numberOfPorts = lstSelectedPorts.size();
                for(int i = 0; i<numberOfPorts;i++){

                    if(SelectedStartPort.portID == lstSelectedPorts.get(i).portID){
                        //init startingIndex
                        indexSelectedOriginPort = i;
                        f_AfterOpt_SelectedPortID = SelectedStartPort.portID;
                    }
                    indexToPortMatrix.put(i,lstSelectedPorts.get(i));
                }
                    
                //3. Create TravelDistancesMatrix
                double[][] travelDistances = new double[numberOfPorts][numberOfPorts];
                DAL_PortPair dllPortPair = new DAL_PortPair();
                for(int i = 0; i<numberOfPorts;i++){
                    for(int j =0; j<numberOfPorts; j++){
                        if(i == j){
                            travelDistances[i][j] = 0;
                        }else{
                            var data = dllPortPair.getPairPortDistance_ListByRow(indexToPortMatrix.get(i).portID, 
                                    indexToPortMatrix.get(j).portID).Data;
                            
                            if(data != null && data.size() > 0){
                                //we got the distance
                                travelDistances[i][j] = data.get(0).Distance;
                                travelDistances[j][i] = travelDistances[i][j];
                            }
                        }
                    }
                }
                printTravelDistances(travelDistances, numberOfPorts);
                
                //4. Init GA Parameters
                GA_PortAlgorithm portAlgorithm = new GA_PortAlgorithm(
                    selectedShipCategory,
                    numberOfPorts,indexSelectedOriginPort,
                    indexToPortMatrix,travelDistances,
                    targetOrperatingCostFitness,
                    generationSize,reproductionSize,mutationRate,
                    type,tournamentSize,
                    functionTypeUsed
                );
                
                //5 Start Optimization
                result  = portAlgorithm.optimize();
                if(result != null && result.dataPortHistory != null){
                    JOptionPane.showMessageDialog(null, "Finished!","Message", JOptionPane.INFORMATION_MESSAGE);
                
                //6 Display result in GUIs
                    setGUI_resultGenome();
                }

            }
        
        }else if(Integer.valueOf(txtTargetOperationalCostFitness.getText())<0){
            JOptionPane.showMessageDialog(rootPane, "Target Operational Cost must be greater than 0.","Warning",JOptionPane.WARNING_MESSAGE);
        }
        else if (lstSelectedPorts.size() < 2){
            JOptionPane.showMessageDialog(rootPane, "Selected Ports should be more than 2.","Warning",JOptionPane.WARNING_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Missing Data!\nMake sure to choose a ship and add ports.","Warning",JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_btnStartAlgoActionPerformed

    private void txtGenomeTotalDistanceTravelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGenomeTotalDistanceTravelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGenomeTotalDistanceTravelActionPerformed

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
    private javax.swing.JPanel ResultPanel;
    private javax.swing.JLabel TitlePortSelection;
    private javax.swing.JButton btnAddPort;
    private javax.swing.JButton btnAlterFuelFunction;
    private javax.swing.JButton btnClearPort;
    private javax.swing.JButton btnStartAlgo;
    private javax.swing.JComboBox<String> cboMapType;
    private javax.swing.JComboBox<PortDTO> cboPortToSelected;
    private javax.swing.JComboBox<String> cboSelectedFuelFunction;
    private javax.swing.JComboBox<String> cboSelectionType;
    private javax.swing.JComboBox<ShipCategoryDTO> cboShipCode;
    private javax.swing.JComboBox<PortDTO> cboStartingPort;
    private javax.swing.JTable dt_PortDetails;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JPanel jpAlgorithm;
    private javax.swing.JPanel jpCheckBoxContainer;
    private javax.swing.JPanel jpDisplayStatus;
    private javax.swing.JPanel jpPortSelection;
    private javax.swing.JPanel jpRouteDisplay;
    private javax.swing.JPanel jpShipSelection;
    private javax.swing.JTabbedPane jtInputs;
    private Entity.GUI_Entity.DataMap.JXMapViewerCustom jxMapViewer_Simulation;
    private javax.swing.JLabel lblChosenSpeed;
    private javax.swing.JLabel lblDisplayStatus;
    private javax.swing.JLabel lblDistance;
    private javax.swing.JLabel lblFuelBunkerCost;
    private javax.swing.JLabel lblFuelBunkered;
    private javax.swing.JLabel lblGenerationSize;
    private javax.swing.JLabel lblIdealSpeed;
    private javax.swing.JLabel lblLoadedDemand;
    private javax.swing.JLabel lblMaxSpeed;
    private javax.swing.JLabel lblMinSpeed;
    private javax.swing.JLabel lblMurationRate;
    private javax.swing.JLabel lblOperationalCost;
    private javax.swing.JLabel lblPortFrom;
    private javax.swing.JLabel lblPortToSelected;
    private javax.swing.JLabel lblReproductionSize;
    private javax.swing.JLabel lblShipBunkerCapacity;
    private javax.swing.JLabel lblShipBunkerLevel;
    private javax.swing.JLabel lblShipCapacity;
    private javax.swing.JLabel lblShipCode;
    private javax.swing.JLabel lblShipDescription;
    private javax.swing.JLabel lblShipLoadUnloadTimePerContainer;
    private javax.swing.JLabel lblTimeTaken;
    private javax.swing.JLabel lblTotalPortSelected;
    private javax.swing.JLabel lblTournamentSize;
    private javax.swing.JLabel lblTravelFuelCost;
    private javax.swing.JMenuItem mnEnd;
    private javax.swing.JMenuItem mnStart;
    private javax.swing.JPopupMenu pmnChoosePointType;
    private javax.swing.JSlider sldGenerationSize;
    private javax.swing.JSlider sldMutationRateBig;
    private javax.swing.JSlider sldReproductionSize;
    private javax.swing.JSlider sldTournamentSize;
    private javax.swing.JTextArea taFinalPath;
    private javax.swing.JTextArea taSelectedPortSequence;
    private javax.swing.JTabbedPane tabDisplayDetail;
    private javax.swing.JPanel tabMap;
    private javax.swing.JPanel tabPortOutputDetail;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField txtDesignSpeed;
    private javax.swing.JTextField txtDistance;
    private javax.swing.JTextField txtFuelBunkerCost;
    private javax.swing.JTextField txtFuelBunkered;
    private javax.swing.JTextField txtGenomeTotalDistanceTravel;
    private javax.swing.JTextField txtGenomeTotalOperationCost;
    private javax.swing.JTextField txtGenomeTotalTimeTaken;
    private javax.swing.JTextField txtLoadedDemand;
    private javax.swing.JTextField txtMaxSpeed;
    private javax.swing.JTextField txtMinSpeed;
    private javax.swing.JTextField txtNumberOfPorts;
    private javax.swing.JTextField txtOperationalCost;
    private javax.swing.JTextField txtPortTo;
    private javax.swing.JTextField txtSelectedSpeed;
    private javax.swing.JTextField txtShipBunkerCapacity;
    private javax.swing.JTextField txtShipBunkerLevel;
    private javax.swing.JTextField txtShipCapacity;
    private javax.swing.JTextField txtShipDescription;
    private javax.swing.JTextField txtShipLoadUnloadTimePerContainer;
    private javax.swing.JTextField txtTargetOperationalCostFitness;
    private javax.swing.JTextField txtTimeTaken;
    private javax.swing.JTextField txtTravelFuelCost;
    // End of variables declaration//GEN-END:variables
}
