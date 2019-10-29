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
        Double totalGold = 0.0;
        HashMap<String,Double> ss =map.IDcoins;
        for(Double g : ss.values()) {
            if(g>=0) totalGold+=g;
        }
        long ab =System.currentTimeMillis();
        Position start = new Position(latitude,longitude);
        Random rnd = new Random(RNGseed);
//        Annealing a = new Annealing(map,2000,400,200.0f,0.995f,rnd,start);
//        a.solve();
        Statefuldrone sd = new Statefuldrone(latitude,longitude,RNGseed,map);

        sd.statefulMove();

       // for (Position p :map.getCoorList()) {
       //     System.out.print(p.latitude);
       // }
        Statelessdrone d = new Statelessdrone(latitude,longitude,RNGseed,map);
        try {
            FileOutputStream bos = new FileOutputStream("output.txt");
            BufferedOutputStream buff = new BufferedOutputStream(bos);
            String trace = sd.toString();
            buff.write(trace.getBytes());
            buff.flush();
            buff.close();
            map = sd.getMap();
            String json = map.outputJson();
            FileOutputStream bos2 = new FileOutputStream("2018.geojson");
            buff =new BufferedOutputStream(bos2);
            buff.write(json.getBytes());
            buff.flush();
            long aa = System.currentTimeMillis();
            
            System.out.println("\n"+sd.coin/totalGold);
            System.out.print(aa-ab);
//            ArrayList<Position> path=a.givePath();
//            for(Position sp:path) {
//                map.addTrace(sp);
//            }

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
