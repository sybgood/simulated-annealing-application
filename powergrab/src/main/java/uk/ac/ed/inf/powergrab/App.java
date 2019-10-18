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
       // for (Position p :map.getCoorList()) {
       //     System.out.print(p.latitude);
       // }
        Statelessdrone d = new Statelessdrone(latitude,longitude,RNGseed,map);
       // d.statelessMove();
        try {
            FileOutputStream bos = new FileOutputStream("output.txt");
            BufferedOutputStream buff = new BufferedOutputStream(bos);
            String trace = d.statelessMove();
            buff.write(trace.getBytes());
            buff.flush();
            buff.close();
            map = d.getMap();
            String json = map.outputJson();
            FileOutputStream bos2 = new FileOutputStream("2018.geojson");
            BufferedOutputStream buff2 =new  BufferedOutputStream(bos2);
            buff2.write(json.getBytes());
            buff2.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }

    private static BufferedOutputStream BufferedOutputStream(FileOutputStream bos2) {
        // TODO Auto-generated method stub
        return null;
    }

}
