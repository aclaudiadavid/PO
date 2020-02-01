package sth.app.student;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSurveyException;

/**
 * 4.5.2. Answer survey.
 */
public class DoAnswerSurvey extends sth.app.common.ProjectCommand {
    private Input<String> _message;
    private Input<Integer> _hours;

    /**
    * @param receiver
    */
    public DoAnswerSurvey(SchoolManager receiver) {
        super(Label.ANSWER_SURVEY, receiver);
        _hours = _form.addIntegerInput(Message.requestProjectHours());
        _message = _form.addStringInput(Message.requestComment());
    }

    /** @see sth.app.common.ProjectCommand#myExecute() */
    @Override
    public final void myExecute() throws DialogException {

        if (!_receiver.hasDiscipline(_discipline.value())) {
            throw new NoSuchDisciplineException(_discipline.value());
        }
        else if (!_receiver.disciplineHasProject(_discipline.value(), _project.value())) {
            throw new NoSuchProjectException(_discipline.value(), _project.value());
        }
        else if (_receiver.getSurvey(_discipline.value(), _project.value()) == null || !_receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("aberto")) {
            throw new NoSurveyException(_discipline.value(), _project.value());
        }
        else if (!_receiver.hasSubmitted(_discipline.value(), _project.value())) {
            throw new NoSuchProjectException(_discipline.value(), _project.value());
        }

        _receiver.submitSurvey(_discipline.value(), _project.value(), _message.value(), _hours.value());

    }
}
