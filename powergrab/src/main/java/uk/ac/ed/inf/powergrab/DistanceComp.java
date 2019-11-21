package uk.ac.ed.inf.powergrab;

import java.util.Comparator;
/**
 * 
 * @author s1742667
 *
 */
public class DistanceComp implements Comparator <Position> {
    private Position TargetP;
    /**
     * 
     * @param p Target position, used for compare.
     */
    public DistanceComp(Position p) {
        TargetP = p;
    }
    /**
     * 
     * @param p1 Position 
     * @param p2 Position
     * If the distance between p1 and target position is greater than the 
     * distance between p2 and target position. p1 will have larger index 
     * than p2.
     */
    @Override
    public int compare(Position p1, Position p2) {
        Double a = drone.calDistance(p1,TargetP);
        Double b = drone.calDistance(p2, TargetP);
        return (a>b) ? 1 : -1 ;
    }
}
