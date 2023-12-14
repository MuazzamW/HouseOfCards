package com.houseOfCards.app;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        Trial_Runner runner = new Trial_Runner(100000);
        //Graph graph = new Graph();
        //graph.readDataLineByLine();
        //System.out.println(System.getProperty("java.class.path"));
        runner.playGame();

    
    }
}
 