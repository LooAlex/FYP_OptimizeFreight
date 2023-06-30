
package Entities.GUI_Entities.Waypoint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
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

    public MyWaypoint(String name, GeoPosition coord) {
        super(coord);
        this.name = name;
        initButton();  //did not have this code before<-- 29/06/23
    }

    public MyWaypoint() {
    }
    
    private void initButton(){
        button = new ButtonWaypoint();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked: "+name);
            }
        });
     
    }

    
    
    
    
}
