package javaelevator;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

//TODO what if someone presses the call button? (called from a floor rep'd by a randomized value - in a separate thread at a random time.)
public class Elevator {
	private Integer currentIdx = 0;
	private Boolean[] floors;
	private boolean up = true;
	
	public Elevator(Integer floorCount) {
		floors = new Boolean[floorCount == null ? 32 : floorCount];
		Arrays.fill(floors, false);
	}

	private void delay(Integer millis) {
		for(int j=0;j<3;j++) try {
			Thread.sleep(millis / 3);
			System.out.print(".");
		} catch (InterruptedException e) { /* carry on */ }
	}
	
	private Integer move() {
		if(IntStream.range(0, floors.length-1).anyMatch(i -> floors[i])) { //don't move if there are no floors selected
			if(up) {
				Boolean hasHigherFloorSelected = IntStream.range(0, floors.length-1).anyMatch(i -> i>currentIdx && floors[i]);
				if(!hasHigherFloorSelected) {
					up = false;
				}
			}
			if(!up) {
				Boolean hasLowerFloorSelected = IntStream.range(0, floors.length-1).anyMatch(i -> i<currentIdx && floors[i]);
				if(!hasLowerFloorSelected) {
					up = true;
				}
			}
			System.out.printf("Going %s!\n", up ? "up" : "down");
			Integer inc = up ? 1 : -1;
			//Integer i=currentFloor + inc;
			for(; !floors[currentIdx]; currentIdx+=inc) {
				System.out.print(currentIdx+1);
				delay(750);
			}
			System.out.print("Almost there");
			delay(500);
			System.out.print("Stay on target");
			delay(500);
			floors[currentIdx] = false;
		}
		return currentIdx;
	}
	
	private void run() {
		System.out.println("Welcome to EleVADER! May the force be with you.");
		System.out.println(VADER);
		Scanner in = new Scanner(System.in);
		try {
			String nextLine = "";
			while(nextLine != null && !nextLine.equalsIgnoreCase("q")) {
				System.out.printf("Now on %dF. ", currentIdx+1);
				System.out.println("To which floor(s) may I take you? (Enter space-separated floor #(s), 'q' to quit, or just press 'enter' to continue) > ");
				nextLine = in.nextLine();
				if(nextLine.equalsIgnoreCase("q")) {
					System.out.println("Thank you for using EleVADER. Your journey to the dark side is complete.");
					System.out.println("");
					System.out.println(VADER);			
				} else {
					try {
						Arrays.stream(nextLine.split(" "))
							.map(Integer::valueOf)
							.forEach(newlySelectedFloor -> floors[newlySelectedFloor-1] = true);
						move();
					} catch(NumberFormatException ex) {
						if(nextLine == null || nextLine.trim().isEmpty()) {
							move();
						} else {
							System.out.println("Unknown floor - you have failed me for the last time.");
							System.out.println("Enter 1 or more whole numbers that represent floors where the EleVADER should stop.");
							System.out.println("Separate the numbers with a space.");
						}
					}
				}
				System.out.println("");
			}
		} finally {
			in.close();
		}
	}
	
	public static void main(String[] args) {
		Elevator elevator;
		try {
			elevator = new Elevator(Integer.valueOf(args[0]));
		} catch(NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException ex) {
			elevator = new Elevator(null);
		}
		elevator.run();
	}
    
  private static final String VADER =
		  "   _________________________________ \n" + 
		  "  |:::::::::::::;;::::::::::::::::::|  \n" + 
		  "  |:::::::::::'~||~~~``:::::::::::::| \n" + 
		  "  |::::::::'   .':     o`:::::::::::| \n" + 
		  "  |:::::::' oo | |o  o    ::::::::::| \n" + 
		  "  |::::::: 8  .'.'    8 o  :::::::::| \n" + 
		  "  |::::::: 8  | |     8    :::::::::| \n" + 
		  "  |::::::: _._| |_,...8    :::::::::| \n" + 
		  "  |::::::'~--.   .--. `.   `::::::::| \n" + 
		  "  |:::::'     =8     ~  \\ o ::::::::| \n" + 
		  "  |::::'       8._ 88.   \\ o::::::::| \n" + 
		  "  |:::'   __. ,.ooo~~.    \\ o`::::::| \n" + 
		  "  |:::   . -. 88`78o/:     \\  `:::::| \n" + 
		  "  |::'     /. o o \\ ::      \\88`::::| \n" + 
		  "  |:;     o|| 8 8 |d.        `8 `:::| \n" + 
		  "  |:.       - ^ ^ -'           `-`::| \n" + 
		  "  |::.                          .:::| \n" + 
		  "  |:::::.....           ::'     ``::| \n" + 
		  "  |::::::::-'`-        88          `| \n" + 
		  "  |:::::-'.          -       ::     | \n" + 
		  "  |:-~. . .                   :     | \n" + 
		  "  | .. .   ..:   o:8      88o       | \n" + 
		  "  |. .     :::   8:P     d888. . .  | \n" + 
		  "  |.   .   :88   88      888'  . .  | \n" + 
		  "  |   o8  d88P . 88   ' d88P   ..   | \n" + 
		  "  |  88P  888   d8P   ' 888         | \n" + 
		  "  |   8  d88P.'d:8  .- dP~ o8       | \n" + 
		  "  |      888   888    d~ o888       | \n" + 
		  "  |_________________________________| \n";
		  
}
