package uk.ac.ed.inf.powergrab;

public class Position { 
    public double latitude;
    public double longitude;
    private static double r = 0.0003;//movement speed

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
                return new Position(latitude+r*Math.sin(67.5*Math.PI/180),longitude +r * Math.cos(67.5*Math.PI/180));
            case NE:
                return new Position(latitude+r*Math.cos(45*Math.PI/180),longitude + r*Math.sin(45*Math.PI/180));
            case ENE:
                return new Position(latitude + r*Math.sin(22.5*Math.PI/180),longitude + r*Math.cos(22.5*Math.PI/180));          
            case ESE:
                return new Position(latitude - r*Math.sin(22.5*Math.PI/180),longitude + r*Math.cos(22.5*Math.PI/180));
            case SE:
                return new Position(latitude - r*Math.sin(45*Math.PI/180),longitude + r*Math.cos(45*Math.PI/180));
            case SSE:
                return new Position(latitude - r*Math.sin(67.5*Math.PI/180),longitude + r * Math.cos(67.5*Math.PI/180));
            case SSW:
                return new Position(latitude - r*Math.sin(67.5*Math.PI/180),longitude - r * Math.cos(67.5*Math.PI/180));
            case SW:
                return new Position(latitude - r*Math.sin(45*Math.PI/180),longitude - r*Math.cos(45*Math.PI/180));
            case WSW:
                return new Position(latitude - r*Math.sin(22.5*Math.PI/180),longitude - r*Math.cos(22.5*Math.PI/180));
            case WNW:
                return new Position(latitude + r*Math.sin(22.5*Math.PI/180),longitude - r*Math.cos(22.5*Math.PI/180));
            case NW:
                return new Position(latitude + r*Math.sin(45*Math.PI/180),longitude - r*Math.cos(45*Math.PI/180));
            case NNW:
                return new Position(latitude+r*Math.sin(67.5*Math.PI/180),longitude - r * Math.cos(67.5*Math.PI/180));
            default: return null;
        }
            
        }
    public boolean inPlayArea() { 
        if (this.latitude<=55.942617||this.latitude>=55.946233) return false;;
        if (this.longitude<=-3.192473||this.longitude>=-3.184319) return false;
        return true;
    }
}
