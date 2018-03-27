
public class TransitionModel { //transition model class so that the probabilities can easily be implemented or changed.
	
	float intendedProb;
	float unintendedProb;

	TransitionModel(float intendedActionProb, float unintendedActionProb){ //constructor for the TransitionModel.
		this.intendedProb = intendedActionProb;
		this.unintendedProb = unintendedActionProb;
	}
	
}
