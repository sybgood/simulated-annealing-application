package uk.ac.ed.inf.powergrab;

public class Position { 
    public double latitude;
    public double longitude;
    private static final double r = 0.0003;//movement speed
    private static final double w2 = r*Math.cos(67.5*Math.PI/180);
    private static final double h2 = r*Math.sin(67.5*Math.PI/180);
    private static final double w3 = r*Math.cos(45*Math.PI/180);
    private static final double h3 = r*Math.sin(45*Math.PI/180);
    private static final double w4 = r*Math.cos(22.5*Math.PI/180);
    private static final double h4 = r*Math.sin(22.5*Math.PI/180);
    public Position(double latitude, double longitude) { 
        this.latitude  = latitude;
        this.longitude = longitude;
    }
    public Position nextPosition(Direction direction) {
        switch (direction) {
            case S:
                return new Position (latitude - r, longitude);
            case N:
                return new Position (latitude + r, longitude);
            case W:
                return new Position(latitude,longitude-r);            
            case E:
                return new Position(latitude,longitude+r);
            case NNE:
                return new Position(latitude + h2,longitude + w2);
            case NE:
                return new Position(latitude + w3,longitude + h3);
            case ENE:
                return new Position(latitude + h4,longitude + w4);          
            case ESE:
                return new Position(latitude - h4,longitude + w4);
            case SE:
                return new Position(latitude - h3,longitude + w3);
            case SSE:
                return new Position(latitude - h2,longitude + w2);
            case SSW:
                return new Position(latitude - h2,longitude - w2);
            case SW:
                return new Position(latitude - h3,longitude - w3);
            case WSW:
                return new Position(latitude - h4,longitude - w4);
            case WNW:
                return new Position(latitude + h4,longitude - w4);
            case NW:
                return new Position(latitude + h3,longitude - w3);
            case NNW:
                return new Position(latitude + h2,longitude - w2);
            default: return null;
        }
            
        }
    public boolean inPlayArea() { 
        if (this.latitude<=55.942617||this.latitude>=55.946233) return false;;
        if (this.longitude<=-3.192473||this.longitude>=-3.184319) return false;
        return true;
    }
}
