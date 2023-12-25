package com.houseOfCards.app;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) throws Exception{
        /*
        int trialNumber = 100000;
        Trial_Runner runner = new Trial_Runner(trialNumber);
        runner.playGame();
        */

        SwingUtilities.invokeAndWait(()->{  
        Graph example=new Graph("Bar Chart Window", "Winning Cards vs Frequency", "Outcome", "Frequency", 100000);  
        example.setSize(800, 400);  
        example.setLocationRelativeTo(null);  
        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        example.setVisible(true);  
    });
    
    }
}
 