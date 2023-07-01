
package Entities.GUI_Entities.Waypoint;

/**
 *
 * @author Loo Alex
 * IEventWaypoint can have its different abstract functions that can be override on the go and used once a waypoint is selected.
 * See frm_tst_map
 */
public interface IEventWaypoint {
    public void selected (MyWaypoint waypoint); 
}
