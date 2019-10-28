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
   
    
    public static void main( String[] args )
    {
        String day = args[0];
        String month = args[1];
        String year = args[2];
        Double latitude = Double.parseDouble(args[3]);
        Double longitude = Double.parseDouble(args[4]);
        Integer RNGseed = Integer.parseInt(args[5]);
        String difficulty = args[6];
        
        Map map = new Map(year,month,day);
        Double totalGold=0.0;
        for(String k:map.IDcoins.keySet()) {
            Double g = map.IDcoins.get(k);
            if(g>0) totalGold+=g;
        }
        Annealing a = new Annealing(map,40,500,200.0f, 0.995f,5);
        a.solve();

       // for (Position p :map.getCoorList()) {
       //     System.out.print(p.latitude);
       // }
        long ab =System.currentTimeMillis();
        Statelessdrone d = new Statelessdrone(latitude,longitude,RNGseed,map);
        try {
//            FileOutputStream bos = new FileOutputStream("output.txt");
//            BufferedOutputStream buff = new BufferedOutputStream(bos);
//            String trace = d.statelessMove();
//            buff.write(trace.getBytes());
//            buff.flush();
//            buff.close();
//            map = d.getMap();
//            String json = map.outputJson();
//            FileOutputStream bos2 = new FileOutputStream("2018.geojson");
//            buff =new BufferedOutputStream(bos2);
//            buff.write(json.getBytes());
//            buff.flush();
//            long aa = System.currentTimeMillis();
//            System.out.print(aa-ab);
            FileOutputStream bos3 = new FileOutputStream("test.geojson");
            BufferedOutputStream buff = new BufferedOutputStream(bos3);
            ArrayList<Position> path=a.givePath();
            for(Position sp:path) {
                map.addTrace(sp);
            }
            String json = map.outputJson();
            buff.write(json.getBytes());
            buff.flush();
         } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();}
    }
        
//        Position p1 = new Position(55.943372541675856,-3.185032961811619);
//        Position p2 = new Position(55.9435,-3.1850);
//        Position p3 = new Position(55.9436,-3.1847);
//        Double d1= drone.CalDistance(p1, p2);
//        Double d2 = drone.CalDistance(p1, p3);


}
