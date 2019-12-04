package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author s1742667
 *
 */
class Statefuldrone extends drone {
    private ArrayList<Position> target; 
    private StringBuilder str = new StringBuilder(); // Record the detail of each move.
    private ArrayList<Position> CoorList; // List of position of charge station.
    /**
     * 
     * @param latitude
     * @param longitude
     * @param seed
     * @param map
     * Constructor, do the same thing as its parent class.
     * And it will also store the list of stations' position.
     */
    protected Statefuldrone(Double latitude, Double longitude,int seed,Map map) {
        super(latitude, longitude, seed, map);
        CoorList = map.getCoorList();
    }
    /**
     * 
     * @param p Position
     * @return the station's position closest to the given position p.
     * 
     */
    private Position findClosest (Position p) {
        Double m_dis = Double.MAX_VALUE;
        Double c_dis = 0.0;
        Position p0 = CoorList.get(0);
        for(Position tempP : CoorList) {
            c_dis = calDistance(p,tempP);
            if(c_dis<=m_dis) {
                p0 = tempP;
                m_dis = c_dis;
            }
        }
        return p0;
    }
    /**
     * By using simulated annealing, get the travelling sequence for the stateful drone. 
     * The function stores the order in target.
     */
    private void getTargetPath() {
        Annealing a = new Annealing(map,1000,300,300.0f,0.986f,rnd,curr);
        Annealing b = new Annealing(map,500,600,2160.0f,0.97973f,rnd,curr);
        a.solve();
        b.solve();
        b.heatAgain();
        a.heatAgain();
        if (b.bestEvaluation>a.bestEvaluation) target = a.givePath();
        else target = b.givePath();
        target.remove(0);
    }
    
