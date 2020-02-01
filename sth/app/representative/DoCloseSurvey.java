package sth.app.representative;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSurveyException;
import sth.app.exception.ClosingSurveyException;

/**
 * 4.5.4. Close survey.
 */
public class DoCloseSurvey extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoCloseSurvey(SchoolManager receiver) {
        super(Label.CLOSE_SURVEY, receiver);
    }

    /** @see sth.app.common.ProjectCommand#myExecute() */
    @Override
    public final void myExecute() throws DialogException {
        if (!_receiver.inCourse(_discipline.value())) {
            throw new NoSuchDisciplineException(_discipline.value());
        }
        else if (!_receiver.disciplineHasProject(_discipline.value(), _project.value())) {
            throw new NoSuchProjectException(_discipline.value(), _project.value());
        }
        else if (_receiver.getSurvey(_discipline.value(), _project.value()) == null) {
            throw new NoSurveyException(_discipline.value(), _project.value());
        }
        else if (_receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("por abrir") || _receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("finalizado")) {
            throw new ClosingSurveyException(_discipline.value(), _project.value());
        }

        _receiver.closeSurvey(_discipline.value(), _project.value());
    }
}
