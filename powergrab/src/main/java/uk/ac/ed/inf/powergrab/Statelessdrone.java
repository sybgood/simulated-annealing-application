package uk.ac.ed.inf.powergrab;
import java.util.Random;

public class Statelessdrone extends drone {

    public Statelessdrone(Double latitude, Double longitude,int seed, Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    public Direction statelessSearch() { //If there exist a charge Station within 0.0003 degree range , then go towards that station, otherwise return a random direction.}
        return null;
    }

    public void statelessMove() {
        while(steps>0) {
            if(power<=0) break;
            Direction d = statelessSearch();
            move(d);
        }
    }
}
