package sth.core;

public class Open implements State, java.io.Serializable {
	public void doAction(Survey survey) {
		survey.open(this);
	}

	public String toString() {
		return "aberto";
	}
}