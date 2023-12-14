package com.houseOfCards.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;



class Card{
        
    private final String suit;
    private final String rank;
    
    
    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }
    
    public String getSuit(){
        return suit;
    }

    public String getRank(){
        return rank;
    }
    
    @Override
    public String toString() {
        return "Card [suit=" + suit + ", rank=" + rank + "]";
    }

    public static boolean areTheSameCard(Card card1, Card card2){
        return (card1.getSuit()==card2.getSuit() && card1.getRank() == card2.getRank()) ? true:false;
    }
    
}

class Deck{
    
    private static final String suits[] = {"C", "D", "H", "S"};
    private static final String ranks[] = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    public ArrayList<Card> cards;
    
    public Deck(){
        cards = new ArrayList<Card>();
        for (int i = 0; i<suits.length; i++) {
            for(int j=0; j<ranks.length; j++){
                this.cards.add(new Card(suits[i],ranks[j]));
            }
        }  

        //Shuffle after the creation
        Collections.shuffle(this.cards);
        
    }

    private List<Card> getMatch(String suit, String rank){
        return this.cards.stream().filter(card -> suit.equals(card.getSuit()) && rank.equals(card.getRank())).collect(Collectors.toList());
    }

    public boolean exists (String suit, String rank) {
        return getMatch(suit, rank).size()>0;
    }

    public void removeCard(String suit, String rank){
        this.cards.remove(getMatch(suit, rank).get(0));
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public List<Card> deal(int numCards){
        shuffle();
        return cards.subList(0, numCards);        
    }

    public void addBack(List<Card> cardsToAdd){
        cards.addAll(cardsToAdd);
        shuffle();
    }

    public int getSize(){
        return cards.size();
    }

}

class Tray{

    private ArrayList<Card> trayCards;

    public Tray(){
        this.trayCards = new ArrayList<Card>();
    }

    public void shuffle(){
        Collections.shuffle(trayCards);
    }

    public void addToTray(List<Card> cards){
        trayCards.addAll(cards);
    }

    public List<Card> deal(int numCards){
        return trayCards.subList(0, numCards);
    }

    public void empty(){
        trayCards.clear();
    }

}


public class Trial_Runner 
{
    private final int TRIALS;
    private final Deck deck;
    private final int WAGER = 50000;
    private Random rand = new Random();
    private Tray tray;
    private ArrayList<Card> finalHand;
    private int matches;
    private int earnings;
    private int netGain;
    //private SheetHandler writer;
    SheetHandler handler;

    public static final ArrayList<Card> WINNING_CARDS = new ArrayList<>(Arrays.asList(
    new Card("S", "A"),
    new Card("S", "2"),
    new Card("S", "3"),
    new Card("S", "4"),
    new Card("S", "5"),
    new Card("S", "6")
    ));

    private final Map<Integer, Integer> payout = new HashMap<Integer, Integer>() {{
        put(0, 0);
        put(1, 20000);
        put(2, 50000);
        put(3, 100000);
        put(4, 250000);
        put(5, 2500000);
        put(6, 250000000);
    }};

    public Trial_Runner(int trials) throws IOException{

        this.TRIALS = trials;
        deck = new Deck();
        finalHand = new ArrayList<Card>();
        tray = new Tray();
        handler = new SheetHandler();

    }

    private void runTrial() throws IOException{ //return game details
        matches = 0;
        earnings=0;
        netGain=0;
        int spin1 = rand.nextInt(6)+1;
        int spin2 = (rand.nextInt(6)+1)*3+9;

        for(Card card:WINNING_CARDS){
            if(deck.exists(card.getSuit(), card.getRank())){
                deck.removeCard(card.getSuit(), card.getRank());
            }
        }

        tray.addToTray(WINNING_CARDS);
        tray.addToTray(deck.deal(spin2-WINNING_CARDS.size()));

        tray.shuffle();

        finalHand.addAll(tray.deal(spin1));

        matches=numMatches();
        earnings = payout.get(matches);
        netGain = earnings-WAGER;
        System.out.println(String.format("%d, %d, %d, %d", spin1,spin2,matches,earnings));

        deck.addBack(WINNING_CARDS);
        tray.empty();
        finalHand.clear();
        int[] nums = {spin1, spin2, matches, earnings, netGain};
       
        String[] line = Arrays.stream(nums) 
            .mapToObj(String::valueOf) 
            .toArray(String[]::new);
    
        
        
        System.out.println(line);
        handler.writeToCSV(line);
        
    }

    private int numMatches(){//error here
        System.out.println(finalHand);
        return finalHand.stream().filter(card -> WINNING_CARDS.contains(card)).collect(Collectors.toList()).size();
    }

    public void playGame() throws IOException{
        for(int i = 0; i< TRIALS; i++){
            runTrial();
        }
        handler.closeWriter();
    } 
    
}
