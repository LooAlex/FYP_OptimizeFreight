
package testArena.tuto_map.GUI_Waypoint;

import Entity.PortDTO;

/**
 *
 * @author Loo Alex
 * IEventWaypoint can have its different abstract functions that can be override amd  on the go in the main
 * and then pass through a constructor in waypoint to 
 * See frm_tst_map
 */
public interface IEventWaypoint {
    public void selected (MyWaypoint waypoint); //for frm_tst_map

}
