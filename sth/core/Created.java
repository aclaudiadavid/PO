package sth.core;

public class Created implements State, java.io.Serializable {

	public void doAction(Survey survey) {
		survey.create(this);
	}

	public String toString() {
		return "por abrir";
	}
}