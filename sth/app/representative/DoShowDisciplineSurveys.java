package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

import sth.core.SchoolManager;
import sth.app.exception.NoSuchDisciplineException;

import java.util.List;

/**
 * 4.6.6. Show discipline surveys.
 */
public class DoShowDisciplineSurveys extends Command<SchoolManager> {
    Input<String> _discipline;

    /**
    * @param receiver
    */
    public DoShowDisciplineSurveys(SchoolManager receiver) {
        super(Label.SHOW_DISCIPLINE_SURVEYS, receiver);
        _discipline = _form.addStringInput(Message.requestDisciplineName());
    }

    /** @see pt.tecnico.po.ui.Command#execute() */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        
        if (!_receiver.inCourse(_discipline.value())) {
            throw new NoSuchDisciplineException(_discipline.value());
        } 

        List<String> l;

        l = _receiver.getSurveyResultsRepresentative(_discipline.value());

        for (String o: l) {
            _display.addLine(o);
        }

        _display.display();
    }
}
