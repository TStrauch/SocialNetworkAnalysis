package tools.filterSocialgraph;

import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Timo on 01.05.16.
 */
public class Filter {
    public static String inputSocialGraph = "data/outputCSV/socialgraph.csv";
//    public static String inputCheckinUserids = "data/input/userIds_checkins_above20.txt";
    public static String inputCheckinUserids = "data/input/users_by_checkins.csv";
    public static String outputSocialGraphFiltered = "data/outputFilteredCSV/socialgraph_by_checkins.txt";

    public static void main(String[] args) throws IOException {
        Filter f = new Filter();
        f.filter();
//        c.outputFile();
    }

    public void filter() throws IOException {
        //read userIds from checkins and build lookup table

        Set<String> checkinUserids = new HashSet<String>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(inputCheckinUserids)));
        String inCheckin;
        while((inCheckin = reader.readLine()) != null){
//            mapCheckinUserids.add(inCheckin);
            String[] split = inCheckin.split(";");
            String userid = split[0];
            userid = userid.replace(".0","");
            checkinUserids.add(userid);
        }
        reader.close();

        //only leave those entries in the socialgraph that represent "power" users (many checkins)
        //also remove duplicate connections (we create an undirected network)
        Hashtable<String, String> lookup = new Hashtable<String, String>();
        reader = new BufferedReader(new FileReader(new File(inputSocialGraph)));
        BufferedWriter writer = new BufferedWriter ( new FileWriter (new File(outputSocialGraphFiltered)));
        String inSocial;
        boolean duplicate = false;
        int countConnections = 0;
        reader.readLine(); //skip headerline
        while((inSocial = reader.readLine()) != null){

            inSocial = inSocial.replaceAll("\\n", "");
            String[] userids = inSocial.split(";");

            if(lookup.get(userids[1]) != null){
                if (lookup.get(userids[1]).equals(userids[0])){
                    duplicate = true;
                }
                else{
                    duplicate = false;
                }
            }

            if(checkinUserids.contains(userids[0]) && checkinUserids.contains(userids[1]) && !duplicate){
                writer.write(inSocial+"\n");
                countConnections++;
                lookup.put(userids[0], userids[1]);
            }

        }
        reader.close();
        writer.close();

        System.out.println("remaining social connections: "+countConnections);
    }
}
