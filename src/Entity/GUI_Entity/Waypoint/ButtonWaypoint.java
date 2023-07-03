package Entity.GUI_Entity.Waypoint;

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
        String strPathIcon = "./src/Icons/";
        ImageIcon newImageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/Icons/myPin.png")).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        setIcon(newImageIcon);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));//this change the cursor from arrow to hand cursor when on top of a waypoint>>
        setSize(24, 24);//set the size of this button waypoint, it tuto it was 24,24
        
        //remove border
        Border emptyBorder = BorderFactory.createEmptyBorder();
        setBorder(emptyBorder);
    }
    
    
    
}
