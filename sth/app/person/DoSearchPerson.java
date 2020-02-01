package sth.app.person;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.core.SchoolManager;

import java.util.List;


/**
 * 4.2.4. Search person.
 */
public class DoSearchPerson extends Command<SchoolManager> {
    Input<String> _name;

    /**
    * @param receiver
    */
    public DoSearchPerson(SchoolManager receiver) {
        super(Label.SEARCH_PERSON, receiver);
        _name = _form.addStringInput(Message.requestPersonName());
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() {
        _form.parse();
        List<String> l = _receiver.searchPerson(_name.value());

        for (String o: l) {
            _display.addLine(o);
        }
        _display.display();
    }
}
