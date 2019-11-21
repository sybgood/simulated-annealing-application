package uk.ac.ed.inf.powergrab;

import java.io.*;

import java.net.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.google.gson.*;
import com.mapbox.geojson.*;
import java.util.ArrayList;
/**
 * @author s1742667
 */
public class Map {

    protected HashMap<Position,Double> PositionCoins = new HashMap<Position,Double>(); // Map a position of station to its coins.
    protected HashMap<Position,Double> PositionPower = new HashMap<Position,Double>(); // Map a position of station to ist power.
    private ArrayList<Position> CoordinatedList = new ArrayList<Position>(); // List of stations' position.
    private List<Point> trace = new ArrayList<Point>(); // The drones' moving trace record.
    private String result;  // A geostring which we download from internet.
    
    /**
     * 
     * @param year
     * @param month
     * @param day
     * 
     *  Class constructor, it takes the listed parameter and download the specific map. Then it fills the fields by calling other functions.
     */
    protected Map(String year,String month,String day) {
            String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"+year+"/"+month+"/"+day+"/"+"powergrabmap.geojson";
            try {
                //HTTP URL connection   
                URL mapURL = new URL(mapString);
                URLConnection conn = mapURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.connect();
                InputStream mapSource = conn.getInputStream();
                // Read map.
                result = new BufferedReader(new InputStreamReader(mapSource)).lines().collect(Collectors.joining("\n"));
                FeatureCollection fc = FeatureCollection.fromJson(result);
                for (Feature f : fc.features()) {
                    CoinPower(f);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
    }
    /*
     * Return the list of charge stations' position.
     */
    protected ArrayList<Position> getCoorList(){
        return this.CoordinatedList;
    }
    /**
     * 
     * @param pp Position, which is the current position of drone.
     * Record the trace of the drone.
     */
    protected void addTrace(Position pp) {
        Point p = Point.fromLngLat(pp.longitude, pp.latitude);
        trace.add(p);
    }
    /**
     * Output the geostring included the origin map plus new trace of drone features.
     */
    protected String outputJson() {
        LineString l = LineString.fromLngLats(trace);
        Feature f = Feature.fromGeometry(l);
        FeatureCollection fc = FeatureCollection.fromJson(result);
        ArrayList<Feature> fl = (ArrayList<Feature>) fc.features();
        fl.add(f);
        fc = FeatureCollection.fromFeatures(fl);
        return fc.toJson();
    }
    /**
     * 
     * @param f Each station feature.
     * CoinPower reads the feature and fetch its Position, coin and power. 
     * Then it fills the fields.
     */
    private void CoinPower(Feature f) {
        Geometry g = f.geometry();
        Point p = (Point)(g);
        Position coor = new Position(p.latitude(),p.longitude());
        JsonElement coin = f.getProperty("coins");
        Double coins = (double) coin.getAsFloat();
        JsonElement power = f.getProperty("power");
        Double powers = (double) power.getAsFloat();
        PositionCoins.put(coor, coins);
        PositionPower.put(coor, powers);
        CoordinatedList.add(coor);
    }



}
