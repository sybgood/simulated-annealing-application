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
    public drone(Double latitude,Double longitude,int seed,Map map) {
        curr = new Position(latitude,longitude);
        map = map;
        rnd = new Random(seed);
    }
    
    public void move(Direction direction) {
        Position next = curr.nextPosition(direction);
        if (canMove()) {
            if(next.inPlayArea()) {
                curr = next;
                power = power - 1.25;
                steps--;
            }
            else {
                System.out.print("Not in area!");
            }
        }
        else {
            System.out.print("Game over");
        }
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
                Double lati = nextp.latitude;
                Double loni = nextp.longitude;
                
                for (ArrayList<Double> coor: map.getCoorList()) {
                    if(calculateD(lati,loni,coor.get(1),coor.get(0))<=0.00025) {
                        k.put(d,map.CoordinateId.get(coor));
                    }
                }
            }
        }
        return k;
    }
    
    public Double calculateD(Double x1,Double y1,Double x2,Double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
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
