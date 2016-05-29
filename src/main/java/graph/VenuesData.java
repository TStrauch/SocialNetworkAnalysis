package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Timo on 15.05.16.
 */
public class VenuesData {
    public static HashMap<String, HashMap<String, String>> venueInfoMap = new HashMap<String, HashMap<String, String>>();

    public static void readVenues(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        reader.readLine(); //headerline;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(";");
            String countCheckins = split[0].replaceAll("\\.0", "");
            String label = split[3];
            String venueid = split[4].replaceAll("\\.0", "");

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("countCheckins", countCheckins);
            map.put("label", label);

            venueInfoMap.put(venueid, map);
        }
    }
}
