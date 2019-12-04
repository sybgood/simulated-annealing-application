package uk.ac.ed.inf.powergrab;

class Position { 
    private double latitude;
    private double longitude;
    private static final double r = 0.0003;//movement speed
    private static final double w2 = r*Math.cos(67.5*Math.PI/180);
    private static final double h2 = r*Math.sin(67.5*Math.PI/180);
    private static final double w3 = r*Math.cos(45*Math.PI/180);
    private static final double h3 = r*Math.sin(45*Math.PI/180);
    private static final double w4 = r*Math.cos(22.5*Math.PI/180);
    private static final double h4 = r*Math.sin(22.5*Math.PI/180);
    protected Position(double latitude, double longitude) { 
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
    /*Return a new position accordng to the direction*/
    protected Position nextPosition(Direction direction) {
        switch (direction) {
            case S:
                return new Position (getLatitude() - r, getLongitude());
            case N:
                return new Position (getLatitude() + r, getLongitude());
            case W:
                return new Position(getLatitude(),getLongitude()-r);            
            case E:
                return new Position(getLatitude(),getLongitude()+r);
            case NNE:
                return new Position(getLatitude() + h2,getLongitude() + w2);
            case NE:
                return new Position(getLatitude() + w3,getLongitude() + h3);
            case ENE:
                return new Position(getLatitude() + h4,getLongitude() + w4);          
            case ESE:
                return new Position(getLatitude() - h4,getLongitude() + w4);
            case SE:
                return new Position(getLatitude() - h3,getLongitude() + w3);
            case SSE:
                return new Position(getLatitude() - h2,getLongitude() + w2);
            case SSW:
                return new Position(getLatitude() - h2,getLongitude() - w2);
            case SW:
                return new Position(getLatitude() - h3,getLongitude() - w3);
            case WSW:
                return new Position(getLatitude() - h4,getLongitude() - w4);
            case WNW:
                return new Position(getLatitude() + h4,getLongitude() - w4);
            case NW:
                return new Position(getLatitude() + h3,getLongitude() - w3);
            case NNW:
                return new Position(getLatitude() + h2,getLongitude() - w2);
            default: return null;
        }
            
        }
    /*
     * Check whether current position is in play area.
     */
    protected boolean inPlayArea() { 
        if (this.getLatitude()<=55.942617||this.getLatitude()>=55.946233) return false;;
        if (this.getLongitude()<=-3.192473||this.getLongitude()>=-3.184319) return false;
        return true;
    }
    /*
     * Getter and setter.
     */
    protected double getLatitude() {
        return latitude;
    }
    protected void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    protected double getLongitude() {
        return longitude;
    }
    protected void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
