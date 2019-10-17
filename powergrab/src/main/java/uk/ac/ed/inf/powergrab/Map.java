package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.google.gson.*;
import com.mapbox.geojson.*;
import java.util.ArrayList;
public class Map {
    
    public HashMap<List<Double>,String> CoordinateId = new HashMap<List<Double>,String>();
    public HashMap<String,Double> IDcoins = new HashMap<String,Double>();
    public HashMap<String,Double> IDpower = new HashMap<String,Double>();
    private ArrayList<String> ID = new ArrayList<String>();
    private ArrayList<ArrayList<Double>> CoordinatedList = new ArrayList<ArrayList<Double>>();

    public ArrayList<String> getIDlist() {
    return ID;
    }

    public ArrayList<ArrayList<Double>> getCoorList(){
        return this.CoordinatedList;
    }
    
    public Map(String year,String month,String day) {
            String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"+year+"/"+month+"/"+day+"/"+"powergrabmap.geojson";
            try {
                URL mapURL = new URL(mapString);
                URLConnection conn = mapURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.connect();
                InputStream mapSource = conn.getInputStream();
                // Read map.
                String result = new BufferedReader(new InputStreamReader(mapSource)).lines().collect(Collectors.joining("\n"));
                FeatureCollection fc = FeatureCollection.fromJson(result);
                List<Feature> Featurelist = fc.features();
                for (Feature f : Featurelist) {
//-------------- store map information--------------------------------------------
                    JsonElement id = f.getProperty("id");
                    String ids = id.getAsString(); 
                    ID.add(ids);
                    JsonElement coin = f.getProperty("coins");
                    Double coins = (double) coin.getAsFloat();
                    JsonElement power = f.getProperty("power");
                    Double powers = (double) power.getAsFloat();
                    IDcoins.put(ids, coins);
                    IDpower.put(ids, powers);
//-----------------Fetch coordinates for current feature---------------------------------                
                    Geometry g = f.geometry();
                        Point p = (Point)(g);
                        ArrayList<Double> l = new ArrayList<Double>();
                        l.add(p.longitude());
                        l.add(p.latitude());
                        CoordinateId.put(l,ids);
                        CoordinatedList.add(l);
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
