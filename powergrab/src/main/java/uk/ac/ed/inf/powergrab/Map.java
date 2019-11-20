package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.google.gson.*;
import com.mapbox.geojson.*;
import java.util.ArrayList;
public class Map {
    
    protected HashMap<Position,Double> PositionCoins = new HashMap<Position,Double>();
    protected HashMap<Position,Double> PositionPower = new HashMap<Position,Double>();
    private ArrayList<Position> CoordinatedList = new ArrayList<Position>();
    private List<Point> trace = new ArrayList<Point>();
    private String result;
    
 

    public ArrayList<Position> getCoorList(){
        return this.CoordinatedList;
    }

    public void addTrace(Position pp) {
        Point p = Point.fromLngLat(pp.longitude, pp.latitude);
        trace.add(p);
    }
    
    public String outputJson() { // Output the geojson contains linestring.
        LineString l = LineString.fromLngLats(trace);
        Feature f = Feature.fromGeometry(l);
        FeatureCollection fc = FeatureCollection.fromJson(result);
        ArrayList<Feature> fl = (ArrayList<Feature>) fc.features();
        fl.add(f);
        fc = FeatureCollection.fromFeatures(fl);
        return fc.toJson();
    }
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
    // Constructor
    public Map(String year,String month,String day) {
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
//-------------- store map information--------------------------------------------
                    CoinPower(f);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


}
