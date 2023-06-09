package time;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

import java.text.SimpleDateFormat;
import java.util.Date;

public class check extends DefaultInternalAction {

    private static final String TIME_FORMAT = "HH:mm:ss";

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        String time = getCurrentTime();
        logTime(ts, time);
        return unifyTime(un, args, time);
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        return dateFormat.format(new Date());
    }

    private void logTime(TransitionSystem ts, String time) {
        ts.getLogger().info("Check Time=" + time);
    }

    private boolean unifyTime(Unifier un, Term[] args, String time) {
        return un.unifies(args[0], new StringTermImpl(time));
    }
}