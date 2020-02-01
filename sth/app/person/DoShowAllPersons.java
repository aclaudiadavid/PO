package sth.app.person;

import pt.tecnico.po.ui.Command;
import sth.core.SchoolManager;

import java.util.List;

/**
 * 4.2.3. Show all persons.
 */
public class DoShowAllPersons extends Command<SchoolManager> {
    /**
    * @param receiver
    */
    public DoShowAllPersons(SchoolManager receiver) {
        super(Label.SHOW_ALL_PERSONS, receiver);
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() {
        List<String> l = _receiver.showAllPersons();

        for (String o: l) {
            _display.addLine(o);
        }
        _display.display();
    }
}
