package uk.ac.ed.inf.powergrab;
import java.util.HashMap;
import java.util.Random;

public class Statelessdrone extends drone {

    public Statelessdrone(Double latitude, Double longitude,int seed, Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    public Direction statelessSearch() { //If there exist a charge Station within 0.0003 degree range , then go towards that station, otherwise return a random direction.}
        HashMap<Direction,String> h = super.haveStation(curr);
        if(!h.isEmpty()) {   
            for (Direction d:h.keySet()) {
                String Id = h.get(d);
                if(map.IDpower.get(Id)>0) {
                    return d;
                }
            }
        }
        Integer number = rnd.nextInt(16);
        Direction d = Direction.values()[number];
        return d;
    }

    public void statelessMove() {
        while(steps>0) {
            if(power<=0) break;
            Direction d = statelessSearch();
            move(d);
        }
        System.out.print("game over");
    }
}
