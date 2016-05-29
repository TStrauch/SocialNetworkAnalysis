package graph;

import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.scoring.*;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.PajekNetWriter;
import graph.model.UserEdgeArc;
import graph.model.UserVertex;
import graph.partition.CustomPartition;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timo on 14.05.16.
 */
public class SocialgraphUserCheckinVenue {
    private static Socialgraph socialgraph;
    private static String checkinFile;
    private static HashMap<String, HashMap<String, Long>> mapCheckinVenueUser = new HashMap<String, HashMap<String, Long>>();

    public static HashMap<String, HashMap<String, Long>>  createFirstCheckinPerVenueMap(String cf, boolean forceNew) throws IOException, ParseException {

        if(!forceNew){
            try {
                readMapCheckinVenueUser();
            }
            catch(IOException e){

            }
        }

        if(mapCheckinVenueUser.size() > 0){
            return mapCheckinVenueUser;
        }

        checkinFile = cf;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

        BufferedReader reader = new BufferedReader(new FileReader(new File(checkinFile)));
        reader.readLine();
        String inCheckin;
        while((inCheckin = reader.readLine()) != null){
            String[] split = inCheckin.split(";");
            String createdAtStr = split[2].replaceAll("\"", "");
            String userid = split[4].replaceAll("\\.0","");
            String label = split[5];
            String venueid = split[6].replaceAll("\\.0","");

            if (!keepCheckin(label)){
                continue;
            }

            Long date = 0L;
            try {
                date = df.parse(createdAtStr).getTime();
            }
            catch(ParseException e){
                continue;
            }

            if(mapCheckinVenueUser.get(venueid) == null){
                HashMap<String, Long> map = new HashMap<String, Long>();
                mapCheckinVenueUser.put(venueid, map);
            }
            HashMap<String, Long> mapUserDate = mapCheckinVenueUser.get(venueid);
            if(mapUserDate.get(userid) == null){
                mapUserDate.put(userid, date);
            }
            else{
                Long currentdate = mapUserDate.get(userid);
                if (date < currentdate){
                    mapUserDate.put(userid, date);
                }
            }
        }

        saveMapCheckinVenueUser();

        return mapCheckinVenueUser;
    }

    private static boolean keepCheckin(String label){
        boolean keep = true;

        if (label.toLowerCase().contains("airport")){
            keep = false;
        }
        if (label.toLowerCase().contains("plane")){
            keep = false;
        }

        return keep;
    }

    private static void saveMapCheckinVenueUser() throws IOException {
        BufferedWriter writer = new BufferedWriter ( new FileWriter (new File("data/workingFiles/cacheMapCheckinVenueUser.csv")));

        int counter = 0;
        for (String venueid : mapCheckinVenueUser.keySet()) {
            writer.write(venueid+":::");
            HashMap<String, Long> mapUseridDate = mapCheckinVenueUser.get(venueid);
            for (String userid : mapUseridDate.keySet()) {
                Long date = mapUseridDate.get(userid);
                writer.write(userid+"->");
                writer.write(date+"");
                writer.write(";");
            }
            writer.write("\n");
            counter++;

//            System.out.println(venueid);
        }
        writer.close();
//        System.out.println("Wrote "+counter+" venues");

        readMapCheckinVenueUser();
    }
    private static void readMapCheckinVenueUser() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("data/workingFiles/cacheMapCheckinVenueUser.csv")));
        String line;
        int counter = 0;
        while((line = reader.readLine()) != null){
            String[] split = line.split(":::");
            String venueid = split[0];

            String[] userDatePairs = split[1].split(";");
            for (String pair: userDatePairs){
                String[] detailPair = pair.split("->");
                String userid = detailPair[0];
                Long date = Long.parseLong(detailPair[1]);

                if(mapCheckinVenueUser.get(venueid) == null){
                    HashMap<String, Long> map = new HashMap<String, Long>();
                    mapCheckinVenueUser.put(venueid, map);
                }
                HashMap<String, Long> mapUserDate = mapCheckinVenueUser.get(venueid);
                mapUserDate.put(userid, date);
            }
            counter++;
        }

