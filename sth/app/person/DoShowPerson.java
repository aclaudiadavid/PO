package sth.app.person;

import pt.tecnico.po.ui.Command;
import sth.core.SchoolManager;

import java.util.List;

/**
 * 4.2.1. Show person.
 */
public class DoShowPerson extends Command<SchoolManager> {
    /**
    * @param receiver
    */
    public DoShowPerson(SchoolManager receiver) {
        super(Label.SHOW_PERSON, receiver);
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() {
        List<String> l = _receiver.showPerson();

        for (String o: l) {
            _display.addLine(o);
        }
        _display.display();
    }
}
