
package BLL;

import Entity.DataMap_Entity.RoutingData;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.Helper;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 *
 * @author Loo Alex
 */
public class RoutingService {
    private static RoutingService instance;
    private final GraphHopper hopper;
    
    public static RoutingService getInstance(){
        if(instance == null){
            instance  = new RoutingService();
        }
        
        return instance;
    }
    
    private RoutingService(){
        hopper = createGraphHopperInstance("osm-file/cambodia-latest.osm.pbf");
    }
    
    //use Graphhopper github @: https://github.com/graphhopper/graphhopper/blob/master/example/src/main/java/com/graphhopper/example/RoutingExample.java
    //change function from static to private
    //function below take a graph file directory location as string, pass it 
    private GraphHopper createGraphHopperInstance(String ghLoc) {
        GraphHopper grahHopper = new GraphHopper();
        grahHopper.setOSMFile(ghLoc); //get the file that has the osm map data with street etc, from directory
        
        // specify where to store graphhopper files
        grahHopper.setGraphHopperLocation("target/routing-graph-cache");

        // see docs/core/profiles.md to learn more about profiles
        grahHopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));

        // this enables speed mode for the profile we called car
        grahHopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        // now this can take minutes if it imports or a few seconds for loading of course this is dependent on the area you import
        grahHopper.importOrLoad();
        return grahHopper;
    }
    
      public List<RoutingData> routing(double fromLat, double fromLon, double toLat, double toLon) {
        //GHRequest take the fromPoint and toPoint lat and long
        // simple configuration of the request object
        GHRequest req = new GHRequest(fromLat,fromLon,toLat,toLon).
                // note that we have to specify which profile we are using even when there is only one like here
                        setProfile("car").
                // define the language for the turn instructions
                        setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);

        // handle errors, will throw an error if you choos streets that are outside the osm.pbf file we used when creating graphhopper instance
        if (rsp.hasErrors())
            throw new RuntimeException(rsp.getErrors().toString());

        // use the best path, see the GHResponse class for more possibilities.
        ResponsePath path = rsp.getBest();

        // points, distance in meters and time in millis of the full path
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();

        Translation tr = hopper.getTranslationMap().getWithFallBack(Locale.UK);
        InstructionList il = path.getInstructions();
        // iterate over all turn instructions
        
        //--added
        List<RoutingData> list = new ArrayList<>();
      
        for (Instruction instruction : il) {
            // System.out.println("distance " + instruction.getDistance() + " for instruction: " + instruction.getTurnDescription(tr));
            list.add( new RoutingData(instruction.getDistance(), instruction.getTurnDescription(tr), instruction.getPoints()));
        }
        
        return list;
    }
}
