package sth.app.representative;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSuchProjectException;
import sth.app.exception.DuplicateSurveyException;

/**
 * 4.5.1. Create survey.
 */
public class DoCreateSurvey extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoCreateSurvey(SchoolManager receiver) {
        super(Label.CREATE_SURVEY, receiver);
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
        else if (_receiver.getSurvey(_discipline.value(), _project.value()) != null) {
            throw new DuplicateSurveyException(_discipline.value(), _project.value());
        }
        else if (!_receiver.isProjectOpen(_discipline.value(), _project.value())) {
            throw new NoSuchProjectException(_discipline.value(), _project.value());
        }

        _receiver.createSurvey(_discipline.value(), _project.value());
    }
}
