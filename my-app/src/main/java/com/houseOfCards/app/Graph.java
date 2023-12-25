package com.houseOfCards.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultCategoryDataset;

import com.opencsv.*;

public class Graph extends JFrame {  
  

  private double numTrials; 
  private String chartTitle; 
  private HashMap<Integer, Integer> dataSet;
  private final URL csvURL;
  private final File csvFile;
  private CSVReader csvReader;

  private static final long serialVersionUID = 1L;
  
  
  public Graph(String windowTitle, String title, String xAxisTitle, String yAxisTitle, double numTrials) {  
    super(windowTitle);
    this.numTrials = numTrials;
    this.chartTitle = title; 
    this.dataSet = new HashMap<Integer, Integer>();
    this.csvURL = SheetHandler.getCSVURL();
    this.csvFile = new File(csvURL.getFile());
    try{
        this.csvReader = new CSVReader(new FileReader(csvFile));
    }catch(FileNotFoundException e){
        System.out.println("File not found");
    }
     
    // Create Dataset  
    CategoryDataset dataset = createDataset();  
      
    //Create chart  
    JFreeChart chart=ChartFactory.createBarChart(  
        chartTitle, //Chart Title  
        xAxisTitle, // Category axis  
        yAxisTitle, // Value axis  
        dataset,  
        PlotOrientation.VERTICAL,  
        true,true,false  
       );  
  
    ChartPanel panel=new ChartPanel(chart);  
    setContentPane(panel);
    createDataset();  
  }
  
  private void collectFrequencies(){
    try {  
        String[] nextRecord; 

        while ((nextRecord = csvReader.readNext()) != null) {
            if(!dataSet.containsKey(Integer.parseInt(nextRecord[2]))){
                dataSet.put(Integer.parseInt(nextRecord[2]),0);
            }else{
                dataSet.computeIfPresent(Integer.parseInt(nextRecord[2]), (k, v) -> v + 1);
            }
        }
        csvReader.close(); 
    } 
    catch (Exception e) { 
        e.printStackTrace(); 
    }
  }
  
  private CategoryDataset createDataset() {  
    collectFrequencies();
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
    
    for (Map.Entry<Integer, Integer> entry : dataSet.entrySet()) {
        Integer outcome = entry.getKey();
        Integer frequency = entry.getValue();
        System.out.println(String.format("%d, %d",outcome,frequency));
        dataset.addValue(frequency/numTrials, outcome, outcome);
    }   
  
    return dataset;  
  }   
} 