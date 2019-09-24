package uk.ac.ed.inf.powergrab;

public class Position { 
    public double latitude;
    public double longitude;
    private double r = 0.0003;//movement speed
    private double w2 = r*Math.cos(67.5*Math.PI/180);
    private double w3 = r*Math.cos(45*Math.PI/180);
    private double w4 = r*Math.cos(22.5*Math.PI/180);
    private double h2 = r*Math.sin(67.5*Math.PI/180);
    private double h3 = r*Math.sin(45*Math.PI/180);
    private double h4 = r*Math.sin(22.5*Math.PI/180);

    public Position(double latitude, double longitude) { 
        this.latitude  = latitude;
        this.longitude = longitude;
    }
    public Position nextPosition(Direction direction) {
        double n_latitude = latitude;
        double n_longitude = longitude;
        switch (direction) {
            case S:
                n_latitude = latitude - r;
                break;
                
            case N:
                n_latitude = latitude + 0.0003;
                break;
                
            case W:
                n_longitude = longitude - r;
                break;
                
            case E:
                n_longitude = longitude + r;
                break;
            case NNE:
                n_longitude = longitude + w2;
                n_latitude = latitude + h2;
                break;
            case NE:
                n_longitude = longitude +w3;
                n_latitude = latitude + h3;
                break;
            case ENE:
                n_longitude = longitude + w4;
                n_latitude = latitude + h4;
                break;          
            case ESE:
                n_longitude = longitude + w4;
                n_latitude = latitude - h4;
                break;
            case SE:
                n_longitude = longitude + w3;
                n_latitude = latitude - h3;
                break;
            case SSE:
                n_longitude = longitude + w2;
                n_latitude = latitude - h2;
                break;
            case SSW:
                n_longitude = longitude - w2;
                n_latitude = latitude - h2;
                break;
            case SW:
                n_longitude = longitude - w3;
                n_latitude = latitude - h3;
                break;
            case WSW:
                n_longitude = longitude - w4;
                n_latitude =latitude - h4;
                break;
            case WNW:
                n_longitude = longitude - w4;
                n_latitude = latitude + h4;
                break;
            case NW:
                n_longitude = longitude - w3;
                n_latitude = latitude + h3;
                break;
            case NNW:
                n_longitude = longitude - w2;
                n_latitude = latitude + h2;
                break;
                           }
        return new Position(n_latitude,n_longitude);
        }
    public boolean inPlayArea() { 
        boolean x=true, y = true;
        if (this.latitude<=55.942617||this.latitude>=55.946233) x = false;
        if (this.longitude<=-3.192473||this.longitude>=-3.184319) y = false;
        return x&&y;
    }
}
