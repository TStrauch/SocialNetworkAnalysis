package graph.partition;

import graph.Socialgraph;
import graph.model.UserVertex;

import java.util.*;

/**
 * Created by Timo on 13.05.16.
 */
public class CustomPartition {
    private static final long ONE_WEEK = 7*24*60*60*1000;
    private static final long ONE_MONTH = ONE_WEEK * 30;

    private static final long FIRST_WEEK = ONE_WEEK;
    private static final long SECOND_WEEK = FIRST_WEEK + ONE_WEEK;
    private static final long THIRD_WEEK = SECOND_WEEK + ONE_WEEK;
    private static final long FOURTH_WEEK = THIRD_WEEK + ONE_WEEK;
    private static final long FIFTH_WEEK = FOURTH_WEEK + ONE_WEEK;
    private static final long SIXTH_WEEK = FIFTH_WEEK + ONE_WEEK;
    private static final long SEVENTH_WEEK = SIXTH_WEEK + ONE_WEEK;
    private static final long EIGHTH_WEEK = SEVENTH_WEEK + ONE_WEEK;
    private static final long NINETH_WEEK = EIGHTH_WEEK + ONE_WEEK;
    private static final long TENTH_WEEK = NINETH_WEEK + ONE_WEEK;



    public static void createPartitioning(String venueid,
                HashMap<String, Long> mapUserCheckin, HashMap<String, UserVertex> mapUseridVertex){

        //find minimum
        Long minimumDate = Long.MAX_VALUE;
        for (String userid: mapUserCheckin.keySet()){
            Long date = mapUserCheckin.get(userid);
            if (date < minimumDate){
                minimumDate = date;
            }
        }

        //now that the minimum date is known the differences can be calculated
        for (String userid: mapUserCheckin.keySet()){
            Long date = mapUserCheckin.get(userid);
            Long difference = date - minimumDate;

            if(difference < 0){System.out.println("Error in createPartioning!");};

            long partial = difference / ONE_WEEK;
            double ceil = Math.ceil(partial);

            UserVertex userVertex = mapUseridVertex.get(userid);
            userVertex.addPartition(venueid, (int)ceil);

            UserVertex.updateMinMaxPartitionValue((int)ceil);
        }
    }


}
