package gameelements;
import mapelements.*;

import java.util.ArrayList;

public class Player {
    /**
     *   This class is to construct player;
     *   This class is to calculate how many armies are given according to numbers of cards.
     *   @Author Shirley / XUEYING LI
     *   @CreateTime 07.OCT.2017
     *   @param Name Name of player
     *   @param cards Store numbers of three cards, infantry, cavalry,artillery respectively
     *   @param changeCardTimes Store times of cards exchange
     *   @param countries Store countries of the player
     *   @param armies Store how many armies player have
     *   @param exchangeTime Store exchange times of players
     */
    String name = null;
    int[] cards = new int[3];
    int changeCardTimes = 0;
    ArrayList <Country> countries = new ArrayList<Country>();
    int armies;
    int exchangeTime;

    /**
     *   This method is a class constructor.
     *
     */

    public Player(String newName, int[] newCards, ArrayList<Country> newCountries,int NewArmies, int NewExchangeTime){
        name = newName;
        cards = newCards;
        countries = newCountries;
        armies = NewArmies;
        exchangeTime = NewExchangeTime;
    }

    // get name of player
    public String getName() {
        return name;
    }

    // get name of player
    public int[] getCards() {
        return cards;
    }

    //get Change Card Times
    public int getChangeCardTimes() {
        return changeCardTimes;
    }

    //get countries of player
    public ArrayList<Country> getCountries() {
        return countries;
    }

    //set cards
    public void setCards(int[] cards){
        this.cards = cards;
    }

    /**
     *  This method is to judge if player must exchange cards with armies.
     *
     *  @return true if player is forced to exchange cards
     */

    public boolean ifForceExchange(){
        if ((cards[0]+cards[1]+cards[2]) >= 5)
            return true;
        else
            return false;
    }

    /**
     *   To calculate how many armies after exchanging and change number of armies of player
     *
     */

    public void CalculateArmies(){
        int armies = this.exchangeTime*5;
        this.armies = this.armies+armies;
    }

    /*
     *   To test
     *
     *
    public static void main(String[] args){
        int [] cards = {3,2,2};
        ArrayList <Country> country = new ArrayList<Country>(3);
        Player shirley = new Player("shirley",cards,country,0,0);
        if(shirley.ifForceExchange()){
            ExchangeInteraction ei = new ExchangeInteraction();
            ei.GetAndSetCards(cards);
            ei.SetButtonLabel();
            shirley.cards = ei.getCards();
            shirley.exchangeTime = ei.count;
        }
        shirley.CalculateArmies();
        System.out.println(shirley.armies);
    */

    }

