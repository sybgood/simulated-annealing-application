package uk.ac.ed.inf.powergrab;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import com.mapbox.geojson.*;
import java.util.Random;

public class App 
{
    private static void play(Double latitude, Double longitude,int seed,Map map,String difficuity,String date) {
        if(difficuity.equals("stateless")) {
            Statelessdrone d= new  Statelessdrone(latitude, longitude, seed, map);
            String contents = d.statelessMove();
            String title = difficuity+"-"+date+".txt";
            writeInComputer(contents,title);
            contents = d.map.outputJson();
            title = difficuity+"-"+date+".geojson";
            writeInComputer(contents,title);
        }
        else if(difficuity.equals("stateful")) {
            Statefuldrone sd = new Statefuldrone(latitude,longitude,seed,map);
            sd.statefulMove();
            String contents = sd.returnTrace();
            String title = difficuity+"-"+date+".txt";
            writeInComputer(contents,title);
            contents = sd.map.outputJson();
            title = difficuity+"-"+date+".geojson";
            writeInComputer(contents,title);
        }
    }
    private static void writeInComputer(String contents,String title) {
            FileOutputStream bos;
            try {
                bos = new FileOutputStream(title);
                BufferedOutputStream buff = new BufferedOutputStream(bos);
                buff.write(contents.getBytes());
                buff.flush();
                buff.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    public static void main( String[] args )
    {
        String day = args[0];
        String month = args[1];
        String year = args[2];
        Double latitude = Double.parseDouble(args[3]);
        Double longitude = Double.parseDouble(args[4]);
        Integer RNGseed = Integer.parseInt(args[5]);
        String difficuity = args[6];        
        Map map = new Map(year,month,day);
        String date = day+"-"+month+"-"+year; 
        long ab =System.currentTimeMillis(); 
        play(latitude,longitude,RNGseed,map,difficuity,date);
        long aa = System.currentTimeMillis();
        System.out.print(aa-ab);
    }
}
