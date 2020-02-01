package sth.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

import sth.app.exception.NoSuchPersonException;
import sth.core.School;
import sth.core.SchoolManager;
import sth.core.exception.NoSuchPersonIdException;

import java.util.List;
import java.util.ArrayList;

/**
 * 4.1.1. Open existing document.
 */
public class DoOpen extends Command<SchoolManager> {
    private Input<String> _fname;
    private School _oldSchool;

    /**
    * @param receiver
    */
    public DoOpen(SchoolManager receiver) {
        super(Label.OPEN, receiver);
        _fname = _form.addStringInput(Message.openFile());
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        _oldSchool = _receiver.getSchool();
        _receiver.updateFile(_fname.value());

        try {
            _receiver.doOpen();
        } catch (NoSuchPersonIdException a) {
            a.printStackTrace();
        }


        if (!_receiver.getSchool().personExists(_receiver.getLogin().getId())) {
            _receiver.changeSchool(_oldSchool);
            throw new NoSuchPersonException(_receiver.getLogin().getId());
        }

        List<String> messages = new ArrayList<>();

        messages = _receiver.getMessages();

        for (String s: messages) {
            _display.addLine(s);
        }

        _display.display();
    }

}
