import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class RoadEnv extends Environment {

    // common literals
    public static final Literal of = Literal.parseLiteral("acquire(processor)");
    public static final Literal clf = Literal.parseLiteral("pay(processor)");
    public static final Literal gb = Literal.parseLiteral("get(car)");
    public static final Literal hb = Literal.parseLiteral("hand_in(car)");
    public static final Literal sb = Literal.parseLiteral("accept_delivery(car)");
    public static final Literal hob = Literal.parseLiteral("has(customer,car)");

    public static final Literal af = Literal.parseLiteral("at(retailer,processor)");
    public static final Literal ao = Literal.parseLiteral("at(retailer,customer)");

    static Logger logger = Logger.getLogger(RoadEnv.class.getName());

    RoadModel model; // the model of the grid

    @Override
    public void init(String[] args) {
        model = new RoadModel();

        if (args.length == 1 && args[0].equals("gui")) {
            RoadView view = new RoadView(model);
            model.setView(view);
        }

        updatePercepts();
    }

    /** creates the agents percepts based on the RoadModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("retailer");
        clearPercepts("customer");

        // get the retailer location
        Location lRetailer = model.getAgPos(0);

        // add agent location to its percepts
        if (lRetailer.equals(model.lProcessor)) {
            addPercept("retailer", af);
        }
        if (lRetailer.equals(model.lCustomer)) {
            addPercept("retailer", ao);
        }

        // add car "status" the percepts
        if (model.processorReady) {
            addPercept("retailer", Literal.parseLiteral("stock(car," + model.availableCars + ")"));
        }
        if (model.accept_deliveryCount > 0) {
            addPercept("retailer", hob);
            addPercept("customer", hob);
        }
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        System.out.println("[" + ag + "] doing: " + action);
        boolean result = false;
        if (action.equals(of)) { // of = acquire(processor)
            result = model.acquireCar();

        } else if (action.equals(clf)) { // clf = pay(processor)
            result = model.payProcessor();

        } else if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            if (l.equals("processor")) {
                dest = model.lProcessor;
            } else if (l.equals("customer")) {
                dest = model.lCustomer;
            }

            try {
                result = model.moveTowards(dest);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (action.equals(gb)) {
            result = model.getCar();

        } else if (action.equals(hb)) {
            result = model.handInCar();

        } else if (action.equals(sb)) {
            result = model.accept_deliveryCar();

        } else if (action.getFunctor().equals("prepare")) {
            // wait 4 seconds to finish "produce"
            try {
                Thread.sleep(4000);
                result = model.produceCar((int) ((NumberTerm) action.getTerm(1)).solve());
            } catch (Exception e) {
                logger.info("Failed to prepare car!" + e);
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
