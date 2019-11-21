package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
/**
 * 
 * @author s1742667
 *
 */
public class Statefuldrone extends drone {
    private ArrayList<Position> target; 
    private StringBuilder str = new StringBuilder(); // Trace recording.
    private ArrayList<Position> CoorList; // List of position of charge station.
    /**
     * 
     * @param latitude
     * @param longitude
     * @param seed
     * @param map
     * Constructor, do the same thing as its parent class. And it will also store the list of stations' position.
     */
    protected Statefuldrone(Double latitude, Double longitude,int seed,Map map) {
        super(latitude, longitude, seed, map);
        CoorList = map.getCoorList();

        // TODO Auto-generated constructor stub
    }
    /**
     * 
     * @param p Position
     * @return the closest station to the given position p.
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
        // Annealing a = new Annealing(map,2000,400,200.0f,0.995f,rnd,curr);
        Annealing a = new Annealing(map,1000,300,200.0f,0.995f,rnd,curr);

        a.solve();
        target = a.givePath();
        target.remove(0);
    }
    /**
     * Stateful drone's play function.
     * When called, the drone will keep moving and charging until it can't move anymore.
     */
    @Override
    protected String play() {
        getTargetPath();
        Boolean b; 
        int i;
        map.addTrace(curr);
        ArrayList<Integer> remain = new ArrayList<Integer>(); // Remain stores the position that the drone failed in reaching.
        int j=0;    
        int target_size = target.size();
        /* Travelling all the position stored in target, if moving success, we remove that position in target list.*/
        for(i=0;i<target_size;i++) {
            b = moveTo(target.get(j));
            if(!b) {
             if(!canMove()) return str.toString();
             remain.add(j);
             j++;
            }
            else {
                target.remove(j);
            }
        }
        /*The drone has already tried travelling all the targets*/
            System.out.println(250-steps);
            int remain_size =remain.size();
            While0:
            while(canMove()) {
                if(!remain.isEmpty()) {
                    j=0;
                    for(i=0;i<remain_size;i++) {
                        b = moveTo(target.get(remain.get(i)));                       
                        if(!b) {
                            if(!canMove()) break While0;
                            j++;
                        }
                        else remain.remove(j);
                    }
                }
                /*If we charged all positive station, we just random select good direction to move*/
                if(remain_size==0) {
                    while(canMove()) {
                        randomMove();
                    }
                    return str.toString();
                }
                else if(remain.size()!=remain_size) {
                    randomMove();
                }
                /*Following code will be excuted only when the drone is surrounded by the negative charge station, which means
                 * any direction will lead the drone lose its power.
                 */
                else {
                    HashMap<Position,Double> modifycoins= new HashMap<Position,Double>();
                    HashMap<Position,Double> modifypower= new HashMap<Position,Double>();
                    Position nc = CoorList.get(0);
                    Double coins;
                    Double powers;
                    do{
                        Collections.sort(CoorList,new DistanceComp(curr));
                        for (i=0;i<CoorList.size();i++) {
                            nc = CoorList.get(i);
                            coins = map.PositionCoins.get(nc);
                            powers = map.PositionCoins.get(nc);
                            if(coins<0) {
                                modifycoins.put(nc,2*coins);
                                modifypower.put(nc, 2*powers);
                                map.PositionCoins.put(nc, -coins);
                                map.PositionPower.put(nc, -powers);
                                break;
                            }
                        }
                    } while(!moveTo(nc));
                    for(Position p0 :modifycoins.keySet()) {
                        map.PositionCoins.put(p0,modifycoins.get(p0));
                        map.PositionCoins.put(p0,modifypower.get(p0));
                    }
                    super.meetChargeStation(nc);
                    play();
                }
            }
        return str.toString(); 
    }
    /**
     * Generate a direction that do not let the drone lose its power/coins and move the drone.
     */
    protected void randomMove() {
        HashMap<Direction, Position> DStation = haveStation(curr);
        for(Direction d : Direction.values()) {
            if (!DStation.containsKey(d)||map.PositionCoins.get(DStation.get(d))>=0) {
                Double prev_latitude =curr.latitude;
                Double prev_longitude = curr.longitude;
                boolean succ = super.move(d);
                if(succ) {
                    str.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
                    +","+coin+","+power+"\n");                      
                    break;
                }
            }
        }
    }
    /**
     * 
     * @param p1 Position
     * @param p2 Position
     * @return which direction p2 is in p1.
     */
    protected static Direction targetDirection(Position p1,Position p2) {
        Double x = p2.longitude-p1.longitude;
        Double y = p2.latitude-p1.latitude;
        Double degree = Math.atan2(y,x)*180/Math.PI;
        if(degree>-11.25 && degree<=11.25)return Direction.E;
        if(degree>11.25  && degree<=33.75) return Direction.ENE;
        if(degree>33.75  && degree<=56.25) return Direction.NE;
        if(degree>56.25  && degree<=78.75) return Direction.NNE;
        if(degree>78.75  && degree<=101.25)return Direction.N;
        if(degree>101.25 && degree<=123.75) return Direction.NNW;
        if(degree>123.75 && degree<=146.25)return Direction.NW;
        if(degree>146.25 && degree<=168.75)return Direction.WNW;
        if((degree>168.75&& degree<=180)||(degree>=-180&&degree<=-168.75))return Direction.W;
        if(degree>-168.75&& degree<=-146.25) return Direction.WSW;
        if(degree>-146.25&& degree<=-123.75)return Direction.SW;
        if(degree>-123.75&& degree<=-101.25) return Direction.SSW;
        if(degree>-101.25&& degree<=-78.75) return Direction.S;
        if(degree>-78.75 && degree<=-56.25) return Direction.SSE;
        if(degree>-56.25 && degree<=-33.75) return Direction.SE;
        if(degree>-33.75 && degree<=-11.25) return Direction.ESE;
        return null;
    }
    // Return a hashmap that map from direction to station ID. And the set of direction is the the input direciton itself and 6 nearby direction.
    /**
     * 
     * @param p1 Position
     * @param ap List of Position
     * @return Whether p1 is inside ap.
     */
    private boolean isInside(Position p1,ArrayList<Position> ap) {
        Double d1 = p1.latitude;
        Double d2 = p1.longitude;
        for(Position p2:ap) {
            if( Double.compare(d1, p2.latitude) == 0 && Double.compare(d2, p2.longitude) == 0)
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
            if(branches.get(0).size()>1&&isNear(p,p0)&&charge_station.equals(p)) {
                best = branches.get(0);
                break;
            }
            
            if(branches.get(0).size()>30) return false;
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
//                    Position pp = new Position(55.9437410500966,-3.1873695165273106);
//                    DStation = haveStation(pp);
//                    System.out.println("oops");
                    DStation = haveStation(temp.get(0));
                    for(Direction d: Direction.values()) {
                        if(DStation.containsKey(d)) {
                            if(map.PositionCoins.get(DStation.get(d))<0) {
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
                        Collections.sort(branches,new heurstic(p));
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
                prev_latitude =curr.latitude;
                prev_longitude = curr.longitude;
                p0 = best.get(i);
                Direction d = targetDirection(curr,p0);
                Position charge_station = findClosest(p0);
                if(super.canMove()) {
                    super.move(d);
                    if(isNear(curr,charge_station)) super.meetChargeStation(charge_station);
                    str.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
                            +","+coin+","+power+"\n");
                }
            }
            return true;
        }
        else return false;   
    }
    /**
     * Inner class, override of comparator.
     * Act like heurstic function.
     */
    class heurstic implements Comparator<ArrayList<Position>>{
        private Position TargetP;
        /**
         * 
         * @param p Position
         * Constructor, stores a position.
         */
        heurstic(Position p) {
            TargetP = p;
        }
        /**
         * @param b1 A series of position which is drone's predicated movement 
         * @param b2
         * f(n) = h(n)+g(n)
         * Where h(n) is euclidean distance from target to current position
         * g(n) is the cost from start to current.
         * branch with lower f(n) score will be ordered in front.
         */
        @Override
        public int compare(ArrayList<Position> b1, ArrayList<Position> b2) {
            // TODO Auto-generated method stub
            Double h0 = calDistance(b1.get(0),TargetP);
            Double h1 = calDistance(b2.get(0),TargetP);
            Double g0 = b1.size()*0.0003;
            Double g1 = b2.size()*0.0003;
            Double f0 = h0+g0;
            Double f1 = h1+g1;
            return (f0>=f1)? 1 : -1;
        }

        
        
    }
}
