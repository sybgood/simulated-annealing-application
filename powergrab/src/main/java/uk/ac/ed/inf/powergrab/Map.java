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
    
    protected HashMap<Position,String> CoordinateId = new HashMap<Position,String>();
    protected HashMap<String,Double> IDcoins = new HashMap<String,Double>();
    protected HashMap<String,Double> IDpower = new HashMap<String,Double>();
    private ArrayList<String> ID = new ArrayList<String>();
    private ArrayList<Position> CoordinatedList = new ArrayList<Position>();
    private List<Point> trace = new ArrayList<Point>();
    private String result;
    
    
    public ArrayList<String> getIDlist() {
    return ID;
    }

    public ArrayList<Position> getCoorList(){
        return this.CoordinatedList;
    }
    
    private void fetch_coor(Geometry g,String id) {
        Point p = (Point)(g);
        Position coor = new Position(p.latitude(),p.longitude());
        CoordinateId.put(coor,id);
        CoordinatedList.add(coor);
    }
    

    public void addTrace(Position pp) {
        Point p = Point.fromLngLat(pp.longitude, pp.latitude);
        trace.add(p);
    }
    
    public String outputJson() {
        LineString l = LineString.fromLngLats(trace);
        Feature f = Feature.fromGeometry(l);
        FeatureCollection fc = FeatureCollection.fromJson(result);
        ArrayList<Feature> fl = (ArrayList<Feature>) fc.features();
        fl.add(f);
        fc = FeatureCollection.fromFeatures(fl);
        return fc.toJson();
    }
    private String CoinPower(Feature f) {
        JsonElement id = f.getProperty("id");
        String ids = id.getAsString(); 
        ID.add(ids);
        JsonElement coin = f.getProperty("coins");
        Double coins = (double) coin.getAsFloat();
        JsonElement power = f.getProperty("power");
        Double powers = (double) power.getAsFloat();
        IDcoins.put(ids, coins);
        IDpower.put(ids, powers);
        return ids;
    }
    
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
                    String ids = CoinPower(f);
//-----------------Fetch coordinates for current feature---------------------------------                
                    Geometry g = f.geometry();
                    fetch_coor(g,ids); 
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
