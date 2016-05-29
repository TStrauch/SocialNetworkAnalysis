package graph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model.UserEdgeArc;
import graph.model.UserVertex;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Timo on 13.05.16.
 */
public class Socialgraph {
    private String checkinFile;
    private String socialgraphFile;

    public HashMap<String, UserVertex> mapCheckinUserids = new HashMap<String, UserVertex>();
    public HashMap<UserVertex, UserVertex> mapSocialgraph = new HashMap<UserVertex, UserVertex>();
    public UndirectedSparseGraph<UserVertex, UserEdgeArc> jungGraph = new UndirectedSparseGraph<UserVertex, UserEdgeArc>();

    public Socialgraph(String checkinFile, String socialgraphFile) throws IOException {
        this.checkinFile = checkinFile;
        this.socialgraphFile = socialgraphFile;

        this.createSocialgraph();
//        this.saveSocialgraph("data/input/socialgraph_by_checkins.csv");
    }


    public static void main(String[] args) throws IOException {
        Socialgraph socialgraph = new Socialgraph("data/input/users_by_checkins2.csv", "data/input/socialgraph_v2.csv");
//        socialgraph.saveFilteredGraph("data/input/socialgraph_by_checkins.csv");
//        Graph graph = socialgraph.createSocialgraph();
//        System.out.println(graph);
    }


    public Graph createSocialgraph() throws IOException {

        this.createMapCheckinUsers();
        this.createJungGraph();

        return this.jungGraph;
    }

    private void createMapCheckinUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(this.checkinFile)));
        reader.readLine(); //header line
        String inCheckin;
        while((inCheckin = reader.readLine()) != null){
            String[] split = inCheckin.split(";");
            String userid = split[0];
            userid = userid.replace(".0","");
            String countCheckins = split[1];
            countCheckins = countCheckins.replace(".0","");

            UserVertex userVertex = new UserVertex(userid, Integer.parseInt(countCheckins));

            this.mapCheckinUserids.put(userid, userVertex);
        }
        reader.close();
    }

    private void saveSocialgraph(String path) throws IOException {

        this.createMapCheckinUsers();

        BufferedWriter writer = new BufferedWriter ( new FileWriter (new File(path)));
        writer.write("first_user_id;second_user_id\n");

        for (UserVertex userVertex : mapCheckinUserids.values()) {
            this.jungGraph.addVertex(userVertex);
        }

        //only leave those entries in the socialgraph that represent "power" users (many checkins)
        //also remove duplicate connections (we create an undirected network)
        Set<String> lookup = new HashSet<String>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(this.socialgraphFile)));
        String inSocial;
        boolean duplicate = false;
        int countConnections = 0;
        reader.readLine(); //skip headerline
        while((inSocial = reader.readLine()) != null){

            inSocial = inSocial.replaceAll("\\n", "");
            String[] userids = inSocial.split(";");

            if(lookup.contains(this.hash(userids[1], userids[0])) || lookup.contains(this.hash(userids[0], userids[1]))){
                duplicate = true;
            }
            else{
                duplicate = false;
            }

            if(!duplicate){
                countConnections++;
            }

            if(mapCheckinUserids.keySet().contains(userids[0]) && mapCheckinUserids.keySet().contains(userids[1]) && !duplicate){
                countConnections++;
                lookup.add(hash(userids[0], userids[1]));

                UserVertex userVertex1 = mapCheckinUserids.get(userids[0]);
                UserVertex userVertex2 = mapCheckinUserids.get(userids[1]);

                this.jungGraph.addEdge(new UserEdgeArc(), userVertex1, userVertex2, EdgeType.UNDIRECTED);

                writer.write(inSocial+"\n");
            }

        }
        reader.close();
        writer.close();
    }


    private void createJungGraph() throws IOException {
        for (UserVertex userVertex : mapCheckinUserids.values()) {
            this.jungGraph.addVertex(userVertex);
        }

        //only leave those entries in the socialgraph that represent "power" users (many checkins)
        //also remove duplicate connections (we create an undirected network)
        Set<String> lookup = new HashSet<String>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(this.socialgraphFile)));
        String inSocial;
        boolean duplicate = false;
        int countConnections = 0;
        reader.readLine(); //skip headerline
        while((inSocial = reader.readLine()) != null){

            inSocial = inSocial.replaceAll("\\n", "");
            String[] userids = inSocial.split(";");

//            if(lookup.contains(this.hash(userids[1], userids[0])) || lookup.contains(this.hash(userids[0], userids[1]))){
            if(lookup.contains(this.hash(userids[1], userids[0]))){
                duplicate = true;
            }
            else{
                duplicate = false;
            }

            if(!duplicate){
                countConnections++;
            }

//            if(mapCheckinUserids.keySet().contains(userids[0]) && mapCheckinUserids.keySet().contains(userids[1]) && !duplicate){
            if(mapCheckinUserids.keySet().contains(userids[0]) && mapCheckinUserids.keySet().contains(userids[1]) && !duplicate){
                countConnections++;
                lookup.add(hash(userids[0], userids[1]));

                UserVertex userVertex1 = mapCheckinUserids.get(userids[0]);
                UserVertex userVertex2 = mapCheckinUserids.get(userids[1]);

                this.jungGraph.addEdge(new UserEdgeArc(), userVertex1, userVertex2, EdgeType.UNDIRECTED);

            }

        }
        reader.close();

//        System.out.println("Total number of connections: "+countConnections);
//        System.out.println("Retained number of connections: "+this.jungGraph.getEdgeCount());
//        System.out.println("Number of remaining users "+this.mapCheckinUserids.size());
    }


    private String hash(String s1, String s2){
        return s1+"<-->"+s2;
    }
}
