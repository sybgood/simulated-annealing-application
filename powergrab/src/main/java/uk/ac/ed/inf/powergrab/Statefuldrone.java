package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import java.util.Collections;
import java.util.HashMap;

public class Statefuldrone extends drone {
    private ArrayList<Position> target; // A list that guiding the drone, 
    //which order of position the drone should go.
    //private Direction[] nDset = new Direction[7]; // It contains 7 direction that the drone should go.
    //private static final int[] directionIndex = {0,1,-1,2,-2,3,-3}; // Will be expained in report.
   // private static final Direction[] directionSet = Direction.values(); 
    //private static final int Dlen = directionSet.length; // Indicates how many direction we have.
    private StringBuilder str = new StringBuilder(); // Trace recording.
    private ArrayList<Position> CoorList;
    // Constructor.
    public Statefuldrone(Double latitude, Double longitude,int seed,Map map) {
        super(latitude, longitude, seed, map);
        CoorList = map.getCoorList();

        // TODO Auto-generated constructor stub
    }
    private void getTargetPath() {
        // By using simulated annealing, get which order of position we should go.
  //      Annealing a = new Annealing(map,2000,400,200.0f,0.995f,rnd,curr);
        Annealing a = new Annealing(map,1000,300,200.0f,0.995f,rnd,curr);

        a.solve();
        target = a.givePath();
        target.remove(0);
    }
    @Override
    //Move function, the drone will not stop until it run out of power or it achieves 250 moves.
    public String move() {
        getTargetPath();
        Boolean b; // Indicates whether we got a successful move or not.
        int i;
        map.addTrace(curr);
        ArrayList<Integer> remain = new ArrayList<Integer>();
        int j=0;    
        int target_size = target.size();
        for(i=0;i<target_size;i++) {
            b = moveTo(target.get(j));
            if(!b) {
             if(!canMove()) return str.toString();
             remain.add(j);
             j++;
//             continue;
            }
            else {
                target.remove(j);
            }
        }
        if(target_size!=remain.size()) {
                While0:
                while(canMove()) {
                    if(!remain.isEmpty()) {
                      //  System.out.print("\n"+remain.size());
                        for(i=0;i<remain.size();i++) {
                            b = moveTo(target.get(remain.get(i)));                       
                       //     System.out.print("\n"+remain.get(i));
                            if(!b) {
                                if(!canMove()) break While0;
                                continue;
                            }
                        }
                    }
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
        }
        else {
            HashMap<Position,Double> modify= new HashMap<Position,Double>();
            Position nc = CoorList.get(0);
            Double coins;
            do{
                Collections.sort(CoorList,new DistanceComp(curr));
                for (i=0;i<CoorList.size();i++) {
                    nc = CoorList.get(i);
                    coins = map.PositionCoins.get(CoorList.get(i));
                    if(coins<0) {
                        modify.put(nc,coins);
                        coins = -coins;
                        break;
                    }
                }
            } while(!moveTo(nc));
            for(Position p0 :modify.keySet()) {
                coins = map.PositionCoins.get(p0);
                map.PositionCoins.put(p0, -coins);
            }
            super.meetChargeStation(nc);
            move();
        }
        return str.toString(); 
    }
    // Return the direction from p1 to p2.
    public static Direction targetDirection(Position p1,Position p2) {
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
    
    private boolean isInside(Position p1,ArrayList<Position> ap) {
        Double d1 = p1.latitude;
        Double d2 = p1.longitude;
        for(Position p2:ap) {
            if( Double.compare(d1, p2.latitude) == 0 && Double.compare(d2, p2.longitude) == 0)
                return true;
        }
        return false;
    }
    private boolean moveTo(Position p) {
        ArrayList<ArrayList<Position>> branches = new ArrayList<ArrayList<Position>>();
        ArrayList<Position> start = new ArrayList<Position>();
        ArrayList<Position> best = new ArrayList<Position>();
        ArrayList<Position> explored = new ArrayList<Position>();
        start.add(curr);
        branches.add(start);
        while(!branches.isEmpty()){
            Position p0 = branches.get(0).get(0);
            Collections.sort(CoorList,new DistanceComp(p0));
            
            if(branches.get(0).size()>1&&isNear(p,p0)&&CoorList.get(0).equals(p)) {
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
                        for (Direction d:directionSet) {
                            ArrayList<Position> ttmp = new ArrayList<Position>(temp);
                            ttmp.add(0,p0.nextPosition(d));
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
                Collections.sort(CoorList,new DistanceComp(p0));

                if(super.canMove()) {
                    super.move(d);
                    if(isNear(curr,CoorList.get(0))) super.meetChargeStation(CoorList.get(0));
                    str.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
                            +","+coin+","+power+"\n");
                }
            }
//            prev_latitude =curr.latitude;
//            prev_longitude = curr.longitude;
//            p0 = best.get(0);
//            Direction d = targetDirection(curr,p0);
//            if(super.canMove()) {
//                super.move(d);
////                boolean k=false;
////                if(isNear(curr,p)&&CoorList.get(0).equals(p)) k = true;
//                super.meetChargeStation(p);
//                str.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
//                        +","+coin+","+power+"\n");
////                System.out.print(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
////                        +","+coin+","+power+"\n");
//            }
            return true;
        }
        else return false;
//        HashMap<Direction,String> DStation;
//        Boolean b;
//
//        ArrayList<Position> CoorList = map.getCoorList();
//        Collections.sort(CoorList,new DistanceComp(curr));
//        while(p!=CoorList.get(0)||!isNear(curr,p)) {
//            Direction d = targetDirection(curr,p);
//            DStation = haveStation(curr,d);
//            Double prev_latitude =curr.latitude;
//            Double prev_longitude = curr.longitude;
//            if(DStation.isEmpty()) {
//                b = move(d);
//                if(!b&&!canMove()) {
//                 //   System.out.print(b);
//                    return b;
//                }
//                else{str.append(prev_latitude+","+prev_longitude+","+d+","+curr.latitude+","+curr.longitude
//                        +","+coin+","+power+"\n");
//                }
//            }
//            else {
//                int i;
//                Direction dd;
//                int random_number =rnd.nextInt(5);
//                for(i=0;i<nDset.length;i++) {
//                    dd = nDset[(i+random_number)%7];
//                    if(!DStation.containsKey(dd)){
//                        b = move(dd);
//                        if(!b && !canMove()) return b;
//                        if(b){
//                            str.append(prev_latitude+","+prev_longitude+","+dd+","+curr.latitude+","+curr.longitude
//                                +","+coin+","+power+"\n");
//                            break;
//                        }
//                    }
//                    else {
//                        if(map.IDcoins.get(DStation.get(dd))>=0) {
//                            b = move(dd);
//                            if(!b) {
//                                if(!canMove())return b;
//                                continue;
//                            }
//                            super.meetChargeStation(DStation.get(dd));
//                            str.append(prev_latitude+","+prev_longitude+","+dd+","+curr.latitude+","+curr.longitude
//                                    +","+coin+","+power+"\n");
//                            break;
//                        }
//                    }
//                }
//                if(i==nDset.length) return false;
//            }
//            Collections.sort(CoorList,new DistanceComp(curr));
//        }
//        super.meetChargeStation(map.CoordinateId.get(p));
//        return true;
        
    }
    class heurstic implements Comparator<ArrayList<Position>>{
        private Position TargetP;
        public heurstic(Position p) {
            TargetP = p;
        }
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
//    public String statelessmove() {
//        getTargetPath();
//        Boolean b; // Indicates whether we got a successful move or not.
//        int i;
//        Position np; // next target charge station's position.
//        map.addTrace(curr);
//        ArrayList<Integer> remain = new ArrayList<Integer>();
//            np = target.get(0);
//            if(isNear(curr,np)) { // To avoid the drone charge at the begining.
//                target.remove(0);
//                target.add(1,np);
//            }
//        for(i=0;i<target.size();i++) {
//            b = moveTo(target.get(i));
//            if(!b) {
//             if(!canMove()) break;
//             remain.add(i);
////             continue;
//            }
//        }
//        if(i==target.size()) {
//                While:
//                while(canMove()) {
//                    if(!remain.isEmpty()) {
//                      //  System.out.print("\n"+remain.size());
//                        for(i=0;i<remain.size();i++) {
//                            b = moveTo(target.get(remain.get(i)));                       
//                       //     System.out.print("\n"+remain.get(i));
//                            if(!b) {
//                                if(!canMove()) break While;
//                                continue;
//                            }
//                        }
//                    }
//                    moveTo(target.get(rnd.nextInt(target.size())));
//                }
//        }
//        return str.toString(); 
//    }
}
