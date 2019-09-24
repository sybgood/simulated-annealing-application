package uk.ac.ed.inf.powergrab;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        final Position p0 = new Position(55.944425, -3.188396);
        Position p1 = p0.nextPosition(Direction.N);
        System.out.print(p1.longitude==p0.longitude);
    }
}
