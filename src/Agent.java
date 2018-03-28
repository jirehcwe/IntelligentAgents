import java.util.Random;

public class Agent {	
	
	public enum Action {				//defining all actions the agent can take.
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}
		
	private TransitionModel tModel;   	//definining attributes of the agent: Transition Model and Environment the agent is in
	private Environment env;
		
	//utility values of the environment
	float[][] uOriginal;
		
	
	//initialise arbitary initial policy
	Action[][] optimalPolicy;
	
	Grapher graphics;
	
	
	//constructor with environment and transition model input.
	public Agent(TransitionModel tModel, Environment env) {
		this.tModel = tModel;
		this.env = env;
		this.graphics = new Grapher(this.env);
		this.optimalPolicy = new Action[env.rows][env.columns];
		
		uOriginal = new float[env.rows][env.columns];
		
		for (int col = 0; col < env.columns; col++) {
			for (int row = 0; row < env.rows; row++) {					//initialise all utility values to initial 0
				uOriginal[row][col] = 0;
			}
		}
			
		
			
	}
	
	Environment GetEnvironment() {
		return this.env;
	}
	
	void GenerateRandomPolicy() {					//random policy is generated
		for (int col = 0; col < env.columns; col++) {
			for (int row = 0; row < env.rows; row++) {
				
				Random rand = new Random();
				switch(rand.nextInt(4)) {
				case 0:
					this.optimalPolicy[row][col] = Action.UP;
					break;
				case 1:
					this.optimalPolicy[row][col] = Action.DOWN;
					break;
				case 2:
					this.optimalPolicy[row][col] = Action.LEFT;
					break;
				case 3:
					this.optimalPolicy[row][col] = Action.RIGHT;
					break;
					
				}
				
				
			}
		}
	}
	
	
	//We perform value iterative process since there are many states and the max operator in the bellman equation is not linear. 
	//Starting with arbitrary initial values, we iterate and update utilities from the utility of the neighbours.
	public Action[][] ValueIteration(float discount, float epsilon) {
		//inputs: Markov decision process consisting of: set of states with:
		//1)	set of actions (enum Action as above)
		//2) 	transition model (tModel) and reward function (inside env Object of this Agent)
		//3)	discount factor and maximum error(epsilon)
		
		//initialising iterative utility array for temporary storage
		float[][] uPrime = new float[env.rows][env.columns];
		
		for (int col = 0; col < env.columns; col++) { //filling with zeroes
			for (int row = 0; row < env.rows; row++) {
				
				uPrime[row][col] = 0;
				
			}
		}
		
		//attribute to store iteration
		int currentIteration = 1;
		
		//attribute to store change in utility from uPrime to uOriginal
		float maxChangeInUtil = 0;
		
		//calculate the termination condition for the iterative loops
		float minDelta = epsilon*(1-discount)/discount;
		
		//start of iterations
		

		do { 			
			
			
			
			
			//Update utilies of uOriginal from uPrime
			for (int col = 0; col < env.columns; col++) {
				for (int row = 0; row < env.rows; row++) {
					
					uOriginal[row][col] = uPrime[row][col];
					
				}
			}
			
			
			maxChangeInUtil = 0;					//reset change in utility
		
			
			
		for (int col = 0; col < env.columns; col++) {			//for each state in the environment
			for (int row = 0; row < env.rows; row++) {
				if (env.rewards[row][col] == 0) {
//					System.out.println("wall");					//ignore walls
				}else {	
					float utilOfMaximisingAction = SumMaxUtility(row, col);			//SumMaxUtility calculates the maximum Utility of the state given				
																					//the transition model all the actions and it's neighbours.
					float newUtil = 0;												//newUtil is an attribute to store new utility of rewards[s] + discount*SumMaxUtility
					

					newUtil = env.rewards[row][col] + discount*utilOfMaximisingAction;	//assignment of the new utility of the state
					optimalPolicy[row][col] = ActionOfMaxUtility(row, col);				//Action Of Max Utility uses one-step look ahead to determine the best action to take given the current state.

					
					uPrime[row][col]= newUtil; 											//assignment of newUtility into the uPrime array.
					
					float changeInUtil = Math.abs(uOriginal[row][col] - uPrime[row][col]);		//calculating the change in Utility from old to new.

					if (changeInUtil > maxChangeInUtil) { 									//if the current utility difference is bigger than the max difference
						maxChangeInUtil = changeInUtil;										//assign it as the max change in utility
//						System.out.println("new maxError:" + maxChangeInUtil);
					}
				}
			}
		}
		
		if(currentIteration % 10 ==0)
		System.out.println("Finishing iteration " + currentIteration + " for value iteration.");
		
		graphics.AddIterationDataToDataset(uOriginal.clone(), currentIteration, env);			//Add utility data to grapher for this iteration
		
		currentIteration++;																	//prints current iteration number
		} while (maxChangeInUtil >= minDelta);												//termination condition for iteration.
		
		currentIteration--;
		System.out.println("Finishing iteration " + currentIteration + " for value iteration.");
		
		System.out.println("Finished value iteration.");
		return optimalPolicy;																	//returns U.
	}
																														
