package gamemodels;

import java.util.Scanner;

/**
 * This is a view class, that allow player to prepare dices in the following attack and defense.
 */
public class DicePreparation {

	public static int attacker_dices=0;
	public static int defender_dices=0;


	/**
	 * This is to test
	 * @param args
	 */
	public static void main(String[] args) {
		
		RiskDice risk_dice=new RiskDice();

		Scanner attack_scanner=new Scanner(System.in);
		Scanner defense_scanner=new Scanner(System.in);
		
		System.out.println("Input the number of dices, attacker:");
		attacker_dices=attack_scanner.nextInt();
		attack_scanner.nextLine();
		
		System.out.println("Input the number of dices, defender:");
		defender_dices=defense_scanner.nextInt();
		defense_scanner.nextLine();
		
		System.out.println(attacker_dices+"  "+defender_dices);
		
		//risk_dice.startDicing(attacker, num_attacker, defender, num_defender) startDicing();
		
		attack_scanner.close();
		defense_scanner.close();
	}

}
