package sth.app.teaching;

import pt.tecnico.po.ui.DialogException;
import sth.app.exception.NoSuchDisciplineException;
import sth.app.exception.DuplicateProjectException;
import sth.core.SchoolManager;

/**
 * 4.4.1. Create project.
 */
public class DoCreateProject extends sth.app.common.ProjectCommand {
    /**
    * @param receiver
    */
    public DoCreateProject(SchoolManager receiver) {
        super(Label.CREATE_PROJECT, receiver);
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void myExecute() throws DialogException {
        if (!_receiver.hasDiscipline(_discipline.value())) {
            throw new NoSuchDisciplineException(_discipline.value());
        }
        else if (_receiver.disciplineHasProject(_discipline.value(), _project.value())) {
            throw new DuplicateProjectException(_discipline.value(), _project.value());
        }
        _receiver.createProject(_discipline.value(), _project.value());
    }
}
