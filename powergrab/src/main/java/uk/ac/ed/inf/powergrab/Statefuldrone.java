package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Statefuldrone extends drone {
    private ArrayList<Position> target;
    private Direction[] nDset = new Direction[7];
    private static final int[] kk = {0,1,-1,2,-2,3,-3};
    private static final Direction[] directionSet = Direction.values();
    private static final int Dlen = directionSet.length;
    private StringBuilder str = new StringBuilder();

    public Statefuldrone(Double latitude, Double longitude,int seed,Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    private void getTargetPath() {
        Annealing a = new Annealing(map,2000,400,200.0f,0.995f,rnd,curr);
        a.solve();
        target = a.givePath();
        target.remove(0);
    }
    public String toString() {
        return str.toString();
        
    }
    public void statefulMove() {
        getTargetPath();
        Boolean b;
        int i;
        ArrayList<Integer> remain = new ArrayList<Integer>();
        for(i=0;i<target.size();i++) {
            b = moveTo(target.get(i));
            if(!b) {
             if(!canMove()) break;
             remain.add(i);
             continue;
            }
        }
        if(i==target.size()) {
                While:
                while(canMove()) {
                    if(!remain.isEmpty()) {
                      //  System.out.print("\n"+remain.size());
                        for(i=0;i<remain.size();i++) {
                            b = moveTo(target.get(remain.get(i)));                       
                       //     System.out.print("\n"+remain.get(i));
                            if(!b) {
                                if(!canMove()) break While;
                                continue;
                            }
                        }
                    }
                    moveTo(target.get(rnd.nextInt(target.size())));
                }
        }
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
    private HashMap<Direction,String> haveStation(Position p,Direction D) {
        HashMap<Direction,String> k = new HashMap<Direction,String>();
        // wait for map.getCoorList().sort()
        int i;
        Position coor;
        Position nextp;
        int index=0;
        for (i = 0;i<Dlen;i++) {
            if (directionSet[i] == D) index = i;
        }
        int l = 0;
        for(int j:kk) {
            nDset[l] = directionSet[(index+Dlen+j)%Dlen];
            l++;
        }
        ArrayList<Position> CoorList = map.getCoorList();
        for (Direction d : nDset) {
            i = 0;
            nextp = curr.nextPosition(d);
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
    
    private boolean moveTo(Position p) {
        HashMap<Direction,String> DStation;
        Boolean b;
        ArrayList<Position> CoorList = map.getCoorList();
        Collections.sort(CoorList,new DistanceComp(curr));
        while(p!=CoorList.get(0)) {
            Direction d = targetDirection(curr,p);
            DStation = haveStation(curr,d);
            Double prev_latitude =curr.latitude;
            Double prev_longitude = curr.longitude;
            
            if(DStation.isEmpty()) {
                b = move(d);
                if(!b) {
                 //   System.out.print(b);
                    return b;
                }
                str.append(prev_latitude+" "+prev_longitude+" "+d+" "+curr.latitude+" "+curr.longitude
                        +" "+coin+" "+power+"\n");
            }
            else {
                int i;
                Direction dd;
                for(i=0;i<nDset.length;i++) {
                    dd = nDset[i];
                    if(!DStation.containsKey(dd)) {
                        b = move(dd);
                        if(!b && !canMove()) return b;
                        else{
                            str.append(prev_latitude+" "+prev_longitude+" "+d+" "+curr.latitude+" "+curr.longitude
                                +" "+coin+" "+power+"\n");
                        }
                    }
                    else {
                        if(map.IDcoins.get(DStation.get(dd))>=0) {
                            b = move(dd);
                            if(!b) {
                                if(!canMove())return b;
                                break;
                            }
                            super.meetChargeStation(DStation.get(dd));
                            str.append(prev_latitude+" "+prev_longitude+" "+d+" "+curr.latitude+" "+curr.longitude
                                    +" "+coin+" "+power+"\n");
                            break;
                        }
                    }
                }
                if(i==nDset.length) return false;
            }
        }
        super.meetChargeStation(map.CoordinateId.get(p));
        return true;
    }
}

