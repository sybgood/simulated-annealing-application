package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class Statelessdrone extends drone {

    public Statelessdrone(Double latitude, Double longitude,int seed, Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    
    public Direction statelessSearch() { //If there exist a charge Station within 0.0003 degree range , then go towards that station, otherwise return a random direction.}
        HashMap<Direction,String> h = super.haveStation(curr);
        Direction[] directionSet = Direction.values();
        List<Direction>Directionlist = new ArrayList<Direction>(Arrays.asList(directionSet));
        if(!h.isEmpty()) {   
            for (Direction d:h.keySet()) {
                String Id = h.get(d);
                if(map.IDpower.get(Id)>0) {
                    nearStation = Id;
                    return d;
                }
                if(map.IDpower.get(Id)<0) {
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

    public String statelessMove() {
        StringBuilder s = new StringBuilder();
        map.addTrace(curr);
        while(steps>0) {
            if(power<=0) break;
            Direction d = statelessSearch();
            Double prev_latitude =curr.latitude;
            Double prev_longitude = curr.longitude;
            Boolean isSuccess = super.move(d);
            if(!nearStation.isEmpty())
            {
                super.meetChargeStation(nearStation);
                nearStation = "";
            }
            if(isSuccess) {
                s.append(prev_latitude+" "+prev_longitude+" "+d+" "+curr.latitude+" "+curr.longitude
                        +" "+coin+" "+power+"\n");
                //System.out.print(steps+"\n");
            }          
        }
        return s.toString();
    }
}
