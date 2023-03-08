package ptolemy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ptolemy.AnimationPanel;

class Main {
    // how many anomalies (return to same place in epicycle) occur over a given number of rotations around the deferent
    //TODO: values for Venus here: 5, 8
    //for Mercury: 145, 46
    public static final double EPICYCLE_RETURNS = 5;
    public static final double PER_THIS_MANY_ROTATIONS = 8;

    public static final double DAYS_PER_ITERATION = 5;
    private static AnimationPanel panel;

    private static JFrame frame;

    public static void main(String[] args) {

        frame = new JFrame("Ptolemy Epicycle Demonstrations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        panel = new AnimationPanel(EPICYCLE_RETURNS, PER_THIS_MANY_ROTATIONS, DAYS_PER_ITERATION);
        panel.revalidate();
        panel.repaint();
        frame.add(panel);
        frame.setSize(400, 400);
        frame.setVisible(true);

        // set up key listenings
        Action littleForwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panel.inch(true);
                panel.revalidate();
                panel.repaint();
            }
        };
        Action littleBackwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panel.inch(false);
                panel.revalidate();
                panel.repaint();
            }
        };
        Action forwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panel.iterate(true);
                panel.revalidate();
                panel.repaint();
            }
        };
        Action backwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panel.iterate(false);
                panel.revalidate();
                panel.repaint();
            }
        };
        Action freezeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panel.freeze();
            }
        };
        Action clearAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panel.clear();
            }
        };
        panel.getInputMap().put(KeyStroke.getKeyStroke("E"),
                "forwardLittle");
        panel.getInputMap().put(KeyStroke.getKeyStroke("Q"),
                "backwardLittle");
        panel.getActionMap().put("forwardLittle",
                littleForwardAction);
        panel.getActionMap().put("backwardLittle",
                littleBackwardAction);
        panel.getInputMap().put(KeyStroke.getKeyStroke("D"),
                "forward");
        panel.getInputMap().put(KeyStroke.getKeyStroke("A"),
                "backward");
        panel.getActionMap().put("forward",
                forwardAction);
        panel.getActionMap().put("backward",
                backwardAction);
        panel.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                "freeze");
        panel.getActionMap().put("freeze",
                freezeAction);
        panel.getInputMap().put(KeyStroke.getKeyStroke("DELETE"),
                "clear");
        panel.getActionMap().put("clear",
                clearAction);
    }
}