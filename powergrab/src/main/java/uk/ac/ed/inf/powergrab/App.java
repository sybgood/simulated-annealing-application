package uk.ac.ed.inf.powergrab;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import com.mapbox.geojson.*;

/**
 * Hello world!
 *
 */
public class App 
{
   
    
    public static void main( String[] args )
    {
        String year = (String)(args[0]);
        String month = (String)(args[1]);
        String day = (String)(args[2]);
        Map map = new Map(year,month,day);
    }

}
