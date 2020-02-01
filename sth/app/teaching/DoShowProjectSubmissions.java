package sth.app.teaching;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.NoSuchProjectException;

import java.util.List;

/**
 * 4.4.3. Show project submissions.
 */
public class DoShowProjectSubmissions extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoShowProjectSubmissions(SchoolManager receiver) {
        super(Label.SHOW_PROJECT_SUBMISSIONS, receiver);
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

        _display.addLine(_discipline.value() + " - " + _project.value());

        List<String> l = _receiver.getSubmissions(_discipline.value(), _project.value());

        for (String s: l) {
            _display.addLine(s);
        }

        _display.display();
    }
}
