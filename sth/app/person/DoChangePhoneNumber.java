package sth.app.person;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.core.SchoolManager;

import java.util.List;

/**
 * 4.2.2. Change phone number.
 */
public class DoChangePhoneNumber extends Command<SchoolManager> {
    Input<Integer> _n;

    /**
    * @param receiver
    */
    public DoChangePhoneNumber(SchoolManager receiver) {
        super(Label.CHANGE_PHONE_NUMBER, receiver);
        _n = _form.addIntegerInput(Message.requestPhoneNumber());
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() {
        _form.parse();
        List<String> l = _receiver.changePhone(_n.value());

        for (String o: l) {
            _display.addLine(o);
        }
        _display.display();
    }
}
