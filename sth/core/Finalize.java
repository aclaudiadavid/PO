package sth.core;

public class Finalize implements State, java.io.Serializable {
	public void doAction(Survey survey) {
		survey.finalCondition(this);
	}

	public String toString() {
		return "finalizado";
	}
}