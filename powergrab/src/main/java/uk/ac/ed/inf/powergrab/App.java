package uk.ac.ed.inf.powergrab;
import java.io.*;

public class App 
{
    private static void play(Double latitude, Double longitude,int seed,Map map,String difficuity,String date) {
        if(difficuity.equals("stateless")) {
            Statelessdrone d= new  Statelessdrone(latitude, longitude, seed, map);
            String contents = d.move();
            String title = difficuity+"-"+date+".txt";
            writeInComputer(contents,title);
            contents = d.map.outputJson();
            title = difficuity+"-"+date+".geojson";
            writeInComputer(contents,title);
        }
        else if(difficuity.equals("stateful")) {
            Statefuldrone sd = new Statefuldrone(latitude,longitude,seed,map);
            String contents = sd.move();
            String title = difficuity+"-"+date+".txt";
            writeInComputer(contents,title);
            contents = sd.map.outputJson();
            title = difficuity+"-"+date+".geojson";
            writeInComputer(contents,title);
        }
        else System.out.print("No such difficulty");
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
//        for (int i = 1;i<=12;i++) {
//            String day0;
//            if(i<10) {
//            day0 = "0"+i;
//            }
//            else day0 = ""+i;
//            Map map0 = new Map(year,day0,day0);
//            date = day0+"-"+day0+"-"+year; 
//            long ab =System.currentTimeMillis(); 
//            play(latitude,longitude,RNGseed,map0,"stateful",date);
//            long aa = System.currentTimeMillis();
//            System.out.println(aa-ab);
//            Map map1 = new Map(year,day0,day0);
//            play(latitude,longitude,RNGseed,map1,"stateless",date);
//            long ac = System.currentTimeMillis();
//            System.out.println(ac-ab);
//        }
//        long ab =System.currentTimeMillis(); 
        play(latitude,longitude,RNGseed,map,difficuity,date);
//        long aa = System.currentTimeMillis();
//        System.out.print(aa-ab);
    }
}
