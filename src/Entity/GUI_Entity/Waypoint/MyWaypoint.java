
package Entity.GUI_Entity.Waypoint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.xml.stream.events.StartDocument;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Loo Alex
 * Class waypoint that will hold the special ButtonWaypoint we created and will manage how the waypoints will behave.
 */
public class MyWaypoint extends DefaultWaypoint{
    
    private String name;
    private JButton button;
    private PointType pointType; //needed to add enum variable found below first
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public PointType getPointType() {
        return pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }
    
    public MyWaypoint(String name,PointType pointType,IEventWaypoint Ievent, GeoPosition coord) {
        super(coord);
        this.name = name;
        this.pointType = pointType;
        initButton(Ievent);  //did not have this code before<-- 29/06/23
    }

    public MyWaypoint() {
    }
        
    //initButton is a funciton that prep the button for current waypoint being created, its icon and functions.
    private void initButton( IEventWaypoint Ievent){
        button = new ButtonWaypoint(); //takes care UI aspect, icon etc of the button waypoint
        button.addActionListener(new ActionListener() {
            //add an eventlistener to to the button of the waypoint being created, so that when we click on waypoint, something happens
            @Override
            public void actionPerformed(ActionEvent e) {
             //inate functions of any JButton for an addActionListener is the function actionPerformed, inside this function
             //is all the things that the system will do once this button for this waypoint is clicked.
                
                // if this button is clicked :
                Ievent.selected(MyWaypoint.this); //take argument the current waypoint and pass it.
                //dont forget this Ievent object is obtained from main -inthiscase- <<frm_tst_map>>
                
            }
        });
     
    }
    
    
    public static enum PointType{
        START,END;
    }
    
    
    
    
}
