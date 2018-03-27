import java.util.Random;

public class Environment {		//environment class to store useful information about the environment

	public int rows;
	public int columns;

	public float[][] rewards; //2d array of reward based on the rows and columns.
	
	public Environment(int rows, int columns, float[][] rewards) { //constructor for the Environment
		this.rows = rows;
		this.columns = columns;
		this.rewards = rewards;
	}
	
	public Environment(int size) {							//randomly generates an square environment of size X size.
		
		this.rows = size;
		this.columns = size;
		this.rewards = new float[size][size];
		
		Random rand = new Random();
		
		for (int row = 0;row<size;row++){
			for(int col = 0;col<size;col++) {
				switch(rand.nextInt(4)) {
				case 0:
					if (rand.nextInt(1)==0) {
					rewards[row][col] = AgentApp.AWALLSQ;
					} else {
					rewards[row][col] = AgentApp.EMPTYSQ;
					}
					break;
				case 1:
					if (rand.nextInt(3)==0) {
					rewards[row][col] = AgentApp.GREENSQ;
					}else {
					rewards[row][col] = AgentApp.EMPTYSQ;
					}
					break;
				case 2:
					if (rand.nextInt(3)==0) {
					rewards[row][col] = AgentApp.BROWNSQ;
					} else {
					rewards[row][col] = AgentApp.EMPTYSQ;
					}
					break;
				case 3:
					rewards[row][col] = AgentApp.EMPTYSQ;
					break;
				}
				
			}
		}
		
		
	}
	
	void PrintEnvironment() {										//prints an environment.
																	// o represents a green square
																	// x represents a brown square
		System.out.println("Printing Environment....");				// - represents a wall and
																	// if it is a space, it is an empty square
		for (int row = 0;row<this.rows;row++){
			for(int col = 0;col<this.columns;col++) {
				
				if (rewards[row][col] == AgentApp.AWALLSQ) {
					System.out.print(" - ");
				}
				else if(rewards[row][col] == AgentApp.GREENSQ) {
					System.out.print(" o ");
				}else if(rewards[row][col] == AgentApp.BROWNSQ) {
					System.out.print(" x ");
				}else if(rewards[row][col] == AgentApp.EMPTYSQ) {
					System.out.print("   ");
				}
				
				System.out.print("|");
			}
			System.out.println();
		}
	}
	
}
