package sth.core;

public class Close implements State, java.io.Serializable {
	public void doAction(Survey survey) {
		survey.close(this);
	}

	public String toString() {
		return "fechado";
	}
}