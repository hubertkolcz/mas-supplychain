import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/** class that implements the Model of Supply Chain application */
public class RoadModel extends GridWorldModel {

    // constants for the grid objects
    public static final int PROCESSOR = 16;
    public static final int CUSTOMER = 32;

    // the grid size
    public static final int GSize = 2;

    boolean processorReady = false; // whether Processor is ready to pass the car(s) to Retailer
    boolean carryingCar = false; // whether Retailer is delivering a car to Customer at the moment
    int accept_deliveryCount = 0; // how many accept_delivery the customer did
    int availableCars = 1; // how many cars are available in Retailer's stock

    Location lProcessor = new Location(0, 0);
    Location lCustomer = new Location(GSize - 1, GSize - 1);

    public RoadModel() {
        // create a x7 grid with one mobile agent
        super(GSize, GSize, 2);

        // initial location of retailer (column 3, line 3)
        // ag code 0 means the retailer
        setAgPos(0, GSize / 2, GSize / 2);

        // initial location of processor and customer
        add(PROCESSOR, lProcessor);
        add(CUSTOMER, lCustomer);
    }

    boolean acquireCar() {
        if (!processorReady) {
            processorReady = true;
            return true;
        } else {
            return false;
        }
    }

    boolean payProcessor() {
        if (processorReady) {
            processorReady = false;
            return true;
        } else {
            return false;
        }
    }

    boolean moveTowards(Location dest) {
        Location currentLocation = getAgPos(0);
        if (currentLocation.x < dest.x) {
            currentLocation.x++;
        } else if (currentLocation.x > dest.x) {
            currentLocation.x--;
        }
        if (currentLocation.y < dest.y) {
            currentLocation.y++;
        } else if (currentLocation.y > dest.y) {
            currentLocation.y--;
        }
        setAgPos(0, currentLocation); // move the retailer in the grid

        // repaint the processor and customer locations
        if (view != null) {
            view.update(lProcessor.x, lProcessor.y);
            view.update(lCustomer.x, lCustomer.y);
        }
        return true;
    }

    boolean getCar() {
        if (processorReady && availableCars > 0 && !carryingCar) {
            availableCars--;
            carryingCar = true;
            if (view != null)
                view.update(lProcessor.x, lProcessor.y);
            return true;
        } else {
            return false;
        }
    }

// Producer actions, each view update causes new execution cycle (update of internal believes)
    boolean produceCar(int n) {
        availableCars += n;
        if (view != null)
            view.update(lProcessor.x, lProcessor.y);
        return true;
    }

    boolean takeCar() {
        if (carryingCar) {
            accept_deliveryCount = 1;
            carryingCar = false;
            if (view != null)
                view.update(lCustomer.x, lCustomer.y);
            return true;
        } else {
            return false;
        }
    }

    boolean accept_deliveryCar() {
        if (accept_deliveryCount > 0) {
            accept_deliveryCount--;
            if (view != null)
                view.update(lCustomer.x, lCustomer.y);
            return true;
        } else {
            return false;
        }
    }
}
