package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

abstract class drone {
    protected Double coin = 0.0; 
    protected Double power = 250.0; 
    protected int steps = 250;
    protected Position curr;
    protected Map map;  
    protected final Random rnd; 
    
    /**
     * 
     * @param latitude 
     * @param longitude
     * @param seed
     * @param map
     * 
     * Drone constructor. Laitude and longitude are the starting position.
     * Meanwhile, we make a random number generator rnd by the given seed.
     * Also it stores a map in its fields.
     * 
     */
    public drone(Double latitude,Double longitude,int seed,Map map) {
        curr = new Position(latitude,longitude);
        if(!curr.inPlayArea()) throw new IllegalArgumentException ("Start position is not in play area!");
        this.map = map;
        rnd = new Random(seed);
    }
    /**
     * 
     * @param direction
     * @return boolean value, indicates whether this move is success or not.
     * Move the drone to the given direction.
     */
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
    /**
     * 
     *@return  a string which contains each move's detail for the drone.
     */
    public abstract String play();

    /**
     * 
     * @return Boolean value shows whether the drone can move or not.
     * It has 2 condition. The drone must have enough power, and the drone still doesn't reach 250 steps.
     */
    protected Boolean canMove() {
        return (power>=1.25&&steps>0);
    }
    /**
     * 
     * @param p1 Position
     * @param p2 Position
     * @return Euclidean distance between p1 and p2.
     */
    protected static Double calDistance(Position p1, Position p2) {
        Double x1 = p1.getLatitude();
        Double x2 = p2.getLatitude();
        Double y1 = p1.getLongitude();
        Double y2 = p2.getLongitude();
        return (Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
    }
    /**
     * 
     * @param p1 Position
     * @param p2 Position
     * @return whether p1 and p2 are within 0.00025 unit range.
     */
    protected static Boolean isNear(Position p1, Position p2) {
        return (calDistance(p1,p2)<=0.00025);
    }
    /**
     * 
     * @param p0
     * p0 should be a station's position. If so, the drone will charge from its corresponding station.
     */
    protected void meetChargeStation(Position p0) {
        if(map.getCoorList().contains(p0)) {
            Double Station_power = map.getPositionPower().get(p0);
            Double Station_coin = map.getPositionCoins().get(p0);
            if(Station_power>=0&&Station_coin>=0) {
                power += Station_power;
                Station_power = 0.0;
                coin += Station_coin;
                Station_coin = 0.0;
                map.getPositionCoins().put(p0,Station_coin);
                map.getPositionPower().put(p0,Station_power);
            }
            else {
                if(coin>Math.abs(Station_coin)) {
                    coin += Station_coin;
                    Station_coin = 0.0;
                }
                else {
                    coin=0.0;
                    Station_coin+=coin;
                }
                if(power>Math.abs(Station_power)) {
                    power+=Station_power;
                    Station_power=0.0;
                }
                else {
                    Station_power+=power;
                    power=0.0;
                }
                map.getPositionCoins().put(p0,Station_coin);
                map.getPositionPower().put(p0,Station_power);
            }
        }
        else System.out.print("This position is not a valid charge station's position!");
    }
    /**
     * 
     * @param p Position current position
     * @return Hashmap with direction as a key, position is the charge station position.
     * haveStation first loop 16 direction, find which direction may lead the drone charge from a station.
     * If move to direction D can let the drone charge from a charge station S, then we put (D,S.position) to the map. 
     * Otherwise we do nothing to the map.
     * 
     */

    protected HashMap<Direction,Position> haveStation(Position p) {     
        HashMap<Direction,Position> k = new HashMap<Direction,Position>();
        int i;
        Position coor;
        Position nextp;
        for (Direction d : Direction.values()) { 
            i = 0;
            nextp = p.nextPosition(d);
            if(!nextp.inPlayArea()) continue;
            ArrayList<Position> CoorList = map.getCoorList();
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
