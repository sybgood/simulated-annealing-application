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
        switch (direction) {
            case S:
                latitude = latitude - r;
                break;
            case N:
                latitude = latitude + r;
                break;
            case W:
                longitude = longitude - r;
                break;
            case E:
                longitude = longitude + r;
                break;
            case NNE:
                longitude = longitude + w2;
                latitude = latitude + h2;
                break;
            case NE:
                longitude = longitude +w3;
                latitude = latitude + h3;
                break;
            case ENE:
                longitude = longitude + w4;
                latitude = latitude + h4;
                break;          
            case ESE:
                longitude = longitude + w4;
                latitude = latitude - h4;
                break;
            case SE:
                longitude = longitude + w3;
                latitude = latitude - h3;
                break;
            case SSE:
                longitude = longitude + w2;
                latitude = latitude - h2;
                break;
            case SSW:
                longitude = longitude - w2;
                latitude = latitude - h2;
                break;
            case SW:
                longitude = longitude - w3;
                latitude = latitude - h3;
                break;
            case WSW:
                longitude = longitude - w4;
                latitude =latitude - h4;
                break;
            case WNW:
                longitude = longitude - w4;
                latitude = latitude + h4;
                break;
            case NW:
                longitude = longitude - w3;
                latitude = latitude + h3;
                break;
            case NNW:
                longitude = longitude - w2;
                latitude = latitude + h2;
                break;
                           }
        Position p = new Position(latitude,longitude);
        return p;
        }
    public boolean inPlayArea() { 
        return false;
    }
}
