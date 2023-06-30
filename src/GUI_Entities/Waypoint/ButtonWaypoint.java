package GUI_Entities.Waypoint;

import java.awt.Cursor;
import java.awt.Image;//to resize icons
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 *
 * @author Loo Alex
 * This Class is to create waypoints button, from shape of image, size and how the cursor behave when on top of a waypoint.
 */
public class ButtonWaypoint extends JButton{

    public ButtonWaypoint (){
        setContentAreaFilled(false); //<<setContentAreaFilled(false) will make button fill area blank aka empty/transparent>>
        
//those code not work
        ImageIcon imageIcon = new ImageIcon("/Icons/standard_waypoint.png"); // load the image to a imageIcon,putting the "." before is a wildcard to say all that match 
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon newImageIcon = new ImageIcon(newimg);  // transform it back

        //ImageIcon newImageIcon = new ImageIcon(new ImageIcon("/Icons/myPin.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        setIcon(newImageIcon);
//-----------

        //<<those code below work>>
        //setIcon(new ImageIcon(getClass().getResource("/Icons/myPin.png")));//<<setting png with no bg>>
        //setIcon(new ImageIcon(getClass().getResource("/Icons/standard_waypoint.png")));
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));//this change the cursor from arrow to hand cursor when on top of a waypoint>>
        setSize(24,24);//set the size of this button waypoint, it tuto it was 24,24
        
        //remove border, working code
//        Border emptyBorder = BorderFactory.createEmptyBorder();
//        setBorder(emptyBorder);
    }
    
    
    
}
