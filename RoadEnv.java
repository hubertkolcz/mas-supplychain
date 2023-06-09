import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class RoadEnv extends Environment {

    // common literals
    public static final Literal NEGOTIATE_WITH_PROCESSOR = Literal.parseLiteral("negotiate(processor)");
    public static final Literal PAY_TO_PROCESSOR = Literal.parseLiteral("pay(processor)");
    public static final Literal TRY_CAR = Literal.parseLiteral("try(car)");
    public static final Literal GIVE_CAR = Literal.parseLiteral("give_car(car)");
    public static final Literal ACCEPT_DELIVERY = Literal.parseLiteral("pay(retailer)");
    public static final Literal HAS_CAR = Literal.parseLiteral("has(customer,car)");

    public static final Literal RETAILER_AT_PROCESSOR = Literal.parseLiteral("at(retailer,processor)");
    public static final Literal RETAILER_AT_CUSTOMER = Literal.parseLiteral("at(retailer,customer)");

    static Logger LOGGER = Logger.getLogger(RoadEnv.class.getName());

    RoadModel model; // the model of the grid

    @Override
    public void init(String[] args) {
        model = new RoadModel();
        updatePercepts();
    }

    /**
     * updated the agents beliefs based on the RoadModel - position of Retailer on
     * Grid
     */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("retailer");
        clearPercepts("customer");

        // get the retailer location
        Location lRetailer = model.getAgPos(0);

        // add Retailer location to its percepts
        if (lRetailer.equals(model.lProcessor)) {
            addPercept("retailer", RETAILER_AT_PROCESSOR);
        }
        if (lRetailer.equals(model.lCustomer)) {
            addPercept("retailer", RETAILER_AT_CUSTOMER);
        }

        // add belief about number of cars in the stock of Retailer, during transportation
        if (model.processorReady) {
            addPercept("retailer", Literal.parseLiteral("stock(car," + model.availableCars + ")"));
        }
        if (model.accept_deliveryCount > 0) {
            addPercept("retailer", HAS_CAR);
            addPercept("customer", HAS_CAR);
        }
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        System.out.println("[" + ag + "] doing: " + action);
        boolean result = false;
        if (action.equals(NEGOTIATE_WITH_PROCESSOR)) {
            result = model.acquireCar();

        } else if (action.equals(PAY_TO_PROCESSOR)) {
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

        } else if (action.equals(TRY_CAR)) {
            result = model.getCar();

        } else if (action.equals(GIVE_CAR)) {
            result = model.takeCar();

        } else if (action.equals(ACCEPT_DELIVERY)) {
            result = model.accept_deliveryCar();

        } else if (action.getFunctor().equals("prepare")) {
            // wait 2 seconds to finish "produce"
            try {
                Thread.sleep(2000); // simulating time which takes to produce
                result = model.produceCar((int) ((NumberTerm) action.getTerm(1)).solve()); // casting to int
            } catch (Exception e) {
                LOGGER.info("Failed to prepare car!" + e);
            }

        } else {
            LOGGER.info("Failed to execute action " + action);
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
