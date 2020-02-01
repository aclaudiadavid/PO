package sth.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.core.SchoolManager;

/**
 * 4.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<SchoolManager> {
    private Input<String> _fname;

    /**
    * @param receiver
    */
    public DoSave(SchoolManager receiver) {
        super(Label.SAVE, receiver);
    }

    /** @see Command#execute() */
    @Override
    public final void execute() {
        String f = _receiver.getFile();
        if (f == null) {
            _fname = _form.addStringInput(Message.newSaveAs());
            _form.parse();
            _receiver.updateFile(_fname.value());
        }
        _receiver.doSave();
    }
}
