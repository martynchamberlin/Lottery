
import java.util.*;

public abstract class Game implements GameInterface {

	protected int rangeSelect, rangePowerSelect, multiplier, num_matching, power, output;
	protected String powerName, bonus;
	protected boolean using_bonus;
	
	protected List<Integer> chosenNumbers = new ArrayList<Integer>(); 
	protected List<Integer> chosenPowerNumber = new ArrayList<Integer>(); 
	protected List<Integer> winningNumbers = new ArrayList<Integer>();
	protected List<Integer> winningPowerNumber = new ArrayList<Integer>();
	
	public void play() 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Do you want to pick the lucky numbers yourself, or "
				+ "would you rather the computer generate them?"
				+ "\n1. I'd rather pick them."
				+ "\n2. Have the computer do it.");
		int num = input.nextInt();
		switch (num)
		{
		case 1:
			let_the_user_pick_their_numbers( chosenNumbers, 5, rangeSelect );
			let_the_user_pick_their_numbers( chosenPowerNumber, 1, rangePowerSelect );
			break;
		case 2:
		default:
			have_computer_generate_numbers( chosenNumbers, 5, rangeSelect );
			have_computer_generate_numbers( chosenPowerNumber, 1, rangePowerSelect );
		}
		
		System.out.printf("To increase potential winnings, would you like to add %s for just $1?\n", bonus);
		System.out.println("1.Yes \n2.No");
		int increase_earnings = input.nextInt();
		switch ( increase_earnings )
		{
		case 1:
			using_bonus = true;
			break;
		case 2:
		default:
			using_bonus = false;
			break;
		}
		
		// Now that we have the user numbers, let's generate the winning numbers
		have_computer_generate_numbers( winningNumbers, 5, rangeSelect );
		have_computer_generate_numbers( winningPowerNumber, 1, rangePowerSelect );
		
		// Drum roll... let the user see what's happened!
		System.out.printf("You chose the numbers %s. "
				+ "Your %s was %s.\n", printNumbers( chosenNumbers ), powerName, printNumbers(chosenPowerNumber) );
		
		System.out.printf("The winning numbers are %s "
				+ "and the %s is %s.\n", printNumbers( winningNumbers ), powerName, printNumbers(winningPowerNumber) );
		
		// set the stage for determining the amount of money lost
		output = 0;
		num_matching = num_matching_nums( chosenNumbers, winningNumbers );
		power = num_matching_nums( chosenPowerNumber, winningPowerNumber );
		// The next part is too specific to each game to be able to abstract it in this base class.
		// Time to "code in the specific" here
		int earnings = calculateEarnings() * multiplier;

		if ( earnings > 0 )
		{
			System.out.printf("Congratulations! You win %d.\n", earnings );
		}
		else
		{
			System.out.println("Alas, you lost all your money on that one.");
		}
		// No reason to show the menu to play again too quickly. User needs to meditate on the amount 
		// of money they just lost.
		try
		{
			Thread.sleep( 2000 );
		}
		catch(InterruptedException E)
		{
			System.out.println("This thread is having a hard time getting to sleep!");
		}
		
		// Clear the lists since the user may want to play again
		chosenNumbers.clear();
		chosenPowerNumber.clear();
	}

	protected void let_the_user_pick_their_numbers( List<Integer> rangeChosen, int num_nums_to_generate, int size_of_pool )
	{		
		Scanner input = new Scanner(System.in);
		// This variable stores the legitimacy of the last piece of data input by the user
		boolean user_did_select_a_valid_number = true;
		char plural = '\0'; // set char to empty
		if ( num_nums_to_generate > 1 )
		{
			plural = 's';
		}
		do {
			if ( rangeChosen.size() == 0 )
			{
				System.out.printf("Select %d number%c between 1 and %d for the %s.\n", num_nums_to_generate, plural, size_of_pool, powerName);
			}
			else if ( user_did_select_a_valid_number )
			{
				System.out.printf("Great! Please select another number between 1 and %d.\n", rangePowerSelect);
			}
			int num =  input.nextInt();
			if ( num >= 1 && num <= size_of_pool)
			{
				if ( ! rangeChosen.contains( num ) )
				{
					rangeChosen.add( num );
					user_did_select_a_valid_number = true;
				}
				else
				{
					System.out.println("Oops, you've already picked that number!");
					user_did_select_a_valid_number = false;
				}
			}
			else
			{
				System.out.println("Oops! That number was out of range. Please try again.");
				user_did_select_a_valid_number = false;
			}
		} while (rangeChosen.size() < num_nums_to_generate );
	}

	/** 
	 * This function takes a list of integers and populates it with random 
	 * numbers, starting with zero and ending with @param num_nums. 
	 * It is important that no two random numbers are the same. You could check this 
	 * via recursion (and discard randomly generated numbers that had already been 
	 * generated from a previous loop) but Greg told me of this other solution and I
	 * feel it is superior to what I had planned. It essentially involves creating a 
	 * pool of numbers and removing from that pool a number at a random index. 
	 * This guarantees that no two random numbers are the same.
	 */
	protected void have_computer_generate_numbers( List<Integer> putThemInHere, int num_nums_to_generate, int size_of_pool )
	{
		// First, populate our pool of numbers
		List<Integer> number_of_numbers = new ArrayList<Integer>();
		for ( int i = 1; i <= size_of_pool; i++ )
		{
			number_of_numbers.add( i );
		}

		// Now generate our random numbers and populate them into the list
		do
		{
			int rand_index = getRandom( 0, number_of_numbers.size()); // decrement start and finish by 1 since indexes are 0 to n-1
			int rand = number_of_numbers.remove( rand_index );
			putThemInHere.add( rand );
		} while (putThemInHere.size() < num_nums_to_generate );
	}

	public String printNumbers( List<Integer> nums )
	{
		String output = "";
		for( int i = 0; i < nums.size(); i++ )
		{
			output += nums.get( i );
			if ( i + 1 < nums.size() )
			{
				output += ", ";
				if ( i + 2 == nums.size() )
					output += "and ";
			}
		}
		return output;
	}
	
	public int num_matching_nums( List<Integer> set1, List<Integer> set2 )
	{
		int num = 0;
		for (int i = 0; i < set1.size(); i++ )
		{
			if ( set2.contains( set1.get(i)))
			{
				num++;
			}
		}
		return num;
	}
	
	public int getRandom( int start, int finish )
	{
		// if start = 1 and finish = 10, the number could be anywhere from 
		// 1 to 10. 
		return new Random().nextInt( finish ) + start;
		
	}
}

