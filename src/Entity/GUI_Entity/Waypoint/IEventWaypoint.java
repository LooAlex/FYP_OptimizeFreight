
package Entity.GUI_Entity.Waypoint;

import Entity.Port;

/**
 *
 * @author Loo Alex
 * IEventWaypoint can have its different abstract functions that can be override amd  on the go in the main
 * and then pass through a constructor in waypoint to 
 * See frm_tst_map
 */
public interface IEventWaypoint {
    public void selected (MyWaypoint waypoint); //for frm_tst_map
    public void selected (Port port);           //for optimization algo
}
