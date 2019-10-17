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
        String day = (String)(args[0]);
        String month = (String)(args[1]);
        String year = (String)(args[2]);
        Double latitude = Double.parseDouble(args[3]);
        Double longitude = Double.parseDouble(args[4]);
        Integer RNGseed = Integer.parseInt(args[5]);
        String difficulty = args[6];
        
        Map map = new Map(year,month,day);
        
    }

}
