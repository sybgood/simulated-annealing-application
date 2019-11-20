package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public abstract class drone {
    protected Double coin = 0.0; 
    protected Double power = 250.0; 
    protected int steps = 250; // States how many steps the drone can move, start at 250.
    protected Position curr; // Current position
    protected Map map; // The map it store.
    protected final Random rnd; // Randome seed.
    
    // Constructor
    public drone(Double latitude,Double longitude,int seed,Map map) {
        curr = new Position(latitude,longitude);
        this.map = map;
        rnd = new Random(seed);
    }
    // Move to the given direction, return whether success.
    protected Boolean move(Direction direction) {
        Position next = curr.nextPosition(direction);
        if (canMove()) {
            if(next.inPlayArea()) {
                map.addTrace(next);
                curr = next;
                power -= 1.25;
                steps--;
                return true;
            }
        }
        return false;
    }
    public abstract String move();
    
    public Map getMap() {
        return map;
    }
    // Check self power and steps.
    protected Boolean canMove() {
        if (power>1.25&&steps>0) return true;
        return false;
    }
    // Give a certain position, check whether there exist a position in its next move.
    // Please see the report to get the full details.
    // Return a hashmap with direction as key, and the Station's Id as values


    public static Double calDistance(Position p1, Position p2) {
        Double x1 = p1.latitude;
        Double x2 = p2.latitude;
        Double y1 = p1.longitude;
        Double y2 = p2.longitude;
        return (Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
    }
    
    public static Boolean isNear(Position p1, Position p2) {
        return (calDistance(p1,p2)<=0.00025);
    }
    
    protected void meetChargeStation(Position ID) {
        Double Station_power = map.PositionPower.get(ID);
        Double Station_coin = map.PositionCoins.get(ID);
        if(Station_power>=0) {
            power += Station_power;
            Station_power = 0.0;
            coin += Station_coin;
            Station_coin = 0.0;
            map.PositionCoins.put(ID,Station_coin);
            map.PositionPower.put(ID,Station_power);
        }
        else {
            if(coin>Station_coin) {
                coin += Station_coin;
                Station_coin = 0.0;
            }
            else {
                coin=0.0;
                Station_coin+=coin;
            }
            if(power>Station_power) {
                power+=Station_power;
                Station_power=0.0;
            }
            else {
                Station_power+=power;
                power=0.0;
            }
            map.PositionCoins.put(ID,Station_coin);
            map.PositionPower.put(ID,Station_power);
        }
    }
    protected HashMap<Direction,Position> haveStation(Position p) { 
        HashMap<Direction,Position> k = new HashMap<Direction,Position>();
        int i;
        Position coor;
        Position nextp;
        for (Direction d : Direction.values()) { // Travesal all the directions
            i = 0;
            nextp = p.nextPosition(d); 
            ArrayList<Position> CoorList = map.getCoorList();
            // Sort the station consider the distance to the next position.
            Collections.sort(CoorList,new DistanceComp(nextp));
            while(true) {
                coor = CoorList.get(i);
                if(isNear(nextp,coor)&&!k.containsKey(d)) k.put(d,coor);
                else break;
                i++;
            }
        }
        return k;
    }
}
