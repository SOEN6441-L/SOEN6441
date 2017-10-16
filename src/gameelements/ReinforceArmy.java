package gameelements;

import mapelements.Country;

import javax.swing.*;

/**
 * The class of ReinforceArmy phase
 */

public class ReinforceArmy {

    public int  countryNumbers;
    public Player player;
    public String[] countryList;
    public String[] armyList;


    /**
     * The constructor of class ReinforceArmy
     * @param player The Player that is under the turn
     */
    public ReinforceArmy(Player player){
        this.player = player;
        this.countryNumbers = player.getCountries().size();




        this.countryList = new String[countryNumbers];
        int i = 0;
        for (Country country: player.countries) {
            countryList[i] = country.countryName;
            i++;
        }


        player.armies  = calculateArmyNumber(countryNumbers);


        this.armyList = new String[player.armies];
        for (int x = 0; x< player.armies; x++){
            armyList[x] = String.valueOf(x+1);
        }



        while(player.armies > 0){
            Object selectedCountry = JOptionPane.showInputDialog(null, "Choose Country",
                    "Input", JOptionPane.INFORMATION_MESSAGE, null, countryList,
                    countryList[0]);

            Object selectedArmyNumber = JOptionPane.showInputDialog(null, "Choose Country",
                    "Input", JOptionPane.INFORMATION_MESSAGE, null, armyList,
                    armyList[0]);
            int armyNumberSelect = Integer.parseInt((String) selectedArmyNumber);
            placeArmy((String)selectedCountry, armyNumberSelect);

            player.armies -= armyNumberSelect;

        }

    }

    /**
     * The function to calculate how many armies player get this turn
     * @param countryNumbers
     * @return the number of armies
     */
    private int calculateArmyNumber(int countryNumbers) {
        int armyNumbers;
        armyNumbers = Math.floorDiv(countryNumbers, 3);
        if (this.player.ifForceExchange()){
            changeCard();
        }else{
            String[] changeOrNot = new String[2];
            changeOrNot[0] = "Yes";
            changeOrNot[1] = "No";
            Object change = JOptionPane.showInputDialog(null, "Do you want to change cards?",
                    "Input", JOptionPane.INFORMATION_MESSAGE, null, armyList,
                    changeOrNot[0]);
            if (change.equals("Yes")){
                changeCard();
            }

        }
        if (armyNumbers < 3){
            armyNumbers = 3;
        }

        return armyNumbers;
    }

    /**
     * The funtion to let player to place the armies
     * @param countryName the name of the country player want to place army on
     * @param numberOfArmies the number of the army player want to place
     */
    public void placeArmy(String countryName, int numberOfArmies){
        for (Country country: player.countries) {
            if(country.countryName.equals(countryName)){
                country.armyNumber += numberOfArmies;
            }
        }
    }

    /**
     * This function is for player to change cards to armies
     */
    public void changeCard(){
        ExchangeInteraction ei = new ExchangeInteraction();
        ei.GetAndSetCards(this.player.cards);
        ei.SetButtonLabel();
        this.player.exchangeTime = ei.count;
        this.player.cards = ei.cards;
        this.player.CalculateArmies();
    }

}
