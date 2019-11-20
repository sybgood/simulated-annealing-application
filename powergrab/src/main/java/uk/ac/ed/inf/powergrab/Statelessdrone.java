package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class Statelessdrone extends drone {
    protected Position nearStation = new Position(0,0);  // Position for the station then the drone will charge.

    public Statelessdrone(Double latitude, Double longitude,int seed, Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    
    public Direction statelessSearch() { //If there exist a charge Station within 0.0003 degree range, 
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

    // Move function, the drone will not stop until it run out of power or it achieves 250 moves.

    @Override
    public String move() {
        StringBuilder s = new StringBuilder(); // Record the trace.
        Boolean isSuccess; // Indicates whether the movement is success or not.
        Double prev_latitude;
        Double prev_longitude;
        map.addTrace(curr);
        while(steps>0) {
            if(power<=0) break;
            Direction d = statelessSearch(); // Select a direction.
            // record the current latitude and longitude.
            prev_latitude =curr.latitude;
            prev_longitude = curr.longitude;
            
            isSuccess = super.move(d); // Move.
            if(nearStation.inPlayArea()) // Check whether we got a station within the range.
            {
                super.meetChargeStation(nearStation); // Charge
                nearStation.latitude = 0; // Initialise the nearStation.
            }
            if(isSuccess) { // If moving success, we will record this move.
                s.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
                        +","+coin+","+power+"\n");
            }          
        }
        return s.toString();
    }
}
