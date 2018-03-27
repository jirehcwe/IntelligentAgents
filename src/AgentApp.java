import javax.swing.JFrame;

public class AgentApp {
	
	public final static float EMPTYSQ = -0.04f;
	public final static float GREENSQ = 1;
	public final static float BROWNSQ = -1;
	public final static float AWALLSQ = 0;	
	
	public static void main(String[] args) {
		
		float[][] rewards = { 	{GREENSQ , AWALLSQ , GREENSQ , EMPTYSQ, EMPTYSQ , GREENSQ},
								{EMPTYSQ , BROWNSQ , EMPTYSQ , GREENSQ, AWALLSQ , BROWNSQ},
								{EMPTYSQ , EMPTYSQ , BROWNSQ , EMPTYSQ, GREENSQ , EMPTYSQ},
								{EMPTYSQ , EMPTYSQ , EMPTYSQ , BROWNSQ, EMPTYSQ , GREENSQ},
								{EMPTYSQ , AWALLSQ , AWALLSQ , AWALLSQ, BROWNSQ , EMPTYSQ},
								{EMPTYSQ , EMPTYSQ , EMPTYSQ , EMPTYSQ, EMPTYSQ , EMPTYSQ}
								};  //initialising R(s)
	
		int cols = 6;
		int rows = 6;
		float discount = 0.99f;
		float epsilon = 1f;
		
		Environment env = new Environment(rows, cols, rewards); //initialise environment
		
		TransitionModel tModel = new TransitionModel(0.8f, 0.1f); //initialise transition model
		
		Agent valueIterAgent = new Agent(tModel, env);	//creating agents
		Agent policyIterAgent = new Agent(tModel, env);
				
		Agent.Action[][] valueIterPolicy = valueIterAgent.ValueIteration(discount, epsilon); 
		valueIterAgent.PrintOptimalPolicy();		
		valueIterAgent.PrintMaxUtility();
		
		Agent.Action[][] policyIterPolicy = policyIterAgent.PolicyIteration(discount);	
		policyIterAgent.PrintOptimalPolicy();		
		policyIterAgent.PrintMaxUtility();
		
		env.PrintEnvironment();
		CreateGraph(valueIterAgent, "Value Iteration");
		
		CreateGraph(policyIterAgent, "Policy Iteration");
		
		boolean isSame = true;
		
		
		System.out.println("Testing policies....");
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
