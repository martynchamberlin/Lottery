/**
 * This program lets you play the lottery!
 * 
 * Student name: D. Martyn Chamberlin
 * Due date: 4/14/14
 *
 */

import java.util.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int user_input = 0;
		Scanner input = new Scanner(System.in); 
		Game game; // polymorphism! 

		do
		{
			System.out.println( "Which game would you like to play?"
					+ "\n1. PowerBall "
					+ "\n2. MegaMillion"
					+ "\n3. HotLotto"
					+ "\n4. Exit");
			user_input = input.nextInt();
			switch (user_input)
			{
				case 1: 
					game = new Powerball();
					game.play();
					break;
				case 2: 
					game = new MegaMillion();
					game.play();
					break;
				case 3: 
					game = new HotLotto();
					game.play();
					break;
				case 4:
					break;
				default: 
					System.out.println("Please choose a number between 1 and 4!");
			}
		} while (user_input != 4);
	}
}
