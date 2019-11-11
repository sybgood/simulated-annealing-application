package uk.ac.ed.inf.powergrab;

import java.util.Comparator;

public class DistanceComp implements Comparator <Position> {
    private Position TargetP;
    public DistanceComp(Position p) {
        TargetP = p;
    }
    @Override
    public int compare(Position p1, Position p2) {
        Double a = drone.calDistance(p1,TargetP);
        Double b = drone.calDistance(p2, TargetP);
        return (a>b) ? 1 : -1 ;
    }
}
