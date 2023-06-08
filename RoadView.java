import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/** class that implements the View of Supply Chain application */
public class RoadView extends GridWorldView {
    RoadModel hmodel;

    public RoadView(RoadModel model) {
        super(model, "Supply Chain Management System", 700);
        hmodel = model;
        defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
        setVisible(true);
        repaint();
    }
}
