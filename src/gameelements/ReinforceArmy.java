package gameelements;

import mapelements.Country;

import javax.swing.*;

/**
 * Created by liarthur on 2017/10/14.
 */
public class ReinforceArmy {

    public int  countryNumbers;
    public Player player;
    public String[] countryList;
    public String[] armyList;

    //计算并显示reinforce army数(chongwen)，card change（xueying已完成， 未联调），补齐三支，分配军队。（chongwen）
    public ReinforceArmy(Player player){
        this.player = player;
        this.countryNumbers = player.getCountries().size();


        this.countryList = new String[countryNumbers];
        int i = 0;
        for (Country country: player.countries) {
            countryList[i] = country.countryName;
            i++;
        }


        player.armies  = caculateArmyNumber(countryNumbers);

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

    private int caculateArmyNumber(int countryNumbers) {
        int armyNumbers;
        armyNumbers = Math.floorDiv(countryNumbers, 3);
        if (armyNumbers < 3){
            armyNumbers = 3;
        }

        return armyNumbers;
    }

    public void placeArmy(String countryName, int numberOfArmies){
        for (Country country: player.countries) {
            if(country.countryName.equals(countryName)){
                country.armyNumber += numberOfArmies;
            }
        }
    }

}
