package Entity.GUI_Entity.GUI_Port;

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
public class ButtonPort extends JButton{

    public ButtonPort (){
        setContentAreaFilled(false); 
        
        ImageIcon newImageIcon = new ImageIcon(new ImageIcon(
                getClass().getResource("/Icons/myPin.png")
        ).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        
        setIcon(newImageIcon);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(24, 24);
        
        //remove border
        Border emptyBorder = BorderFactory.createEmptyBorder();
        setBorder(emptyBorder);
    }
    
    
    
}
