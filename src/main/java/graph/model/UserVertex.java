package graph.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Timo on 13.05.16.
 */
public class UserVertex {
    private String userid;
    private int countCheckins;
    private HashMap<String, Double> partitionPerVenue = new HashMap<String, Double>();

    //measures
    private HashMap<String, Integer> degreePerVenue = new HashMap<String, Integer>();
    private HashMap<String, Double> prestigePerVenue = new HashMap<String, Double>();
    private HashMap<String, Integer> outDegreePerVenue = new HashMap<String, Integer>();
    private HashMap<String, Double> thresholdPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> outInDegreeQuotientPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> hubPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> authorityPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> degreeCentralityPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> betweennessCentralityPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> closenessCentralityPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> eigenvectorCentralityPerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> proximityPrestigePerVenue = new HashMap<String, Double>();
    private HashMap<String, Double> influenceScorePerVenue = new HashMap<String, Double>();

    //weights for the influence-score calculation
    private static int weightDegreeCentrality = 1;
    private static int weightClosenessCentrality = 2;
    private static int weightBetweennessCentrality = 2;
    private static int weightPrestige = 2;
    private static int weightProximityPrestige = 8;
    private static int weightPartition = 8;
    private static int weightThreshold = 4;
    private static int weightDirectionalVsUndirectional = 2;


    //min max values for measures
    private static HashMap<String, double[]> minMaxPerMeasure = new HashMap<String, double[]>();

    public UserVertex(){}

    public UserVertex(String userid, int countCheckins){
        this.userid = userid;
        this.countCheckins = countCheckins;
    }


    public String getUserid() {
        return userid;
    }

    public HashMap<String, Double> getPartitionPerVenue() {
        return partitionPerVenue;
    }

    public void setPartitionPerVenue(HashMap<String, Double> partitionPerVenue) {
        this.partitionPerVenue = partitionPerVenue;
    }

    public void addPartition(String venueid, double partition){
        this.partitionPerVenue.put(venueid, partition);
    }

