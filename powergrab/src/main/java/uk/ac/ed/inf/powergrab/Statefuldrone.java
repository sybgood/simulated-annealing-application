package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

public class Statefuldrone extends drone {
    private Double totalGold;
    private ArrayList<Position> stationOrder =new ArrayList<Position>();
    public Statefuldrone(Double latitude, Double longitude,int seed,Map map) {
        super(latitude, longitude, seed, map);
        // TODO Auto-generated constructor stub
    }
    private void transformMap() {
        Double g;
        for(String k:map.IDcoins.keySet()) {
            g=map.IDcoins.get(k);
            if(g>=0) totalGold+=g;
        }
    }
}

