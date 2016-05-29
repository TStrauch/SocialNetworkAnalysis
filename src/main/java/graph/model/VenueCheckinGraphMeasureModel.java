package graph.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timo on 15.05.16.
 */
public class VenueCheckinGraphMeasureModel {
    private String venueid;
    private String label;
    private int numOfUsersWhoCheckedIn;
    private double degreeCentralization;
    private double betweennessCentralization;
    private double closenessCentralization;
    private double avgShortestPathDistance;
    private double clusteringCoefficient;
    private int numOfConnections;
    private double connectionsOverUsers;

    public VenueCheckinGraphMeasureModel(){}

    public VenueCheckinGraphMeasureModel(String venueid, int numOfUsersWhoCheckedIn, String label, double degreeCentralization, double betweennessCentralization, double closenessCentralization, double avgShortestPathDistance, double clusteringCoefficient, int numOfConnections,double connectionsOverUsers) {
        this.venueid = venueid;
        this.numOfUsersWhoCheckedIn = numOfUsersWhoCheckedIn;
        this.label = label;
        this.degreeCentralization = degreeCentralization;
        this.betweennessCentralization = betweennessCentralization;
        this.closenessCentralization = closenessCentralization;
        this.avgShortestPathDistance = avgShortestPathDistance;
        this.clusteringCoefficient = clusteringCoefficient;
        this.numOfConnections = numOfConnections;
        this.connectionsOverUsers = connectionsOverUsers;

    }

    public static void saveToFile(String path, List<VenueCheckinGraphMeasureModel> list) throws IOException {
        BufferedWriter writer = new BufferedWriter ( new FileWriter(new File(path)));

        writer.write("venueid;label;numOfUsersWhoCheckedIn;degreeCentralization;betweennessCentralization;closenessCentralization;avgShortestPathDistance;clusteringCoefficient;numOfConnections;connectionsOverUsers\n");
        for (VenueCheckinGraphMeasureModel model : list) {
            writer.write(model.venueid+";");
            writer.write(model.label+";");
            writer.write(model.numOfUsersWhoCheckedIn +";");
            writer.write(model.degreeCentralization+";");
            writer.write(model.betweennessCentralization+";");
            writer.write(model.closenessCentralization+";");
            writer.write(model.avgShortestPathDistance+";");
            writer.write(model.closenessCentralization+";");
            writer.write(model.numOfConnections+";");
            writer.write(model.connectionsOverUsers+";");
            writer.write("\n");
        }
        writer.close();
    }

    public static List<VenueCheckinGraphMeasureModel> readFromFile(String path) throws IOException {
        List<VenueCheckinGraphMeasureModel> list = new ArrayList<VenueCheckinGraphMeasureModel>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        reader.readLine(); //headerline;
        String line;
        while((line = reader.readLine()) != null){
            String[] split = line.split(";");
            VenueCheckinGraphMeasureModel model = new VenueCheckinGraphMeasureModel(
                    split[0],
                    Integer.parseInt(split[2]),
                    split[1],
                    Double.parseDouble(split[3]),
                    Double.parseDouble(split[4]),
                    Double.parseDouble(split[5]),
                    Double.parseDouble(split[6]),
                    Double.parseDouble(split[7]),
                    Integer.parseInt(split[8]),
                    Double.parseDouble(split[9])
            );
            list.add(model);

        }
        reader.close();
        return list;
    }


    public double getConnectionsOverUsers() {
        return connectionsOverUsers;
    }

    public void setConnectionsOverUsers(double connectionsOverUsers) {
        this.connectionsOverUsers = connectionsOverUsers;
    }

    public int getNumOfConnections() {
        return numOfConnections;
    }

    public void setNumOfConnections(int numOfConnections) {
        this.numOfConnections = numOfConnections;
    }

    public String getVenueid() {
        return venueid;
    }

    public void setVenueid(String venueid) {
        this.venueid = venueid;
    }

    public int getNumOfUsersWhoCheckedIn() {
        return numOfUsersWhoCheckedIn;
    }

    public void setNumOfUsersWhoCheckedIn(int numOfUsersWhoCheckedIn) {
        this.numOfUsersWhoCheckedIn = numOfUsersWhoCheckedIn;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getDegreeCentralization() {
        return degreeCentralization;
    }

    public void setDegreeCentralization(double degreeCentralization) {
        this.degreeCentralization = degreeCentralization;
    }

    public double getBetweennessCentralization() {
        return betweennessCentralization;
    }

    public void setBetweennessCentralization(double betweennessCentralization) {
        this.betweennessCentralization = betweennessCentralization;
    }

    public double getClosenessCentralization() {
        return closenessCentralization;
    }

    public void setClosenessCentralization(double closenessCentralization) {
        this.closenessCentralization = closenessCentralization;
    }

    public double getAvgShortestPathDistance() {
        return avgShortestPathDistance;
    }

    public void setAvgShortestPathDistance(double avgShortestPathDistance) {
        this.avgShortestPathDistance = avgShortestPathDistance;
    }

    public double getClusteringCoefficient() {
        return clusteringCoefficient;
    }

    public void setClusteringCoefficient(double clusteringCoefficient) {
        this.clusteringCoefficient = clusteringCoefficient;
    }
}
