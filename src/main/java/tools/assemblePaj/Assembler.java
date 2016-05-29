package tools.assemblePaj;

import java.io.*;

/**
 * Created by Timo on 01.05.16.
 */
public class Assembler {
    //make sure to run the text2pajek program before executing this code.
    public static String inputSocialGraphNet = "data/outputFilteredCSV/socialgraph_checkins_above20.net";
    public static String outputPaj = "data/outputPaj/foursquarePaj.paj";

    public static void main(String[] args) throws IOException {
        Assembler a = new Assembler();
        a.assemble();
    }

    public void assemble() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(new File(inputSocialGraphNet)));
        BufferedWriter writer = new BufferedWriter ( new FileWriter(new File(outputPaj)));

        //create header content of paj file
        writer.write("*Network Socialgraph_checkins_above20.net\n");


        String inNet;
        while((inNet = reader.readLine()) != null){
            writer.write(inNet+"\n");

        }

        writer.close();
    }
}
