package com.lw.swing.ui;

import org.jdesktop.core.animation.timing.*;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.core.animation.timing.triggers.TimingTriggerEvent;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;
import org.jdesktop.swing.animation.timing.triggers.TriggerUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by liwen on 2017/4/28.
 */
public class LodingComponent extends JComponent {
    private Color foregroundColor =new Color(0x1ab394);
    private int rectMaxHeight = 30;
    private int rectMinHeight = 15;
    private int rectMinWidth = 6;
    private int rectMaing = 5;
    private List<ShapeR> shapes = new ArrayList<ShapeR>();
    private LogoAnimatorPanel logo = new LogoAnimatorPanel();
    private String layout = BorderLayout.WEST;
    private String text = "";
    private JLabel textLabel = new JLabel("", JLabel.CENTER);

    public LodingComponent(String text, String layout) {
        this.text = text;
        this.layout = layout;
        initSwing();
    }

    public void setText(String text) {
        this.text = text;
        textLabel.setText(text);
    }

    private void initSwing() {
        this.setLayout(new BorderLayout());
        if (layout.endsWith(BorderLayout.NORTH) || layout.endsWith(BorderLayout.SOUTH)) {
            textLabel.setHorizontalAlignment(JLabel.CENTER);
        } else {
            textLabel.setHorizontalAlignment(JLabel.LEFT);
        }
        textLabel.setForeground(foregroundColor);
        textLabel.setText(text);
        logo.setPreferredSize(new Dimension(60, 30));
        this.add(logo);
        this.add(textLabel, layout);
    }

    public void stop() {

        try {
            for (ShapeR shape : shapes) {
                shape.getAnimator().cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {

        try {
            stop();

            if (!shapes.isEmpty()) {
                ShapeR shape = shapes.get(0);
                shape.getAnimator().restart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        TimingSource ts = new SwingTimerTimingSource();
        Animator.setDefaultTimingSource(ts);
        ts.init();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Test");
                frame.setSize(1000, 400);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                final LodingComponent centerPanel = new LodingComponent("正在加载...", BorderLayout.WEST);
                final JPanel panel = new JPanel(new FlowLayout());
                JButton jb = new JButton("dsa");

                jb.addActionListener(new AbstractAction() {
                    int a = 0;

                    public void actionPerformed(ActionEvent e) {
                        a = a == 0 ? 1 : 0;
                        if (a == 0) {
                            centerPanel.stop();
                        } else {
                            centerPanel.start();
                        }
                    }
                });
                panel.add(centerPanel);
                panel.add(jb);
                frame.add(panel);
                frame.setVisible(true);


            }
        });
    }

    public class LogoAnimatorPanel extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(foregroundColor);
            if (shapes.isEmpty()) {
                int x = (getWidth() - (5 * rectMinWidth) - 4 * rectMaing) / 2;
                int y = (getHeight() - rectMinHeight) / 2;
                ShapeR shape = new ShapeR(0);
                ShapeR shape1 = new ShapeR(100);
                ShapeR shape2 = new ShapeR(200);
                ShapeR shape3 = new ShapeR(300);
                ShapeR shape4 = new ShapeR(400);

                TriggerUtility.addTimingTrigger(shape.getAnimator(), shape1.getAnimator(), TimingTriggerEvent.START);
                TriggerUtility.addTimingTrigger(shape1.getAnimator(), shape2.getAnimator(), TimingTriggerEvent.START);
                TriggerUtility.addTimingTrigger(shape2.getAnimator(), shape3.getAnimator(), TimingTriggerEvent.START);
                TriggerUtility.addTimingTrigger(shape3.getAnimator(), shape4.getAnimator(), TimingTriggerEvent.START);
                TriggerUtility.addTimingTrigger(shape4.getAnimator(), shape.getAnimator(), TimingTriggerEvent.STOP);
                shapes.add(shape);
                shapes.add(shape1);
                shapes.add(shape2);
                shapes.add(shape3);
                shapes.add(shape4);
                for (int i = 0; i < shapes.size(); i++) {
                    shapes.get(i).setBounds(x + i * (rectMaing + rectMinWidth), y, rectMinWidth, rectMinHeight);
                }
                shape.getAnimator().start();
            }
            for (int i = 0; i < 5; i++) {
                ShapeR shape = shapes.get(i);
                int h = (int) (shape.getBounds().getHeight() * shape.getScaleY());
                int w = (int) shape.getBounds().getWidth();
                int x = shape.getBounds().x;
                int y = (getHeight() - h) / 2;
                g2.fillRect(x, y, w, h);
            }
            g2.dispose();

        }
    }

    public class ShapeR extends Rectangle {
        private Animator animator;
        private double scaleY = 0d;
        private final Interpolator SPLINE_1_0_1_1 = new SplineInterpolator(1.00, 0.00, 1.00, 1.00);


        public ShapeR(int delay) {
            KeyFrames.Builder<java.lang.Double> builder = new KeyFrames.Builder<java.lang.Double>(1D);
            builder.addFrame(2D, SPLINE_1_0_1_1);
            builder.addFrame(1D, SPLINE_1_0_1_1);
            final KeyFrames<java.lang.Double> framesX = builder.build();
            TimingTarget dropModifier = PropertySetter.getTarget(this, "scaleY", framesX);
            animator = new Animator.Builder().setStartDelay(delay, MILLISECONDS).setDuration(600, MILLISECONDS).addTarget(dropModifier).build();
        }

        public Animator getAnimator() {
            return animator;
        }

        public double getScaleY() {

            return scaleY;
        }

        public void setScaleY(double scaleY) {
            this.scaleY = scaleY;
            repaint();
        }
    }
}
