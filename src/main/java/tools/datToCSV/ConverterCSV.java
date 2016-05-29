package tools.datToCSV;

import java.io.*;

/**
 * Created by Timo on 01.05.16.
 */
public class ConverterCSV {
    public static String inputPath = "data/input/users.dat";
    public static String outputPath = "data/input/users.csv";

    public static String regexVerticalBars = " {0,}\\| {0,}";
    public static String regexLeadingSpaces = "^\\s+";
    public static String regexTrailingSpaces = "\\s+\\Z";

    public static void main(String[] args) throws IOException {
        ConverterCSV c = new ConverterCSV();
        c.convert();
//        c.outputFile();
    }

    public void convert() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(inputPath)));
        BufferedWriter writer = new BufferedWriter ( new FileWriter (new File(outputPath)));

        String in;
        String out;
        int rowNum = 1;
        while((in = reader.readLine()) != null){

            if( rowNum != 2){
                out = in.replaceAll(regexVerticalBars,";");
                out = out.replaceFirst(regexLeadingSpaces, "");
                out = out.replaceFirst(regexTrailingSpaces, "");
                writer.write(out+"\n");
            }


            rowNum++;
        }

        writer.close();
    }


    public void outputFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(outputPath)));

        String in;
        while((in = reader.readLine()) != null){
            System.out.println(in);
        }
    }
}
