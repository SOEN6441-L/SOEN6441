package gameelements;
import mapelements.*;

import java.util.ArrayList;

public class Player {
    /*
    *   This class is to construct player;
    *   This class is to calculate how many armies are given according to numbers of cards.
    *   @Shirley / Xueying Li
    *   @07.OCT.2017
    *
     */
    String name = null;
    int[] cards = new int[3];
    int changeCardTimes = 0;
    ArrayList <Country> countries = new ArrayList<Country>();
    int count = 5;
    int armies;
    int exchangeTime;
    int turn;

    /*
    *   This method is a class constructor.
     */

    public Player(String newName, int[] newCards, ArrayList<Country> newCountries,int NewArmies, int exchangeTime,int CurrentTurn){
        name = newName;
        cards = newCards;
        countries = newCountries;
        armies = NewArmies;
        turn = CurrentTurn;
    }

    public String getName() {
        return name;
    }

    public int[] getCards() {
        return cards;
    }

    public int getChangeCardTimes() {
        return changeCardTimes;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCards(int[] cards){
        this.cards = cards;
    }

    /*
    *   This method is to judge if player must exchange cards with armies.
     */

    public boolean ifForceExchange(){
        if ((cards[0]+cards[1]+cards[2]) >= 5)
            return true;
        else
            return false;
    }

    /*
    *   This method is to determine if it is legal to exchange.
     */


    public static void main(String[] args){
        int exchangeTime;
        int [] cards = {3,2,2};
        ArrayList <Country> country = new ArrayList<Country>(3);
        Player shirley = new Player("shirley",cards,country,0,0,0);
        if(shirley.ifForceExchange()){
            ExchangeInteraction ei = new ExchangeInteraction();
            ei.GetAndSetCards(cards);
            ei.SetButtonLabel();
            cards = ei.getCards();
            exchangeTime = ei.count;
        }
        shirley.armies = shirley.turn * 5 * shirley.exchangeTime;

    }
}
