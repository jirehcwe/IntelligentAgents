# Intelligent Agents In Mazeworld using Value and Policy Iteration

This is a Java project based on the pseudocode written in Artificial Intelligence: A Modern Approach by Stuart Russell and Peter Norvig.

It looks at agents described in Chapter 16 and 17 of the book, agents that exist in Mazeworld.

These agents use two important algorithms: Policy and Value Iteration, which are implemented here.

The environment in this instance is this 6x6 grid maze:

![6x6 Maze](https://github.com/jirehcwe/IntelligentAgents/blob/master/diagrams/6x6%20maze.png)

The agent uses a transition model where moving in the intended direction has a probability of 0.8 and moving in right angles to the intended direction has probabilities of 0.1 each.

There is a more complicated maze which yields different optimal policies for value and policy iteration.

The environment is as such. (optimal policy overlaid)

![12x12 Maze](https://github.com/jirehcwe/IntelligentAgents/blob/master/diagrams/12x12%20maze.png)

The diagrams were done in draw.io.

# Code

Environment.java simply is a data container for the environment you create. It also has helper functions to print the environment to the console. (PrintEnvironment()) You simply create environments by inputting the size of the environment into the constructor (the environment is a square).

Grapher.java helps to plot graphs like those shown below.

It is implemented in BonusAgentApp.java and AgentApp.java as CreateGraph().

TransitionModel is another abstraction to allow easy changing of the transition model of the agent.

Finally, the agent itself in Agent.java. This agent implements both policy and value iteration functions and returns an appropriate 2D action array telling us the optimal policy calculated. There are also a few helper functions that help keep the main function more concise.

The code can be easily editted for your own mazes and environments. 

I hope to learn more about intelligent agents and apply them to game design in the future!

# Additional Details

I used JFreeChart to draw the utility graphs so you might need to install those dependencies in your build path.

You can get graphs such as these:
![12x12 Utility graph](https://github.com/jirehcwe/IntelligentAgents/blob/master/diagrams/12x12%20Utilities%20graph.png)

That was from the 12x12 maze. If you look at the red line (last one to converge), it is the 11,11 square, which is expected since the information takes the longest to propagate to that square.
