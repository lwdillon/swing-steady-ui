package com.lw.swing.ui;


import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PulsatingBorder implements Border {
    private float thickness = 0.0f;
    private JComponent c;
    @InjectedResource
    private Color errorActiveColor;

    public PulsatingBorder(JComponent c, Color color) {
        this.c = c;
        this.errorActiveColor = color;
    }

    public PulsatingBorder(JComponent c) {
        ResourceInjector.get().inject(this);
        this.c = c;
    }

    public void paintBorder(Component c, Graphics g,
                            int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle2D r = new Rectangle2D.Double(x, y, width - 1, height - 1);
        g2.setStroke(new BasicStroke(2.0f * getThickness()));
        g2.setComposite(AlphaComposite.SrcOver.derive(getThickness()));
        g2.setColor(errorActiveColor);
        g2.draw(r);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(2, 2, 2, 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
        c.repaint();
    }
}