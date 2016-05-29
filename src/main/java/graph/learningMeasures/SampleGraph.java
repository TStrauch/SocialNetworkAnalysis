package graph.learningMeasures;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model.UserEdgeArc;
import graph.model.UserVertex;

/**
 * Created by Timo on 13.05.16.
 */
public class SampleGraph {
    public UndirectedSparseGraph<String, String> jungGraph = new UndirectedSparseGraph<String, String>();

    public SampleGraph(){
        String[] people = {
                "Timo",
                "Artem",
                "Nadja",
                "Sophia",
                "Matze",
                "Olli",
                "Martin",
                "Sascha",
                "Max",
                "Jonas P",
                "Jonas B",
                "Philip",
                "Tommi",
                "Prof. Stuckenschmidt",
                "Prof. Heinzl",
                "Prof. Bizer"
        };

        for (String s : people) {
            this.jungGraph.addVertex(s);
        }

        this.jungGraph.addEdge(hash(people[0], people[1]),people[0], people[1], EdgeType.UNDIRECTED ); //Timo Artem
        this.jungGraph.addEdge(hash(people[0], people[2]),people[0], people[2], EdgeType.UNDIRECTED ); //Timo Nadja
        this.jungGraph.addEdge(hash(people[0], people[3]),people[0], people[3], EdgeType.UNDIRECTED ); //Timo Sophia
        this.jungGraph.addEdge(hash(people[0], people[4]),people[0], people[4], EdgeType.UNDIRECTED ); //Timo Matze
        this.jungGraph.addEdge(hash(people[0], people[5]),people[0], people[5], EdgeType.UNDIRECTED ); //Timo Olli
        this.jungGraph.addEdge(hash(people[0], people[6]),people[0], people[6], EdgeType.UNDIRECTED ); //Timo Martin
        this.jungGraph.addEdge(hash(people[0], people[7]),people[0], people[7], EdgeType.UNDIRECTED ); //Timo sascha
        this.jungGraph.addEdge(hash(people[0], people[8]),people[0], people[8], EdgeType.UNDIRECTED ); //Timo Max
        this.jungGraph.addEdge(hash(people[0], people[9]),people[0], people[9], EdgeType.UNDIRECTED ); //Timo Jonas P
        this.jungGraph.addEdge(hash(people[0], people[10]),people[0], people[10], EdgeType.UNDIRECTED ); //Timo Jonas B
        this.jungGraph.addEdge(hash(people[0], people[11]),people[0], people[11], EdgeType.UNDIRECTED ); //Timo Philip
        this.jungGraph.addEdge(hash(people[0], people[12]),people[0], people[12], EdgeType.UNDIRECTED ); //Timo Tommi
        this.jungGraph.addEdge(hash(people[0], people[13]),people[0], people[13], EdgeType.UNDIRECTED ); //Timo Stucken
        this.jungGraph.addEdge(hash(people[0], people[14]),people[0], people[14], EdgeType.UNDIRECTED ); //Timo Heinzl
        this.jungGraph.addEdge(hash(people[0], people[15]),people[0], people[15], EdgeType.UNDIRECTED ); //Timo Bizer


        this.jungGraph.addEdge(hash(people[1], people[2]),people[1], people[2], EdgeType.UNDIRECTED ); //Artem Nadja
        this.jungGraph.addEdge(hash(people[1], people[5]),people[1], people[5], EdgeType.UNDIRECTED ); //Artem Olli
        this.jungGraph.addEdge(hash(people[1], people[6]),people[1], people[6], EdgeType.UNDIRECTED ); //Artem Martin
        this.jungGraph.addEdge(hash(people[1], people[9]),people[1], people[9], EdgeType.UNDIRECTED ); //Artem Jonas P
        this.jungGraph.addEdge(hash(people[1], people[12]),people[1], people[12], EdgeType.UNDIRECTED ); //Artem Tommi
        this.jungGraph.addEdge(hash(people[1], people[13]),people[1], people[13], EdgeType.UNDIRECTED ); //Artem Stucken
        this.jungGraph.addEdge(hash(people[1], people[14]),people[1], people[14], EdgeType.UNDIRECTED ); //Artem Heinzl
        this.jungGraph.addEdge(hash(people[1], people[15]),people[1], people[15], EdgeType.UNDIRECTED ); //Artem Bizer


        this.jungGraph.addEdge(hash(people[2], people[3]),people[2], people[3], EdgeType.UNDIRECTED ); //Nadja Sophia
        this.jungGraph.addEdge(hash(people[2], people[9]),people[2], people[9], EdgeType.UNDIRECTED ); //Nadja Jonas P
        this.jungGraph.addEdge(hash(people[2], people[13]),people[2], people[13], EdgeType.UNDIRECTED ); //Nadja Stucken
        this.jungGraph.addEdge(hash(people[2], people[15]),people[2], people[15], EdgeType.UNDIRECTED ); //Nadja Bizer

        this.jungGraph.addEdge(hash(people[3], people[4]),people[3], people[4], EdgeType.UNDIRECTED ); //Sophia Matze
        this.jungGraph.addEdge(hash(people[3], people[6]),people[3], people[6], EdgeType.UNDIRECTED ); //Sophia Martin
        this.jungGraph.addEdge(hash(people[3], people[8]),people[3], people[8], EdgeType.UNDIRECTED ); //Sophia Max
        this.jungGraph.addEdge(hash(people[3], people[9]),people[3], people[9], EdgeType.UNDIRECTED ); //Sophia Jonas P
        this.jungGraph.addEdge(hash(people[3], people[10]),people[3], people[10], EdgeType.UNDIRECTED ); //Sophia Jonas B
        this.jungGraph.addEdge(hash(people[3], people[11]),people[3], people[11], EdgeType.UNDIRECTED ); //Sophia Philip
        this.jungGraph.addEdge(hash(people[3], people[12]),people[3], people[12], EdgeType.UNDIRECTED ); //Sophia Tommi
        this.jungGraph.addEdge(hash(people[3], people[14]),people[3], people[14], EdgeType.UNDIRECTED ); //Sophia Heinzl
        this.jungGraph.addEdge(hash(people[3], people[15]),people[3], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer


        this.jungGraph.addEdge(hash(people[4], people[8]),people[4], people[8], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[4], people[9]),people[4], people[9], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[4], people[10]),people[4], people[10], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[4], people[11]),people[4], people[11], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[4], people[12]),people[4], people[12], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[4], people[14]),people[4], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[4], people[15]),people[4], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer


        this.jungGraph.addEdge(hash(people[5], people[6]),people[5], people[6], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[5], people[7]),people[5], people[7], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[5], people[9]),people[5], people[9], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[5], people[13]),people[5], people[13], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[5], people[15]),people[5], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer


        this.jungGraph.addEdge(hash(people[6], people[7]),people[6], people[7], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[8]),people[6], people[8], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[9]),people[6], people[9], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[10]),people[6], people[10], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[11]),people[6], people[11], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[12]),people[6], people[12], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[13]),people[6], people[13], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[6], people[15]),people[6], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer


        this.jungGraph.addEdge(hash(people[7], people[9]),people[7], people[9], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[7], people[10]),people[7], people[10], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[7], people[13]),people[7], people[13], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[7], people[14]),people[7], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[7], people[15]),people[7], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer


        this.jungGraph.addEdge(hash(people[8], people[9]),people[8], people[9], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[8], people[10]),people[8], people[10], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[8], people[11]),people[8], people[11], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[8], people[12]),people[8], people[12], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[8], people[14]),people[8], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer

        this.jungGraph.addEdge(hash(people[9], people[10]),people[9], people[10], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[9], people[11]),people[9], people[11], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[9], people[12]),people[9], people[12], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[9], people[13]),people[9], people[13], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[9], people[14]),people[9], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[9], people[15]),people[9], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer

        this.jungGraph.addEdge(hash(people[10], people[11]),people[10], people[11], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[10], people[12]),people[10], people[12], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[10], people[14]),people[10], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[10], people[15]),people[10], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer

        this.jungGraph.addEdge(hash(people[11], people[12]),people[11], people[12], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[11], people[14]),people[11], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[11], people[15]),people[11], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer

        this.jungGraph.addEdge(hash(people[12], people[13]),people[12], people[13], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[12], people[14]),people[12], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[12], people[15]),people[12], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer

        this.jungGraph.addEdge(hash(people[13], people[14]),people[13], people[14], EdgeType.UNDIRECTED ); //Sophia Bizer
        this.jungGraph.addEdge(hash(people[13], people[15]),people[13], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer

        this.jungGraph.addEdge(hash(people[14], people[15]),people[14], people[15], EdgeType.UNDIRECTED ); //Sophia Bizer

    }

    private String hash(String s1, String s2){
        return s1+"<-->"+s2;
    }
}
