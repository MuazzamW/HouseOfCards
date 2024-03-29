package com.houseOfCards.app;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.io.File;

import com.opencsv.*;

public class SheetHandler {

    private static URL csvURL = SheetHandler.class.getClassLoader().getResource("trials.csv");
    
    private FileWriter writer;
    private FileReader reader;
    public static File csv = new File(csvURL.getFile());
    private CSVWriter csvWriter;

    public SheetHandler(){

        try{
            writer = new FileWriter(csv);
            reader = new FileReader(csv);
            csvWriter = new CSVWriter(writer);
        }catch(IOException e){
            System.out.println("Path does not exist!");
        }
        
        

    }

    public void readDataLineByLine(){
        try {  
            reader = new FileReader(csv); 
  
        CSVReader csvReader = new CSVReader(reader); 
        String[] nextRecord; 

        while ((nextRecord = csvReader.readNext()) != null) { 
            for (String cell : nextRecord) { 
                System.out.print(cell + "\t"); 
            } 
            System.out.println(); 
        }
        csvReader.close(); 
    } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }
        
    }

    public void writeToCSV(String[] args) throws IOException{
        csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(args);    
    }

    public void clearCsv() throws Exception {
        FileWriter fw = new FileWriter(csv,false); 
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
    }

    public void closeWriter() throws IOException{
        csvWriter.close();
    }

    public static URL getCSVURL(){
        return csvURL;
    }

    
}
