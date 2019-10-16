package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.google.gson.*;
import com.mapbox.geojson.*;

public class Map {
private HashMap IDcoordinate = new HashMap<String,List<Double>>();
private HashMap IDcoins = new HashMap<String,Float>();
private HashMap IDpower = new HashMap<String,Float>();


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
                JsonElement coin = f.getProperty("coins");
                Float coins = coin.getAsFloat();
                JsonElement power = f.getProperty("power");
                Float powers = power.getAsFloat();
                IDcoins.put(ids, coins);
                IDpower.put(ids, power);
                
                Geometry g = f.geometry();
                if(g.type()=="Point") {
                    Point p = (Point)(g);
                    List<Double> l = p.coordinates();
                    IDcoordinate.put(ids,l);
                }
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
