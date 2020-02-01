package sth.app.teaching;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSurveyException;

import java.util.List;

/**
 * 4.4.5. Show survey results.
 */
public class DoShowSurveyResults extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoShowSurveyResults(SchoolManager receiver) {
        super(Label.SHOW_SURVEY_RESULTS, receiver);
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
        else if (_receiver.getSurveyTeacher(_discipline.value(), _project.value()) == null) {
            throw new NoSurveyException(_discipline.value(), _project.value());
        }

        List<String> l = _receiver.getSurveyResults(_discipline.value(), _project.value());

        if (l == null) {
            throw new NoSurveyException(_discipline.value(), _project.value());
        }
        else {
            for (String s: l) {
                _display.addLine(s);
            }
        }

        _display.display();
    }
}
