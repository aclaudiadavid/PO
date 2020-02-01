package sth.app.teaching;

import pt.tecnico.po.ui.DialogException;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;

/**
 * 4.4.2. Close project.
 */
public class DoCloseProject extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoCloseProject(SchoolManager receiver) {
        super(Label.CLOSE_PROJECT, receiver);
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
        _receiver.closeProject(_discipline.value(), _project.value());
    }
}
