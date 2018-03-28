import javax.swing.JFrame;

public class BonusAgentApp {
	
	public final static float EMPTYSQ = -0.04f;
	public final static float GREENSQ = 1;
	public final static float BROWNSQ = -1;
	public final static float AWALLSQ = 0;	
	
	public static void main(String[] args) {
			
	float[][] rewards = { 		{AWALLSQ , EMPTYSQ  , AWALLSQ , EMPTYSQ, EMPTYSQ , EMPTYSQ, EMPTYSQ , EMPTYSQ  , AWALLSQ , BROWNSQ, BROWNSQ , BROWNSQ},
								{AWALLSQ , EMPTYSQ  , AWALLSQ , EMPTYSQ, AWALLSQ , AWALLSQ, AWALLSQ , EMPTYSQ  , AWALLSQ , BROWNSQ, AWALLSQ , BROWNSQ},
								{AWALLSQ , EMPTYSQ  , AWALLSQ , EMPTYSQ, AWALLSQ , AWALLSQ, AWALLSQ , EMPTYSQ  , AWALLSQ , BROWNSQ, AWALLSQ , BROWNSQ},
								{AWALLSQ , EMPTYSQ  , AWALLSQ , EMPTYSQ, AWALLSQ , EMPTYSQ, EMPTYSQ , EMPTYSQ  , AWALLSQ , GREENSQ, AWALLSQ , BROWNSQ},
								{AWALLSQ , EMPTYSQ  , EMPTYSQ , EMPTYSQ, AWALLSQ , EMPTYSQ, AWALLSQ , AWALLSQ  , AWALLSQ , AWALLSQ, AWALLSQ , BROWNSQ},
								{AWALLSQ , AWALLSQ  , EMPTYSQ , AWALLSQ, AWALLSQ , EMPTYSQ, EMPTYSQ , EMPTYSQ  , EMPTYSQ , BROWNSQ, EMPTYSQ , EMPTYSQ},
								{EMPTYSQ , BROWNSQ  , EMPTYSQ , AWALLSQ, AWALLSQ , EMPTYSQ, AWALLSQ , AWALLSQ  , AWALLSQ , BROWNSQ, AWALLSQ , AWALLSQ},
								{EMPTYSQ , AWALLSQ  , EMPTYSQ , AWALLSQ, AWALLSQ , EMPTYSQ, AWALLSQ , AWALLSQ  , AWALLSQ , BROWNSQ, AWALLSQ , AWALLSQ},
								{EMPTYSQ , AWALLSQ  , EMPTYSQ , AWALLSQ, AWALLSQ , EMPTYSQ, AWALLSQ , BROWNSQ  , BROWNSQ , BROWNSQ, AWALLSQ , GREENSQ},
								{EMPTYSQ , EMPTYSQ  , EMPTYSQ , BROWNSQ, AWALLSQ , EMPTYSQ, AWALLSQ , BROWNSQ  , AWALLSQ , AWALLSQ, GREENSQ , GREENSQ},
								{AWALLSQ , AWALLSQ  , AWALLSQ , AWALLSQ, AWALLSQ , EMPTYSQ, AWALLSQ , BROWNSQ  , AWALLSQ , AWALLSQ, AWALLSQ , GREENSQ},
								{GREENSQ , EMPTYSQ  , EMPTYSQ , EMPTYSQ, EMPTYSQ , EMPTYSQ, AWALLSQ , BROWNSQ  , BROWNSQ , BROWNSQ, BROWNSQ , GREENSQ},
							};  //initialising R(s). This is the labyrinth 12x12 maze as seen in Part 2 of the report.

							

		int rows = 12;
		int columns = 12;
		
		float discount = 0.99f;
		float epsilon = 1f;
		
		
		Environment env = new Environment(rows, columns, rewards); 				//initialise environment
		
		TransitionModel tModel = new TransitionModel(0.8f, 0.1f); 			//initialise transition model
		
		Agent valueIterAgent = new Agent(tModel, env);						//creating agents
		Agent policyIterAgent = new Agent(tModel, env);
				
		Agent.Action[][] valueIterPolicy = valueIterAgent.ValueIteration(discount, epsilon); 		//assigns the optimal policy found by value iteration to an Action array 
		valueIterAgent.PrintOptimalPolicy();														//prints optimal policy to console
		valueIterAgent.PrintMaxUtility();														//prints utilities of optimal policy to console
		
		Agent.Action[][] policyIterPolicy = policyIterAgent.PolicyIteration(discount);			//assigns the optimal policy found by policy iteration to an Action array 
		policyIterAgent.PrintOptimalPolicy();													//prints optimal policy to console
		policyIterAgent.PrintMaxUtility();														//prints utilities of optimal policy to console
		
		env.PrintEnvironment();																	//prints representation of environment
		
		CreateGraph(valueIterAgent, "Value Iteration");											//create graph for value iteration
			
		CreateGraph(policyIterAgent, "Policy Iteration");											//create graph for policy iteration
		
		boolean isSame = true;
		
		
		System.out.println("Testing policies....");												//test the two resulting policies for similarity
		for (int row = 0;row<env.rows;row++){
			for(int col = 0;col<env.columns;col++) {
				if (env.rewards[row][col]==AgentApp.AWALLSQ) {
					
				} else {
				Agent.Action value = valueIterPolicy[row][col];
				Agent.Action policy = policyIterPolicy[row][col];
				if (value != policy) {
					isSame = false;
				}
				}
			}
		}
		
		
		if (isSame ==false) {																	//prints result of similarity test
			System.out.println("Policies are different.");
		} else {
			System.out.println("Policies are the same.");
		}
		
		

		
		
	}
	
	public static void CreateGraph(Agent ag, String name) {										//function to create JPanel for graph to exist
		JFrame jFrame = new JFrame("Utilities against Iterations: " + name);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(ag.GetGraphics().CreatePanel(ag.GetEnvironment()));
        jFrame.setSize(1800,1000);
        jFrame.setVisible(true);
	}
	
}
