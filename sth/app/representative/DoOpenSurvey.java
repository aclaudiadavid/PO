package sth.app.representative;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSurveyException;
import sth.app.exception.OpeningSurveyException;

/**
 * 4.6.3. Open survey.
 */
public class DoOpenSurvey extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoOpenSurvey(SchoolManager receiver) {
        super(Label.OPEN_SURVEY, receiver);
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
            throw new OpeningSurveyException(_discipline.value(), _project.value());
        }

        _receiver.openSurvey(_discipline.value(), _project.value());
    }
}
