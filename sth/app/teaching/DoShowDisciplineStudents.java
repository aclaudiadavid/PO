package sth.app.teaching;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.app.exception.NoSuchDisciplineException;
import sth.core.SchoolManager;

import java.util.List;


/**
 * 4.4.4. Show course students.
 */
public class DoShowDisciplineStudents extends Command<SchoolManager> {
    private Input<String> _dis;


    /**
    * @param receiver
    */
    public DoShowDisciplineStudents(SchoolManager receiver) {
        super(Label.SHOW_COURSE_STUDENTS, receiver);
        _dis = _form.addStringInput(Message.requestDisciplineName());
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() throws DialogException {
        _form.parse();

        if (!_receiver.hasDiscipline(_dis.value())) {
            throw new NoSuchDisciplineException(_dis.value());
        }

        List<String> l = _receiver.getStudents(_dis.value());
        for (String o: l) {
            _display.addLine(o);
        }
        _display.display();
    }
}
