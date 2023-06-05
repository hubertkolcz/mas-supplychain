import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

    // common literals
    public static final Literal of = Literal.parseLiteral("open(processor)");
    public static final Literal clf = Literal.parseLiteral("close(processor)");
    public static final Literal gb = Literal.parseLiteral("get(beer)");
    public static final Literal hb = Literal.parseLiteral("hand_in(beer)");
    public static final Literal sb = Literal.parseLiteral("sip(beer)");
    public static final Literal hob = Literal.parseLiteral("has(owner,beer)");

    public static final Literal af = Literal.parseLiteral("at(retailer,processor)");
    public static final Literal ao = Literal.parseLiteral("at(retailer,owner)");

    static Logger logger = Logger.getLogger(HouseEnv.class.getName());

    HouseModel model; // the model of the grid

    @Override
    public void init(String[] args) {
        model = new HouseModel();

        if (args.length == 1 && args[0].equals("gui")) {
            HouseView view = new HouseView(model);
            model.setView(view);
        }

        updatePercepts();
    }

    /** creates the agents percepts based on the HouseModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("retailer");
        clearPercepts("owner");

        // get the retailer location
        Location lRetailer = model.getAgPos(0);

        // add agent location to its percepts
        if (lRetailer.equals(model.lProcessor)) {
            addPercept("retailer", af);
        }
        if (lRetailer.equals(model.lOwner)) {
            addPercept("retailer", ao);
        }

        // add beer "status" the percepts
        if (model.processorOpen) {
            addPercept("retailer", Literal.parseLiteral("stock(beer," + model.availableBeers + ")"));
        }
        if (model.sipCount > 0) {
            addPercept("retailer", hob);
            addPercept("owner", hob);
        }
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        System.out.println("[" + ag + "] doing: " + action);
        boolean result = false;
        if (action.equals(of)) { // of = open(processor)
            result = model.openProcessor();

        } else if (action.equals(clf)) { // clf = close(processor)
            result = model.closeProcessor();

        } else if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            if (l.equals("processor")) {
                dest = model.lProcessor;
            } else if (l.equals("owner")) {
                dest = model.lOwner;
            }

            try {
                result = model.moveTowards(dest);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (action.equals(gb)) {
            result = model.getBeer();

        } else if (action.equals(hb)) {
            result = model.handInBeer();

        } else if (action.equals(sb)) {
            result = model.sipBeer();

        } else if (action.getFunctor().equals("deliver")) {
            // wait 4 seconds to finish "deliver"
            try {
                Thread.sleep(4000);
                result = model.addBeer((int) ((NumberTerm) action.getTerm(1)).solve());
            } catch (Exception e) {
                logger.info("Failed to execute action deliver!" + e);
            }

        } else {
            logger.info("Failed to execute action " + action);
        }

        if (result) {
            updatePercepts();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        return result;
    }
}
