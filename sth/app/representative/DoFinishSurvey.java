package sth.app.representative;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSurveyException;
import sth.app.exception.FinishingSurveyException;

/**
 * 4.6.5. Finish survey.
 */
public class DoFinishSurvey extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoFinishSurvey(SchoolManager receiver) {
        super(Label.FINISH_SURVEY, receiver);
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
        else if (_receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("por abrir") || _receiver.getSurveyStatus(_discipline.value(), _project.value()).equals("aberto")) {
            throw new FinishingSurveyException(_discipline.value(), _project.value());
        }

        _receiver.finishSurvey(_discipline.value(), _project.value());
    }
}
