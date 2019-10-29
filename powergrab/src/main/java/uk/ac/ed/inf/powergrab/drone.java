package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public abstract class drone {
    protected Double coin = 0.0;
    protected Double power = 250.0;
    protected int steps = 250;
    protected Position curr;
    protected Map map;
    protected final Random rnd;
    protected String nearStation="";
    
    public drone(Double latitude,Double longitude,int seed,Map map) {
        curr = new Position(latitude,longitude);
        this.map = map;
        rnd = new Random(seed);
    }
    
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
    
    public Map getMap() {
        return map;
    }
    protected Boolean canMove() {
        if (power>1.25&&steps>0) return true;
        return false;
    }

    protected HashMap<Direction,String> haveStation(Position p) { 
        HashMap<Direction,String> k = new HashMap<Direction,String>();
        // wait for map.getCoorList().sort()
        int i;
        Position coor;
        Position nextp;
        for (Direction d : Direction.values()) {
            i = 0;
            nextp = curr.nextPosition(d);
            ArrayList<Position> CoorList = map.getCoorList();
            Collections.sort(CoorList,new DistanceComp(nextp));
            while(true) {
                coor = CoorList.get(i);
                if(isNear(nextp,coor)&&!k.containsKey(d)) k.put(d,map.CoordinateId.get(coor));
                else break;
                i++;
            }
        }
        return k;
    }

    public static Double CalDistance(Position p1, Position p2) {
        Double x1 = p1.latitude;
        Double x2 = p2.latitude;
        Double y1 = p1.longitude;
        Double y2 = p2.longitude;
        return (Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
    }
    
    public static Boolean isNear(Position p1, Position p2) {
        return (CalDistance(p1,p2)<=0.00025);
    }
    
    protected void meetChargeStation(String ID) {
        Double Station_power = map.IDpower.get(ID);
        Double Station_coin = map.IDcoins.get(ID);
        if(Station_power>=0) {
            power += Station_power;
            Station_power = 0.0;
            coin += Station_coin;
            Station_coin = 0.0;
            map.IDcoins.put(ID,Station_coin);
            map.IDpower.put(ID,Station_power);
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
            map.IDcoins.put(ID,Station_coin);
            map.IDpower.put(ID,Station_power);
        }
    }

}
