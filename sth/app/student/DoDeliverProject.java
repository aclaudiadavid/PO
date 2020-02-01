package sth.app.student;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.core.SchoolManager;

import sth.app.exception.NoSuchProjectException;
import sth.app.exception.NoSuchDisciplineException;

/**
 * 4.5.1. Deliver project.
 */
public class DoDeliverProject extends sth.app.common.ProjectCommand {
    private Input<String> _message;

    /**
    * @param receiver
    */
    public DoDeliverProject(SchoolManager receiver) {
        super(Label.DELIVER_PROJECT, receiver);
        _message = _form.addStringInput(Message.requestDeliveryMessage());
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void myExecute() throws DialogException {
        if (!_receiver.hasDiscipline(_discipline.value())) {
            throw new NoSuchDisciplineException(_discipline.value());
        }
        else if (_receiver.getProject(_discipline.value(), _project.value()) == null) {
            throw new NoSuchProjectException(_discipline.value(), _project.value());
        }
        else if (!_receiver.isProjectOpen(_discipline.value(), _project.value())) {
            throw new NoSuchProjectException(_discipline.value(), _project.value());
        }

        _receiver.submitProject(_discipline.value(), _project.value(), _message.value());
    }
}
