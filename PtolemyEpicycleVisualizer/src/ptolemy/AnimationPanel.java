package ptolemy;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

class AnimationPanel extends JPanel {
    public static final boolean OPPOSITE = false; // make negative to show epicycle going in opposite direction
    private ArrayList<double[]> frozen;
    private ArrayList<int[]> planetPath;

    private final int eccenterX = 199;
    private final int eccenterY = 179;
    private final int earthX = 199;
    private final int earthY = 199;
    private final int eccentricRad = 100;

    //TODO: values for Venus here: 0.707*eccentricRad
    //TODO: values for Mercury here:0.423*eccentricRad
    private final int epicycleRad = (int) (.707 * eccentricRad);

    private double epicycleSpeed;
    private double eccentricSpeed;

    private double currentPositionAroundEpicycle = 0; // in degrees
    private double currentPositionAroundEccentric = 0; // in degrees

    private double iteration = 0; // current "step"
    private double iterationEpicycle = 0; // current "step" for epicycle
    private double daysPerIter; // how many days pass in each shown "step" (can be < 1)

    public AnimationPanel(double anomalies, double perRotations, double DPI) {
        super();

        frozen = new ArrayList<>();
        planetPath = new ArrayList<>();

        // convert repetitions to degrees per day
        epicycleSpeed = ((double)360 / 365) * (anomalies / perRotations);
        System.out.println(epicycleSpeed);
        eccentricSpeed = (double) 360 / 365;
        System.out.println(eccentricSpeed);
        daysPerIter = DPI;

        simulate();
    }

    // moving regular intervals through the cycle
    public void iterate(boolean forward) {
        if (forward) {
            if (iteration < Double.MAX_VALUE - (daysPerIter + 1)) {
                iteration += daysPerIter;
            }

            if (!OPPOSITE) {
                if (iterationEpicycle < Double.MAX_VALUE - (daysPerIter + 1)) {
                    iterationEpicycle += daysPerIter;
                }
            } else {
                if (iterationEpicycle > daysPerIter) {
                    iterationEpicycle -= daysPerIter;
                }
            }
        } else {
            if (iteration > daysPerIter) {
                iteration -= daysPerIter;
            }

            if (OPPOSITE) {
                if (iterationEpicycle < Double.MAX_VALUE - (daysPerIter + 1)) {
                    iterationEpicycle += daysPerIter;
                }
            } else {
                if (iterationEpicycle > daysPerIter) {
                    iterationEpicycle -= daysPerIter;
                }
            }
        }
        simulate();
    }

    // moving smaller intervals through the cycle
    public void inch(boolean forward) {
        if (forward) {
            if (iteration < Double.MAX_VALUE - 0.5) {
                iteration += 0.5;
            }
        } else {
            if (iteration > 0.5) {
                iteration -= 0.5;
            }
        }
        simulate();
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, 400, 400);

        // draw and label earth and eccenter
        g.setColor(Color.BLUE);
        g.fillOval(earthX, earthY, 2, 2);
        g.drawString("Earth", earthX + 3, earthY + 5);
        g.setColor(Color.RED);
        g.fillOval(eccenterX, eccenterY, 2, 2);
        g.drawString("Eccenter", eccenterX + 3, eccenterY + 5);

        // draw main eccentric circle and earth circle
        /*g.setColor(Color.gray);
        g.drawOval(earthX - eccentricRad, earthY - eccentricRad, eccentricRad * 2, eccentricRad * 2);*/
        g.setColor(Color.black);
        g.drawOval(eccenterX - eccentricRad, eccenterY - eccentricRad, eccentricRad * 2, eccentricRad * 2);

        //draw current epicycle
        g.setColor(Color.GREEN);
        drawEpicycle(g, currentPositionAroundEccentric, currentPositionAroundEpicycle);

        //draw line from earth center to previous point too (other is done in drawEpicycle())
        g.setColor(Color.pink);
        if (planetPath.size() > 1) {
            g.drawLine(earthX, earthY, planetPath.get(planetPath.size() - 2)[0], planetPath.get(planetPath.size() - 2)[1]);
        }
       /* if (planetPath.size() > 1) {
            double slope = (double) (planetPath.get(planetPath.size() - 2)[1] - earthY) / (planetPath.get(planetPath.size() - 2)[0] - earthX);
            double intercept = earthY - (slope * earthX);
            if (slope > 0) {
                g.drawLine(0, (int) intercept, (int) 200, (int) (slope * 200 + intercept));
            } else {
                g.drawLine(0, (int) intercept, (int) -200, (int) (slope * -200 + intercept));
            }
        }*/

        for (double[] selected : frozen) {
            drawEpicycle(g, selected[0], selected[1]);
        }
    }

    private void drawEpicycle(Graphics g, double posEcc, double posEpi) {
        // find current epicycle center along circle center
        double epicycleCenterX = eccentricRad * Math.cos(Math.toRadians(posEcc)) + eccenterX;
        double epicycleCenterY = eccentricRad * Math.sin(Math.toRadians(posEcc)) + eccenterY;

        System.out.println("Epicycle Center: "+posEcc);

        // draw center of epicycle and epicycle itself
        g.setColor(Color.GREEN);
        g.fillOval((int) epicycleCenterX, (int) epicycleCenterY, 2, 2);
        g.drawOval((int) epicycleCenterX - epicycleRad, (int) epicycleCenterY - epicycleRad, epicycleRad * 2,
                epicycleRad * 2);

        // find where the planet is on its epicycle is in a similar way
        double planetCenterX = epicycleRad * Math.cos(Math.toRadians(posEpi)) + epicycleCenterX;
        double planetCenterY = epicycleRad * Math.sin(Math.toRadians(posEpi)) + epicycleCenterY;
        System.out.println("Planet Center: "+posEpi);

        //draw planet path for all steps
        g.setColor(Color.CYAN);
        for (int i = 0; i < planetPath.size()-1; i++) {
            g.drawLine(planetPath.get(i)[0], planetPath.get(i)[1], planetPath.get(i + 1)[0], planetPath.get(i + 1)[1]);
        }

        // draw line from earth center to current point
        g.setColor(Color.PINK);
        g.drawLine((int) planetCenterX, (int) planetCenterY, earthX, earthY);

        // draw planet on epicycle
        g.setColor(Color.DARK_GRAY);
        g.fillOval((int) planetCenterX - 2, (int) planetCenterY - 2, 5,
                5);
    }

    private void simulate() {
        // adjust based on the current "day" (given by iteration) in degrees around circle
        currentPositionAroundEccentric = iteration * eccentricSpeed;
        currentPositionAroundEpicycle = iteration * epicycleSpeed + iteration * eccentricSpeed;
        double epicycleCenterX = eccentricRad * Math.cos(Math.toRadians(currentPositionAroundEccentric)) + eccenterX;
        double epicycleCenterY = eccentricRad * Math.sin(Math.toRadians(currentPositionAroundEccentric)) + eccenterY;
        int[] newPoint = new int[] {(int) (epicycleRad * Math.cos(Math.toRadians(currentPositionAroundEpicycle)) + epicycleCenterX), (int) (epicycleRad * Math.sin(Math.toRadians(currentPositionAroundEpicycle)) + epicycleCenterY)};
        planetPath.add(newPoint);
    }

    public void freeze() {
        frozen.add(new double[] {currentPositionAroundEccentric, currentPositionAroundEpicycle});
    }

    public void clear() {
        frozen.clear();
        planetPath.clear();
    }
}
