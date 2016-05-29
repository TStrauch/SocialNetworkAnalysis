package tools.matchVenues;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Timo on 08.05.16.
 */
public class Matcher {
    public static String inputOurVenues = "data/input/venues.csv";
    public static String inputLabeledVenuesManualV1 = "data/input/venues_labeled_manual.csv";
    public static String inputLabeledVenuesManualV2 = "data/input/venues_labeled_manual_2.csv";
    public static String inputLabeledVenues = "data/input/dataset_TIST2015_POIs.txt";
    public static String outputOurVenuesLabeled = "data/output/venues_labeled_test.csv";

    public static void main(String[] args) throws IOException {
        BufferedReader readerOurVenues = new BufferedReader(new FileReader(new File(inputOurVenues)));
        BufferedReader readerLabeledVenues = new BufferedReader(new FileReader(new File(inputLabeledVenues)));
        BufferedReader readerLabeledVenuesManualV1 = new BufferedReader(new FileReader(new File(inputLabeledVenuesManualV1)));
        BufferedReader readerLabeledVenuesManualV2 = new BufferedReader(new FileReader(new File(inputLabeledVenuesManualV2)));
        BufferedWriter writer = new BufferedWriter ( new FileWriter (new File(outputOurVenuesLabeled)));

        writer.write("venue_id;latitude;longitude;label\n");

        //read our venues
        int numInputRows = 0;
        HashMap<String, String> ourVenues = new HashMap<String, String>();
        readerOurVenues.readLine();
        String line;
        while ((line = readerOurVenues.readLine()) != null){
            String[] split = line.split(";");
            String venueid = "";
            String latitude = "";
            String longitude = "";
            try{
                venueid = split[0].replaceAll("\\.0","");
                latitude = split[1];
                longitude = split[2];
            } catch (Exception e){
                continue;
            }

            //trim lat and long
            try {
                String[] splitLat = latitude.split("\\.");
                latitude = splitLat[0] +"."+ splitLat[1].substring(0,4);
                String[] splitLong = longitude.split("\\.");
                longitude = splitLong[0] +"."+ splitLong[1].substring(0,4);
            } catch (Exception e){}

            //create lat-long hash
            String hash = hash(latitude, longitude);

            //add to ourVenues hashmap
            ourVenues.put(hash, venueid);
            numInputRows++;
        }

        System.out.println("Number of venues before labeling: "+numInputRows);

        //read manually labeled venues and create an index
        HashMap<String, HashMap<String, String>> venuesManualLabel = new HashMap<String, HashMap<String, String>>();

        readerLabeledVenuesManualV1.readLine();
        line = "";
        while((line = readerLabeledVenuesManualV1.readLine()) != null){
            String[] split = line.split("\\|");
            String label = split[0].replaceAll("\"", "");
            String lat = split[2].replaceAll("\"", "");
            String longi = split[3].replaceAll("\"", "");
            String venueid = split[4].replaceAll("\\.0", "");

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("label", label);
            map.put("latitude", lat);
            map.put("longitude", longi);


            venuesManualLabel.put(venueid, map);
        }


        readerLabeledVenuesManualV2.readLine();
        line = "";
        while((line = readerLabeledVenuesManualV2.readLine()) != null){
            String[] split = line.split(";");
            String latitude = split[0];
            String longitude = split[1];
            String venueid = split[3].replaceAll("\\.0","");
            String label = split[4];

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("label", label);
            map.put("latitude", latitude);
            map.put("longitude", longitude);

            if (venuesManualLabel.get(venueid) != null){
                System.out.println("got duplicate venue");
            }

            venuesManualLabel.put(venueid, map);
        }

        System.out.println("Number of manually labeled venues: "+venuesManualLabel.size());





        //read labeled venues
        int numRowsWritten = 0;
        Set<String> labeledIds = new HashSet<String>();

        readerLabeledVenues.readLine();
        line = "";
        while((line = readerLabeledVenues.readLine()) != null){
            String[] split = line.split("\t");
            String latitude = split[1];
            String longitude = split[2];
            String label = split[3];

            //trim lat and long
            try {
                String[] splitLat = latitude.split("\\.");
                latitude = splitLat[0] +"."+ splitLat[1].substring(0,4);
                String[] splitLong = longitude.split("\\.");
                longitude = splitLong[0] +"."+ splitLong[1].substring(0,4);
            } catch (Exception e){}

            //check if exists in ourVenues
            String hash = hash(latitude, longitude);
            String id = ourVenues.get(hash);
            if (id != null){
                writer.write(id+";"+latitude+";"+longitude+";"+label+"\n");
                numRowsWritten++;
                labeledIds.add(id);
            }
        }

        //now also write those ids that we labeled manually but that we could not match
        for (String venueid : venuesManualLabel.keySet()) {
            HashMap<String, String> map = venuesManualLabel.get(venueid);
            String label = map.get("label");
            String latitude = map.get("latitude");
            String longitude = map.get("longitude");

            if (!labeledIds.contains(venueid)) {
                numRowsWritten++;
                writer.write(venueid + ";" + latitude + ";" + longitude + ";" + label + "\n");
            }
        }


        writer.close();

        System.out.println("Read "+numInputRows);
        System.out.println("Wrote "+numRowsWritten);
    }

    public static String hash(String s1, String s2){
        return s1+";"+s2;
    }
}
