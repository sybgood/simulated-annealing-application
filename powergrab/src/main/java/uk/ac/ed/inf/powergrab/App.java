package uk.ac.ed.inf.powergrab;
import java.io.*;
/**
 * 
 * @author s1742667
 * App class is the class where main function located in. Any command line argument will be parsed first in here.
 */
public class App 
{
    /**
     * @param latitude : Double latitude of position
     * @param longitude : Double longitude of position
     * @param seed : integer random number generator seed
     * @param map  : map which stores informations of groups of station.
     * @param difficulty : "stateless" or "stateful"
     * @param date : String with format "day-month-year". i.e "01-01-2019"
     * 
     * play take the parameter list above, and it will create a drone according to the difficulty given. And use drone build in function to play the game.
     * Then output the trace of the drone in both txt and geojson files.
     */
    private static void play(Double latitude, Double longitude,int seed,Map map,String difficulty,String date) {
        if(difficulty.equals("stateless")) {
            Statelessdrone d= new  Statelessdrone(latitude, longitude, seed, map);
            String contents = d.play();
            String title = difficulty+"-"+date+".txt";
            writeInComputer(contents,title);
            contents = d.map.outputJson();
            title = difficulty+"-"+date+".geojson";
            writeInComputer(contents,title);
        }
        else if(difficulty.equals("stateful")) {
            Statefuldrone sd = new Statefuldrone(latitude,longitude,seed,map);
            String contents = sd.play();
            String title = difficulty+"-"+date+".txt";
            writeInComputer(contents,title);
            contents = sd.map.outputJson();
            title = difficulty+"-"+date+".geojson";
            writeInComputer(contents,title);
        }
        else System.out.print("No such difficulty");
    }
    /**
     * 
     * @param contents: String either trace log or features in geojson format.
     * @param title: String filenames
     * This function use file output stream to output the contents to the file with file's name title.
     */
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
    /**
     * 
     * Takes 6 argument as input. Which are day month year and double latitude and longitude and integer random seed
     * and difficulty.
     * It creates a map by given date. Called the play function to play the games.
     */
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
        play(latitude,longitude,RNGseed,map,difficulty,date);
//        long aa = System.currentTimeMillis();
//        System.out.print(aa-ab);

    }
}