//        System.out.println("Read "+counter+" lines");
    }

    public static HashMap<String, HashMap<String, Long>> getMapCheckinVenueUser() {
        return mapCheckinVenueUser;
    }

    public static void main (String[] args) throws IOException, ParseException {
        HashMap<String, HashMap<String, Long>> map = SocialgraphUserCheckinVenue.createFirstCheckinPerVenueMap("data/input/checkins_filtered2_labeled.csv", true);

    }


    /**
     *
     *
     *
     * Here begins the object specific part
     */




    private String venueid;
    private DirectedSparseGraph<UserVertex, UserEdgeArc> userCheckinGraph;

    public SocialgraphUserCheckinVenue(Socialgraph g, String venueid){
        socialgraph = g;
        this.venueid = venueid;

        //create userCheckinGraph
        CustomPartition.createPartitioning(venueid, mapCheckinVenueUser.get(venueid), socialgraph.mapCheckinUserids);
        this.createUserCheckinGraph();
    }

    public DirectedSparseGraph<UserVertex, UserEdgeArc> getUserCheckinGraph() {
        return userCheckinGraph;
    }

    public void printUserCheckinGraph(){
        System.out.println("Vertices:");
        for (UserVertex userVertex : userCheckinGraph.getVertices()) {
            System.out.print(userVertex.toString(this.venueid));
            System.out.print("; ");
        }
        System.out.println("\n");

        System.out.println("Edges:");
        for (UserEdgeArc userEdgeArc : userCheckinGraph.getEdges()) {
            System.out.print(userEdgeArc);
            System.out.print("; ");
        }
        System.out.println("\n");
    }

    public String getVenueid() {
        return venueid;
    }

    private void createUserCheckinGraph(){
        userCheckinGraph = new DirectedSparseGraph<UserVertex, UserEdgeArc>();

        for (String userid: mapCheckinVenueUser.get(venueid).keySet()){
            userCheckinGraph.addVertex(socialgraph.mapCheckinUserids.get(userid));
        }

        for (UserVertex userVertex1 : userCheckinGraph.getVertices()) {
            for (UserVertex userVertex2 : userCheckinGraph.getVertices()) {

                //do we have this edge already?
                if(userCheckinGraph.findEdge(userVertex2, userVertex1) != null){
                    continue;
                }

                UserEdgeArc edge = socialgraph.jungGraph.findEdge(userVertex2, userVertex1);

                if(edge != null){

                    double partition1 = userVertex1.getPartition(this.venueid);
                    double partition2 = userVertex2.getPartition(this.venueid);

                    if (partition2 > partition1){
                        userCheckinGraph.addEdge(edge, userVertex2, userVertex1, EdgeType.DIRECTED);
                    }
                    else if (partition1 == partition2){
                        userCheckinGraph.addEdge(edge, userVertex1, userVertex2, EdgeType.DIRECTED);
                        userCheckinGraph.addEdge(new UserEdgeArc(), userVertex2, userVertex1, EdgeType.DIRECTED);
                    }
                    else{
                        userCheckinGraph.addEdge(edge, userVertex1, userVertex2, EdgeType.DIRECTED);
                    }
                }
            }
        }

    }

    //here methods are implemented that calculate different measures for the userCheckinGraph
    public double getDegreeCentralization(){
        return this.calcCentralizationScoreInteger(new DegreeScorer<UserVertex>(this.userCheckinGraph));
    }
    public double getBetweennessCentralization(){
        return this.calcCentralizationScoreDouble(new BetweennessCentrality<UserVertex, UserEdgeArc>(this.userCheckinGraph));
    }
    public double getClosenessCentralization(){
        return this.calcCentralizationScoreDouble(new ClosenessCentrality<UserVertex, UserEdgeArc>(this.userCheckinGraph));
    }
    public double getClusteringCoefficient(){
        Map<UserVertex, Double> clusteringCoefficients = Metrics.clusteringCoefficients(this.userCheckinGraph);

        double sumCoeffiencts = 0.0;
        for (Double coeff : clusteringCoefficients.values()) {
            sumCoeffiencts += coeff;
        }

        return sumCoeffiencts / this.userCheckinGraph.getVertexCount();
    }
    public double getAvgShortestPath(){
        DijkstraDistance<UserVertex, UserEdgeArc> dijkstraDistance = new DijkstraDistance<UserVertex, UserEdgeArc>(this.userCheckinGraph);

        double sumDistances = 0.0;
        for (UserVertex v1 : this.userCheckinGraph.getVertices()){
            for (UserVertex v2 : this.userCheckinGraph.getVertices()){
                Number distance = dijkstraDistance.getDistance(v1, v2);

                if (distance != null) {
                    sumDistances += distance.doubleValue();
                }
            }
        }
        return sumDistances / (this.userCheckinGraph.getVertexCount() * (this.userCheckinGraph.getVertexCount() - 1));
    }


    private double calcCentralizationScoreInteger(VertexScorer<UserVertex, Integer> scorer){
        int maxDegree = 0;
        for (UserVertex v : this.userCheckinGraph.getVertices()){
            int degree = scorer.getVertexScore(v);
            if (degree > maxDegree){
                maxDegree = degree;
            }
        }

        double sumDegree = 0;
        for (UserVertex v : this.userCheckinGraph.getVertices()){
            int degree = scorer.getVertexScore(v);
            sumDegree += maxDegree - degree;
        }

        return sumDegree / ((this.userCheckinGraph.getVertexCount() - 1) * (this.userCheckinGraph.getVertexCount() - 2));
    }

    private double calcCentralizationScoreDouble(VertexScorer<UserVertex, Double> scorer){
        double maxDegree = 0.0;
        for (UserVertex v : this.userCheckinGraph.getVertices()){
            double degree = scorer.getVertexScore(v);
            if (degree > maxDegree){
                maxDegree = degree;
            }
        }

        double sumDegree = 0;
        for (UserVertex v : this.userCheckinGraph.getVertices()){
            double degree = scorer.getVertexScore(v);
            sumDegree += maxDegree - degree;
        }

        return sumDegree / ((this.userCheckinGraph.getVertexCount() - 1) * (this.userCheckinGraph.getVertexCount() - 2));
    }


    /*
     * measures for single vertices
     */

    public void assignAllScores(){
        DegreeScorer<UserVertex> degreeCentrality = new DegreeScorer<UserVertex>(this.userCheckinGraph);
        BetweennessCentrality<UserVertex, UserEdgeArc> betweennessCentrality = new BetweennessCentrality<UserVertex, UserEdgeArc>(this.userCheckinGraph);
        ClosenessCentrality<UserVertex, UserEdgeArc> closenessCentrality = new ClosenessCentrality<UserVertex, UserEdgeArc>(this.userCheckinGraph);
        EigenvectorCentrality<UserVertex, UserEdgeArc> eigenvectorCentrality = new EigenvectorCentrality<UserVertex, UserEdgeArc>(this.userCheckinGraph);
        HITS<UserVertex, UserEdgeArc> hits = new HITS<UserVertex, UserEdgeArc>(this.userCheckinGraph);
        hits.evaluate();

        for (UserVertex userVertex : this.userCheckinGraph.getVertices()) {

            //prestige
            userVertex.getPrestigePerVenue().put(this.venueid, (double)this.userCheckinGraph.inDegree(userVertex));
            UserVertex.updateMinMaxPrestigeValue((double)this.userCheckinGraph.inDegree(userVertex));


            //threshold
            double threshold = (double)this.userCheckinGraph.outDegree(userVertex) / (double)this.userCheckinGraph.degree(userVertex);
            if (Double.isNaN(threshold)){
                threshold = 0.0;
            }
            userVertex.getThresholdPerVenue().put(this.venueid, threshold);
            UserVertex.updateMinMaxThresholdValue(threshold);

            //degree centrality
            double degreeCentralityScore = (double) degreeCentrality.getVertexScore(userVertex);
            if(Double.isNaN(degreeCentralityScore)){
                degreeCentralityScore = 0.0;
            }
            userVertex.getDegreeCentralityPerVenue().put(this.venueid, degreeCentralityScore);
            UserVertex.updateMinMaxDegreeCentralityValue(degreeCentralityScore);

            //betweenness centrality
            Double betweennessCentralityScore = betweennessCentrality.getVertexScore(userVertex);
            if(Double.isNaN(betweennessCentralityScore)){
                betweennessCentralityScore = 0.0;
            }
            userVertex.getBetweennessCentralityPerVenue().put(this.venueid, betweennessCentralityScore);
            UserVertex.updateMinMaxBetweennessCentralityValue(betweennessCentralityScore);

            //closeness centrality
            Double closenessCentralityScore = closenessCentrality.getVertexScore(userVertex);
            if(Double.isNaN(closenessCentralityScore)){
                closenessCentralityScore = 0.0;
            }
            userVertex.getClosenessCentralityPerVenue().put(this.venueid, closenessCentralityScore);
            UserVertex.updateClosenessCentralityValue(closenessCentralityScore);

            //proximity prestige
            Double proximityPrestige = getProximityPrestige(userVertex);
            userVertex.getProximityPrestigePerVenue().put(this.venueid, proximityPrestige);
            UserVertex.updateProximityPrestigeValue(proximityPrestige);
        }

    }

    public void normalizeAllScores() {
        for (UserVertex userVertex : this.userCheckinGraph.getVertices()) {


            //normalize partition
            double normPartition = this.normalizeScore(userVertex.getPartition(this.venueid), UserVertex.getMinMaxPartitionValue());
            userVertex.getPartitionPerVenue().put(this.venueid, normPartition);

            //normalize prestige
            double normPrestige = this.normalizeScore(userVertex.getPrestigePerVenue().get(this.venueid), UserVertex.getMinMaxPrestigeValue());
            userVertex.getPrestigePerVenue().put(this.venueid, normPrestige);

            //normalize proximity prestige
            double normProxPrestige = this.normalizeScore(userVertex.getProximityPrestigePerVenue().get(this.venueid), UserVertex.getMinMaxProximityPrestigeValue());
            userVertex.getProximityPrestigePerVenue().put(this.venueid, normProxPrestige);

            //normalize threshold
            double normThreshold = this.normalizeScore(userVertex.getThresholdPerVenue().get(this.venueid), UserVertex.getMinMaxThresholdValue());
            userVertex.getThresholdPerVenue().put(this.venueid, normThreshold);

            //normalize degree centrality
            double normDegreeCentrality = this.normalizeScore(userVertex.getDegreeCentralityPerVenue().get(this.venueid), UserVertex.getMinMaxDegreeCentralityValue());
            userVertex.getDegreeCentralityPerVenue().put(this.venueid, normDegreeCentrality);

            //normalize betweenness centrality
            double normBetweennessCentrality = this.normalizeScore(userVertex.getBetweennessCentralityPerVenue().get(this.venueid), UserVertex.getMinMaxBetweennessCentralityValue());
            userVertex.getBetweennessCentralityPerVenue().put(this.venueid, normBetweennessCentrality);

            //normalize closeness centrality
            double normClosenessCentrality = this.normalizeScore(userVertex.getClosenessCentralityPerVenue().get(this.venueid), UserVertex.getMinMaxClosenessCentralityValue());
            userVertex.getClosenessCentralityPerVenue().put(this.venueid, normClosenessCentrality);
        }

    }

    private double normalizeScore(double val, double[] minmax) {
        double result = val - minmax[0];
        return result / (minmax[1]-minmax[0]);
    }

    public Double getProximityPrestige(UserVertex u){
        int weightDistanceVsPartition = 5;

        //create inversed graph (arcs are inversed)
        DirectedSparseGraph<UserVertex, UserEdgeArc> invertedGraph = new DirectedSparseGraph<UserVertex, UserEdgeArc>();

        for (UserVertex userVertex1 : this.userCheckinGraph.getVertices()) {
            invertedGraph.addVertex(userVertex1);

            for (UserVertex userVertex2 : this.userCheckinGraph.getVertices()) {
                UserEdgeArc edge = this.userCheckinGraph.findEdge(userVertex1, userVertex2);
                if (edge != null){
                    invertedGraph.addEdge(edge, userVertex2, userVertex1); //invert the edge
                }
            }

        }


        DijkstraShortestPath<UserVertex, UserEdgeArc> dijskstra = new DijkstraShortestPath<UserVertex, UserEdgeArc>(invertedGraph);
        Map<UserVertex, UserEdgeArc> incomingEdgeMap = dijskstra.getIncomingEdgeMap(u);

        incomingEdgeMap.remove(u);

        if (incomingEdgeMap.size() == 0){
            return 0.0;
        }

        double sumDistanceProximityPrestige = 0.0;
        for (UserVertex targetVertex : incomingEdgeMap.keySet()) {
            Number distance = dijskstra.getDistance(u,targetVertex);
            double distanceV = distance.doubleValue();
            distanceV *= weightDistanceVsPartition;
            double partition = targetVertex.getPartition(this.venueid);

            sumDistanceProximityPrestige += (distanceV + partition);
        }

        //proximity factors
        double avgDistanceProximityPrestige =  sumDistanceProximityPrestige / (double)incomingEdgeMap.size();
        double fractionInputDomain = (double)incomingEdgeMap.size() / (double)invertedGraph.getVertexCount();

        double proximityPrestige = fractionInputDomain / avgDistanceProximityPrestige;


        return proximityPrestige;
    }


    public void computeInfluenceScore() {
        for (UserVertex userVertex : this.userCheckinGraph.getVertices()) {
            userVertex.computeInfluenceScore(this.venueid);
        }

    }

    //DEPRECATED
    public void assignAuthorityAndHubScores(){

        //create inversed graph (arcs are inversed)
        DirectedSparseGraph<UserVertex, UserEdgeArc> invertedGraph = new DirectedSparseGraph<UserVertex, UserEdgeArc>();

        for (UserVertex userVertex1 : this.userCheckinGraph.getVertices()) {
            invertedGraph.addVertex(userVertex1);

            for (UserVertex userVertex2 : this.userCheckinGraph.getVertices()) {
                UserEdgeArc edge = this.userCheckinGraph.findEdge(userVertex1, userVertex2);
                if (edge != null){
                    invertedGraph.addEdge(edge, userVertex2, userVertex1); //invert the edge
                }
            }

        }


        //calculate Metrics
        HITS<UserVertex, UserEdgeArc> hits = new HITS<UserVertex, UserEdgeArc>(invertedGraph);
        hits.evaluate();

        for (UserVertex userVertex : invertedGraph.getVertices()) {
            HITS.Scores vertexScore = hits.getVertexScore(userVertex);
            userVertex.getHubPerVenue().put(this.venueid, vertexScore.hub);
            userVertex.getAuthorityPerVenue().put(this.venueid, vertexScore.authority);
        }
    }


    //write to paj file
    public void savePajekNetworkFile(String path) throws IOException {
        Function<UserVertex, String> mappingUser = new Function<UserVertex, String>() {
            public String apply(UserVertex userVertex) {
                return userVertex.getUserid();
            }
        };

        Function<UserEdgeArc, Number> mappingEdge = new Function<UserEdgeArc, Number>() {
            public Number apply(UserEdgeArc userEdgeArc) {
                return 1.0;
            }
        };


        PajekNetWriter<UserVertex, UserEdgeArc> pajekNetWriter = new PajekNetWriter<UserVertex, UserEdgeArc>();
        pajekNetWriter.save(this.userCheckinGraph, path, mappingUser, mappingEdge);
    }

    public void savePajekClusterFile(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter ( new FileWriter (new File(path)));
        writer.write("*Vertices "+this.userCheckinGraph.getVertexCount()+"\n");

        for (UserVertex userVertex : this.userCheckinGraph.getVertices()) {

            writer.write((int)userVertex.getPartition(this.venueid)+"\n");
        }

        writer.close();

    }


}
