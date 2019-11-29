package uk.ac.ed.inf.powergrab;

import java.io.*;

import java.net.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.google.gson.*;
import com.mapbox.geojson.*;
import java.util.ArrayList;
/**
 * @author s1742667
 */
public class Map {

    protected HashMap<Position,Double> PositionCoins = new HashMap<Position,Double>(); // Map a position of station to its coins.
    protected HashMap<Position,Double> PositionPower = new HashMap<Position,Double>(); // Map a position of station to ist power.
    private ArrayList<Position> CoordinatedList = new ArrayList<Position>(); // List of stations' position.
    private List<Point> trace = new ArrayList<Point>(); // The drones' moving trace record.
    private String result;  // A geostring which we download from internet.
    
    /**
     * 
     * @param year
     * @param month
     * @param day
     * 
     *  Class constructor, it takes the listed parameter and download the specific map. Then it fills the fields by calling other functions.
     */
    protected Map(String year,String month,String day) {
            String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"+year+"/"+month+"/"+day+"/"+"powergrabmap.geojson";
            try {
                //HTTP URL connection   
                URL mapURL = new URL(mapString);
                URLConnection conn = mapURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.connect();
                InputStream mapSource = conn.getInputStream();
                // Read map.
                result = new BufferedReader(new InputStreamReader(mapSource)).lines().collect(Collectors.joining("\n"));
               /* result = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1870174,55.9434134]},\"properties\":{\"id\":\"5b12-d69c-9a01-54dd-c512-34e7\",\"coins\":99.022696797125,\"power\":107.2428436928052,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00ce00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1902907,55.9444992]},\"properties\":{\"id\":\"d0ab-39d2-53b2-9d03-6447-1e9d\",\"coins\":-56.71190103296761,\"power\":-122.84614603045597,\"marker-symbol\":\"danger\",\"marker-color\":\"#b40000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1919037,55.945618]},\"properties\":{\"marker-size\":\"medium\",\"id\":\"3412-68af-2268-891b-563f-6c9f\",\"coins\":-107.45342338878315,\"power\":-39.1943882054413,\"marker-symbol\":\"danger\",\"marker-color\":\"#930000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1922037,55.945618]},\"properties\":{\"marker-size\":\"medium\",\"id\":\"6412-68af-2268-891b-563f-6c9f\",\"coins\":-107.45342338878315,\"power\":-39.1943882054413,\"marker-symbol\":\"danger\",\"marker-color\":\"#930000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1862262,55.9458555]},\"properties\":{\"id\":\"de4f-6693-15a9-c1b6-21ca-3f9c\",\"coins\":119.281390089303,\"power\":118.19288049386616,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00ed00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.191167,55.9448889]},\"properties\":{\"id\":\"c757-e343-3f63-11c4-bdb6-1f2c\",\"coins\":52.87227120733573,\"power\":88.85410726856854,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#008e00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1877265,55.9448377]},\"properties\":{\"id\":\"d4c0-c8bc-cac4-39b6-c127-b451\",\"coins\":4.287919531109935,\"power\":4.539636588174647,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#000900\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1916587,55.9459456]},\"properties\":{\"id\":\"a453-03c7-5b9f-136d-138c-3a71\",\"coins\":-9.925233965175195,\"power\":-86.03357714859763,\"marker-symbol\":\"danger\",\"marker-color\":\"#600000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1918587,55.9457956]},\"properties\":{\"id\":\"a453-03c7-5b9f-136d-138c-3a71\",\"coins\":-9.925233965175195,\"power\":-86.03357714859763,\"marker-symbol\":\"danger\",\"marker-color\":\"#600000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1916587,55.9461656]},\"properties\":{\"id\":\"a454-03c7-5b9f-136d-138c-3a71\",\"coins\":-9.925233965175195,\"power\":-86.03357714859763,\"marker-symbol\":\"danger\",\"marker-color\":\"#600000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1874389,55.9427969]},\"properties\":{\"id\":\"d8ec-9618-2d4a-9766-9069-470f\",\"coins\":92.30813150873013,\"power\":94.07308443900807,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00ba00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1857881,55.9446632]},\"properties\":{\"id\":\"2a53-440a-5e6f-446e-deae-2585\",\"coins\":-83.52693535020254,\"power\":-65.47650260902589,\"marker-symbol\":\"danger\",\"marker-color\":\"#950000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1906966,55.9437462]},\"properties\":{\"id\":\"a83a-82e1-b99b-1a43-7748-1254\",\"coins\":-0.5889818895007531,\"power\":-42.889380250762926,\"marker-symbol\":\"danger\",\"marker-color\":\"#2b0000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1843792,55.9459442]},\"properties\":{\"id\":\"4f45-cb17-b46b-1d92-8fb0-f0b4\",\"coins\":27.034300715663413,\"power\":32.63607049909154,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#003c00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1845815,55.9456754]},\"properties\":{\"id\":\"c2a3-8c14-eb86-9b6c-f6ee-e904\",\"coins\":20.00107662246073,\"power\":47.564883901336714,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#004400\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1880092,55.9453184]},\"properties\":{\"id\":\"2e19-fc57-8256-3541-d9bd-f662\",\"coins\":-0.6647038055978133,\"power\":-78.16509597674981,\"marker-symbol\":\"danger\",\"marker-color\":\"#4f0000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1855899,55.9445683]},\"properties\":{\"id\":\"a2ad-d456-d076-a2d8-2794-1dac\",\"coins\":101.72683200082832,\"power\":109.29283690471911,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00d300\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1896545,55.9448101]},\"properties\":{\"id\":\"fdfa-1388-df44-88b4-86fb-8cd5\",\"coins\":83.53980383465097,\"power\":50.08987327642522,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#008600\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1881061,55.9434606]},\"properties\":{\"id\":\"ee40-dd41-a085-2d8f-52a9-adf0\",\"coins\":18.13993319640897,\"power\":14.575569507095501,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#002100\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.186748,55.9444414]},\"properties\":{\"id\":\"b9a6-9b18-ea77-3fc3-193a-8253\",\"coins\":35.74444372072284,\"power\":8.969931751690725,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#002d00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1907286,55.9457773]},\"properties\":{\"id\":\"9173-65b7-003d-53fb-a479-7ebd\",\"coins\":94.23699079501695,\"power\":38.16608242034776,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#008400\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1885864,55.9456704]},\"properties\":{\"id\":\"bde7-ad3d-6906-7ab9-b4c2-523e\",\"coins\":-51.91122054599422,\"power\":-54.528929635519795,\"marker-symbol\":\"danger\",\"marker-color\":\"#6a0000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1888669,55.9437882]},\"properties\":{\"id\":\"7150-f88a-cb39-702a-ed84-c998\",\"coins\":42.46983070195117,\"power\":54.08434723277205,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#006100\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1865125,55.9431193]},\"properties\":{\"id\":\"78dd-1c00-db6e-2513-91f2-a9b9\",\"coins\":23.30074002453257,\"power\":67.8069143679543,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#005b00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1891157,55.9451884]},\"properties\":{\"id\":\"2c3b-4c5b-2c4d-4d88-db49-f6b6\",\"coins\":-112.53271162135272,\"power\":-107.22370586609534,\"marker-symbol\":\"danger\",\"marker-color\":\"#dc0000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1859482,55.9451213]},\"properties\":{\"id\":\"d518-33a7-69ab-f771-d571-8d36\",\"coins\":-33.47570781641096,\"power\":-88.74271418188336,\"marker-symbol\":\"danger\",\"marker-color\":\"#7a0000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1859855,55.9436205]},\"properties\":{\"id\":\"faa3-02ff-d8e2-f92f-ab40-e20e\",\"coins\":-32.973825262211555,\"power\":-101.5537339954173,\"marker-symbol\":\"danger\",\"marker-color\":\"#870000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1924425,55.9432016]},\"properties\":{\"id\":\"ff48-81d2-9786-588b-8eef-8486\",\"coins\":70.5038585300482,\"power\":87.92147952105397,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#009e00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1887499,55.9438472]},\"properties\":{\"id\":\"07fa-ef33-76df-a8ad-f7b9-2904\",\"coins\":96.7260752348259,\"power\":25.640464530964426,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#007a00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1844916,55.9451455]},\"properties\":{\"id\":\"16f9-76ed-5925-40ff-60cd-4a55\",\"coins\":31.787143821320974,\"power\":30.095837626535214,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#003e00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1905875,55.9431234]},\"properties\":{\"id\":\"3359-68df-d042-06b6-af2b-968b\",\"coins\":-111.55200275743645,\"power\":-38.83862566414703,\"marker-symbol\":\"danger\",\"marker-color\":\"#960000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1880813,55.9450334]},\"properties\":{\"id\":\"a79d-694c-815e-a35f-801f-59e1\",\"coins\":65.92199300373802,\"power\":120.24637957356057,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00ba00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1844509,55.9458327]},\"properties\":{\"id\":\"8011-f3ef-ab28-2c81-2eeb-6f04\",\"coins\":81.77498539369832,\"power\":119.38224450220348,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00c900\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1887622,55.9450768]},\"properties\":{\"id\":\"4f93-e296-40a8-c35a-8f4f-5986\",\"coins\":94.6374292945435,\"power\":34.29940012242673,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#008100\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1859109,55.9430288]},\"properties\":{\"id\":\"afe6-7f3e-6534-e7ca-a6c1-8f7d\",\"coins\":119.42229350428521,\"power\":95.39537165752404,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00d700\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1861418,55.9442526]},\"properties\":{\"id\":\"fcc4-e490-1aa7-1901-455e-2867\",\"coins\":108.13690490622187,\"power\":79.90548261866073,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00bc00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1857747,55.9440879]},\"properties\":{\"id\":\"7ce4-9c5e-0528-838b-55d8-b540\",\"coins\":2.177203302045752,\"power\":54.314753402451466,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#003800\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1893294,55.943714]},\"properties\":{\"id\":\"31ea-957a-9b4e-b7cc-2077-3dc9\",\"coins\":82.22953350372073,\"power\":8.386030584110499,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#005b00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1857632,55.9445303]},\"properties\":{\"id\":\"f5b6-313c-3cd2-96b3-17fb-1dd2\",\"coins\":35.110156827058496,\"power\":3.3107487153290482,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#002600\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1923814,55.945466]},\"properties\":{\"marker-size\":\"medium\",\"id\":\"5484-320f-0906-eb6d-5ee6-9f51\",\"coins\":-120.56984294095675,\"power\":-60.27280500103298,\"marker-symbol\":\"danger\",\"marker-color\":\"#b5001b\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1905501,55.9461353]},\"properties\":{\"id\":\"d6de-3c5e-1673-3526-9294-7220\",\"coins\":122.67966885544975,\"power\":38.48292032062919,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00a100\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1918293,55.9443886]},\"properties\":{\"id\":\"e98b-1701-7b29-dc62-679b-6c72\",\"coins\":96.80446035668352,\"power\":119.52545929377595,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00d800\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1853797,55.9462104]},\"properties\":{\"id\":\"9f65-02a4-ef7b-7b66-46d8-1045\",\"coins\":-73.76242143514047,\"power\":-55.543579154811326,\"marker-symbol\":\"danger\",\"marker-color\":\"#810000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1850745,55.9453673]},\"properties\":{\"id\":\"2bd5-97bb-e79a-501f-dbb9-1e94\",\"coins\":3.1389554942997666,\"power\":40.9639048150333,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#002c00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1882776,55.9428487]},\"properties\":{\"id\":\"b3e6-d1e0-b36c-d1b9-29ac-3d46\",\"coins\":4.091550561645993,\"power\":11.983426659425437,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#001000\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1884536,55.9428592]},\"properties\":{\"id\":\"3a3f-b853-3e36-b950-bc93-fe73\",\"coins\":25.321052007093073,\"power\":102.0186527908283,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#007f00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1872575,55.9461066]},\"properties\":{\"id\":\"5904-1920-27bb-49c6-9be7-f700\",\"coins\":24.848542456639034,\"power\":57.78585601888367,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#005300\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1893866,55.9427457]},\"properties\":{\"id\":\"61fe-720e-10d0-d8f9-0883-e020\",\"coins\":117.64692473093703,\"power\":27.853071041667842,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#009100\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1923529,55.9462326]},\"properties\":{\"marker-size\":\"medium\",\"id\":\"6870-519e-7b47-924b-9561-ebfb\",\"coins\":88.39953537123513,\"power\":76.47495901364886,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00a50d\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1912554,55.9455469]},\"properties\":{\"id\":\"205f-8ed8-08f2-5a1d-bf16-2591\",\"coins\":74.15811715176214,\"power\":50.0505170590813,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#007c00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1878143,55.9450011]},\"properties\":{\"id\":\"3118-5efc-f447-6a9f-3a14-0245\",\"coins\":77.2999722601476,\"power\":111.70006191675152,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00bd00\"}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1846655,55.9446986]},\"properties\":{\"id\":\"1f48-550c-ee32-f784-2b74-6e65\",\"coins\":115.68187984350045,\"power\":49.2819862286957,\"marker-symbol\":\"lighthouse\",\"marker-color\":\"#00a500\"}}]}\n" + 
                        "";*/
                FeatureCollection fc = FeatureCollection.fromJson(result);
                for (Feature f : fc.features()) {
                    coinPower(f);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Input map's date is not correct! Please check your input date.");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            
    }
    /**
     * @return the list of charge stations' position.
     */
    protected ArrayList<Position> getCoorList(){
        return this.CoordinatedList;
    }
    /**
     * 
     * @param pp Position, which is the current position of drone.
     * Record the trace of the drone.
     */
    protected void addTrace(Position pp) {
        Point p = Point.fromLngLat(pp.longitude, pp.latitude);
        trace.add(p);
    }
    /**
     * Output the geoJson format string included the origin map plus new trace of drone features.
     */
    protected String outputJson() {
        LineString l = LineString.fromLngLats(trace);
        Feature f = Feature.fromGeometry(l);
        FeatureCollection fc = FeatureCollection.fromJson(result);
        ArrayList<Feature> fl = (ArrayList<Feature>) fc.features();
        fl.add(f);
        fc = FeatureCollection.fromFeatures(fl);
        return fc.toJson();
    }
    /**
     * 
     * @param f Each station feature.
     * CoinPower reads the feature and fetch its Position, coin and power. 
     * Then it fills the fields.
     */
    private void coinPower(Feature f) {
        Geometry g = f.geometry();
        Point p = (Point)(g);
        Position coor = new Position(p.latitude(),p.longitude());
        JsonElement coin = f.getProperty("coins");
        Double coins = (double) coin.getAsFloat();
        JsonElement power = f.getProperty("power");
        Double powers = (double) power.getAsFloat();
        PositionCoins.put(coor, coins);
        PositionPower.put(coor, powers);
        CoordinatedList.add(coor);
    }



}
