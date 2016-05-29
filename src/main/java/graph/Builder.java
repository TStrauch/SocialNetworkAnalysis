package graph;

import graph.model.UserVertex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Timo on 13.05.16.
 * http://jung.sourceforge.net/doc/manual.html
 */
public class Builder {

    public static void main(String[] args) throws IOException, ParseException {

        /**
         *
         *
         * extract venue graph to pajek
         */

        VenuesData.readVenues("data/input/venues_labeled_checkins2.csv");
        System.out.println("[Builder] Venues read.");

//        Socialgraph socialgraph = new Socialgraph("data/input/users_by_checkins2.csv", "data/input/socialgraph_v2.csv");
        Socialgraph socialgraph = new Socialgraph("data/input/users_by_checkins2.csv", "data/input/socialgraph_by_checkins.csv");
        System.out.println("[Builder] Socialgraph built.");

        SocialgraphUserCheckinVenue.createFirstCheckinPerVenueMap("data/input/checkins_filtered2_labeled.csv", true);
        System.out.println("[Builder] First Checkin per Venue Map created.");

        saveUserCheckinVenueGraphToPajek("11373",socialgraph);
//        saveUserCheckinVenueGraphToPajek("396509",socialgraph);
//        saveUserCheckinVenueGraphToPajek("54363",socialgraph);
//        saveUserCheckinVenueGraphToPajek("103165",socialgraph);
//        saveUserCheckinVenueGraphToPajek("42502",socialgraph);


        /**
         *
         *
         *
         * calculate scores for the vertices in the interesting networks and export them in a file
         */

        List<String> venueids = new ArrayList<String>();
        venueids.add("11373");
//        venueids.add("170042");
//        venueids.add("1042301");
//        venueids.add("205294");
//        venueids.add("54377");
//        venueids.add("54545");
//        venueids.add("68611");
//        venueids.add("169601");
//        venueids.add("54270");
//        venueids.add("163120");
//        venueids.add("9822");
//        venueids.add("62118");
//        venueids.add("114073");
//        venueids.add("42342");
//        venueids.add("17242");
//        venueids.add("15963");
//        venueids.add("7491");
//        venueids.add("119503");
//        venueids.add("7284");
//        venueids.add("6399");
//        venueids.add("103165");
//        venueids.add("54273");
//        venueids.add("54431");
//        venueids.add("9822");
//
        SocialgraphUserCheckinVenue socialgraphUserCheckinVenue;

        for(String venueId: venueids){
            socialgraphUserCheckinVenue = new SocialgraphUserCheckinVenue(socialgraph, venueId);
            System.out.println("[Builder] user-checkin-venue graph created for id "+venueId+".");
            socialgraphUserCheckinVenue.assignAllScores();
            socialgraphUserCheckinVenue.normalizeAllScores();
            socialgraphUserCheckinVenue.computeInfluenceScore();
            writeUserMetricsToFile("data/output/usermetrics_"+venueId+".csv", venueId, socialgraphUserCheckinVenue);
        }



    }


    public static void writeUserMetricsToFile(String path, String venueid, SocialgraphUserCheckinVenue graph) throws IOException {
        BufferedWriter writer = new BufferedWriter ( new FileWriter(new File(path)));

        int count = 0;
        for (UserVertex userVertex : graph.getUserCheckinGraph().getVertices()) {
            if (count == 0){
                HashMap<String, Double> content = userVertex.calculateJointMetrics(venueid);

//                if(content.get("degreeCentrality") == 0){ //uncomment to remove isolated vertices
//                    continue;
//                }

                for (String header : content.keySet()) {
                    writer.write(header+";");
                }
                writer.write("\n");
            }
//            if(userVertex.getDegreeCentralityPerVenue().get(venueid) > 0) { //uncmment to remove isolated vertices
                writer.write(userVertex.toString(venueid) + "\n");
//            }
            count++;
        }



        writer.close();


    }

    public static void saveUserCheckinVenueGraphToPajek(String venueid, Socialgraph socialgraph) throws IOException {
        SocialgraphUserCheckinVenue socialgraphUserCheckinVenue = new SocialgraphUserCheckinVenue(socialgraph, venueid);
        socialgraphUserCheckinVenue.savePajekNetworkFile("data/outputPaj/network_checkins_"+venueid+".net");
        socialgraphUserCheckinVenue.savePajekClusterFile("data/outputPaj/cluster_checkins_"+venueid+".clu");
    }
}
