package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 * Stateless drone, has naive strategy.
 * @author s1742667
 */
public class Statelessdrone extends drone {
    protected Position nearStation = new Position(0,0);  // Position for the station then the drone will charge.
    /**
     * Constructor, do the same thing as its parent class.
     */
    protected Statelessdrone(Double latitude, Double longitude,int seed, Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    /**
     * Tell the drone which way to go.
     * @return direction that won't make drone lose coins.
     */
    protected Direction statelessSearch() { //If there exist a charge Station within 0.0003 degree range, 
        //then go towards that station, otherwise return a random direction.
        HashMap<Direction,Position> h = haveStation(curr);
        Direction[] directionSet = Direction.values();
        List<Direction>Directionlist = new ArrayList<Direction>(Arrays.asList(directionSet));
        if(!h.isEmpty()) {   
            for (Direction d:h.keySet()) {
                Position chargeStation = h.get(d);
                if(map.PositionPower.get(chargeStation)>0) {
                    nearStation = chargeStation;
                    return d;
                }
                if(map.PositionPower.get(chargeStation)<0) {
                    Directionlist.remove(d);
                }
            }
        }
        if(!Directionlist.isEmpty()) {
            Integer number = rnd.nextInt(Directionlist.size());
            Direction d = Directionlist.get(number);
            return d;
        }
        Direction d =  Direction.values()[rnd.nextInt(16)];
        nearStation = h.get(d);
        return d;
    }

    /**
     * 
     * Stateless drone's play function. When call, the stateless drone will keep moving until it run out of power/steps.
     * 
     */
    @Override
    protected String play() {
        StringBuilder s = new StringBuilder(); 
        Boolean isSuccess; 
        Double prev_latitude;
        Double prev_longitude;
        map.addTrace(curr);
        while(canMove()) {
            Direction d = statelessSearch(); // Select a direction.
            prev_latitude =curr.latitude;
            prev_longitude = curr.longitude;
            
            isSuccess = super.move(d); 
            if(nearStation.inPlayArea()) 
            {
                super.meetChargeStation(nearStation); 
                nearStation.latitude = 0; 
            }
            if(isSuccess) { 
                s.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
                        +","+coin+","+power+"\n");
            }          
        }
        return s.toString();
    }
}
