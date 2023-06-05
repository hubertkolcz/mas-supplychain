import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/** class that implements the Model of Domestic Retailer application */
public class RoadModel extends GridWorldModel {

    // constants for the grid objects
    public static final int PROCESSOR = 16;
    public static final int CUSTOMER = 32;

    // the grid size
    public static final int GSize = 7;

    boolean processorReady = false; // whether the processor is acquiring
    boolean carryingCar = false; // whether the retailer is carrying car
    int accept_deliveryCount = 0; // how many accept_delivery the customer did
    int availableCars = 2; // how many cars are available

    Location lProcessor = new Location(0, 0);
    Location lCustomer = new Location(GSize - 1, GSize - 1);

    public RoadModel() {
        // create a 7x7 grid with one mobile agent
        super(GSize, GSize, 1);

        // initial location of retailer (column 3, line 3)
        // ag code 0 means the retailer
        setAgPos(0, GSize / 2, GSize / 2);

        // initial location of processor and customer
        add(PROCESSOR, lProcessor);
        add(CUSTOMER, lCustomer);
    }

    boolean acquireProcessor() {
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
        Location r1 = getAgPos(0);
        if (r1.x < dest.x)
            r1.x++;
        else if (r1.x > dest.x)
            r1.x--;
        if (r1.y < dest.y)
            r1.y++;
        else if (r1.y > dest.y)
            r1.y--;
        setAgPos(0, r1); // move the retailer in the grid

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

    boolean addCar(int n) {
        availableCars += n;
        if (view != null)
            view.update(lProcessor.x, lProcessor.y);
        return true;
    }

    boolean handInCar() {
        if (carryingCar) {
            accept_deliveryCount = 10;
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