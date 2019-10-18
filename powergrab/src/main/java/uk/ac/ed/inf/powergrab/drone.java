package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class drone {
    protected Double coin = 0.0;
    protected Double power = 250.0;
    protected int steps = 250;
    protected Position curr;
    protected Map map;
    protected Random rnd;
    protected String nearStation="";

    public drone(Double latitude,Double longitude,int seed,Map map) {
        curr = new Position(latitude,longitude);
        this.map = map;
        rnd = new Random(seed);
    }
    
    public Boolean move(Direction direction) {
        Position next = curr.nextPosition(direction);
        if (canMove()) {
            if(next.inPlayArea()) {
                map.addLineString(curr, next);
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
    private Boolean canMove() {
        if (power>1.25&&steps>0) return true;
        return false;
    }
    
    public HashMap<Direction,String> haveStation(Position p) { 
        HashMap<Direction,String> k = new HashMap<Direction,String>();
        
        for (Direction d : Direction.values()) {
            Position nextp = curr.nextPosition(d);
            if(nextp.inPlayArea()) {
                for (Position coor: map.getCoorList()) {
                    if(isNear(nextp,coor)) {
                        k.put(d,map.CoordinateId.get(coor));
                    }
                }
            }
        }
        return k;
    }
    
    public static Boolean isNear(Position p1, Position p2) {
        Double x1 = p1.latitude;
        Double x2 = p2.latitude;
        Double y1 = p1.longitude;
        Double y2 = p2.longitude;
        return (Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2))<=0.00025);
    }
    
    public void meetChargeStation(String ID) {
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
