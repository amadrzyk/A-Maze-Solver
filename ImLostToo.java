import java.io.*;

/**
 * This class takes in a maze text file and solves the maze using the A* algorithm. It then 
 * prints out whether or not it found an end tile, the number of steps it took to get to 
 * the end tile, the number of tiles still in the priority queue, and the number of steps taken.
 * 
 * @author alexmadrzyk
 */

public class ImLostToo {
	
	// CLASS VARIABLES
	private static Maze maze;  						// The maze variable is available everywhere within this class
	private static  boolean foundEndTile = false;	// Turns to true when an end tile is found
	private static int steps = 0;					// Number of steps to the end tile
	private static int totalSteps = 0;				// Total number of steps taken
	
	/**
	 * This method calculates the priority of the hexagon tile based on the A* algorithm. 
	 * It's basically f(x) = g(x) + h(x), where x is a Hexagon, f(x) is a priority of x, 
	 * g(x) is how far x is from the start (look at stepsToMe), and h(x) is some
	 * heuristic - an estimated value for how far x is from the end.
	 * @param currentHex The hexagon that is going to be evaluated (the x in the equation)
	 * @return returns the priority of the tile (as a double)
	 */
	private static double findPriority (Hexagon currentHex){
		double priority = 0.00;
		double f_x;
		double g_x = currentHex.getSteps();
		double h_x = currentHex.distanceToEnd(maze);
		
		/* 
		* Note: I noticed that this algorithm is more efficient when the amount of steps 
		* taken by the program isn't used in the calculation. 
		*/
		f_x = g_x + h_x;
		
		/* 
		* WHY THE NEGATIVE?
		* In this example, the lower the f(x) value, the higher the priority (my implementation 
		* of PriorityQueueADT puts the highest priority at the start of the queue, naturally).
		*
		* To achieve this, I'm going to multiply every f(x) value by -1, so that the smaller f(x) values 
		* have a higher value (and therefore a higher priority) compared to the the larger f(x) values, 
		* who now have a lower value (and therefore a lower priority). Now when I enqueue a hexagon based on 
		* its priority, the least negative value (the smallest f(x)) will be at the front of the queue.
		*/
		priority = -(f_x);
		
		return priority;
	}
	
	
	public static void main (String[] args) throws InterruptedException {
		try {
			if(args.length < 1){
				throw new IllegalArgumentException("Please provide a Maze file as a command line argument.");
			}
			
			// Creating maze object reference
			maze = new Maze(args[0]);
			
			// Creating a hexagon reference, setting as first tile
			Hexagon startHexagon = maze.getStart();
			startHexagon.setSteps(0);
			Hexagon currentHexagon = startHexagon;
			
			// Creating queues
			PriorityQueueADT<Hexagon> priorityQueue = new LinkedPriorityQueue<Hexagon>(); 	// This is a priority queue with "open" (unevaluated) tiles
			priorityQueue.enqueue(startHexagon, findPriority(startHexagon));
			
			// Loop until the end tile is found or until the priority queue is empty
			do {
				// Quit program if current tile is the end tile
				if (currentHexagon.isEnd()){
					currentHexagon.setFinished();
					foundEndTile = true;
				} else {
		            // First update the current tile's color, as it will be changed soon
		            if (currentHexagon.isStart()){
						currentHexagon.setStarted();
					} else {
						currentHexagon.setDequeued();
					}
		            
					// Set current hexagon to tile with highest priority
					currentHexagon = priorityQueue.dequeue();
					currentHexagon.setCurrent();
		            
					// Queue each neighboring tile into priority queue, if possible
					for (int i = 0; i <= 5; i++){
						Hexagon neighbor = currentHexagon.getNeighbour(i);
						
						// If neighboring tile is traversable
						if (neighbor != null && !neighbor.isDequeued() && !neighbor.isEnqueued() && !neighbor.isWall()){
							// Queue neighbor based on priority
							neighbor.setSteps(steps);
							neighbor.setEnqueued();
							priorityQueue.enqueue(neighbor, findPriority(neighbor));
							totalSteps++;
						}
					}
					
					// Adding a delay for visual effect
		            Thread.sleep(50);
		     
					// Update steps values
					steps++;
					totalSteps++;
				}
				// Each time through we need to update the maze window with repaint()
				maze.repaint();
				
			} while(!foundEndTile && !priorityQueue.isEmpty());
			
			/*
			 *  Once the program searches the maze using the above algorithm,
			 * 	it will print out the following using a System.out.println or .format:
			 */
			
			// Checks if the end was found, or if it was not found
			if (priorityQueue.isEmpty()){
				System.out.println("The end tile was not found.");
			} else if (foundEndTile){
				System.out.println("The end tile was found!");
				// Number of steps that it took to finish
				System.out.format("It took %d steps to find the end tile.\n", steps);
			}
			
			// How many tiles were still in the priority queue
			System.out.format("There were %d tiles still in the priority queue.\n", priorityQueue.size());
			
			// Number of steps taken in total (all enqueueing and dequeueing operations)
			System.out.format("This program took a total of %d steps.\n", totalSteps);
		} 
		
		/* 
		 * VARIOUS CATCH BLOCKS
		 */
		catch(IllegalArgumentException e) {
			System.out.println("A method has been passed an illegal or inappropriate argument:\n\t" + e.getMessage());
		}		
		catch(FileNotFoundException f){
			System.out.println("An attempt to open the file denoted by the specified pathname has failed:\n\t" + f.getMessage());
		}
		catch(IOException g){
			System.out.println("There was a file IO error: \n\t" + g.getMessage());
		}
		catch(UnknownMazeCharacterException h){
			System.out.println("This maze encountered a character during Maze building that it cannot recognize:\n\t" + h.getMessage());
		}
		catch(InvalidNeighbourIndexException i){
			System.out.println("There was an attempted access outside valid hexagon neighbor index range. Since it is a Hexagon, "
					+ "it can only have 6 neighbors (0-5 inclusive): \n\t" + i.getMessage());
		}
		catch(EmptyCollectionException j){
			System.out.println(j.getMessage());
		}	
	}
}