	private float SumMaxUtility(int row, int col) {			//SumMaxUtility calculates the maximum Utility of the state given										
															//the transition model all the actions and it's neighbours.
		float[] possibleUtils = {0, 0, 0, 0};				//initialise a temporary array to store all utilities of different states
		try { 												//calculates utility of all four actions
		possibleUtils[0] = IntendedUtility(row,  col, Action.UP) + UnintendedUtility(row, col, Action.UP);		//IntendedUtility takes the current state and given action  
																												//and returns 0.8*utility of new state.
		possibleUtils[1] = IntendedUtility(row,  col, Action.DOWN) + UnintendedUtility(row, col, Action.DOWN);	//UnintendedUtility also takes current state and given action
																												//and returns 0.1*utility of tile CCW to current + 0.1*utility of tile CW to current
		possibleUtils[2] = IntendedUtility(row,  col, Action.LEFT) + UnintendedUtility(row, col, Action.LEFT);

		possibleUtils[3] = IntendedUtility(row,  col, Action.RIGHT) + UnintendedUtility(row, col, Action.RIGHT);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("column:"+col + " row:"+ row);
		}
		
		float maxUtil = possibleUtils[0]; //finds highest utility action							
		for (float util: possibleUtils) {  
			if ( util > maxUtil) {
				maxUtil = util;
			}
		}
		
		return maxUtil;						//return highest utility
	}
	
