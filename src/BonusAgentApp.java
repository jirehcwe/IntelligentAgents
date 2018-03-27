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
							};  //initialising R(s)

		

		int rows = 12;
		int columns = 12;
		
		float discount = 0.99f;
		float epsilon = 1f;
		
		
		Environment Env = new Environment(rows, columns, rewards); //initialise environment
		
		TransitionModel tModel = new TransitionModel(0.8f, 0.1f); //initialise transition model
		
		Agent valueIterAgent = new Agent(tModel, Env);	//creating agents
		Agent policyIterAgent = new Agent(tModel, Env);
				
		Agent.Action[][] valueIterPolicy = valueIterAgent.ValueIteration(discount, epsilon); 
		valueIterAgent.PrintOptimalPolicy();		
		valueIterAgent.PrintMaxUtility();
		
		Agent.Action[][] policyIterPolicy = policyIterAgent.PolicyIteration(discount);	
		policyIterAgent.PrintOptimalPolicy();		
		policyIterAgent.PrintMaxUtility();
		
		CreateGraph(valueIterAgent, "Value Iteration");
		
		CreateGraph(policyIterAgent, "Policy Iteration");
		
		boolean isSame = true;
		
		Env.PrintEnvironment();
		
		System.out.println("Testing policies....");
		for (int row = 0;row<Env.rows;row++){
			for(int col = 0;col<Env.columns;col++) {
				if (Env.rewards[row][col]==AgentApp.AWALLSQ) {
					
				} else {
					Agent.Action value = valueIterPolicy[row][col];
					Agent.Action policy = policyIterPolicy[row][col];
					if (value != policy) {
						isSame = false;
					}
				}
			}
		}
		
		
		
		if (isSame ==false) {
			System.out.println("Policies are different.");
		} else {
			System.out.println("Policies are the same.");
		}
		
		
		
	}
	
	public static void CreateGraph(Agent ag, String name) {
		JFrame jFrame = new JFrame("Utilities against Iterations: " + name);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(ag.GetGraphics().CreatePanel(ag.GetEnvironment()));
        jFrame.setSize(1800,1000);
        jFrame.setVisible(true);
	}
	
}