    /**
     * Stateful drone's play function.
     * When called, the drone will keep moving and charging
     * until it can't move anymore.
     */
    @Override
    public String play() {
        getTargetPath();
        Boolean b; 
        int i;
        map.addTrace(curr);
        int j=0;    
        int target_size = target.size();
        /* Travelling all the position stored in target, if moving success, 
         * we remove that position in target list.*/
        for(i=0;i<target_size;i++) {
            b = moveTo(target.get(j));
            if(!b) {
             if(!canMove()) return str.toString();
             j++;
            }
            else {
                target.remove(j);
            }
        }
 //           System.out.println("Steps need to finish charging:"+(250-steps));
            While0:
            while(canMove()) {
                if(!target.isEmpty()) {
                    j=0;
                    for(i=0;i<target.size();i++) {
                        b = moveTo(target.get(j));                       
                        if(!b) {
                            if(!canMove()) break While0;
                            j++;
                        }
                        else target.remove(j);
                    }
                }
                /*If we charged all positive station,
                 *we just random select suitable direction to move*/
                if(target.size()==0) {
                    while(canMove()) {
                        randomMove();
                    }
                    return str.toString();
                }
                else  {
                        if(canMove()) {
                            boolean bb = this.unLimitedMoveTo(target.get(0));
                            if(bb)target.remove(0);
                        }
                }
            }
        return str.toString(); 
    }
    /**
     * Move the drone to a direction that won't let the drone lose its power/coins and move the drone.
     */
    protected void randomMove() {
        HashMap<Direction, Position> DStation = haveStation(curr);
        for(Direction d : Direction.values()) {
            if (!DStation.containsKey(d)||map.getPositionCoins().get(DStation.get(d))>=0) {
                Double prev_latitude =curr.getLatitude();
                Double prev_longitude = curr.getLongitude();
                boolean succ = super.move(d);
                if(succ) {
                    str.append(prev_latitude+","+prev_longitude+","+d+","
                            +curr.getLatitude()+","+curr.getLongitude()+","
                                +coin+","+power+"\n");                      
                    break;
                }
            }
        }
    }
    /**
     * 
     * @param p1 Position
     * @param p2 Position
     * @return in what direction is p2 with respect to p1.
     */
    protected static Direction targetDirection(Position p1,Position p2) {
        Double x = p2.getLongitude()-p1.getLongitude();
        Double y = p2.getLatitude()-p1.getLatitude();
        Double degree = Math.atan2(y,x)*180/Math.PI;
        if(degree>-11.25 && degree<=11.25)return Direction.E;
        if(degree>11.25  && degree<=33.75) return Direction.ENE;
        if(degree>33.75  && degree<=56.25) return Direction.NE;
        if(degree>56.25  && degree<=78.75) return Direction.NNE;
        if(degree>78.75  && degree<=101.25)return Direction.N;
        if(degree>101.25 && degree<=123.75) return Direction.NNW;
        if(degree>123.75 && degree<=146.25)return Direction.NW;
        if(degree>146.25 && degree<=168.75)return Direction.WNW;
        if((degree>168.75&& degree<=180)||(degree>=-180&&degree<=-168.75))
            return Direction.W;
        if(degree>-168.75&& degree<=-146.25) return Direction.WSW;
        if(degree>-146.25&& degree<=-123.75)return Direction.SW;
        if(degree>-123.75&& degree<=-101.25) return Direction.SSW;
        if(degree>-101.25&& degree<=-78.75) return Direction.S;
        if(degree>-78.75 && degree<=-56.25) return Direction.SSE;
        if(degree>-56.25 && degree<=-33.75) return Direction.SE;
        if(degree>-33.75 && degree<=-11.25) return Direction.ESE;
        return null;
    }
    /**
     * 
     * @param p1 Position
     * @param ap List of Position
     * @return Whether p1 is inside ap.
     */
    private boolean isInside(Position p1,ArrayList<Position> ap) {
        Double d1 = p1.getLatitude();
        Double d2 = p1.getLongitude();
        for(Position p2:ap) {
            if( Double.compare(d1, p2.getLatitude()) == 0 
                    && Double.compare(d2, p2.getLongitude()) == 0)
                return true;
        }
        return false;
    }
    /**
     * A* algorithm
     * @param p target position
     * @return whether the drone succeed in approaching p.
     */
    private boolean moveTo(Position p) {
        ArrayList<ArrayList<Position>> branches = new ArrayList<ArrayList<Position>>();
        ArrayList<Position> start = new ArrayList<Position>();
        ArrayList<Position> best = new ArrayList<Position>();
        ArrayList<Position> explored = new ArrayList<Position>();
        start.add(curr);
        branches.add(start);
        while(!branches.isEmpty()){
            Position p0 = branches.get(0).get(0);
            Position charge_station = findClosest(p0);
            if(branches.get(0).size()>1 && isNear(p,p0) && charge_station.equals(p)) {
                best = branches.get(0);
                break;
            }
            
            if(branches.size()>5000) {
                return false;
            }
            Boolean delete = false;
            for(int i=1;i<branches.size();i++) {
                ArrayList<Position> b = branches.get(i);
                if(isInside(p0,b)) {
                    delete=true;
                    break;
                }
            }
            if(isInside(p0,explored)||delete) {
                branches.remove(0);
                continue;
            }
            else {
                explored.add(p0);
                HashMap<Direction, Position> DStation;
                ArrayList<Position> temp;
                    temp = branches.get(0);
                    ArrayList<Direction> directionSet = new ArrayList<Direction>();
                    DStation = haveStation(temp.get(0));
                    for(Direction d: Direction.values()) {
                        if(DStation.containsKey(d)) {
                            if(map.getPositionCoins().get(DStation.get(d))<0) {
                                continue;
                            }
                        }
                        directionSet.add(d);
                    }
                    if(!directionSet.isEmpty()) {
                        Position pk;
                        for (Direction d:directionSet) {
                            ArrayList<Position> ttmp = new ArrayList<Position>(temp);
                            pk = p0.nextPosition(d);
                            if(pk.inPlayArea()) {
                            ttmp.add(0,pk);
                            }
                            branches.add(ttmp);
                        }
                        branches.remove(temp);
                        branches.sort((p1,p2) -> {
                            Double h0 = calDistance(p1.get(0),p);
                            Double h1 = calDistance(p2.get(0),p);
                            Double g0 = p1.size()*0.0003;
                            Double g1 = p2.size()*0.0003;
                            Double f0 = h0+g0;
                            Double f1 = h1+g1;
                            return (f0>=f1)? 1 : -1;
                        });
                    }
                    else return false;
            }
        }
        if(!best.isEmpty()) {
            int i;
            Position p0;
            Double prev_latitude;
            Double prev_longitude;
            for(i=best.size()-2;i>=0;i--) {
                prev_latitude =curr.getLatitude();
                prev_longitude = curr.getLongitude();
                p0 = best.get(i);
                Direction d = targetDirection(curr,p0);
                Position charge_station = findClosest(p0);
                if(super.canMove()) {
                    super.move(d);
                    if(isNear(curr,charge_station)) super.meetChargeStation(charge_station);
                    str.append(prev_latitude+","+prev_longitude+","+d
                            +","+curr.getLatitude()+","+curr.getLongitude()
                            +","+coin+","+power+"\n");
                }
            }
            return true;
        }
        else return false;   
    }
    /**
     * 
     * @param p
     * @return
     * Another moveTo method, the difference between unLimitedMoveTo
     * and MoveTo is that the unLimitedMoveTo has no limitations. 
     * And also it will consider pass through the
     * negative charge station.
     * It is designed for the situation that drone is surrounded by negative stations.
     */
    private boolean unLimitedMoveTo(Position p) {
        ArrayList<ArrayList<Position>> branches = new ArrayList<ArrayList<Position>>();
        ArrayList<Position> start = new ArrayList<Position>();
        ArrayList<Position> best = new ArrayList<Position>();
        ArrayList<Position> explored = new ArrayList<Position>();
        start.add(curr);
        branches.add(start);
        int count = 0;
        while(!branches.isEmpty()){
            Position p0 = branches.get(0).get(0);
            Position charge_station = findClosest(p0);
            if(branches.get(0).size()>1&&isNear(p,p0)&&charge_station.equals(p)) {
                best = branches.get(0);
                break;
            }
            
            Boolean delete = false;
            for(int i=1;i<branches.size();i++) {
                ArrayList<Position> b = branches.get(i);
                if(isInside(p0,b)) {
                    delete=true;
                    break;
                }
            }
            if(isInside(p0,explored)||delete) {
                branches.remove(0);
                count = 0;
                continue;
            }
            else {
                explored.add(p0);
                HashMap<Direction, Position> DStation;
                ArrayList<Position> temp;
                    temp = branches.get(0);
                    ArrayList<Direction> directionSet = new ArrayList<Direction>();
                    DStation = haveStation(temp.get(0));
                    Double leastLost = -10000.0;
                    Direction k = Direction.N;
                    for(Direction d: Direction.values()) {
                        if(DStation.containsKey(d)) {
                            Position pk = p0.nextPosition(d);
                            if(pk.inPlayArea()) {
                                Double current_coin = map.getPositionCoins().get(DStation.get(d));
                                if(current_coin>leastLost&&current_coin<0) {
                                    k = d;
                                    leastLost = current_coin;
                                }
                            }
                            if(map.getPositionCoins().get(DStation.get(d))<0) {
                                continue;
                            }
                        }
                        directionSet.add(d);
                    }
                    if(!directionSet.contains(k)&&count==0) {
                        directionSet.add(k);
                        count++;
                    }
                    Position pk;
                    for (Direction d:directionSet) {
                        ArrayList<Position> ttmp = new ArrayList<Position>(temp);
                        pk = p0.nextPosition(d);
                        if(pk.inPlayArea()) {
                        ttmp.add(0,pk);
                        }
                        branches.add(ttmp);
                    }
                    branches.remove(temp);
                    branches.sort((p1,p2) -> {
                        Double h0 = calDistance(p1.get(0),p);
                        Double h1 = calDistance(p2.get(0),p);
                        Double g0 = p1.size()*0.0003;
                        Double g1 = p2.size()*0.0003;
                        Double f0 = h0+g0;
                        Double f1 = h1+g1;
                        return (f0>=f1)? 1 : -1;
                    });             
            }
        }
        if(!best.isEmpty()) {
            int i;
            Position p0;
            Double prev_latitude;
            Double prev_longitude;
            for(i=best.size()-2;i>=0;i--) {
                prev_latitude =curr.getLatitude();
                prev_longitude = curr.getLongitude();
                p0 = best.get(i);
                Direction d = targetDirection(curr,p0);
                Position charge_station = findClosest(p0);
                if(super.canMove()) {
                    super.move(d);
                    if(isNear(curr,charge_station)) super.meetChargeStation(charge_station);
                    str.append(prev_latitude+","+prev_longitude+","+d+","+curr.getLatitude()+","+curr.getLongitude()
                            +","+coin+","+power+"\n");
                }
            }
            return true;
        }
        else return false;   
    }
}
