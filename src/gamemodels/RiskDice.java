package gamemodels;

import java.util.Random;

/**
 * This is a dice model class, which is a part of attack phase.
 * 
 * <p>When this class is invoked, a random number will be provide<br>
 * by the method startDice</p>
 */
public class RiskDice {

	int[] array_attacker_dice;
	int[] array_defender_dice;
	final String RESILT_WIN="win";
	final String RESULT_LOSE="lose";
	
	/**
	 * This method will perform entire dicing process between attacker and defender.
	 * <p>A win or lose result will be provided to player, showing the result of current combat.</p>
	 * @param attacker The attack player
	 * @param defender The defend player
	 * @param num_attacker The dice number of attacker
	 * @param num_defender The dice number of defender
	 * @return combat result
	 */
	public String[] startDicing(PlayerModel attacker, int num_attacker, 
			PlayerModel defender, int num_defender){
		//test only-provide primary value
		DicePreparation dice_preparation=new DicePreparation();
		int[] temp_att;
		int[] temp_def;	//test end
		
		//num of dices in this combat
		array_attacker_dice=new int[dice_preparation.attacker_dices];
		array_defender_dice=new int[dice_preparation.defender_dices];
		//result of combat
		String[] combat_result=new String[num_defender];
		
		if(diceValidation(attacker, num_attacker, defender, num_defender)){
			//save the result if dicing
			for(int i=0;i<array_attacker_dice.length;i++){
				array_attacker_dice[i]=rollDice();
				System.out.println(array_attacker_dice[i]);
			}
			
			for(int j=0;j<array_defender_dice.length;j++){
				array_defender_dice[j]=rollDice();
				System.out.println(array_defender_dice[j]);
			}

			temp_att=sortDice(array_attacker_dice);

			temp_def=sortDice(array_defender_dice);

			for(int i=0;i<num_defender;i++){
				if(temp_att[temp_att.length-1]>temp_def[temp_def.length-1]){
					combat_result[i]=RESILT_WIN;
				}else{
					combat_result[i]=RESULT_LOSE;
				}
			}
			
		}
		return combat_result;
	}
	
	/**
	 * This is a method, which provide random number that represent the result of dice.
	 * 
	 * @return temp_counter The int value, which is the result of current dicing.
	 */
	public int rollDice(){
		Random random=new Random();
		int temp_counter=random.nextInt(6)+1;
		
		return temp_counter;
	}
	
	/**
	 * This is a validation method that validate the input dices are legal or not.
	 * @param attacker	Object if current attacker
	 * @param num_attacker	Number of dices used by attacker.
	 * @param defender	Object of current defender
	 * @param num_defender	Number of dices used by defender.
	 * @return	true or false	input dices pass or fail.
	 */
	public boolean diceValidation(PlayerModel attacker, int num_attacker, 
			PlayerModel defender, int num_defender){
		/*
		 * Instruction:
		 * 1. Maximum number of dice for attacker is three; for defender are two. 
		 * 	Minimum number of dice for both is one.
		 * 2. Number of dice can not more than the armies that current country have.
		 * 3. If attacker's dice larger than defender, attacker win the combat. Otherwise
		 * 	defender will win the combat.
		 */
		
		int temp_attacker_armies=attacker.getTotalArmies()-1;
		int temp_defender_armies=defender.getTotalArmies();
		
		if(num_attacker>3||num_attacker<=0||num_defender>2||num_defender<0){
			return false;
		}else if(num_attacker>temp_attacker_armies||num_defender>temp_defender_armies){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * This is a sort method, which can process result of dices into positive-sequence.
	 * @param arr The int array for the result from last attack
	 * @return arr A sorted array for dice result.
	 */
	public int[] sortDice(int[] arr){
		
		for(int i=0;i<arr.length;i++){
			int numTemp=i;
			for(int j=numTemp+1;j<arr.length;j++){
				//figure out minimum num
				if(arr[j]<arr[numTemp]){
					//record minimum num
					numTemp=j;
				}
			}
			//first round sort finished
			if(i!=numTemp){
				int tempArr=arr[i];
				arr[i]=arr[numTemp];
				arr[numTemp]=tempArr;
			}
		}
		return arr;
	}

}