    public double getPartition(String venueid){
        return this.partitionPerVenue.get(venueid);
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getCountCheckins() {
        return countCheckins;
    }

    public void setCountCheckins(int countCheckins) {
        this.countCheckins = countCheckins;
    }

    public HashMap<String, Integer> getDegreePerVenue() {
        return degreePerVenue;
    }

    public HashMap<String, Double> getPrestigePerVenue() {
        return prestigePerVenue;
    }

    public HashMap<String, Integer> getOutDegreePerVenue() {
        return outDegreePerVenue;
    }

    public HashMap<String, Double> getThresholdPerVenue() {
        return thresholdPerVenue;
    }

    public HashMap<String, Double> getOutInDegreeQuotientPerVenue() {
        return outInDegreeQuotientPerVenue;
    }

    public HashMap<String, Double> getHubPerVenue() {
        return hubPerVenue;
    }

    public HashMap<String, Double> getAuthorityPerVenue() {
        return authorityPerVenue;
    }

    public HashMap<String, Double> getDegreeCentralityPerVenue() {
        return degreeCentralityPerVenue;
    }

    public HashMap<String, Double> getBetweennessCentralityPerVenue() {
        return betweennessCentralityPerVenue;
    }

    public HashMap<String, Double> getClosenessCentralityPerVenue() {
        return closenessCentralityPerVenue;
    }

    public HashMap<String, Double> getEigenvectorCentralityPerVenue() {
        return eigenvectorCentralityPerVenue;
    }

    public HashMap<String, Double> getProximityPrestigePerVenue() {
        return proximityPrestigePerVenue;
    }

    public int getNumOfAssignedVenues(){
        return this.partitionPerVenue.size();
    }

    public String toString(){
        String s = "";
        s += "("+userid+")";
        return s;
    }

    public void computeInfluenceScore(String venueId){
        //undirectional measures
        double degreeCentrality = this.degreeCentralityPerVenue.get(venueId);
        double betweennessCentrality = this.betweennessCentralityPerVenue.get(venueId);
        double closenessCentrality = this.closenessCentralityPerVenue.get(venueId);

        //directional measures
        double prestige = this.prestigePerVenue.get(venueId);
        double proximityPrestige = this.proximityPrestigePerVenue.get(venueId);
        double partition = this.partitionPerVenue.get(venueId);
        double threshold = this.thresholdPerVenue.get(venueId);

        double undirectionalScore = ((betweennessCentrality * weightBetweennessCentrality)
                                    + (closenessCentrality * weightClosenessCentrality)
                                    + (degreeCentrality * weightDegreeCentrality))
                                    /
                                    (weightBetweennessCentrality + weightDegreeCentrality + weightClosenessCentrality);
        double directionalScore = ((proximityPrestige * weightProximityPrestige)
                                    + (prestige * weightPrestige)
                                    + (1 / ((threshold + 1) * weightThreshold)))
                                    /
                                    ((partition + 1) * weightPartition);

        directionalScore = directionalScore * weightDirectionalVsUndirectional;

        double finalInfluenceScore = undirectionalScore + directionalScore;

        this.influenceScorePerVenue.put(venueId, finalInfluenceScore);


    }

    //DEPRECATED
//    public HashMap<String, Double> calculateJointMetricsDEPRECATED(String venueId){
//        //calc the average values for all metrics
//        //also calculate the average partition value
//        LinkedHashMap<String, Double> avgMap = new LinkedHashMap<String, Double>();
//        double avgPartitions = 0;
//        for (Double d : this.partitionPerVenue.values()) {
//            avgPartitions += d;
//        }
//
//        avgMap.put("userid", Double.parseDouble(this.userid));
//        avgMap.put("numCheckins", (double) this.countCheckins);
//        avgMap.put("numOfAssignedVenues", (double)this.getNumOfAssignedVenues());
//        avgMap.put("proximityPrestige", this.calcAvgDouble(this.proximityPrestigePerVenue));
//        avgMap.put("partition", avgPartitions);
//        avgMap.put("threshold", this.calcAvgDouble(this.thresholdPerVenue));
////        avgMap.put("degree", this.calcAvgInteger(this.degreePerVenue));
//        avgMap.put("prestige", this.calcAvgDouble(this.prestigePerVenue));
////        avgMap.put("outDegree", this.calcAvgInteger(this.outDegreePerVenue));
////        avgMap.put("outInDegreeQuotient", this.calcAvgDouble(this.outInDegreeQuotientPerVenue));
////        avgMap.put("hub", this.calcAvgDouble(this.hubPerVenue));
////        avgMap.put("authority", this.calcAvgDouble(this.authorityPerVenue));
////        avgPartitions = avgPartitions / this.partitionPerVenue.size();
//        avgMap.put("betweennessCentrality", this.calcAvgDouble(this.betweennessCentralityPerVenue));
//        avgMap.put("closenessCentrality", this.calcAvgDouble(this.closenessCentralityPerVenue));
//        avgMap.put("degreeCentrality", this.calcAvgDouble(this.degreeCentralityPerVenue));
////        avgMap.put("eigenvectorCentrality", this.calcAvgDouble(this.eigenvectorCentralityPerVenue));
//
//        return avgMap;
//    }

    public HashMap<String, Double> calculateJointMetrics(String venueId){
        //calc the average values for all metrics
        //also calculate the average partition value
        LinkedHashMap<String, Double> map = new LinkedHashMap<String, Double>();

        map.put("userid", Double.parseDouble(this.userid));
        map.put("numCheckins", (double) this.countCheckins);
        map.put("numOfAssignedVenues", (double)this.getNumOfAssignedVenues());
        map.put("proximityPrestige", this.proximityPrestigePerVenue.get(venueId));
        map.put("partition", this.partitionPerVenue.get(venueId));
        map.put("threshold", this.thresholdPerVenue.get(venueId));
        map.put("prestige", this.prestigePerVenue.get(venueId));
        map.put("betweennessCentrality", this.betweennessCentralityPerVenue.get(venueId));
        map.put("closenessCentrality", this.closenessCentralityPerVenue.get(venueId));
        map.put("degreeCentrality", this.degreeCentralityPerVenue.get(venueId));
        map.put("influenceScore", this.influenceScorePerVenue.get(venueId));

        return map;
    }

    public String toString(String venueId){
        HashMap<String, Double> map = this.calculateJointMetrics(venueId);

        String s = "";

        for (Double aDouble : map.values()) {
            s += aDouble+";";
        }

        return s;
    }
//    public static String toStringHeader(){
//        return "userid;numCheckins;degree;inDegree;outDegree;outInDegreeQuotient;threshold;hub;authority;sumPartitions;numOfAssignedVenues;degreeCentrality;betweennessCentrality;closenessCentrality;eigenvectorCentrality;";
//    }

    private Double calcAvgInteger(HashMap<String, Integer> map){
        double sum = 0;
        for (Integer aInteger : map.values()) {
            sum += aInteger;
        }
        return sum / (double)map.size();
    }
    private Double calcAvgDouble(HashMap<String, Double> map){
        double sum = 0;
        for (Double aDouble : map.values()) {
            sum += aDouble;
        }
        return sum / (double)map.size();
    }


    //setting min max values for measures
    public static void updateMinMaxPartitionValue(int val) {
        updateMinMaxValuePerMeasure("partition", val);
    }
    public static void updateMinMaxPrestigeValue(double val) {
        updateMinMaxValuePerMeasure("prestige", val);
    }
    public static void updateMinMaxThresholdValue(double val) {
        updateMinMaxValuePerMeasure("threshold", val);
    }
    public static void updateMinMaxDegreeCentralityValue(double val) {
        updateMinMaxValuePerMeasure("degreeCentrality", val);
    }
    public static void updateMinMaxBetweennessCentralityValue(double val) {
        updateMinMaxValuePerMeasure("betweennessCentrality", val);
    }
    public static void updateClosenessCentralityValue(double val) {
        updateMinMaxValuePerMeasure("closenessCentrality",val);
    }
    public static void updateProximityPrestigeValue(double val) {
        updateMinMaxValuePerMeasure("proximityPrestige",val);
    }


    private static void updateMinMaxValuePerMeasure(String measure, double val){
        double[] partitions = minMaxPerMeasure.get(measure);
        if (partitions == null){
            partitions = new double[2];
            minMaxPerMeasure.put(measure, partitions);
        }

        double min = partitions[0];
        double max = partitions[1];

        if (val < min){
            partitions[0] = val;
        }
        if (val > max){
            partitions[1] = val;
        }
    }

    public static double[] getMinMaxPartitionValue(){
        return minMaxPerMeasure.get("partition");
    }
    public static double[] getMinMaxPrestigeValue() {
        return minMaxPerMeasure.get("prestige");
    }
    public static double[] getMinMaxThresholdValue() {
        return minMaxPerMeasure.get("threshold");
    }
    public static double[] getMinMaxDegreeCentralityValue() {
        return minMaxPerMeasure.get("degreeCentrality");
    }
    public static double[] getMinMaxBetweennessCentralityValue() {
        return minMaxPerMeasure.get("betweennessCentrality");
    }
    public static double[] getMinMaxClosenessCentralityValue() {
        return minMaxPerMeasure.get("closenessCentrality");
    }
    public static double[] getMinMaxProximityPrestigeValue() {
        return minMaxPerMeasure.get("proximityPrestige");
    }


    private static double[] getMinMaxPerMeasure(String measure){
        double[] doubles = minMaxPerMeasure.get(measure);
        return doubles;
    }
}
