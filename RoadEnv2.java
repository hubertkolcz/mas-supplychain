// import jason.asSyntax.*;
// import jason.environment.Environment;
// import jason.environment.grid.Location;
// import java.util.logging.Logger;

// public class RoadEnv extends Environment {
// // common literals
// public static final Literal ACQUIRE_PROCESSOR =
// Literal.parseLiteral("negotiate(processor)");
// public static final Literal PAY_TO_PROCESSOR =
// Literal.parseLiteral("pay(processor)");
// public static final Literal GET_CAR = Literal.parseLiteral("get(car)");
// public static final Literal HAND_IN_CAR =
// Literal.parseLiteral("hand_in(car)");
// public static final Literal ACCEPT_DELIVERY_CAR =
// Literal.parseLiteral("accept_delivery(car)");
// public static final Literal HAS_CUSTOMER_CAR =
// Literal.parseLiteral("has(customer,car)");

// public static final Literal AT_RETAILER_PROCESSOR =
// Literal.parseLiteral("at(retailer,processor)");
// public static final Literal AT_RETAILER_CUSTOMER =
// Literal.parseLiteral("at(retailer,customer)");

// private static final Logger LOGGER =
// Logger.getLogger(RoadEnv.class.getName());

// private RoadModel model; // the model of the grid

// @Override
// public void init(String[] args) {
// model = new RoadModel();
// updatePercepts();
// }

// /** creates the agents percepts based on the RoadModel */
// private void updatePercepts() {
// // clear the percepts of the agents
// clearPercepts("retailer");
// clearPercepts("customer");

// // get the retailer location
// Location lRetailer = model.getAgPos(0);

// // add agent location to its percepts
// if (lRetailer.equals(model.lProcessor)) {
// addPercept("retailer", AT_RETAILER_PROCESSOR);
// }
// if (lRetailer.equals(model.lCustomer)) {
// addPercept("retailer", AT_RETAILER_CUSTOMER);
// }

// // add car "status" the percepts
// if (model.processorReady) {
// addPercept("retailer", Literal.parseLiteral("stock(car," +
// model.availableCars + ")"));
// }
// if (model.accept_deliveryCount > 0) {
// addPercept("retailer", HAS_CUSTOMER_CAR);
// addPercept("customer", HAS_CUSTOMER_CAR);
// }
// }

// @Override
// public boolean executeAction(String ag, Structure action) {
// System.out.println("[" + ag + "] doing: " + action);
// boolean result = false;
// if (action.equals(ACQUIRE_PROCESSOR)) {
// result = model.acquireCar();
// } else if (action.equals(PAY_TO_PROCESSOR)) {
// result = model.payProcessor();
// } else if (action.getFunctor().equals("move_towards")) {
// String l = action.getTerm(0).toString();
// Location dest = null;
// if (l.equals("processor")) {
// dest = model.lProcessor;
// } else if (l.equals("customer")) {
// dest = model.lCustomer;
// }
// try {
// result = model.moveTowards(dest);
// } catch (Exception e) {
// e.printStackTrace();
// }
// } else if (action.equals(GET_CAR)) {
// result = model.getCar();
// } else if (action.equals(HAND_IN_CAR)) {
// result = model.handInCar();
// } else if (action.equals(ACCEPT_DELIVERY_CAR)) {
// result = model.accept_deliveryCar();
// } else if (action.getFunctor().equals("prepare")) {
// // wait 4 seconds to finish "produce"
// try {
// Thread.sleep(4000);
// result = model.produceCar((int) ((NumberTerm) action.getTerm(1)).solve());
// } catch (Exception e) {
// LOGGER.info("Failed to prepare car!" + e);
// }
// } else {
// LOGGER.info("Failed to execute action " + action);
// }

// if (result) {
// updatePercepts();
// try {
// Thread.sleep(100);
// } catch (Exception e) {}
// }

// return result;
// }
// } {

// }
