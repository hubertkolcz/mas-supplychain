import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/** class that implements the View of Domestic Retailer application */
public class HouseView extends GridWorldView {

    HouseModel hmodel;

    public HouseView(HouseModel model) {
        super(model, "Domestic Retailer", 700);
        hmodel = model;
        defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
        setVisible(true);
        repaint();
    }

    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int object) {
        Location lRetailer = hmodel.getAgPos(0);
        super.drawAgent(g, x, y, Color.lightGray, -1);
        switch (object) {
            case HouseModel.PROCESSOR:
                if (lRetailer.equals(hmodel.lProcessor)) {
                    super.drawAgent(g, x, y, Color.yellow, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Processor (" + hmodel.availableCars + ")");
                break;
            case HouseModel.CUSTOMER:
                if (lRetailer.equals(hmodel.lCustomer)) {
                    super.drawAgent(g, x, y, Color.yellow, -1);
                }
                String o = "Customer";
                if (hmodel.accept_deliveryCount > 0) {
                    o += " (" + hmodel.accept_deliveryCount + ")";
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, o);
                break;
        }
        repaint();
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        Location lRetailer = hmodel.getAgPos(0);
        if (!lRetailer.equals(hmodel.lCustomer) && !lRetailer.equals(hmodel.lProcessor)) {
            c = Color.yellow;
            if (hmodel.carryingCar)
                c = Color.orange;
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            super.drawString(g, x, y, defaultFont, "Retailer");
        }
    }
}
