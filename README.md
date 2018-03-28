# Intelligent Agents using Value and Policy Iteration

This is a Java project based on the pseudocode written in Artificial Intelligence: A Modern Approach by Stuart Russell and Peter Norvig.

It looks at agent described in Chapter 16 and 17 of the book, agents that exist in Mazeworld.

These agents use two important algorithms: Policy and Value Iteration, which are implemented here.

The environment in this question is this 6x6 grid maze:

![6x6 Maze](https://github.com/jirehcwe/IntelligentAgents/blob/master/diagrams/6x6%20maze.png)

The agent uses a transition model where moving in the intended direction has a probability of 0.8 and moving in right angles to the intended direction has probabilities of 0.1 each.

There is a more complicated maze which yields different optimal policies for value and policy iteration.

The environment is as such. (optimal policy overlaid)

![12x12 Maze](https://github.com/jirehcwe/IntelligentAgents/blob/master/diagrams/12x12%20maze.png)

The diagrams were done in draw.io.


The code can be easily editted for your own mazes and environments. There is also a random environment generator (part of the Environment class as a constructor).

# Additional Details

I used JFreeChart to draw the utility graphs so you might need to install those dependencies in your build path.

You can get super wacky graphs such as these:
![12x12 Utility graph](https://github.com/jirehcwe/IntelligentAgents/blob/master/diagrams/12x12%20Utilities%20graph.png)

That was from the 12x12 maze. 

# Further Questions

If you have questions, you can email me at jireh_cwe@hotmail.com, I'll be happy to answer them!
