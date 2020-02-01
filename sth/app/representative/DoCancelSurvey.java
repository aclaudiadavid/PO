package sth.app.representative;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSurveyException;
import sth.app.exception.NonEmptySurveyException;
import sth.app.exception.SurveyFinishedException;

/**
 * 4.5.2. Cancel survey.
 */
public class DoCancelSurvey extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoCancelSurvey(SchoolManager receiver) {
        super(Label.CANCEL_SURVEY, receiver);
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
        else if (_receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("finalizado")) {
            throw new SurveyFinishedException(_discipline.value(), _project.value());
        }
        else if (!_receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("fechado") && _receiver.hasReplySurvey(_discipline.value(), _project.value())) {
            throw new NonEmptySurveyException(_discipline.value(), _project.value());
        }

        _receiver.cancelSurvey(_discipline.value(), _project.value());
    }
}