	private Action ActionOfMaxUtility(int row, int col) {		//ActionOfMaxUtility uses one-step look ahead to determine the best action to take given the current state.
		float[] possibleUtils = {0, 0, 0, 0};					//It is similar to SumMaxUtility but it returns the Action associated with the max utility rather than the utility itself.

		try { //calculates utility of all four actions
		possibleUtils[0] = IntendedUtility(row,  col, Action.UP) + UnintendedUtility(row, col, Action.UP);

		possibleUtils[1] = IntendedUtility(row,  col, Action.DOWN) + UnintendedUtility(row, col, Action.DOWN);

		possibleUtils[2] = IntendedUtility(row,  col, Action.LEFT) + UnintendedUtility(row, col, Action.LEFT);

		possibleUtils[3] = IntendedUtility(row,  col, Action.RIGHT) + UnintendedUtility(row, col, Action.RIGHT);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("column:"+col + " row:"+ row);
		}
		
		float maxUtil = possibleUtils[0];
		int indexOfMaxUtil=0;
		
		for (int i = 0; i< possibleUtils.length; i++ ) {  //finds highest utility action
			if ( possibleUtils[i] >= maxUtil) {
				maxUtil = possibleUtils[i];
				indexOfMaxUtil=i;
			}
		}
		
		switch(indexOfMaxUtil) { //using indexOfMaxUtil, it returns the action corresponding to that max utility
		case 0:
			return Action.UP;
		case 1: 
			return Action.DOWN;
		case 2: 
			return Action.LEFT;
		case 3: 
			return Action.RIGHT;
			
		default: System.out.println("Error. No action found");
		return null;
		}
	}
	
	
	//calculates utility for a given state and intended action
	private float IntendedUtility(int row, int col, Action action) throws Exception {

		switch(action){													//given this action
		case UP:		
			if (row-1 < 0){ 											//if the up direction results moving out of the map
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else if (env.rewards[row-1][col] == AgentApp.AWALLSQ){ 	//if the up direction results in collision with the wall
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else {													
				return uOriginal[row-1][col]*tModel.intendedProb;		//the intended direction is clear
			}															//return the utility of the tile ABOVE current tile*intended Probability of 0.8
		case DOWN:
			if (row+1 > env.rows-1){									//if the down direction results moving out of the map
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else if (env.rewards[row+1][col] == AgentApp.AWALLSQ){ 	//if the down direction results in collision with the wall
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else {													//the intended direction is clear
				return uOriginal[row+1][col]*tModel.intendedProb;		//return the utility of the tile BELOW current tile*intended Probability of 0.8
			}	
		case LEFT:
			if (col-1 < 0){ 											//if the left direction results moving out of the map
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else if (env.rewards[row][col-1] == AgentApp.AWALLSQ){ 	//if the left direction results in collision with the wall
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else {													//the intended direction is clear
				return uOriginal[row][col-1]*tModel.intendedProb;		//return the utility of the tile to the LEFT of current tile*intended Probability of 0.8
			}	
		case RIGHT:
			if (col+1 > env.columns-1){ 								//if the right direction results moving out of the map
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else if (env.rewards[row][col+1] == AgentApp.AWALLSQ){ 	//if the right direction results in collision with the wall
				return uOriginal[row][col]*tModel.intendedProb;			//return the current tile utility*intended Probability of 0.8
			} else {													//the intended direction is clear
				return uOriginal[row][col+1]*tModel.intendedProb;		//return the utility of the tile to the RIGHT of current tile*intended Probability of 0.8
			}	
			
		}
		
		throw new Exception("Failed to calculate intended probability.");

	}
	
	//calculates unintended (right angle directions) utility given a state and action
	private float UnintendedUtility(int row, int col, Action action) throws Exception{
		
		float unintendedProb = 0;													//initialise variable to store probability
		
		switch(action){
		case UP:																	//case UP and DOWN are combined as both result in the left and right tiles being  
		case DOWN:																	//the unintended tiles since both have probabilty 0.1
				
			if (col-1 < 0 ){ 														//if the left direction results in moving out of the map
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else if (env.rewards[row][col-1] == AgentApp.AWALLSQ){					//if the left direction results in collision with the wall
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else { 																//the unintended direction is clear
				unintendedProb+= uOriginal[row][col-1]*tModel.unintendedProb;			//add the LEFT tile utility*unintended Probability of 0.1 to the total unintended probability
			}
				
				
			if (col+1 > env.columns-1 ){ 											//if the right direction results in moving out of the map
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else if (env.rewards[row][col+1] == AgentApp.AWALLSQ){					//if the right direction results in collision with the wall
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else { 																//the unintended direction is clear
				unintendedProb+= uOriginal[row][col+1]*tModel.unintendedProb;			//add the RIGHT tile utility*unintended Probability of 0.1 to the total unintended probability
			}
			
			return unintendedProb;													//return the combined total of left and right weighted probability and utility
			
		case LEFT:																	//case LEFT and RIGHT are combined as both result in the above and below tiles being
		case RIGHT:																	//the unintended tiles since both have probabilty 0.1
			
			if (row-1 < 0 ){ 														//if the up direction results in moving out of the map
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else if (env.rewards[row-1][col] == 0){								//if the up direction results in collision with the wall

				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else { 																//the unintended direction is clear

				unintendedProb+= uOriginal[row-1][col]*tModel.unintendedProb;			//add the ABOVE tile utility*unintended Probability of 0.1 to the total unintended probability
			}
				
				
			if (row+1> env.rows-1 ){ 												//if the down direction results in moving out of the map
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else if (env.rewards[row+1][col] == 0){								//if the down direction results in collision with the wall
				unintendedProb+= uOriginal[row][col]*tModel.unintendedProb;			//add the current tile utility*unintended Probability of 0.1 to the total unintended probability
			} else { 																//the unintended direction is clear
				unintendedProb+= uOriginal[row+1][col]*tModel.unintendedProb;			//add the BELOW tile utility*unintended Probability of 0.1 to the total unintended probability
			}
			
			return unintendedProb;													//return the combined total of left and right weighted probability and utility
		}
		
		throw new Exception("Failed to calculate unintended probability.");
	}

	
	public Action[][] PolicyIteration(float discount){
		//Policy iteration is concerned with the best policy rather than maximising utility, since
		//the policy usually is optimal long before the utility converges.
		//Here we use modified policy iteration, which does not use linear algebra methods to solve but is still efficient.
		
		
		GenerateRandomPolicy();				//initialise random starting policy
		
		boolean isUnchanged = true;			//initialise unchanged variable to track whether policy has changed	
																					
		int iterations = 1;					//track iterations
			
		do {
			
			uOriginal = PolicyEvaluation(discount, 30);		//evaulate utility values based on previous policy (optimalPolicy as initialised as random above)
			
			graphics.AddIterationDataToDataset(uOriginal.clone(), iterations, env);			//Add utility data to grapher for this iteration
			
			isUnchanged = true;							//reset isUnchanged for the new set of state changes
			
			for (int row = 0; row < env.rows; row++){		//for each state
				for (int col = 0; col < env.columns; col++) {
					if(env.rewards[row][col] == AgentApp.AWALLSQ) { //ignore wall calculation
						
					} else {
					
						try {
							float maxUtil = SumMaxUtility(row, col);							//find maxUtil of that state looking ahead and find policyDefinedUtil of 
																							//state looking ahead using the policy as the action
							float policyDefinedUtil = IntendedUtility(row, col, optimalPolicy[row][col] ) + UnintendedUtility(row, col, optimalPolicy[row][col]); //this uses the previous functions
								if (maxUtil > policyDefinedUtil) {							//if the maximum utility exceeds the policyDefined one, we should update our policy.
								optimalPolicy[row][col] = ActionOfMaxUtility(row, col);		//this corrects the current policy by assigning the action giving the max utility.
								isUnchanged = false;											//the policy has changed.
								}
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
						}
				}
			}
			System.out.println("Policy Iteration #"+ iterations);
			iterations++;																//next iteration		

		} while (isUnchanged == false);													//while policy is unchanged, continue to iterate
		
										
		
		
		return optimalPolicy;															//return the optimal policy
	}	
	
	private float[][] PolicyEvaluation(float discount, int k){							//policy evaluation takes the current policy and calculates new utilities of the states if the policy were to be executed k times.
		
		float[][] newUtility = new float[env.rows][env.columns];
		
		for (int col = 0; col < env.columns; col++) {
			for (int row = 0; row < env.rows; row++) {
				newUtility[row][col] = 0;
			}
		}
				
		
		for(int i=0;i<k;i++) {															//run simplified value iteration k times
				
			
			
			for (int row = 0; row < env.rows; row++) {
				for (int col = 0; col < env.columns; col++) {							//for each state
					if (env.rewards[row][col] == 0) {									//if wall, ignore calculation
						
					} else {
						try {
						float utilityEstimate = IntendedUtility(row, col, optimalPolicy[row][col]) + UnintendedUtility(row, col, optimalPolicy[row][col]);	//calculate utility estimate using current policy
						newUtility[row][col] = env.rewards[row][col] + discount*utilityEstimate; 															//update utility array with bellman equation
						} catch (Exception e)
						{	
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
				}
			}
			
			uOriginal = newUtility.clone();										//updates the utility map with the temp array newUtility for the next iteration
		}	

		return newUtility;  //return the new utility map
	}
	
	void PrintOptimalPolicy() { //prints the optimal policy as a representation of < , > , ^ and v arrows.
		
		System.out.println("Printing optimal policy....");
		
		
		for (int row = 0; row < env.rows; row++) {
			for (int col = 0; col < env.columns; col++) {
				
				if (env.rewards[row][col] == 0) {
					System.out.print(" - ");
					System.out.print("|");			//print wall as -
					continue;
				}
				switch(optimalPolicy[row][col]) {
				case UP:
					System.out.print(" ^ ");
					break;
				case DOWN:
					System.out.print(" v ");
					break;
				case LEFT:
					System.out.print(" < ");
					break;
				case RIGHT:
					System.out.print(" > ");
					break;
				}
				
				System.out.print("|");
			}
			System.out.println();
		
		}
		
	}
	
	void PrintMaxUtility() { //prints the utility map
		
		System.out.println("Printing Utilities....");
		for (int i = 0; i< env.rows; i++) {
			for (int j = 0; j< env.columns; j++) {
				
				if(env.rewards[i][j] == AgentApp.AWALLSQ) {
					System.out.print(" --wall--");			//ignore walls, print as --wall--
				} else {
				System.out.print(" " + uOriginal[i][j]+ " ");
				
				}
				System.out.print("	|");
			}
			System.out.println();
		}		
			
		}
	
	
	
	Grapher GetGraphics() {		//for graphing
		return graphics;
	}
}
