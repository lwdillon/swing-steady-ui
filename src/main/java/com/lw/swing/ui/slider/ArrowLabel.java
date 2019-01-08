package com.lw.swing.ui.slider;

import com.lw.swing.utils.IconFont;

import javax.swing.*;
import java.awt.*;

public class ArrowLabel extends JLabel {

        private static final long serialVersionUID = 1L;

        protected int direction;

        private double angle = 0;

        public ArrowLabel() {

            setFont(IconFont.ICON_FONT);
            setHorizontalAlignment(JLabel.CENTER);
            setText(IconFont.getIcon("fa-chevron-right"));
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.rotate(Math.toRadians(angle), getWidth() / 2, getHeight() / 2);
            super.paint(g2);
        }

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
            repaint();
        }
    }